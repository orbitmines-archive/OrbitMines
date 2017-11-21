package com.orbitmines.spigot.api.nms.firework;

import com.orbitmines.spigot.api.OrbitMinesApi;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftFirework;
import org.bukkit.entity.Firework;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Fadi on 11-5-2016.
 */
public class FireworkNms_1_8_R1 implements FireworkNms {

    private OrbitMinesApi api;

    public FireworkNms_1_8_R1() {
        this.api = OrbitMinesApi.getApi();
    }

    @Override
    public void explode(final Firework firework) {
        new BukkitRunnable() {
            public void run() {
                ((CraftFirework) firework).getHandle().expectedLifespan = 0;
            }
        }.runTaskLater(api, 1);
    }
}
