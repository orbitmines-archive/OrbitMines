package com.orbitmines.spigot.api.nms.firework;

import com.orbitmines.spigot.OrbitMines;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftFirework;
import org.bukkit.entity.Firework;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Fadi on 11-5-2016.
 */
public class FireworkNms_1_8_R2 implements FireworkNms {

    private OrbitMines orbitMines;

    public FireworkNms_1_8_R2() {
        this.orbitMines = OrbitMines.getInstance();
    }

    @Override
    public void explode(final Firework firework) {
        new BukkitRunnable() {
            public void run() {
                ((CraftFirework) firework).getHandle().expectedLifespan = 0;
            }
        }.runTaskLater(orbitMines, 1);
    }
}
