package com.orbitmines.spigot.servers.kitpvp.handlers.gui;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.servers.kitpvp.KitPvP;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPData;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPKit;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;

public class KitSelectorGUI extends GUI {

    private final KitPvP kitPvP;

    public KitSelectorGUI(KitPvP kitPvP) {
        newInventory(9, "§0§lKit Selector");

        this.kitPvP = kitPvP;
    }

    @Override
    protected boolean onOpen(OMPlayer player) {
        KitPvPPlayer omp = (KitPvPPlayer) player;

        if (omp.getSelectedKit() != null || omp.isSpectator()) {
            omp.sendMessage("Kit", Color.RED, "Je kan de §c§lKit Selector§7 niet gebruiken in de arena!", "You can't use the §c§lKit Selector§7 in the arena!");
            return false;
        }

        boolean saturday = kitPvP.isSaturday();

        for (KitPvPKit kit : KitPvPKit.getKits()) {
            KitPvPData.KitData data = omp.getKitData(kit);

            ItemBuilder icon = kit.getIcon().setDisplayName(kit.getColor().getChatColor() + "§l" + kit.getName());
            int level = data.getUnlockedLevel();

            if (level != 0 || saturday) {
                icon.glow();

                if (level == 0) {
//                    icon.setDisplayName(icon.getDisplayName() + " §d§l§m" + omp.lang("VERGRENDELD", "LOCKED"));
//
//                    icon.addLore("§d§lFREE KIT SATURDAY");
                    icon.setDisplayName(icon.getDisplayName() + " §d§l§m" + omp.lang("VERGRENDELD", "LOCKED"));

                    icon.addLore("§c§lFREE KITS (KITPVP RELEASE)");
                } else {
                    icon.setAmount(level);

                    icon.setDisplayName(icon.getDisplayName() + " §a§lLvl " + level);
                }

                icon.addLore("");
                icon.addLore("§e§l" + omp.lang("RECHTERMUISKLIK", "RIGHT CLICK") + " §8- §a" + omp.lang("Selecteer Kit", "Select Kit"));
                icon.addLore("§6§l" + omp.lang("LINKERMUISKLIK", "LEFT CLICK") + " §8- " + (level == 0 ? "§7" + omp.lang("Koop Kit", "Purchase Kit") : "§7Details" + (level != kit.getLevels().length ? " & Upgrade Kit" : "")));
            } else {
                icon.setDisplayName(icon.getDisplayName() + " §4§l" + omp.lang("VERGRENDELD", "LOCKED"));

                icon.addLore("");
                icon.addLore("§c§l§m" + omp.lang("RECHTERMUISKLIK", "RIGHT CLICK") + " §c§m- " + omp.lang("Selecteer Kit", "Select Kit"));
                icon.addLore("§6§l" + omp.lang("LINKERMUISKLIK", "LEFT CLICK") + " §8- §a" + omp.lang("Koop Kit", "Purchase Kit"));
            }

            icon.addLore("");
            icon.addLore(omp.lang("§7Jouw Statistieken", "§7Your Statistics"));
            icon.addLore("  §7Kills: §c§l" + NumberUtils.locale(data.getKills()));
            icon.addLore("  §7Deaths: §4§l" + NumberUtils.locale(data.getDeaths()));
            icon.addLore("  §7Best streak: §5§l" + NumberUtils.locale(data.getBestStreak()));

            ItemInstance instance;
            if (level != 0 || saturday)
                instance = new ItemInstance(icon.build()) {
                    @Override
                    public void onClick(InventoryClickEvent event, OMPlayer player) {
                        switch (event.getAction()) {
                            /* Right Click */
                            case PICKUP_HALF:
                                omp.joinMap(kit.getLevel(level == 0 ? 1 : level));
                                break;

                            /* Left Click */
                            case PICKUP_ALL:
                                new KitInfoGUI(kitPvP, kit, level == 0 ? 1 : level).open(player);
                                break;
                        }
                    }
                };
            else
                instance = new ItemInstance(icon.build()) {
                    @Override
                    public void onClick(InventoryClickEvent event, OMPlayer player) {
                        switch (event.getAction()) {
                            /* Right Click */
                            case PICKUP_HALF:
                                break;

                            /* Left Click */
                            case PICKUP_ALL:
                                new KitInfoGUI(kitPvP, kit, 1).open(player);
                                break;
                        }
                    }
                };

            add((int) kit.getId(), instance);
        }

        omp.playSound(Sound.ITEM_ARMOR_EQUIP_DIAMOND);

        return true;
    }
}
