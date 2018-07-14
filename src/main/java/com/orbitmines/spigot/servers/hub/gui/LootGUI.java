package com.orbitmines.spigot.servers.hub.gui;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.*;
import com.orbitmines.api.utils.TimeUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.Loot;
import com.orbitmines.spigot.api.PeriodLoot;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.chat.ComponentMessage;
import com.orbitmines.spigot.api.handlers.data.LootData;
import com.orbitmines.spigot.api.handlers.data.PeriodLootData;
import com.orbitmines.spigot.api.handlers.data.VoteData;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class LootGUI extends GUI {
    
    private final int LOOT_PER_PAGE = 9;
    private final int NEW_PER_PAGE = LOOT_PER_PAGE / 9;

    private OrbitMines orbitMines;
    private int page;

    public LootGUI() {
        this(0);
    }

    public LootGUI(int page) {
        this.orbitMines = OrbitMines.getInstance();
        this.page = page;
        newInventory(54, "§0§lSpace Turtle");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        VoteData voteData = (VoteData) omp.getData(Data.Type.VOTES);
        voteData.updateVoteTimeStamps();
        LootData lootData = (LootData) omp.getData(Data.Type.LOOT);
        lootData.load();

        ServerList[] serverLists = ServerList.values();
        for (int i = 0; i < serverLists.length; i++) {
            ServerList serverList = serverLists[i];

            boolean canVote = !voteData.getVoteTimeStamps().containsKey(serverList);

            ItemBuilder item = new ItemBuilder(canVote ? Material.EGG /* TODO: TURTLE_EGG*/ : Material.EGG, serverList.ordinal() + 1, 0, (canVote ? "§9" : "§c") + "§lVote Link " + (serverList.ordinal() + 1));

            item.addLore("§7§o" + serverList.getDomainName());
            item.addLore("");
            item.addLore("§7- §9§l250 Prisms");
            item.addLore("§7");

            if (canVote)
                item.addLore(omp.lang("§aKlik hier om de link te openen.", "§aClick here to open the link."));
            else
                item.addLore(omp.lang("§cJe kan weer voten over ", "§cYou can vote again in ") + TimeUtils.fromTimeStamp(serverList.getCooldown(voteData.getVoteTimeStamps().get(serverList)) * 1000, omp.getLanguage()) + ".");

            if (canVote)
                item.glow();

            add(1, 2 + i, new ItemInstance(item.build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    omp.getPlayer().closeInventory();

                    omp.sendMessage(" §7§lOrbit§8§lMines §9§lVote Link " + (serverList.ordinal() + 1));

                    ComponentMessage cM = new ComponentMessage();
                    cM.add(new Message("  §7- "));
                    cM.add(new Message("§7Klik hier om naar §9§l" + serverList.getDomainName() + "§r§7 te gaan§7.", "§7Click here to go to §9§l" + serverList.getDomainName() + "§r§7."), ClickEvent.Action.OPEN_URL, new Message(serverList.getUrl()), HoverEvent.Action.SHOW_TEXT, new Message("§7Klik hier om §9Vote Link " + (serverList.ordinal() + 1) + "§7 te openen.", "§7Click here to open §9Vote Link " + (serverList.ordinal() + 1) + "§7."));
                    cM.send(omp);

                    omp.playSound(Sound.UI_BUTTON_CLICK);
                }
            });
        }

        PeriodLootData periodLootData = (PeriodLootData) omp.getData(Data.Type.PERIOD_LOOT);

        PeriodLoot[] periodLoot = PeriodLoot.values();
        for (int i = 0; i < periodLoot.length; i++) {
            PeriodLoot loot = periodLoot[i];

            boolean canCollect = (loot != PeriodLoot.MONTHLY_VIP || omp.getVipRank() != VipRank.NONE) && periodLootData.canCollect(loot);

            ItemBuilder item = canCollect ? loot.getClaimable(omp) : loot.getClaimed(omp);
            item.setDisplayName((canCollect ? "§a§l" : "§c§l") + omp.lang(loot.getTitle(omp)));

            item.addLore("");
            for (Message desc : loot.getDescription(omp)) {
                item.addLore(omp.lang(desc));
            }
            item.addLore("");

            if (loot != PeriodLoot.MONTHLY_VIP || omp.getVipRank() != VipRank.NONE) {
                if (canCollect)
                    item.addLore(omp.lang("§aKlik hier om te ontvangen.", "§aClick here to collect."));
                else
                    item.addLore(omp.lang("§cJe krijgt weer beloningen over ", "§cYou can collect again in ") + TimeUtils.fromTimeStamp(periodLootData.getCooldown(loot) * 1000L, omp.getLanguage()) + ".");
            } else {
                item.addLore(omp.lang("§cJe hebt geen Rank.", "§cYou don't have a Rank."));
            }

            if (canCollect)
                item.glow();

            if (canCollect)
                add(2, 3 + i, new ItemInstance(item.build()) {
                    @Override
                    public void onClick(InventoryClickEvent event, OMPlayer omp) {
                        periodLootData.collect(omp, loot);
                        omp.playSound(Sound.ENTITY_PLAYER_LEVELUP);
                        reopen(omp);
                    }
                });
            else
                add(2, 3 + i, new EmptyItemInstance(item.build()));
        }

        int slot = 36;

        for (LootData.Loot.Instance instance : getLootForPage(lootData.getLoot())) {
            if (instance != null) {
                Loot loot = instance.getLoot();
                int count = instance.getCount();

                ItemBuilder item = loot.getIcon(count);
                item.setDisplayName(loot.getDisplayName(count));

                item.addLore("§7" + omp.lang("Zeldzaamheid", "Rarity") + ": " + instance.getRarity().getDisplayName());

                Server server = loot.getServer(count);

                if (server != null)
                    item.addLore("§7Server: " + loot.getServer(count).getDisplayName());

                item.addLore("");
                item.addLore("§2§l§o" + instance.getDescription());

                add(slot, new ItemInstance(item.build()) {
                    @Override
                    public void onClick(InventoryClickEvent event, OMPlayer omp) {
                        if (server == null || server == orbitMines.getServerHandler().getServer())
                            loot.onInteract(omp, instance.getRarity(), instance.getDescription(), count);
                        else
                            omp.sendMessage("Loot", Color.RED, "§7Je kan dit item alleen gebruiken in " + server.getDisplayName() + "§7.", "§7You can only use this item in " + server.getDisplayName() + "§7.");
                    }
                });
            }

            slot++;
        }


        if (page != 0)
            add(5, 0, new ItemInstance(new PlayerSkullBuilder(() -> "Green Arrow Left", 1, omp.lang("§7« Meer Loot", "§7« More Loot")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU1MGI3Zjc0ZTllZDc2MzNhYTI3NGVhMzBjYzNkMmU4N2FiYjM2ZDRkMWY0Y2E2MDhjZDQ0NTkwY2NlMGIifX19").build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    page--;
                    omp.playSound(Sound.UI_BUTTON_CLICK);
                    reopen(omp);
                }
            });
        else
            clear(5, 0);

        if (canHaveMorePages(lootData.getLoot()))
            add(5, 8, new ItemInstance(new PlayerSkullBuilder(() -> "Green Arrow Right", 1, omp.lang("§7Meer Loot »", "§7More Loot »")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTYzMzlmZjJlNTM0MmJhMThiZGM0OGE5OWNjYTY1ZDEyM2NlNzgxZDg3ODI3MmY5ZDk2NGVhZDNiOGFkMzcwIn19fQ==").build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    page++;
                    omp.playSound(Sound.UI_BUTTON_CLICK);
                    reopen(omp);
                }
            });
        else
            clear(5, 8);

        return true;
    }


    private LootData.Loot.Instance[] getLootForPage(List<LootData.Loot.Instance> loot) {
        LootData.Loot.Instance[] pageLoot = new LootData.Loot.Instance[LOOT_PER_PAGE];

        for (int i = 0; i < LOOT_PER_PAGE; i++) {
            if (loot.size() > i)
                pageLoot[i] = loot.get(i);
        }

        if (page != 0) {
            for (int i = 0; i < page; i++) {
                for (int j = 0; j < LOOT_PER_PAGE; j++) {
                    int check = -1;
                    if ((j + 1) % 9 == 0)
                        check = (j + 1) / 9;

                    if (check != -1) {
                        int next = LOOT_PER_PAGE + check + (NEW_PER_PAGE * i);
                        pageLoot[j] = loot.size() > next ? loot.get(next) : null;
                    } else {
                        pageLoot[j] = pageLoot[j + 1];
                    }
                }
            }
        }

        return pageLoot;
    }

    private boolean canHaveMorePages(List<LootData.Loot.Instance> loot) {
        int lootAmount = loot.size();

        if (lootAmount <= LOOT_PER_PAGE)
            return false;

        int maxPage = lootAmount % NEW_PER_PAGE;
        maxPage = maxPage != 0 ? (lootAmount - LOOT_PER_PAGE + (NEW_PER_PAGE - maxPage)) / NEW_PER_PAGE : (lootAmount - LOOT_PER_PAGE) / NEW_PER_PAGE;

        return maxPage > page;
    }
}
