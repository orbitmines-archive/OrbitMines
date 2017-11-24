package com.orbitmines.spigot.servers.survival.events;

import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.region.Region;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class RegionEvents implements Listener {

    private Survival survival;

    public RegionEvents(Survival survival) {
        this.survival = survival;
    }

    @EventHandler
    public void onBreak(BlockPlaceEvent event) {
        Player p = event.getPlayer();

        if (!p.getWorld().getName().equals(survival.getWorld().getName()))
            return;

        SurvivalPlayer omp = SurvivalPlayer.getPlayer(p);

        if (!Region.isInRegion(omp, event.getBlock().getLocation()))
            return;

        event.setCancelled(true);
        omp.updateInventory();
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        if (!p.getWorld().getName().equals(survival.getWorld().getName()))
            return;

        SurvivalPlayer omp = SurvivalPlayer.getPlayer(p);

        if (!Region.isInRegion(omp, event.getBlock().getLocation()))
            return;

        event.setCancelled(true);
    }
}
