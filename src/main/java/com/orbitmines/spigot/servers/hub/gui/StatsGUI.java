package com.orbitmines.spigot.servers.hub.gui;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Message;
import com.orbitmines.api.Server;
import com.orbitmines.api.utils.TimeUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.CachedPlayer;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.data.PlayTimeData;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class StatsGUI extends GUI {

    private OrbitMines orbitMines;

    public StatsGUI() {
        this.orbitMines = OrbitMines.getInstance();
        newInventory(54, "§0§lStats");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        add(1, 3, new EmptyItemInstance(new ItemBuilder(Material.DOUBLE_PLANT, 1, 0, "§e§l" + omp.getSolars() + " Solar" + (omp.getSolars() == 1 ? "" : "s")).build()));

        {
            PlayerSkullBuilder item = new PlayerSkullBuilder(() -> omp.getName(true), 1, omp.getName());
            item.addLore("§7Rank: " + (omp.getStaffRank() == null ? omp.getVipRank().getDisplayName() : (omp.getVipRank() == null ? omp.getStaffRank().getDisplayName() : omp.getStaffRank().getDisplayName() + "§7/" + omp.getVipRank().getDisplayName())));

            PlayTimeData data = (PlayTimeData) omp.getData(Data.Type.PLAY_TIME);
            /* Update Play Time */
            data.load();

            long totalTimePlayed = 0;
            for (Server server : Server.values()) {
                totalTimePlayed += data.getPlayTime().get(server);
            }
            item.addLore("§7Time Played: §a§l" + TimeUtils.biggestTimeUnit(totalTimePlayed * 1000));

            item.addLore(CachedPlayer.getPlayer(omp.getUUID()).getFirstLogin());

            item.addLore("§7Achievements: §d§l" + "0" /* TODO */ + "§7§l/ " + "0");

            item.addLore("§7Cosmetics");
            item.addLore("§7  Hats: §6§l" + "0" /* TODO */ + "§7§l/ " + "0");
            item.addLore("§7  Gadgets: §b§l" + "0" /* TODO */ + "§7§l/ " + "0");

            item.addLore("");
            item.addLore("");

            add(1, 4, new ItemInstance(item.build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    //TODO OPEN ALL ACHIEVEMENTS
                }
            });
        }

        add(1, 5, new EmptyItemInstance(new ItemBuilder(Material.PRISMARINE_SHARD, 1, 0, "§9§l" + omp.getPrisms() + " Prism" + (omp.getPrisms() == 1 ? "" : "s")).build()));

        EmptyItemInstance item = new EmptyItemInstance(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 14, new Message("§cOnbekende Galaxies", "§cUnknown Galaxies").lang(language)).build());
        add(3, 1, item);
        add(3, 3, item);
        add(3, 5, item);
        add(3, 7, item);
        add(4, 2, item);
        add(4, 6, item);

        return true;
    }

    private ItemInstance getItem(Server server) {
        ItemStack itemStack = null;
        switch (server) {

            case KITPVP:
                break;
            case PRISON:
                break;
            case CREATIVE:
                break;
            case HUB:
                break;
            case SURVIVAL:
                break;
            case SKYBLOCK:
                break;
            case FOG:
                break;
            case MINIGAMES:
                break;
            case UHSURVIVAL:
                break;
        }

        return new ItemInstance(itemStack) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {

            }
        };
    }
}
