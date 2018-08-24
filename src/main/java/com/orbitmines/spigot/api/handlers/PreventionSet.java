package com.orbitmines.spigot.api.handlers;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class PreventionSet {

    private OrbitMines orbitMines;

    private Map<Prevention, Listener> listeners;
    private Map<Prevention, List<World>> worlds;

    public PreventionSet() {
        orbitMines = OrbitMines.getInstance();

        listeners = new HashMap<>();
        worlds = new HashMap<>();
    }

    public void prevent(World world, Prevention... preventions) {
        for (Prevention prevention : preventions) {
            prevent(world, prevention);
        }
    }

    public void prevent(World world, Prevention prevention) {
        Listener listener = null;

        switch (prevention) {

            case CHUNK_UNLOAD:
                listener = new PreventChunkUnload();
                break;
            case FOOD_CHANGE:
                listener = new PreventFood();
                break;
            case PVP:
                listener = new PreventPvP();
                break;
            case MOB_SPAWN:
                listener = new PreventMobSpawn();
                break;
            case WEATHER_CHANGE:
                listener = new PreventWeatherChange();
                break;
            case FALL_DAMAGE:
                listener = new PreventFallDamage();
                break;
            case BLOCK_PLACE:
                listener = new PreventBlockPlace();
                break;
            case BLOCK_BREAK:
                listener = new PreventBlockBreak();
                break;
            case BLOCK_INTERACTING:
                listener = new PreventBlockInteracting();
                break;
            case BLOCK_SPREAD:
                listener = new PreventBlockSpread();
                break;
            case MONSTER_EGG_USAGE:
                listener = new PreventMonsterEggUsage();
                break;
            case SWAP_HAND_ITEMS:
                listener = new PreventSwapHandItems();
                break;
            case ITEM_DROP:
                listener = new PreventItemDrop();
                break;
            case ITEM_PICKUP:
                listener = new PreventItemPickup();
                break;
            case PHYSICAL_INTERACTING:
                listener = new PreventPhysicalInteracting();
                break;
            case PHYSICAL_INTERACTING_EXCEPT_PLATES:
                listener = new PreventPhysicalExceptPlatesInteracting();
                break;
            case BUCKET_USAGE:
                listener = new PreventBucketUsage();
                break;
            case CLICK_PLAYER_INVENTORY:
                listener = new PreventClickPlayerInventory();
                break;
            case PLAYER_DAMAGE:
                listener = new PreventPlayerDamage();
                break;
            case LEAF_DECAY:
                listener = new PreventLeafDecay();
                break;
            case ENTITY_INTERACTING:
                listener = new PreventEntityInteracting();
                break;
            case EXPLOSION_DAMAGE:
                listener = new PreventExplosionDamage();
                break;
        }

        listeners.put(prevention, listener);

        if (!worlds.containsKey(prevention))
            worlds.put(prevention, new ArrayList<>());

        worlds.get(prevention).add(world);

        orbitMines.getServer().getPluginManager().registerEvents(listener, orbitMines);
    }

    public void unregister() {
        for (Prevention prevention : listeners.keySet()) {
            HandlerList.unregisterAll(listeners.get(prevention));
            worlds.remove(prevention);
        }
    }

    public enum Prevention {

        CHUNK_UNLOAD,
        FOOD_CHANGE,
        PVP,
        MOB_SPAWN,
        WEATHER_CHANGE,
        FALL_DAMAGE,
        BLOCK_PLACE,
        BLOCK_BREAK,
        BLOCK_INTERACTING,
        BLOCK_SPREAD,
        MONSTER_EGG_USAGE,
        SWAP_HAND_ITEMS,
        ITEM_DROP,
        ITEM_PICKUP,
        PHYSICAL_INTERACTING,
        PHYSICAL_INTERACTING_EXCEPT_PLATES,
        BUCKET_USAGE,
        CLICK_PLAYER_INVENTORY,
        PLAYER_DAMAGE,
        LEAF_DECAY,
        ENTITY_INTERACTING,
        EXPLOSION_DAMAGE

    }

    public class PreventChunkUnload implements Listener {

        @EventHandler
        public void preventChunkUnload(ChunkUnloadEvent event) {
            if (!worlds.get(Prevention.CHUNK_UNLOAD).contains(event.getWorld()))
                return;

            event.setCancelled(true);
        }
    }

    public class PreventFood implements Listener {

        @EventHandler
        public void preventFood(FoodLevelChangeEvent event) {
            if (!worlds.get(Prevention.FOOD_CHANGE).contains(event.getEntity().getWorld()))
                return;

            event.setCancelled(true);
        }
    }

    public class PreventPvP implements Listener {

        @EventHandler
        public void preventPvP(EntityDamageByEntityEvent event) {
            if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
                if (!worlds.get(Prevention.PVP).contains(event.getEntity().getWorld()))
                    return;

                event.setCancelled(true);
            }
        }
    }

    public class PreventMobSpawn implements Listener {

        @EventHandler
        public void preventMobSpawn(CreatureSpawnEvent event) {
            if (!(event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM)) {
                if (!worlds.get(Prevention.MOB_SPAWN).contains(event.getEntity().getWorld()))
                    return;

                event.setCancelled(true);
            }
        }
    }

    public class PreventWeatherChange implements Listener {

        @EventHandler
        public void preventWeatherChange(WeatherChangeEvent event) {
            if (!worlds.get(Prevention.WEATHER_CHANGE).contains(event.getWorld()))
                return;

            event.setCancelled(true);
        }
    }

    public class PreventFallDamage implements Listener {

        @EventHandler
        public void preventFallDamage(EntityDamageEvent event) {
            if (!(event.getEntity() instanceof Player))
                return;

            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (!worlds.get(Prevention.FALL_DAMAGE).contains(event.getEntity().getWorld()))
                    return;

                event.setCancelled(true);
            }
        }
    }

    public class PreventBlockPlace implements Listener {

        @EventHandler
        public void preventBlockPlace(BlockPlaceEvent event) {
            if (!worlds.get(Prevention.BLOCK_PLACE).contains(event.getPlayer().getWorld()))
                return;

            OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());

            if (!omp.isOpMode())
                event.setCancelled(true);
        }
    }

    public class PreventBlockBreak implements Listener {

        @EventHandler
        public void preventBlockBreak(BlockBreakEvent event) {
            if (!worlds.get(Prevention.BLOCK_BREAK).contains(event.getPlayer().getWorld()))
                return;

            OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());

            if (!omp.isOpMode())
                event.setCancelled(true);
        }
    }

    public class PreventBlockInteracting implements Listener {

        @EventHandler
        public void preventBlockInteracting(PlayerInteractEvent event) {
            if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
                return;

            Block block = event.getClickedBlock();

            if (!worlds.get(Prevention.BLOCK_INTERACTING).contains(block.getWorld()))
                return;

            if (ItemUtils.INTERACTABLE.contains(block.getType()))
                event.setCancelled(true);
        }
    }

    public class PreventBlockSpread implements Listener {

        @EventHandler
        public void preventBlockInteracting(BlockSpreadEvent event) {
            if (!worlds.get(Prevention.BLOCK_SPREAD).contains(event.getBlock().getWorld()))
                return;

            event.setCancelled(true);
        }
    }

    public class PreventMonsterEggUsage implements Listener {

        @EventHandler
        public void preventMonsterEggUsage(PlayerInteractEntityEvent event) {
            Player player = event.getPlayer();

            if (!worlds.get(Prevention.MONSTER_EGG_USAGE).contains(player.getWorld()))
                return;

            ItemStack mainHand = player.getInventory().getItemInMainHand();
            ItemStack offHand = player.getInventory().getItemInOffHand();

            if (mainHand != null && ItemUtils.EGGS.contains(mainHand.getType()) || offHand != null && ItemUtils.EGGS.contains(offHand.getType())) {
                event.setCancelled(true);
                OMPlayer.getPlayer(player).updateInventory();
            }
        }

        @EventHandler
        public void preventMonsterEggUsage(PlayerInteractAtEntityEvent event) {
            Player player = event.getPlayer();

            if (!worlds.get(Prevention.MONSTER_EGG_USAGE).contains(player.getWorld()))
                return;

            ItemStack mainHand = player.getInventory().getItemInMainHand();
            ItemStack offHand = player.getInventory().getItemInOffHand();

            if (mainHand != null && ItemUtils.EGGS.contains(mainHand.getType()) || offHand != null && ItemUtils.EGGS.contains(offHand.getType())) {
                event.setCancelled(true);
                OMPlayer.getPlayer(player).updateInventory();
            }
        }

        @EventHandler
        public void preventMonsterEggUsage(PlayerInteractEvent event) {
            Player player = event.getPlayer();

            if (!worlds.get(Prevention.MONSTER_EGG_USAGE).contains(player.getWorld()))
                return;

            ItemStack mainHand = player.getInventory().getItemInMainHand();
            ItemStack offHand = player.getInventory().getItemInOffHand();

            if (mainHand != null && ItemUtils.EGGS.contains(mainHand.getType()) || offHand != null && ItemUtils.EGGS.contains(offHand.getType())) {
                event.setCancelled(true);
                OMPlayer.getPlayer(player).updateInventory();
            }
        }
    }

    public class PreventSwapHandItems implements Listener {

        @EventHandler
        public void preventSwapHandItems(PlayerSwapHandItemsEvent event) {
            Player player = event.getPlayer();

            if (!worlds.get(Prevention.SWAP_HAND_ITEMS).contains(player.getWorld()))
                return;

            event.setCancelled(true);
        }
    }

    public class PreventItemDrop implements Listener {

        @EventHandler
        public void preventItemDrop(PlayerDropItemEvent event) {
            Player player = event.getPlayer();

            if (!worlds.get(Prevention.ITEM_DROP).contains(player.getWorld()))
                return;

            event.setCancelled(true);
        }
    }

    public class PreventItemPickup implements Listener {

        @EventHandler
        public void preventItemPickup(PlayerPickupItemEvent event) {
            Player player = event.getPlayer();

            if (!worlds.get(Prevention.ITEM_PICKUP).contains(player.getWorld()))
                return;

            event.setCancelled(true);
        }
    }

    public class PreventPhysicalInteracting implements Listener {

        @EventHandler
        public void preventPhysicalInteracting(PlayerInteractEvent event) {
            if (event.getAction() != Action.PHYSICAL || !worlds.get(Prevention.PHYSICAL_INTERACTING).contains(event.getPlayer().getWorld()))
                return;

            event.setCancelled(true);
        }
    }

    public class PreventPhysicalExceptPlatesInteracting implements Listener {

        @EventHandler
        public void preventPhysicalInteracting(PlayerInteractEvent event) {
            if (event.getAction() != Action.PHYSICAL || !worlds.get(Prevention.PHYSICAL_INTERACTING_EXCEPT_PLATES).contains(event.getPlayer().getWorld()) || ItemUtils.PRESSURE_PLATES.contains(event.getClickedBlock().getType()))
                return;

            event.setCancelled(true);
        }
    }

    public class PreventBucketUsage implements Listener {

        private final List<Material> buckets = Arrays.asList(Material.WATER_BUCKET, Material.LAVA_BUCKET);

        @EventHandler
        public void preventBucketUsage(PlayerInteractEvent event) {
            if (event.getAction() != Action.RIGHT_CLICK_BLOCK || !worlds.get(Prevention.BLOCK_INTERACTING).contains(event.getPlayer().getWorld()) || event.getItem() == null || !buckets.contains(event.getItem().getType()))
                return;

            event.setCancelled(true);
        }
    }

    public class PreventClickPlayerInventory implements Listener {

        @EventHandler
        public void preventClickPlayerInventory(InventoryClickEvent event) {
            if (event.getClickedInventory() == null || !(event.getClickedInventory() instanceof PlayerInventory) || !worlds.get(Prevention.CLICK_PLAYER_INVENTORY).contains(event.getWhoClicked().getWorld()))
                return;

            event.setCancelled(true);
        }
    }

    public class PreventPlayerDamage implements Listener {

        @EventHandler
        public void preventPlayerDamage(EntityDamageEvent event) {
            if (!(event.getEntity() instanceof Player) || !worlds.get(Prevention.PLAYER_DAMAGE).contains(event.getEntity().getWorld()))
                return;

            event.setCancelled(true);
        }
    }

    public class PreventLeafDecay implements Listener {

        @EventHandler
        public void preventLeafDecay(LeavesDecayEvent event) {
            if (!worlds.get(Prevention.LEAF_DECAY).contains(event.getBlock().getWorld()))
                return;

            event.setCancelled(true);
        }
    }

    public class PreventEntityInteracting implements Listener {

        @EventHandler
        public void preventEntityInteracting(PlayerInteractEntityEvent event) {
            if (!worlds.get(Prevention.ENTITY_INTERACTING).contains(event.getPlayer().getWorld()))
                return;

            event.setCancelled(true);
        }

        @EventHandler
        public void preventEntityInteracting(PlayerInteractAtEntityEvent event) {
            if (!worlds.get(Prevention.ENTITY_INTERACTING).contains(event.getPlayer().getWorld()))
                return;

            event.setCancelled(true);
        }
    }

    public class PreventExplosionDamage implements Listener {

        @EventHandler
        public void preventEntityInteracting(EntityExplodeEvent event) {
            if (!worlds.get(Prevention.EXPLOSION_DAMAGE).contains(event.getLocation().getWorld()))
                return;

            event.setCancelled(true);
        }
    }
}
