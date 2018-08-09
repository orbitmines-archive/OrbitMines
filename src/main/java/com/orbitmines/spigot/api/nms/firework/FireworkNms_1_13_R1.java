package com.orbitmines.spigot.api.nms.firework;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.OrbitMines;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftFirework;
import org.bukkit.entity.Firework;
import org.bukkit.scheduler.BukkitRunnable;

public class FireworkNms_1_13_R1 implements FireworkNms {

    private final OrbitMines orbitMines;

    public FireworkNms_1_13_R1(OrbitMines orbitMines) {
        this.orbitMines = orbitMines;
    }

    @Override
    public void explode(Firework firework) {
        new BukkitRunnable() {
            public void run() {
                ((CraftFirework) firework).getHandle().expectedLifespan = 0;
            }
        }.runTaskLater(orbitMines, 1);
    }
}
