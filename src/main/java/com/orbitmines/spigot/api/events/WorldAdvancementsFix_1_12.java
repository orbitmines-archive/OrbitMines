package com.orbitmines.spigot.api.events;

import com.orbitmines.spigot.OrbitMines;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class WorldAdvancementsFix_1_12 implements Listener {

    public WorldAdvancementsFix_1_12() {
        for (World world : Bukkit.getWorlds()) {
            hideAdvancementsFor(world);
        }
    }

    private void hideAdvancementsFor(World world) {
        world.setGameRuleValue("announceAdvancements", "false");
        OrbitMines.getInstance().getLogger().info("Achievements are now hidden for world '" + world.getName() + "'.");
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        hideAdvancementsFor(event.getWorld());
    }
}
