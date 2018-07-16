package com.orbitmines.spigot.servers.hub.gui.stats;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Server;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.achievements.Achievement;
import com.orbitmines.spigot.api.handlers.achievements.AchievementHandler;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ServerStatsGUI extends GUI {

    protected final OMPlayer player;
    protected final Server server;

    public ServerStatsGUI(OMPlayer player, Server server) {
        this.player = player;
        this.server = server;

        newInventory(54, "§0§lStats (" + player.getName(true) + ")");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        add(1, 3, new ItemInstance(new PlayerSkullBuilder(() -> "Lime Arrow Left", 1, omp.lang("§a« Terug naar Stats", "§a« Back to General Stats")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjUzNDc0MjNlZTU1ZGFhNzkyMzY2OGZjYTg1ODE5ODVmZjUzODlhNDU0MzUzMjFlZmFkNTM3YWYyM2QifX19").build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new StatsGUI(player).open(omp);
            }
        });

        add(1, 4, new EmptyItemInstance(StatsGUI.getItem(player, server, false)));

        {
            Achievement[] achievements = server.achievementValues();

            int size = achievements.length;

            /* In order to center  */
            int slot = 4 - (size % 2 == 0 ? size / 2 : (size - 1) / 2);

            for (int i = 0; i < size; i++) {
                /* Skip middle slot whenever a the amount of types is even to center types correctly */
                if (slot == 4 && size % 2 == 0) {
                    clear(3, slot);
                    slot++;
                }

                if (i < size) {
                    Achievement achievement = achievements[i];
                    AchievementHandler handler = achievement.getHandler();

                    add(3, slot, new EmptyItemInstance(handler.getItemBuilder(player).build()));
                } else {
                    clear(3, slot);
                }

                slot++;
            }
        }

        return true;
    }
}
