package com.orbitmines.spigot.servers.survival.events;

import com.orbitmines.spigot.api.handlers.CachedPlayer;
import com.orbitmines.api.Color;
import com.orbitmines.spigot.api.handlers.chat.ActionBar;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.claim.Claim;
import com.orbitmines.spigot.servers.survival.handlers.claim.ItemProtection;
import com.orbitmines.spigot.servers.survival.handlers.region.Region;
import org.bukkit.*;
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
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dispenser;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class ClaimEvents implements Listener {

    private Survival survival;

    public ClaimEvents(Survival survival) {
        this.survival = survival;
    }

    /*


        Player


     */

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT && event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL)
            return;

        SurvivalPlayer omp = SurvivalPlayer.getPlayer(event.getPlayer());

        Claim claim = survival.getClaimHandler().getClaimAt(event.getTo(), false, omp.getLastClaim());

        if (claim != null && !claim.hasPermission(omp, Claim.Settings.ENDER_PEARL)) {
            String name = claim.getOwnerName();
            new ActionBar(omp, () -> omp.lang(name + " §c§lheeft je geen toegang gegeven om dat hier te gebruiken.", name + " §c§ldidn't give you permission to use that here."), 60).send();
            event.setCancelled(true);

            switch (event.getCause()) {
                case ENDER_PEARL:
                    omp.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
                    break;
                case CHORUS_FRUIT:
                    omp.getInventory().addItem(new ItemStack(Material.CHORUS_FRUIT));
                    break;
            }
        }
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent event) {
        if (event.getTo() == null || event.getTo().getWorld() == null)
            return;

        if (event.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)
            return;

        if (!event.getTo().getWorld().getName().equals(survival.getWorld().getName()))
            return;

        Location to = event.getTo();

        if (event.useTravelAgent()) {
            if (!event.getPortalTravelAgent().getCanCreatePortal())
                return;

            TravelAgent agent = event.getPortalTravelAgent();
            agent.setCanCreatePortal(false);
            to = agent.findOrCreate(to);
            agent.setCanCreatePortal(true);
        }

        /* Not a new portal */
        if (to.getBlock().getType() == Material.PORTAL)
            return;

        SurvivalPlayer omp = SurvivalPlayer.getPlayer(event.getPlayer());

        if (Region.isInRegion(to)) {
            event.setCancelled(true);
            new ActionBar(omp, () -> omp.lang("§c§lJe kan deze portal niet gebruiken omdat hij in een region komt.", "§c§lYou can't use this portal as it ends up in a region."), 60).send();
            return;
        }

        Claim claim = survival.getClaimHandler().getClaimAt(to, false, null);

        if (claim != null && !claim.canBuild(omp, Material.PORTAL)) {
            event.setCancelled(true);
            new ActionBar(omp, () -> omp.lang("§c§lJe kan deze portal niet gebruiken omdat hij in een claim komt.", "§c§lYou can't use this portal as it ends up in a claim."), 60).send();
       }
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof ArmorStand)
            onInteract((PlayerInteractEntityEvent) event);
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if (!event.getRightClicked().getWorld().getName().equals(survival.getWorld().getName()))
            return;

        SurvivalPlayer omp = SurvivalPlayer.getPlayer(event.getPlayer());
        Entity entity = event.getRightClicked();

        if (entity instanceof Tameable) {
            Tameable tameable = (Tameable) entity;

            if (tameable.isTamed() && tameable.getOwner() != null) {
                UUID owner = tameable.getOwner().getUniqueId();

                if (owner.equals(omp.getUUID()) || omp.isOpMode()) {
                    //TODO SWITCH PET OWNERSHIP GUI
//                    if (playerData.petGiveawayRecipient != null) {
//                        tameable.setOwner(playerData.petGiveawayRecipient);
//                        playerData.petGiveawayRecipient = null;
//                        instance.sendMessage(player, TextMode.Success, Messages.PetGiveawayConfirmation);
//                        event.setCancelled(true);
//                    }

                    return;
                }

                CachedPlayer ownerPlayer = CachedPlayer.getPlayer(owner);
                String name = ownerPlayer.getRankPrefixColor().getChatColor() + ownerPlayer.getPlayerName();

                new ActionBar(omp, () -> omp.lang("§c§lDat is van " + name + "§c§l!", "§c§lThat belongs to " + name + "§c§l!"), 60).send();

                event.setCancelled(true);
                return;
            }
        }

        if (entity instanceof ArmorStand || entity instanceof Hanging) {
            if (!survival.getClaimHandler().canBuild(omp, entity.getLocation(), Material.ITEM_FRAME)) {
                event.setCancelled(true);
                return;
            }
        }

        if (omp.isOpMode())
            return;

        if (entity instanceof Vehicle && entity instanceof InventoryHolder) {
            Claim claim = survival.getClaimHandler().getClaimAt(entity.getLocation(), false, null);

            if (claim != null && !claim.canInteract(omp)) {
                event.setCancelled(true);
                return;
            }
        }

        if (entity instanceof Animals || entity.getType() == EntityType.VILLAGER) {
            Claim claim = survival.getClaimHandler().getClaimAt(entity.getLocation(), false, null);

            if (claim != null && !claim.canInteract(omp)) {
                event.setCancelled(true);
                return;
            }
        }

        if (entity instanceof Creature && omp.getItemInMainHand() != null && omp.getItemInMainHand().getType() == Material.LEASH) {
            Claim claim = survival.getClaimHandler().getClaimAt(entity.getLocation(), false, omp.getLastClaim());

            if (claim != null && !claim.canInteract(omp)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (event.getCaught() == null)
            return;

        Entity entity = event.getCaught();

        if (!entity.getWorld().getName().equals(survival.getWorld().getName()))
            return;

        if (entity instanceof ArmorStand || entity instanceof Animals) {
            SurvivalPlayer omp = SurvivalPlayer.getPlayer(event.getPlayer());

            Claim claim = survival.getClaimHandler().getClaimAt(entity.getLocation(), false, omp.getLastClaim());

            if (claim != null && !claim.canInteract(omp)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        SurvivalPlayer omp = SurvivalPlayer.getPlayer(event.getPlayer());

        Item item = event.getItem();
        List<MetadataValue> data = item.getMetadata("SURVIVAL_PROTECTED");

        if (data == null || data.size() == 0)
            return;

        UUID owner = (UUID) data.get(0).value();

        if (omp.getUUID().equals(owner))
            return;

        SurvivalPlayer ownerPlayer = SurvivalPlayer.getPlayer(owner);

        /* Not online, so it can be picked up */
        if (ownerPlayer == null)
            return;

        if (ownerPlayer.hasEnabled(Survival.Settings.DROPS_UNLOCKED))
            return;

        event.setCancelled(true);
        omp.sendMessage(omp.lang("Instellingen", "Settings"), Color.RED, "§7Je kan " + ownerPlayer.getName() + "§7 items niet oppakken, dit moet aangezet worden in zijn/haar instellingen.", "§7You can't pickup " + ownerPlayer.getName() + "§7's items, they have to enabled it in their settings.");
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (!event.getBlockClicked().getWorld().getName().equals(survival.getWorld().getName()))
            return;

        SurvivalPlayer omp = SurvivalPlayer.getPlayer(event.getPlayer());
        Block block = event.getBlockClicked().getRelative(event.getBlockFace());

        if (Region.isInRegion(omp, block.getLocation())) {
            event.setCancelled(true);
            return;
        }

        int distance = 5;

        Claim claim = survival.getClaimHandler().getClaimAt(block.getLocation(), false, omp.getLastClaim());

        if (claim != null) {
            if (!claim.canBuild(omp, Material.WATER)) {
                event.setCancelled(true);
                return;
            }

            distance = 3;
        }

        if (event.getBucket() != Material.LAVA_BUCKET)
            return;

        for (Player player : block.getWorld().getPlayers()) {
            if (!player.equals(omp.getPlayer()) && player.getGameMode() == GameMode.SURVIVAL && block.getY() >= player.getLocation().getBlockY() - 1 && player.getLocation().distanceSquared(block.getLocation()) < distance * distance) {
                omp.sendMessage("Survival", Color.RED, "§7Je kan een lava bucket niet zo dichtbij een speler zetten.", "§7You can't place lava buckets that close to other players.");
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        if (!event.getBlockClicked().getWorld().getName().equals(survival.getWorld().getName()))
            return;

        SurvivalPlayer omp = SurvivalPlayer.getPlayer(event.getPlayer());
        Block block = event.getBlockClicked();

        /* Milking Cow */
        if (block.getType() == Material.AIR || block.getType().isSolid())
            return;

        if (Region.isInRegion(omp, block.getLocation()) || !survival.getClaimHandler().canBuild(omp, block.getLocation(), Material.AIR))
            event.setCancelled(true);
    }
    
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        
        if (action == Action.LEFT_CLICK_AIR || action == Action.PHYSICAL)
            return;

        if (!event.getPlayer().getWorld().getName().equals(survival.getWorld().getName()))
            return;

        Block block = event.getClickedBlock();
        if (block == null)
            return;

        SurvivalPlayer omp = SurvivalPlayer.getPlayer(event.getPlayer());
        Material blockType = block.getType();

        /* Put out Fires */
        if (action == Action.LEFT_CLICK_BLOCK) {
            if (block.getY() < block.getWorld().getMaxHeight() - 1 || event.getBlockFace() != BlockFace.UP) {

                Block adjacent = block.getRelative(event.getBlockFace());
                byte lightLevel = adjacent.getLightFromBlocks();

                if (lightLevel == 15 && adjacent.getType() == Material.FIRE) {
                    if (Region.isInRegion(omp, block.getLocation())) {
                        event.setCancelled(true);
                        omp.getPlayer().sendBlockChange(adjacent.getLocation(), adjacent.getTypeId(), adjacent.getData());
                        return;
                    }

                    Claim claim = survival.getClaimHandler().getClaimAt(block.getLocation(), false, omp.getLastClaim());
                    omp.setLastClaim(claim);

                    if (claim != null && !claim.canBuild(omp, Material.AIR)) {
                        event.setCancelled(true);
                        omp.getPlayer().sendBlockChange(adjacent.getLocation(), adjacent.getTypeId(), adjacent.getData());
                        return;
                    }
                }
            }
        }

        if (action == Action.RIGHT_CLICK_BLOCK && block.getState() instanceof InventoryHolder)
            blockType = Material.CAULDRON;

        switch (blockType) {
            /* Interact */
            case CAULDRON:
            case JUKEBOX:
            case ANVIL:
            case CAKE_BLOCK: {
                Claim claim = survival.getClaimHandler().getClaimAt(block.getLocation(), false, omp.getLastClaim());
                omp.setLastClaim(claim);

                if (claim != null && !claim.canInteract(omp))
                    event.setCancelled(true);

                return;
            }
            /* Access */
            case STONE_BUTTON:
            case WOOD_BUTTON:
            case LEVER:

            case WOOD_DOOR:
            case ACACIA_DOOR:
            case BIRCH_DOOR:
            case JUNGLE_DOOR:
            case SPRUCE_DOOR:
            case DARK_OAK_DOOR:

            case BED_BLOCK:
            case TRAP_DOOR:

            case FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
            case DARK_OAK_FENCE_GATE: {

                Claim claim = survival.getClaimHandler().getClaimAt(block.getLocation(), false, omp.getLastClaim());
                omp.setLastClaim(claim);

                if (claim != null && !claim.canAccess(omp))
                    event.setCancelled(true);

                return;
            }
            /* Build */
            case NOTE_BLOCK:
            case DIODE_BLOCK_ON:
            case DIODE_BLOCK_OFF:
            case DRAGON_EGG:
            case DAYLIGHT_DETECTOR:
            case DAYLIGHT_DETECTOR_INVERTED:
            case REDSTONE_COMPARATOR_ON:
            case REDSTONE_COMPARATOR_OFF:
            case FLOWER_POT: {

                Claim claim = survival.getClaimHandler().getClaimAt(block.getLocation(), false, omp.getLastClaim());
                omp.setLastClaim(claim);

                if (claim != null && !claim.canBuild(omp, blockType))
                    event.setCancelled(true);

                return;
            }
        }

        /* Right Click with item */
        if (action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR)
            return;

        ItemStack item = event.getHand() == EquipmentSlot.HAND ? omp.getItemInMainHand() : omp.getItemInOffHand();

        switch (item.getType()) {
            /* Build */
            case INK_SACK:
            case ARMOR_STAND:
            case MONSTER_EGG:
            case END_CRYSTAL:

            case BOAT:
            case BOAT_ACACIA:
            case BOAT_BIRCH:
            case BOAT_DARK_OAK:
            case BOAT_JUNGLE:
            case BOAT_SPRUCE:

            case MINECART:
            case POWERED_MINECART:
            case STORAGE_MINECART:
            case EXPLOSIVE_MINECART:
            case HOPPER_MINECART: {
                Claim claim = survival.getClaimHandler().getClaimAt(block.getLocation(), false, omp.getLastClaim());
                omp.setLastClaim(claim);

                if (claim != null && !claim.canBuild(omp, item.getType()))
                    event.setCancelled(true);

                return;
            }
        }
    }

    /*


        Blocks


     */

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

                    new ActionBar(omp, () -> omp.lang("§c§lDat is van " + name + "§c§l!", "§c§lThat belongs to " + name + "§c§l!"), 60).send();

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

            SurvivalPlayer omp = attacker == null ? null : SurvivalPlayer.getPlayer(attacker);

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

                    new ActionBar(omp, () -> omp.lang("§c§lDat is van " + name + "§c§l!", "§c§lThat belongs to " + name + "§c§l!"), 60).send();
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

        SurvivalPlayer omp = attacker == null ? null : SurvivalPlayer.getPlayer(attacker);

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

                new ActionBar(omp, () -> omp.lang("§c§lDat is van " + name + "§c§l!", "§c§lThat belongs to " + name + "§c§l!"), 60).send();
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

        SurvivalPlayer thrower = !(projectileSource instanceof Player) ? null : SurvivalPlayer.getPlayer((Player) projectileSource);

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

                                if (thrower != null) {
                                    CachedPlayer ownerPlayer = CachedPlayer.getPlayer(claim.getOwner());
                                    String name = ownerPlayer.getRankPrefixColor().getChatColor() + ownerPlayer.getPlayerName();

                                    new ActionBar(thrower, () -> thrower.lang("§c§lDat is van " + name + "§c§l!", "§c§lThat belongs to " + name + "§c§l!"), 60).send();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /*


        Entities


     */

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
