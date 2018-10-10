package com.orbitmines.spigot.servers.survival.events;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.achievements.StoredProgressAchievement;
import com.orbitmines.spigot.api.utils.LocationUtils;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalAchievements;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class AchievementEvents implements Listener {

    private final OrbitMines orbitMines;

    private final List<DroppedItem> droppedItems;

    public AchievementEvents(OrbitMines orbitMines) {
        this.orbitMines = orbitMines;

        this.droppedItems = new ArrayList<>();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() != Material.DIAMOND_ORE)
            return;

        SurvivalPlayer omp = SurvivalPlayer.getPlayer(event.getPlayer());

        DroppedItem item = new DroppedItem(omp, event.getBlock().getLocation());
        droppedItems.add(item);

        new BukkitRunnable() {
            @Override
            public void run() {
                droppedItems.remove(item);
            }
        }.runTaskLater(orbitMines, 3);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSpawn(ItemSpawnEvent event) {
        if (event.getEntity().getItemStack().getType() == Material.DIAMOND) {
            for (DroppedItem droppedItem : droppedItems) {
                if (droppedItem.equals(event.getLocation())) {
                    StoredProgressAchievement handler = (StoredProgressAchievement) SurvivalAchievements.DIAMONDS.getHandler();
                    handler.progress(droppedItem.omp, event.getEntity().getItemStack().getAmount(), true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExp(PlayerExpChangeEvent event) {
        SurvivalPlayer omp = SurvivalPlayer.getPlayer(event.getPlayer());

        StoredProgressAchievement handler = (StoredProgressAchievement) SurvivalAchievements.LOADS_OF_EXPERIENCE.getHandler();
        handler.setHighest(omp, omp.getPlayer().getLevel(), true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof WitherSkeleton))
            return;

        for (ItemStack item : event.getDrops()) {
            if (item.getType() == Material.WITHER_SKELETON_SKULL) {
                SurvivalPlayer omp = SurvivalPlayer.getPlayer(event.getEntity().getKiller());

                StoredProgressAchievement handler = (StoredProgressAchievement) SurvivalAchievements.TIME_WITHERED_AWAY.getHandler();
                handler.progress(omp, 1, true);
            }
        }
    }

    private class DroppedItem {

        private final SurvivalPlayer omp;
        private final Location location;

        public DroppedItem(SurvivalPlayer omp, Location location) {
            this.omp = omp;
            this.location = location;
        }

        public boolean equals(Location location) {
            return LocationUtils.equals(this.location.getBlock(), location.getBlock());
        }
    }
}
