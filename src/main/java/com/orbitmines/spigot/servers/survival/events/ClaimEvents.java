package com.orbitmines.spigot.servers.survival.events;

import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.claim.Claim;
import com.orbitmines.spigot.servers.survival.handlers.region.Region;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class ClaimEvents implements Listener {

    private Survival survival;



    public ClaimEvents(Survival survival) {
        this.survival = survival;
    }

//    @EventHandler
//    public void onInteract(PlayerInteractEvent event) {
//        Player p = event.getPlayer();
//        ItemStack item = event.getItem();
//
//        if (p.getWorld().getName().equals(survival.getWorld().getName()) || item == null || item.getType() != Material.STONE_HOE)
//            return;
//
//        event.setCancelled(true);
//        SurvivalPlayer.getPlayer(p).updateInventory();
//    }

    @EventHandler
    public void onEntityBlockForm(EntityBlockFormEvent event) {
        if (!event.getBlock().getWorld().getName().equals(survival.getWorld().getName()))
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        SurvivalPlayer omp = SurvivalPlayer.getPlayer((Player) event.getEntity());

        if (Region.isInRegion(omp, event.getBlock().getLocation()) || !survival.getClaimHandler().canBuild(omp, event.getBlock().getLocation(), event.getNewState().getType()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (!event.getBlock().getWorld().getName().equals(survival.getWorld().getName()))
            return;

        Block block = event.getBlock();

        if (event.getEntity() instanceof Wither) {
            /* Wither breaking blocks */

            if (Region.isInRegion(block.getLocation()) || survival.getClaimHandler().getClaimAt(block.getLocation(), false, null) != null)
                event.setCancelled(true);

        } else if (event.getTo() == Material.DIRT && block.getType() == Material.SOIL) {

            /* Trampled crops */
            if (event.getEntity() instanceof Player) {
                if (Region.isInRegion(block.getLocation()) || survival.getClaimHandler().getClaimAt(block.getLocation(), false, null) != null)
                    event.setCancelled(true);
            } else {
                SurvivalPlayer omp = SurvivalPlayer.getPlayer((Player) event.getEntity());

                if (Region.isInRegion(omp, block.getLocation()) || !survival.getClaimHandler().canBuild(omp, block.getLocation(), block.getType()))
                    event.setCancelled(true);
            }
        } else if (event.getEntity() instanceof FallingBlock) {
            /* Sand Cannon Fix */

            FallingBlock entity = (FallingBlock) event.getEntity();

            if (event.getTo() == Material.AIR) {
                entity.setMetadata("SURVIVAL_FALLINGBLOCK", new FixedMetadataValue(survival.getOrbitMines(), block.getLocation()));
            } else {
                List<MetadataValue> values = entity.getMetadata("SURVIVAL_FALLINGBLOCK");

                if (values.size() < 1)
                    return;

                Location start = (Location) values.get(0).value();
                Location next = block.getLocation();

                if (start.getBlockX() != next.getBlockX() || start.getBlockZ() != next.getBlockZ()) {
                    Claim claim = survival.getClaimHandler().getClaimAt(next, false, null);

                    if (Region.isInRegion(next) || !claim.inClaim(start, false, false)) {
                        event.setCancelled(true);

                        ItemStack itemStack = new ItemStack(entity.getMaterial(), 1, entity.getBlockData());
                        Item item = block.getWorld().dropItem(entity.getLocation(), itemStack);
                        item.setVelocity(new Vector());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPortalEnter(EntityPortalEnterEvent event) {
        if (!(event.getEntity() instanceof FallingBlock))
            return;

        event.getEntity().removeMetadata("SURVIVAL_FALLINGBLOCK", survival.getOrbitMines());
    }

    @EventHandler
    public void onPortalExit(EntityPortalExitEvent event) {
        if (!(event.getEntity() instanceof TNTPrimed) || event.getTo().getWorld().getEnvironment() != World.Environment.THE_END)
            return;

        event.getEntity().remove();
    }

    @EventHandler
    public void onInteract(EntityInteractEvent event) {
        if (event.getBlock().getType() != Material.SOIL)
            return;

        if (!event.getBlock().getWorld().getName().equals(survival.getWorld().getName()))
            return;

        for (Entity passenger : event.getEntity().getPassengers()) {
            if (!(passenger instanceof Player))
                continue;

            SurvivalPlayer omp = SurvivalPlayer.getPlayer((Player) passenger);

            if (Region.isInRegion(omp, event.getBlock().getLocation()) || !survival.getClaimHandler().canBuild(omp, event.getBlock().getLocation(), event.getBlock().getRelative(BlockFace.UP).getType())) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        handleExplosion(event.getLocation(), event.blockList());
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent event) {
        handleExplosion(event.getBlock().getLocation(), event.blockList());
    }

    private void handleExplosion(Location location, List<Block> blocks) {
        if (!location.getWorld().getName().equals(survival.getWorld().getName()))
            return;

        Claim cached = null;

        for (Block block : new ArrayList<>(blocks)) {
            if (block.getType() == Material.AIR)
                continue;

            if (Region.isInRegion(block.getLocation())) {
                blocks.remove(block);
                continue;
            }

            Claim claim = survival.getClaimHandler().getClaimAt(block.getLocation(), false, cached);

            if (claim != null) {
                cached = claim;
                blocks.remove(block);
            }
        }
    }
}
