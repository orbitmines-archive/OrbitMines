package com.orbitmines.spigot.servers.survival;

import com.google.common.io.ByteArrayDataInput;
import com.orbitmines.api.PluginMessage;
import com.orbitmines.api.Server;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.TableServerData;
import com.orbitmines.api.database.tables.survival.TableSurvivalClaim;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.OrbitMinesServer;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.PluginMessageHandler;
import com.orbitmines.spigot.api.handlers.PreventionSet;
import com.orbitmines.spigot.api.handlers.chat.ActionBar;
import com.orbitmines.spigot.api.handlers.itemhandlers.ItemHoverActionBar;
import com.orbitmines.spigot.api.handlers.itemhandlers.ItemInteraction;
import com.orbitmines.spigot.api.handlers.scoreboard.DefaultScoreboard;
import com.orbitmines.spigot.api.utils.ConsoleUtils;
import com.orbitmines.spigot.api.utils.LocationUtils;
import com.orbitmines.spigot.api.utils.PlayerUtils;
import com.orbitmines.spigot.api.utils.Serializer;
import com.orbitmines.spigot.servers.survival.events.ClaimEvents;
import com.orbitmines.spigot.servers.survival.events.SignEvent;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.claim.Claim;
import com.orbitmines.spigot.servers.survival.handlers.claim.ClaimHandler;
import com.orbitmines.spigot.servers.survival.handlers.claim.Visualization;
import com.orbitmines.spigot.servers.survival.handlers.region.Region;
import com.orbitmines.spigot.servers.survival.handlers.region.RegionBuilder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class Survival extends OrbitMinesServer {

    private World world;

    private ClaimHandler claimHandler;

    public Survival(OrbitMines orbitMines) {
        super(orbitMines, Server.SURVIVAL, new PluginMessageHandler() {
            @Override
            public void onReceive(ByteArrayDataInput in, PluginMessage message) {

            }
        });

        preventionSet.prevent(orbitMines.getLobby().getWorld(),
                PreventionSet.Prevention.BLOCK_BREAK,
                PreventionSet.Prevention.BLOCK_INTERACTING,
                PreventionSet.Prevention.BLOCK_PLACE,
                PreventionSet.Prevention.CHUNK_UNLOAD,
                PreventionSet.Prevention.ENTITY_INTERACTING,
                PreventionSet.Prevention.LEAF_DECAY,
                PreventionSet.Prevention.PLAYER_DAMAGE,
                PreventionSet.Prevention.WEATHER_CHANGE
        );

        world = Bukkit.getWorld("world");
        preventionSet.prevent(world,
                PreventionSet.Prevention.PVP
        );

        claimHandler = new ClaimHandler(this);

        setupRegions();
        setupClaims();
        setupClaimTool();
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public OMPlayer newPlayerInstance(Player player) {
        return new SurvivalPlayer(this, player);
    }

    @Override
    public boolean teleportToSpawn(Player player) {
        return false;
    }

    @Override
    public Location getSpawnLocation(Player player) {
        return null;
    }

    @Override
    protected void registerEvents() {
        registerEvents(
                new ClaimEvents(this),
                new SignEvent()
        );
    }

    @Override
    protected void registerCommands() {

    }

    @Override
    protected void registerRunnables() {

    }

    public World getWorld() {
        return world;
    }

    public ClaimHandler getClaimHandler() {
        return claimHandler;
    }

    private void setupRegions() {
        if (!Database.get().contains(Table.SERVER_DATA, TableServerData.DATA, new Where(TableServerData.SERVER, getServer().toString()), new Where(TableServerData.TYPE, "TELEPORTABLE")))
            Database.get().insert(Table.SERVER_DATA, Table.SERVER_DATA.values(getServer().toString(), "TELEPORTABLE", Region.TELEPORTABLE + ""));
        else
            Region.TELEPORTABLE = Database.get().getInt(Table.SERVER_DATA, TableServerData.DATA, new Where(TableServerData.SERVER, getServer().toString()), new Where(TableServerData.TYPE, "TELEPORTABLE"));

        for (int i = 0; i < Region.REGION_COUNT; i++) {
            RegionBuilder builder = new RegionBuilder(world, i);
            builder.build();

            new Region(i, builder.getFixedSpawnLocation(), builder.getInventoryX(), builder.getInventoryY());
        }
    }

    private void setupClaims() {
        /* Next ID */
        if (!Database.get().contains(Table.SERVER_DATA, TableServerData.DATA, new Where(TableServerData.SERVER, getServer().toString()), new Where(TableServerData.TYPE, "NEXT_ID")))
            Database.get().insert(Table.SERVER_DATA, Table.SERVER_DATA.values(getServer().toString(), "NEXT_ID", Claim.NEXT_ID + ""));
        else
            Claim.NEXT_ID = Database.get().getLong(Table.SERVER_DATA, TableServerData.DATA, new Where(TableServerData.SERVER, getServer().toString()), new Where(TableServerData.TYPE, "NEXT_ID"));

        List<Map<Column, String>> entries = Database.get().getEntries(Table.SURVIVAL_CLAIM);

        List<Claim> toRemove = new ArrayList<>();
        List<Claim> children = new ArrayList<>();

        for (Map<Column, String> entry : entries) {
            Long id = Long.parseLong(entry.get(TableSurvivalClaim.ID));
            Date createdOn = DateUtils.parse(DateUtils.FORMAT, entry.get(TableSurvivalClaim.CREATED_ON));

            Location corner1 = Serializer.parseLocation(entry.get(TableSurvivalClaim.CORNER_1));
            Location corner2 = Serializer.parseLocation(entry.get(TableSurvivalClaim.CORNER_2));

            UUID owner;
            if (entry.get(TableSurvivalClaim.OWNER).equals(""))
                owner = null;
            else
                owner = UUID.fromString(entry.get(TableSurvivalClaim.OWNER));

            Map<UUID, Claim.Permission> members = new HashMap<>();
            if (!entry.get(TableSurvivalClaim.MEMBERS).equals("")) {
                for (String data : entry.get(TableSurvivalClaim.MEMBERS).split("\\|")) {
                    String[] memberData = data.split(";");

                    members.put(UUID.fromString(memberData[0]), Claim.Permission.valueOf(memberData[1]));
                }
            }

            Map<Claim.Settings, Claim.Permission> settings = new HashMap<>();
            if (!entry.get(TableSurvivalClaim.SETTINGS).equals("")) {
                for (String data : entry.get(TableSurvivalClaim.SETTINGS).split("\\|")) {
                    String[] settingData = data.split(";");

                    settings.put(Claim.Settings.valueOf(settingData[0]), Claim.Permission.valueOf(settingData[1]));
                }
            }

            Claim claim = new Claim(this, id, createdOn, corner1, corner2, owner, members, settings);

            Long parentId = Long.parseLong(entry.get(TableSurvivalClaim.PARENT));

            if (parentId == -1)
                claimHandler.addClaim(claim, false);
            else
                children.add(claim);
        }

        for (Claim child : children) {
            Claim parent = claimHandler.getClaimAt(child.getCorner1(), true, null);

            if (parent == null) {
                toRemove.add(child);
                ConsoleUtils.msg("Removing orphaned claim: " + child.getCorner1().toString());
                continue;
            }

            child.setParent(parent);
            parent.getChildren().add(child);
            child.setRegistered(true);
        }

        for (Claim claim : toRemove) {
            claimHandler.delete(claim);
        }

        ConsoleUtils.success(claimHandler.getClaims().size() + " Claims loaded.");
    }

    private void setupClaimTool() {
        new ItemInteraction(Claim.CLAIMING_TOOL) {
            @Override
            public void onInteract(OMPlayer omp, PlayerInteractEvent event, ItemStack itemStack) {
                event.setCancelled(true);
            }

            @Override
            public void onLeftClick(OMPlayer player, PlayerInteractEvent event, ItemStack itemStack) {
                if (!player.getWorld().getName().equals(Survival.this.getWorld().getName()))
                    return;

                SurvivalPlayer omp = (SurvivalPlayer) player;

                if (omp.getPlayer().isSneaking()) {
                    /* Show all claims nearby */
                    Set<Claim> claims = claimHandler.getNearbyClaims(omp.getLocation());

                    Visualization.show(omp, claims, omp.getPlayer().getEyeLocation().getBlockY(), Visualization.Type.CLAIM, omp.getLocation());

                    new ActionBar(omp, () -> "§a§l" + claims.size() + " " + (claims.size() == 1 ? "Claim" : "Claims") + " " + omp.lang("in de buurt.", "nearby."), 60).send();
                } else {
                    /* Show current claim information */
                    Block block = event.getClickedBlock();
                    if (event.getAction() == Action.LEFT_CLICK_AIR)
                        block = PlayerUtils.getTargetBlock(omp.getPlayer(), 100);

                    if (block == null)
                        return;

                    if (block.getType() == Material.AIR) {
                        new ActionBar(omp, () -> omp.lang("§c§lDat is te ver weg!", "§c§lThat is too far away!"), 60).send();

                        Visualization.revert(omp);
                        return;
                    }

                    Claim claim = claimHandler.getClaimAt(block.getLocation(), false, omp.getLastClaim());

                    if (claim == null) {
                        new ActionBar(omp, () -> omp.lang("§c§lDat block is niet geclaimed!", "§c§lThat block has not been claimed!"), 60).send();

                        Visualization.revert(omp);
                    } else {
                        omp.setLastClaim(claim);

                        String name = claim.getOwnerName();
                        new ActionBar(omp, () -> omp.lang("§a§lDit is geclaimed door " + name + "§a§l.", "§a§lThis has been claimed by " + name + "§a§l."), 60).send();

                        Visualization.show(omp, claim, omp.getPlayer().getEyeLocation().getBlockY(), Visualization.Type.CLAIM, omp.getLocation());
                    }
                }
            }

            @Override
            public void onRightClick(OMPlayer player, PlayerInteractEvent event, ItemStack itemStack) {
                if (!player.getWorld().getName().equals(Survival.this.getWorld().getName()))
                    return;

                SurvivalPlayer omp = (SurvivalPlayer) player;

                Block block = event.getClickedBlock();
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
                    block = PlayerUtils.getTargetBlock(omp.getPlayer(), 100);

                if (block == null)
                    return;

                if (block.getType() == Material.AIR) {
                    new ActionBar(omp, () -> omp.lang("§c§lDat is te ver weg!", "§c§lThat is too far away!"), 60).send();
                    return;
                }

                if (omp.getResizingClaim() != null && omp.getResizingClaim().isRegistered()) {
                    /* Already resizing exisiting claim */

                    if (LocationUtils.equals(block, omp.getLastClaimToolLocation().getBlock()))
                        return;
                    
                    int newX1, newX2, newZ1, newZ2, newY1, newY2;
                    
                    if (omp.getLastClaimToolLocation().getBlockX() == omp.getResizingClaim().getCorner1().getBlockX())
                        newX1 = block.getX();
                    else
                        newX1 = omp.getResizingClaim().getCorner1().getBlockX();

                    if (omp.getLastClaimToolLocation().getBlockX() == omp.getResizingClaim().getCorner2().getBlockX())
                        newX2 = block.getX();
                    else
                        newX2 = omp.getResizingClaim().getCorner2().getBlockX();

                    if (omp.getLastClaimToolLocation().getBlockZ() == omp.getResizingClaim().getCorner1().getBlockZ())
                        newZ1 = block.getZ();
                    else
                        newZ1 = omp.getResizingClaim().getCorner1().getBlockZ();

                    if (omp.getLastClaimToolLocation().getBlockZ() == omp.getResizingClaim().getCorner2().getBlockZ())
                        newZ2 = block.getZ();
                    else
                        newZ2 = omp.getResizingClaim().getCorner2().getBlockZ();

                    newY1 = omp.getResizingClaim().getCorner1().getBlockY();
                    newY2 = block.getY();

                    claimHandler.resizeClaimWithChecks(omp, newX1, newX2, newY1, newY2, newZ1, newZ2);
                    return;
                }

                Claim claim = claimHandler.getClaimAt(block.getLocation(), true, omp.getLastClaim());

                if (claim != null) {
                    /* Not creating a new claim */

                    if (claim.canModify(omp)) {
                        if ((block.getX() == claim.getCorner1().getBlockX() || block.getX() == claim.getCorner2().getBlockX()) && (block.getZ() == claim.getCorner1().getBlockZ() || block.getZ() == claim.getCorner2().getBlockZ())) {
                            /* Click on a corner -> resize */
                            omp.setResizingClaim(claim);
                            omp.setLastClaimToolLocation(block.getLocation());
                            omp.playSound(Sound.UI_BUTTON_CLICK);
                        } else if (omp.getClaimToolType() == Claim.ToolType.CHILD) {
                            /* Create Child? */
                            //                        //if it's the first click, he's trying to start a new subdivision
                            //                        if (playerData.lastShovelLocation == null) {
                            //                            //if the clicked claim was a subdivision, tell him he can't start a new subdivision here
                            //                            if (claim.parent != null) {
                            //                                instance.sendMessage(player, TextMode.Err, Messages.ResizeFailOverlapSubdivision);
                            //                            }
                            //
                            //                            //otherwise start a new subdivision
                            //                            else {
                            //                                instance.sendMessage(player, TextMode.Instr, Messages.SubdivisionStart);
                            //                                playerData.lastShovelLocation = clickedBlock.getLocation();
                            //                                playerData.claimSubdividing = claim;
                            //                            }
                            //                        }
                            //
                            //                        //otherwise, he's trying to finish creating a subdivision by setting the other boundary corner
                            //                        else {
                            //                            //if last shovel location was in a different world, assume the player is starting the create-claim workflow over
                            //                            if (!playerData.lastShovelLocation.getWorld().equals(clickedBlock.getWorld())) {
                            //                                playerData.lastShovelLocation = null;
                            //                                this.onPlayerInteract(event);
                            //                                return;
                            //                            }
                            //
                            //                            //try to create a new claim (will return null if this subdivision overlaps another)
                            //                            CreateClaimResult result = this.dataStore.createClaim(
                            //                                    player.getWorld(),
                            //                                    playerData.lastShovelLocation.getBlockX(), clickedBlock.getX(),
                            //                                    playerData.lastShovelLocation.getBlockY() - instance.config_claims_claimsExtendIntoGroundDistance, clickedBlock.getY() - instance.config_claims_claimsExtendIntoGroundDistance,
                            //                                    playerData.lastShovelLocation.getBlockZ(), clickedBlock.getZ(),
                            //                                    null,  //owner is not used for subdivisions
                            //                                    playerData.claimSubdividing,
                            //                                    null, player);
                            //
                            //                            //if it didn't succeed, tell the player why
                            //                            if (!result.succeeded) {
                            //                                instance.sendMessage(player, TextMode.Err, Messages.CreateSubdivisionOverlap);
                            //
                            //                                Visualization visualization = Visualization.FromClaim(result.claim, clickedBlock.getY(), VisualizationType.ErrorClaim, player.getLocation());
                            //
                            //                                // alert plugins of a visualization
                            //                                Bukkit.getPluginManager().callEvent(new VisualizationEvent(player, result.claim));
                            //
                            //                                Visualization.Apply(player, visualization);
                            //
                            //                                return;
                            //                            }
                            //
                            //                            //otherwise, advise him on the /trust command and show him his new subdivision
                            //                            else {
                            //                                instance.sendMessage(player, TextMode.Success, Messages.SubdivisionSuccess);
                            //                                Visualization visualization = Visualization.FromClaim(result.claim, clickedBlock.getY(), VisualizationType.Claim, player.getLocation());
                            //
                            //                                // alert plugins of a visualization
                            //                                Bukkit.getPluginManager().callEvent(new VisualizationEvent(player, result.claim));
                            //
                            //                                Visualization.Apply(player, visualization);
                            //                                playerData.lastShovelLocation = null;
                            //                                playerData.claimSubdividing = null;
                            //                            }
                            //                        }
                        } else {
                            /* Can't create claim here */
                            //TODO message
                            //						instance.sendMessage(player, TextMode.Err, Messages.CreateClaimFailOverlap);
                            Visualization.show(omp, claim, block.getY(), Visualization.Type.CLAIM, player.getLocation());
                        }
                    } else {
                        /* In someone else's claim */
                        //TODO message instance.sendMessage(player, TextMode.Err, Messages.CreateClaimFailOverlapOtherPlayer, claim.getOwnerName());

                        Visualization.show(omp, claim, block.getY(), Visualization.Type.INVALID, player.getLocation());
                    }

                    return;
                }

                /* Not in an existing claim */
                if (omp.getLastClaimToolLocation() == null) {
                    /* First point not selected */

                    if (!omp.getWorld().getName().equals(Survival.this.getWorld().getName())) {
                        new ActionBar(omp, () -> omp.lang("§c§lJe kan hier niet claimen.", "§c§lClaiming is disabled here."), 40).send();
                        return;
                    }

                    /* Start claiming */
                    omp.setLastClaimToolLocation(block.getLocation());

                    Claim newClaim = new Claim(Survival.this, null, null, block.getLocation(), block.getLocation(), null, new HashMap<>(), new HashMap<>());
                    Visualization.show(omp, newClaim, block.getY(), Visualization.Type.DISPLAY, player.getLocation());
                } else {
                    /* Finishing claim */

                    /* New event if switched worlds */
                    if (!omp.getWorld().getName().equals(Survival.this.getWorld().getName())) {
                        omp.setLastClaimToolLocation(null);
                        onRightClick(omp, event, itemStack);
                        return;
                    }

                    int newWidth = Math.abs(omp.getLastClaimToolLocation().getBlockX() - block.getX()) + 1;
                    int newHeight = Math.abs(omp.getLastClaimToolLocation().getBlockZ() - block.getZ()) + 1;

                    UUID owner = omp.getUUID();

                    if (omp.getClaimToolType() != Claim.ToolType.WITHOUT_OWNER) {
                        if (newWidth < Claim.MIN_WIDTH || newHeight < Claim.MIN_WIDTH) {
                            /* If event fired twice */
//                            if (newWidth != 1 && newHeight != 1) TODO
//                                instance.sendMessage(player, TextMode.Err, Messages.NewClaimTooNarrow, String.valueOf(instance.config_claims_minWidth));

                            return;
                        }

                        int newArea = newWidth * newHeight;
                        if (newArea < Claim.MIN_AREA) {
//                            if (newArea != 1) TODO
//                                instance.sendMessage(player, TextMode.Err, Messages.ResizeClaimInsufficientArea, String.valueOf(instance.config_claims_minArea));

                            return;
                        }

                        if (newArea > omp.getRemainingClaimBlocks()) {
//                            instance.sendMessage(player, TextMode.Err, Messages.CreateClaimInsufficientBlocks, String.valueOf(newClaimArea - remainingBlocks));
                            return;
                        }
                    } else {
                        owner = null;
                    }

                    Claim.CreateResult result = claimHandler.createClaim(omp.getWorld(), omp, owner, null, null, omp.getLastClaimToolLocation().getBlockX(), block.getX(), omp.getLastClaimToolLocation().getBlockY(), block.getY(), omp.getLastClaimToolLocation().getBlockZ(), block.getZ());

                    if (!result.isSucceeded()) {
                        if (result.getClaim() != null) {
//                            instance.sendMessage(player, TextMode.Err, Messages.CreateClaimFailOverlapShort);
                            Visualization.show(omp, result.getClaim(), block.getY(), Visualization.Type.INVALID, player.getLocation());
                        } else {
//                            instance.sendMessage(player, TextMode.Err, Messages.CreateClaimFailOverlapRegion);
                        }
                        return;
                    } else {
//                        instance.sendMessage(player, TextMode.Success, Messages.CreateClaimSuccess);
                        Visualization.show(omp, result.getClaim(), block.getY(), Visualization.Type.CLAIM, player.getLocation());

                        omp.setLastClaimToolLocation(null);
                    }
                }
            }
        };

        new ItemHoverActionBar(Claim.CLAIMING_TOOL, true) {
            @Override
            public String getMessage(OMPlayer player) {
                SurvivalPlayer omp = (SurvivalPlayer) player;

                if (!omp.getWorld().getName().equals(Survival.this.getWorld().getName()))
                    return omp.lang("§c§lJe kan hier niet claimen.", "§c§lClaiming is disabled here.");

                if (omp.getLastClaimToolLocation() == null)
                    return omp.lang("§6§lLINKER MUISKLIK §7| §a§lClaim Informatie        §6§lRECHTER MUISKLIK §7| §a§lClaimen", "§6§lLEFT CLICK §7| §a§lClaim Information        §6§lRIGHT CLICK §7| §a§lClaim");

                return omp.lang("§a§lClaimen...        §c§lPos 1: §6§l" + LocationUtils.friendlyString(omp.getLastClaimToolLocation()) + "        §c§lPos 2: §6§lGEEN", "§a§lClaiming...        §c§lPos 1: §6§l" + LocationUtils.friendlyString(omp.getLastClaimToolLocation()) + "        §c§lPos 2: §6§lNONE");
            }

            @Override
            public void onEnter(OMPlayer omp) {
                super.onEnter(omp);
                omp.playSound(Sound.UI_BUTTON_CLICK);
            }

            @Override
            public void onLeave(OMPlayer omp) {
                super.onLeave(omp);
                ((SurvivalPlayer) omp).setLastClaimToolLocation(null);
            }
        };
    }

    public static class Scoreboard extends DefaultScoreboard {

        public Scoreboard(OrbitMines orbitMines, SurvivalPlayer omp) {
            super(omp,
                    () -> orbitMines.getScoreboardAnimation().get(),
                    () -> "§m--------------",
                    () -> "",
                    () -> "§2§lEarth Money",
                    () -> " " + NumberUtils.locale(omp.getEarthMoney()),
                    () -> "   "
            );
        }
    }

    public enum Settings {

        DROPS_UNLOCKED;

    }
}
