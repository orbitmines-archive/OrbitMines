package com.orbitmines.spigot.servers.survival.events;

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.Color;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.claim.Claim;
import com.orbitmines.spigot.servers.survival.handlers.claim.ItemProtection;
import com.orbitmines.spigot.servers.survival.handlers.region.Region;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Hopper;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dispenser;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class BlockEvents implements Listener {

    private Survival survival;

    public BlockEvents(Survival survival) {
        this.survival = survival;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();

        if (!p.getWorld().getName().equals(survival.getWorld().getName()))
            return;

        Block block = event.getBlock();
        SurvivalPlayer omp = SurvivalPlayer.getPlayer(p);

        if (Region.isInRegion(omp, block.getLocation()) || !survival.getClaimHandler().canBuild(omp, block.getLocation(), block.getType()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player p = event.getPlayer();

        if (!p.getWorld().getName().equals(survival.getWorld().getName()))
            return;

        Block block = event.getBlock();
        SurvivalPlayer omp = SurvivalPlayer.getPlayer(p);

        if (Region.isInRegion(omp, block.getLocation()) || !survival.getClaimHandler().canBuild(omp, block.getLocation(), event.getBlockReplacedState().getType())) {
            event.setCancelled(true);
            omp.updateInventory();
        }
    }

    @EventHandler
    public void onMultiPlace(BlockMultiPlaceEvent event) {
        Player p = event.getPlayer();

        if (!p.getWorld().getName().equals(survival.getWorld().getName()))
            return;

        SurvivalPlayer omp = SurvivalPlayer.getPlayer(p);

        for (BlockState block : event.getReplacedBlockStates()) {
            if (Region.isInRegion(omp, block.getLocation()) || !survival.getClaimHandler().canBuild(omp, block.getLocation(), event.getBlockReplacedState().getType())) {
                event.setCancelled(true);
                omp.updateInventory();

                return;
            }
        }
    }

    @EventHandler
    public void onPisonExtend(BlockPistonExtendEvent event) {
        if (event.getDirection() == BlockFace.DOWN)
            return;

        if (!event.getBlock().getWorld().getName().equals(survival.getWorld().getName()))
            return;

        Block piston = event.getBlock();
        List<Block> blocks = event.getBlocks();

        if (blocks.size() == 0) {
            Block invaded = piston.getRelative(event.getDirection());

            if (invaded.getType() == Material.AIR)
                return;

            if (survival.getClaimHandler().getClaimAt(piston.getLocation(), false, null) == null && (Region.isInRegion(invaded.getLocation()) || survival.getClaimHandler().getClaimAt(invaded.getLocation(), false, null) != null))
                event.setCancelled(true);

            return;
        }

        String pistonOwner = "";
        Claim claim = survival.getClaimHandler().getClaimAt(piston.getLocation(), false, null);

        if (claim != null)
            pistonOwner = claim.getOwnerName();

        /* Check what blocks are being pushed */
        Claim cached = claim;
        for (Block block : blocks) {
            /* If any blocks that are being pushed are in a region, or in someone else's claim */

            if (Region.isInRegion(block.getLocation())) {
                event.setCancelled(true);
                destroyPiston(piston, piston.getType());
                return;
            }

            claim = survival.getClaimHandler().getClaimAt(block.getLocation(), false, cached);

            if (claim == null)
                continue;

            cached = claim;

            if (claim.getOwnerName().equals(pistonOwner))
                continue;

            event.setCancelled(true);
            destroyPiston(piston, piston.getType());
            return;
        }

        /* If any blocks are pushed into a claim */
        for (Block block : blocks) {
            claim = survival.getClaimHandler().getClaimAt(block.getLocation(), false, cached);

            String ownerName = "";
            if (claim != null) {
                cached = claim;
                ownerName = claim.getOwnerName();
            }

            Claim next = survival.getClaimHandler().getClaimAt(block.getRelative(event.getDirection()).getLocation(), false, cached);

            String nextOwnerName = "";
            if (next != null)
                nextOwnerName = next.getOwnerName();

            if (!nextOwnerName.equals(ownerName) && !nextOwnerName.isEmpty()) {
                event.setCancelled(true);
                destroyPiston(piston, piston.getType());
            }
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        if (event.getDirection() == BlockFace.UP)
            return;

        if (!event.getBlock().getWorld().getName().equals(survival.getWorld().getName()))
            return;

        Block piston = event.getBlock();

        String pistomOwner = "";
        Claim claim = survival.getClaimHandler().getClaimAt(piston.getLocation(), false, null);

        if (claim != null)
            pistomOwner = claim.getOwnerName();

        for (Block block : event.getBlocks()) {
            if (Region.isInRegion(block.getLocation())) {
                event.setCancelled(true);
                destroyPiston(piston, Material.PISTON_STICKY_BASE);
                return;
            }

            Claim next = survival.getClaimHandler().getClaimAt(block.getLocation(), false, claim);

            String nextOwnerName = "";
            if (next != null)
                nextOwnerName = next.getOwnerName();

            if (!nextOwnerName.equals(pistomOwner)) {
                event.setCancelled(true);
                destroyPiston(piston, Material.PISTON_STICKY_BASE);
                return;
            }
        }
    }

    private void destroyPiston(Block piston, Material material) {
        piston.getWorld().createExplosion(piston.getLocation(), 0);
        piston.getWorld().dropItem(piston.getLocation(), new ItemStack(material));
        piston.setType(Material.AIR);
    }

    @EventHandler
    public void onSpread(BlockSpreadEvent event) {
        if (event.getSource().getType() != Material.FIRE)
            return;

        if (!event.getBlock().getWorld().getName().equals(survival.getWorld().getName()))
            return;

        if (Region.isInRegion(event.getBlock().getLocation()) || survival.getClaimHandler().getClaimAt(event.getBlock().getLocation(), false, null) != null)
            event.setCancelled(true);
    }

    @EventHandler
    public void onBurn(BlockBurnEvent event) {
        if (!event.getBlock().getWorld().getName().equals(survival.getWorld().getName()))
            return;

        if (Region.isInRegion(event.getBlock().getLocation()) || survival.getClaimHandler().getClaimAt(event.getBlock().getLocation(), false, null) != null)
            event.setCancelled(true);
    }

    private Claim lastSpread = null;

    @EventHandler
    public void onFromTo(BlockFromToEvent event) {
        if (event.getFace() == BlockFace.DOWN)
            return;

        if (!event.getBlock().getWorld().getName().equals(survival.getWorld().getName()))
            return;

        Block to = event.getToBlock();

        if (Region.isInRegion(to.getLocation())) {
            event.setCancelled(true);
            return;
        }

        Claim claim = survival.getClaimHandler().getClaimAt(to.getLocation(), false, lastSpread);

        if (claim == null)
            return;

        this.lastSpread = claim;

        if (claim.inClaim(event.getBlock().getLocation(), false, true))
            return;

        if (!claim.hasParent() || !claim.inClaim(event.getBlock().getLocation(), false, false))
            event.setCancelled(true);
    }

    @EventHandler
    public void onDispense(BlockDispenseEvent event) {
        if (!event.getBlock().getWorld().getName().equals(survival.getWorld().getName()))
            return;

        Block from = event.getBlock();
        Dispenser dispenser = new Dispenser(Material.DISPENSER, from.getData());

        Block to = from.getRelative(dispenser.getFacing());

        if (Region.isInRegion(to.getLocation())) {
            event.setCancelled(true);
            return;
        }

        Claim fromClaim = survival.getClaimHandler().getClaimAt(from.getLocation(), false, null);
        Claim toClaim = survival.getClaimHandler().getClaimAt(to.getLocation(), false, fromClaim);

        if (fromClaim == null && toClaim == null)
            return;

        if (fromClaim == toClaim)
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onTreeGrow(StructureGrowEvent event) {
        if (!event.getWorld().getName().equals(survival.getWorld().getName()))
            return;

        Location root = event.getLocation();
        Claim claim = survival.getClaimHandler().getClaimAt(root, false, null);

        String ownerName = null;

        if (claim != null) {
            if (claim.hasParent())
                claim = claim.getParent();

            ownerName = claim.getOwnerName();
        }

        for (BlockState block : new ArrayList<>(event.getBlocks())) {
            if (Region.isInRegion(block.getLocation())) {
                event.getBlocks().remove(block);
                continue;
            }

            Claim blockClaim = survival.getClaimHandler().getClaimAt(block.getLocation(), false, claim);

            if (blockClaim != null && (ownerName == null || !ownerName.equals(blockClaim.getOwnerName())))
                event.getBlocks().remove(block);
        }
    }

    @EventHandler
    public void onPickupItem(InventoryPickupItemEvent event) {
        /* Prevent hoppers from picking up items that are protected */
        InventoryHolder holder = event.getInventory().getHolder();

        if (holder instanceof HopperMinecart || holder instanceof Hopper) {
            Item item = event.getItem();
            List<MetadataValue> data = item.getMetadata("SURVIVAL_PROTECTED");

            if (data != null && data.size() > 0)
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        List<ItemProtection> items = ItemProtection.getItems();

        Item item = event.getEntity();
        long now = System.currentTimeMillis();

        for (ItemProtection protection : new ArrayList<>(items)) {
            if (protection.getExpirationTimestamp() < now) {
                items.remove(protection);
                continue;
            }

            if (protection.getItemStack().getAmount() != item.getItemStack().getAmount() || protection.getItemStack().getType() != item.getItemStack().getType())
                continue;

            Location spawn = event.getLocation();
            Location expected = protection.getLocation();

            if (!spawn.getWorld().equals(expected.getWorld()) ||
                    spawn.getX() < expected.getX() - 5 ||
                    spawn.getX() > expected.getX() + 5 ||
                    spawn.getZ() < expected.getZ() - 5 ||
                    spawn.getZ() > expected.getZ() + 5 ||
                    spawn.getY() < expected.getY() - 15 ||
                    spawn.getY() > expected.getY() + 3)
                continue;

            item.setMetadata("SURVIVAL_PROTECTED", new FixedMetadataValue(survival.getOrbitMines(), protection.getOwner()));

            items.remove(protection);
            break;
        }
    }

    @EventHandler
    public void onHangingBreak(HangingBreakEvent e) {
        if (!e.getEntity().getWorld().getName().equals(survival.getWorld().getName()))
            return;

        if (e.getCause() == HangingBreakEvent.RemoveCause.EXPLOSION) {
            e.setCancelled(true);
            return;
        }

        if (!(e instanceof HangingBreakByEntityEvent)) {
            e.setCancelled(true);
            return;
        }

        HangingBreakByEntityEvent event = (HangingBreakByEntityEvent) e;

        if (!(event.getRemover() instanceof Player)) {
            event.setCancelled(true);
            return;
        }

        SurvivalPlayer omp = SurvivalPlayer.getPlayer((Player) event.getRemover());

        if (Region.isInRegion(omp, event.getEntity().getLocation()) || !survival.getClaimHandler().canBuild(omp, event.getEntity().getLocation(), Material.AIR))
            event.setCancelled(true);
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        if (!event.getEntity().getWorld().getName().equals(survival.getWorld().getName()))
            return;

        SurvivalPlayer omp = SurvivalPlayer.getPlayer(event.getPlayer());

        if (Region.isInRegion(omp, event.getEntity().getLocation()) || !survival.getClaimHandler().canBuild(omp, event.getEntity().getLocation(), Material.PAINTING))
            event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        handleEntityDamageEvent(event);
    }

    @EventHandler
    public void onCombust(EntityCombustByEntityEvent event) {
        EntityDamageByEntityEvent e = new EntityDamageByEntityEvent(event.getCombuster(), event.getEntity(), EntityDamageEvent.DamageCause.FIRE_TICK, event.getDuration());
        handleEntityDamageEvent(e);
        event.setCancelled(e.isCancelled());
    }

    private void handleEntityDamageEvent(EntityDamageEvent e) {
        if (isMonster(e.getEntity()))
            return;

        if (e.getEntityType() == EntityType.DROPPED_ITEM && e.getEntity().hasMetadata("SURVIVAL_PROTECTED")) {
            e.setCancelled(true);
            return;
        }

        if (e.getCause() != null && e.getEntity() instanceof Tameable) {
            Tameable tameable = (Tameable) e.getEntity();

            if (tameable.isTamed()) {

                switch (e.getCause()) {
                    case ENTITY_EXPLOSION:
                    case FALLING_BLOCK:
                    case FIRE:
                    case FIRE_TICK:
                    case LAVA:
                    case SUFFOCATION:
                        e.setCancelled(true);
                        break;
                }
            }
        }

        if (!e.getEntity().getWorld().getName().equals(survival.getWorld().getName()))
            return;

        if (!(e instanceof EntityDamageByEntityEvent))
            return;

        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;

        Player attacker = null;
        Projectile arrow = null;
        Entity damager = event.getDamager();

        if (damager != null) {
            if (damager instanceof Player) {
                attacker = (Player) damager;
            } else if (damager instanceof Projectile) {
                arrow = (Projectile) damager;

                if (arrow.getShooter() instanceof Player)
                    attacker = (Player) arrow.getShooter();
            }
        }

        switch (event.getEntityType()) {
            case ITEM_FRAME:
            case ARMOR_STAND:
            case VILLAGER:
            case ENDER_CRYSTAL: {

                SurvivalPlayer omp = null;
                if (attacker != null)
                    omp = SurvivalPlayer.getPlayer(attacker);

                Claim claim = survival.getClaimHandler().getClaimAt(event.getEntity().getLocation(), false, omp == null ? null : omp.getLastClaim());

                if (claim != null) {
                    if (omp == null) {
                        if (event.getEntity() instanceof Villager && damager != null && damager instanceof Zombie)
                            return;

                        event.setCancelled(true);
                        return;
                    }

                    if (Region.isInRegion(omp, event.getEntity().getLocation()) || !claim.canBuild(omp, Material.AIR))
                        event.setCancelled(true);
                }

                break;
            }
        }

        if (event.getEntity() instanceof Creature || event.getEntity() instanceof WaterMob) {
            if (event.getEntity() instanceof Tameable) {
                Tameable tameable = (Tameable) event.getEntity();

                if (attacker != null && tameable.isTamed() && tameable.getOwner() != null) {
                    SurvivalPlayer omp = SurvivalPlayer.getPlayer(attacker);
                    UUID owner = tameable.getOwner().getUniqueId();

                    if (omp.getUUID().equals(owner))
                        return;

                    if (omp.isOpMode())
                        return;

                    CachedPlayer ownerPlayer = CachedPlayer.getPlayer(owner);
                    String name = ownerPlayer.getRankPrefixColor().getChatColor() + ownerPlayer.getPlayerName();

                    omp.sendMessage("Claims", Color.RED, "§7Dat is van " + name + "§7!", "§7That belongs to " + name + "§7!");

                    event.setCancelled(true);
                    return;
                }
            }

            if (attacker == null
                    && damager != null
                    && damager.getType() != EntityType.CREEPER
                    && damager.getType() != EntityType.WITHER
                    && damager.getType() != EntityType.ENDER_CRYSTAL
                    && damager.getType() != EntityType.AREA_EFFECT_CLOUD
                    && damager.getType() != EntityType.WITCH
                    && !(damager instanceof Projectile)
                    && !(damager instanceof Explosive)
                    && !(damager instanceof ExplosiveMinecart))
                return;

            SurvivalPlayer omp = null;
            if (attacker != null)
                omp = SurvivalPlayer.getPlayer(attacker);

            Claim claim = survival.getClaimHandler().getClaimAt(event.getEntity().getLocation(), false, omp == null ? null : omp.getLastClaim());

            if (claim != null) {
                if (omp == null) {
                    if (event.getEntityType() == EntityType.VILLAGER && damager != null && (damager.getType() == EntityType.ZOMBIE || damager.getType() == EntityType.VINDICATOR || damager.getType() == EntityType.EVOKER || damager.getType() == EntityType.EVOKER_FANGS || damager.getType() == EntityType.VEX))
                        return;

                    event.setCancelled(true);

                    if (damager != null && damager instanceof Projectile)
                        damager.remove();

                    return;
                }

                if (!claim.canInteract(omp)) {
                    event.setCancelled(true);

                    if (arrow != null)
                        arrow.remove();

                    CachedPlayer ownerPlayer = CachedPlayer.getPlayer(claim.getOwner());
                    String name = ownerPlayer.getRankPrefixColor().getChatColor() + ownerPlayer.getPlayerName();

                    omp.sendMessage("Claims", Color.RED, "§7Dat is van " + name + "§7!", "§7That belongs to " + name + "§7!");
                }

                omp.setLastClaim(claim);
            }
        }
    }

    private boolean isMonster(Entity entity) {
        if (entity instanceof Monster) return true;

        EntityType type = entity.getType();
        if (type == EntityType.GHAST || type == EntityType.MAGMA_CUBE || type == EntityType.SHULKER || type == EntityType.POLAR_BEAR)
            return true;

        if (type == EntityType.RABBIT) {
            Rabbit rabbit = (Rabbit) entity;

            if (rabbit.getRabbitType() == Rabbit.Type.THE_KILLER_BUNNY)
                return true;
        }

        return false;
    }

    @EventHandler
    public void onVehicleDamage(VehicleDamageEvent event) {
        if (event.getVehicle() == null)
            return;

        if (!event.getVehicle().getWorld().getName().equals(survival.getWorld().getName()))
            return;

        Player attacker = null;
        Entity damager = event.getAttacker();
        EntityType damagerType = null;

        if (damager != null) {
            damagerType = damager.getType();

            if (damager.getType() == EntityType.PLAYER) {
                attacker = (Player) damager;
            } else if (damager instanceof Projectile) {
                Projectile arrow = (Projectile) damager;

                if (arrow.getShooter() instanceof Player)
                    attacker = (Player) arrow.getShooter();
            }
        }

        if (attacker == null && damagerType != EntityType.CREEPER && damagerType != EntityType.WITHER && damagerType != EntityType.PRIMED_TNT)
            return;

        SurvivalPlayer omp = null;
        if (attacker != null)
            omp = SurvivalPlayer.getPlayer(attacker);

        Claim claim = survival.getClaimHandler().getClaimAt(event.getVehicle().getLocation(), false, omp == null ? null : omp.getLastClaim());

        if (claim != null) {
            if (omp == null) {
                event.setCancelled(true);
                return;
            }

            if (!claim.canInteract(omp)) {
                event.setCancelled(true);

                CachedPlayer ownerPlayer = CachedPlayer.getPlayer(claim.getOwner());
                String name = ownerPlayer.getRankPrefixColor().getChatColor() + ownerPlayer.getPlayerName();

                omp.sendMessage("Claims", Color.RED, "§7Dat is van " + name + "§7!", "§7That belongs to " + name + "§7!");
            }

            omp.setLastClaim(claim);
        }
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        ThrownPotion potion = event.getPotion();

        ProjectileSource projectileSource = potion.getShooter();
        if (projectileSource == null)
            return;

        SurvivalPlayer thrower = null;
        if (projectileSource instanceof Player)
            thrower = SurvivalPlayer.getPlayer((Player) projectileSource);

        for (PotionEffect effect : potion.getEffects()) {
            PotionEffectType effectType = effect.getType();

            /* Prevent griefers from stealing animals & villagers / killing them */
            if (effectType == PotionEffectType.JUMP || effectType == PotionEffectType.POISON) {
                Claim cached = null;

                for (LivingEntity effected : event.getAffectedEntities()) {
                    if (effected.getType() == EntityType.VILLAGER || effect instanceof Animals) {
                        Claim claim = survival.getClaimHandler().getClaimAt(effected.getLocation(), false, cached);

                        if (claim != null) {
                            cached = claim;

                            if (thrower == null || !claim.canInteract(thrower)) {
                                event.setIntensity(effected, 0);

                                CachedPlayer ownerPlayer = CachedPlayer.getPlayer(claim.getOwner());
                                String name = ownerPlayer.getRankPrefixColor().getChatColor() + ownerPlayer.getPlayerName();

                                thrower.sendMessage("Claims", Color.RED, "§7Dat is van " + name + "§7!", "§7That belongs to " + name + "§7!");
                            }
                        }
                    }
                }
            }
        }
    }
}
