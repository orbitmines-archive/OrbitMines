package com.orbitmines.spigot.servers.hub.gui.discordgroup;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.data.DiscordGroupData;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.UUID;

public class DiscordGroupRequestGUI extends GUI implements DiscordGroupGUIInstance {

    private final int REQUESTS_PER_PAGE = 36;
    private final int NEW_PER_PAGE = REQUESTS_PER_PAGE / 9;

    private int page;

    public DiscordGroupRequestGUI() {
        this(0);
    }

    public DiscordGroupRequestGUI(int page) {
        this.page = page;

        newInventory(54, "§0§lDiscord Squads");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        DiscordGroupData data = (DiscordGroupData) omp.getData(Data.Type.DISCORD_GROUPS);
        List<UUID> sentRequests = data.getSentRequests();

        {
            add(0, 4, new ItemInstance(new PlayerSkullBuilder(() -> omp.getName(true), 1, "§9§l« Back to Manage").build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    new DiscordGroupManageGUI().open(omp);
                }
            });
        }

        int slot = 18;
        for (UUID uuid : getInvitesForPage(sentRequests)) {
            if (uuid != null) {
                CachedPlayer member = CachedPlayer.getPlayer(uuid);
                ItemBuilder item = new ItemBuilder(Material.MAP, 1, member.getRankPrefixColor().getChatColor() + member.getPlayerName());
                List<String> lore = item.getLore();

                lore.add(omp.lang("§7§oUitnodiging", "§7§oInvitation"));
                lore.add("");
                lore.add(omp.lang("§cKlik hier om te verwijderen.", "§cClick here to remove."));

                add(slot, new ItemInstance(item.build()) {
                    @Override
                    public void onClick(InventoryClickEvent event, OMPlayer omp) {
                        data.onRequestCancel(member);

                        if (data.getInvites().size() != 0 || data.getSentRequests().size() != 0)
                            reopen(omp);
                        else
                            new DiscordGroupManageGUI().open(omp);
                    }
                });
            } else {
                inventory.setItem(slot, null);
            }

            slot++;
        }

        if (page != 0)
            add(5, 0, new ItemInstance(new PlayerSkullBuilder(() -> "Blue Arrow Left", 1, omp.lang("§7« Meer Uitnodigingen", "§7« More Invites")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWFlNzg0NTFiZjI2Y2Y0OWZkNWY1NGNkOGYyYjM3Y2QyNWM5MmU1Y2E3NjI5OGIzNjM0Y2I1NDFlOWFkODkifX19").build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    page--;
                    omp.playSound(Sound.UI_BUTTON_CLICK);
                    reopen(omp);
                }
            });
        else
            clear(5, 0);

        if (canHaveMorePages(sentRequests.size()))
            add(5, 8, new ItemInstance(new PlayerSkullBuilder(() -> "Blue Arrow Right", 1, omp.lang("§7Meer Uitnodigingen »", "§7More Invites »")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTE3ZjM2NjZkM2NlZGZhZTU3Nzc4Yzc4MjMwZDQ4MGM3MTlmZDVmNjVmZmEyYWQzMjU1Mzg1ZTQzM2I4NmUifX19").build()) {
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

    @Override
    protected void onClick(InventoryClickEvent event, OMPlayer omp) {

    }

    private UUID[] getInvitesForPage(List<UUID> invites) {
        UUID[] pageInvites = new UUID[REQUESTS_PER_PAGE];

        for (int i = 0; i < REQUESTS_PER_PAGE; i++) {
            if (invites.size() > i)
                pageInvites[i] = invites.get(i);
        }

        if (page != 0) {
            for (int i = 0; i < page; i++) {
                for (int j = 0; j < REQUESTS_PER_PAGE; j++) {
                    int check = -1;
                    if ((j + 1) % 9 == 0)
                        check = (j + 1) / 9;

                    if (check != -1) {
                        int next = REQUESTS_PER_PAGE + check + (NEW_PER_PAGE * i) - 1;
                        pageInvites[j] = invites.size() > next ? invites.get(next) : null;
                    } else {
                        pageInvites[j] = pageInvites[j + 1];
                    }
                }
            }
        }

        return pageInvites;
    }

    private boolean canHaveMorePages(int count) {
        if (count <= REQUESTS_PER_PAGE)
            return false;

        int maxPage = count % NEW_PER_PAGE;
        maxPage = maxPage != 0 ? (count - REQUESTS_PER_PAGE + (NEW_PER_PAGE - maxPage)) / NEW_PER_PAGE : (count - REQUESTS_PER_PAGE) / NEW_PER_PAGE;

        return maxPage > page;
    }
}
