package com.orbitmines.spigot.servers.kitpvp.events;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.spigot.servers.kitpvp.handlers.LevelData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ExpChangeEvent implements Listener {

    private final OrbitMines orbitMines;

    public ExpChangeEvent(OrbitMines orbitMines) {
        this.orbitMines = orbitMines;
    }

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent event) {
        KitPvPPlayer omp = KitPvPPlayer.getPlayer(event.getPlayer());
        LevelData levelData = omp.getLevelData();

        new BukkitRunnable() {
            @Override
            public void run() {
                levelData.updateExperienceBar();
            }
        }.runTaskLater(orbitMines, 1);
    }
}
