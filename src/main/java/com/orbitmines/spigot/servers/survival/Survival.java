package com.orbitmines.spigot.servers.survival;

import com.google.common.io.ByteArrayDataInput;
import com.orbitmines.api.*;
import com.orbitmines.api.Server;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.TableServerData;
import com.orbitmines.api.database.tables.survival.TableSurvivalClaim;
import com.orbitmines.api.database.tables.survival.TableSurvivalPlayers;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.OrbitMinesServer;
import com.orbitmines.spigot.api.Loot;
import com.orbitmines.spigot.api.events.VoidDamageEvent;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.PluginMessageHandler;
import com.orbitmines.spigot.api.handlers.PreventionSet;
import com.orbitmines.spigot.api.handlers.chat.ActionBar;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.api.handlers.data.LootData;
import com.orbitmines.spigot.api.handlers.data.PlayTimeData;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itemhandlers.ItemHoverActionBar;
import com.orbitmines.spigot.api.handlers.itemhandlers.ItemInteraction;
import com.orbitmines.spigot.api.handlers.leaderboard.LeaderBoard;
import com.orbitmines.spigot.api.handlers.leaderboard.hologram.DefaultHologramLeaderBoard;
import com.orbitmines.spigot.api.handlers.npc.Hologram;
import com.orbitmines.spigot.api.handlers.scoreboard.DefaultScoreboard;
import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardSet;
import com.orbitmines.spigot.api.options.chestshops.ChestShopHandler;
import com.orbitmines.spigot.api.utils.ConsoleUtils;
import com.orbitmines.spigot.api.utils.LocationUtils;
import com.orbitmines.spigot.api.utils.PlayerUtils;
import com.orbitmines.spigot.api.utils.Serializer;
import com.orbitmines.spigot.servers.hub.datapoints.HubDataPointSpawnpoint;
import com.orbitmines.spigot.servers.survival.cmds.*;
import com.orbitmines.spigot.servers.survival.cmds.vip.*;
import com.orbitmines.spigot.servers.survival.datapoints.SurvivalDataPointEndReset;
import com.orbitmines.spigot.servers.survival.datapoints.SurvivalDataPointNetherReset;
import com.orbitmines.spigot.servers.survival.events.*;
import com.orbitmines.spigot.servers.survival.gui.claim.ClaimGUI;
import com.orbitmines.spigot.servers.survival.handlers.ResetTimer;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalData;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalDataPointHandler;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.claim.Claim;
import com.orbitmines.spigot.servers.survival.handlers.claim.ClaimHandler;
import com.orbitmines.spigot.servers.survival.handlers.claim.Visualization;
import com.orbitmines.spigot.servers.survival.handlers.region.Region;
import com.orbitmines.spigot.servers.survival.handlers.region.RegionBuilder;
import com.orbitmines.spigot.servers.survival.handlers.teleportable.SurvivalSpawn;
import com.orbitmines.spigot.servers.survival.handlers.teleportable.Warp;
import com.orbitmines.spigot.servers.survival.runnables.ClaimAchievementRunnable;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
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
    private World world_nether;
    private World world_the_end;

    private List<Location> spawnLocations;
    private SurvivalSpawn spawnTp;

    private ClaimHandler claimHandler;

    static {
        new LeaderBoard.Instantiator("EARTH_MONEY") {
            @Override
            public LeaderBoard instantiate(Location location, String[] data) {
                return new DefaultHologramLeaderBoard(location, 0, () -> "§7§lRichest Players", 10, Table.SURVIVAL_PLAYERS, TableSurvivalPlayers.UUID, TableSurvivalPlayers.EARTH_MONEY) {
                    @Override
                    public String getValue(CachedPlayer player, int count) {
                        return "§2§l" + count + " " + (count == 1 ? "Credit" : "Credits");
                    }
                };
            }
        };
        new LeaderBoard.Instantiator("CLAIM_BLOCKS") {
            @Override
            public LeaderBoard instantiate(Location location, String[] data) {
                return new DefaultHologramLeaderBoard(location, 0, () -> "§7§lTop ClaimBlocks", 10, Table.SURVIVAL_PLAYERS, TableSurvivalPlayers.UUID, TableSurvivalPlayers.CLAIM_BLOCKS);
            }
        };
    }

    public Survival(OrbitMines orbitMines) {
        super(orbitMines, Server.SURVIVAL, new PluginMessageHandler() {
            @Override
            public void onReceive(ByteArrayDataInput in, PluginMessage message) {

            }
        });
    }

    @Override
    public void onEnable() {
        orbitMines.getConfigHandler().setup("survival_regions");

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
        orbitMines.getLobby().getWorld().setPVP(false);

        world = Bukkit.getWorld("world");
        world.getWorldBorder().setCenter(0.5, 0.5);
        world.getWorldBorder().setSize(Region.WORLD_BORDER);
        world.setPVP(false);

        world_nether = Bukkit.getWorld("world_nether");
        world_nether.setPVP(false);

        world_the_end = Bukkit.getWorld("world_the_end");
        world_the_end.setPVP(false);

        orbitMines.getLobby().getWorld().setTime(6000);

        claimHandler = new ClaimHandler(this);

        spawnTp = new SurvivalSpawn(this);

        setupRegions();
        setupClaims();
        setupClaimTool();

        Warp.setupWarps();

        setup(new ChestShopHandler() {
            @Override
            public int getMoney(UUID uuid) {
                SurvivalPlayer omp = SurvivalPlayer.getPlayer(uuid);

                if (omp != null)
                    return omp.getEarthMoney();

                SurvivalData data = new SurvivalData(uuid);
                data.load();

                return data.getEarthMoney();
            }

            @Override
            public void addMoney(UUID uuid, int count) {
                SurvivalPlayer omp = SurvivalPlayer.getPlayer(uuid);

                LootData data;

                if (omp != null) {
                    data = (LootData) omp.getData(Data.Type.LOOT);
                } else {
                    data = new LootData(uuid);
                    data.load();
                }

                data.add(Loot.SURVIVAL_CREDITS, Rarity.COMMON, server.getColor().getChatColor() + "§l§oChest Shops", count);
            }

            @Override
            public void removeMoney(UUID uuid, int count) {
                SurvivalPlayer omp = SurvivalPlayer.getPlayer(uuid);

                if (omp != null) {
                    omp.removeEarthMoney(count);
                    return;
                }

                SurvivalData data = new SurvivalData(uuid);
                data.load();

                data.removeEarthMoney(count);
            }

            @Override
            public String getCurrencyDisplay(int count) {
                return "§2§l" + NumberUtils.locale(count) + " " + (count == 1 ? "Credit" : "Credits");
            }

            @Override
            public char getCurrencySymbol() {
                return 'C';
            }

            @Override
            public ItemBuilder getCurrencyIcon() {
                return new ItemBuilder(Material.SCUTE);
            }

            @Override
            public String getScoreboardCurrencyName() {
                return "§2§lCredits";
            }

            @Override
            public ScoreboardSet getNewScoreboardInstance(OrbitMines orbitMines, OMPlayer omp) {
                return new Survival.Scoreboard(orbitMines, (SurvivalPlayer) omp);
            }

            @Override
            public List<World> getWorlds() {
                return Collections.singletonList(world);
            }
        });

        /* DataPoints */
        spawnLocations = ((HubDataPointSpawnpoint) (orbitMines.getLobby().getHandler().getDataPoint(SurvivalDataPointHandler.Type.SPAWNPOINT))).getSpawns();

        for (Location location : ((SurvivalDataPointNetherReset) (orbitMines.getLobby().getHandler().getDataPoint(SurvivalDataPointHandler.Type.NETHER_RESET))).getLocations()) {
            ResetTimer timer = ResetTimer.NETHER_RESET;

            Hologram hologram = new Hologram(location, 1, Hologram.Face.UP);
            hologram.addLine(() -> timer.getDisplayName() + " Reset", false);
            hologram.addLine(() -> "§7" + timer.getResetInString(Language.ENGLISH), false);
            hologram.create();

            timer.getHolograms().add(hologram);
        }
        for (Location location : ((SurvivalDataPointEndReset) (orbitMines.getLobby().getHandler().getDataPoint(SurvivalDataPointHandler.Type.END_RESET))).getLocations()) {
            ResetTimer timer = ResetTimer.END_RESET;

            Hologram hologram = new Hologram(location, 0, Hologram.Face.UP);
            hologram.addLine(() -> timer.getDisplayName() + " Reset", false);
            hologram.addLine(() -> "§7" + timer.getResetInString(Language.ENGLISH), false);
            hologram.create();

            timer.getHolograms().add(hologram);
        }

        /* Setup Nether/End Reset Timers */
        for (ResetTimer timer : ResetTimer.values()) {
            timer.setup(this);
        }
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
        PlayTimeData data = new PlayTimeData(player.getUniqueId());
        data.load();

        return data.getPlayTime().get(server) == 0F;
    }

    @Override
    public Location getSpawnLocation(Player player) {
        return getLobbySpawn();
    }

    @Override
    protected void registerEvents() {
        registerEvents(
                new ClaimEvents(this),
                new DeathEvent(this),
                new FlyEvent(this),
                new SignEvent(),
                new AchievementEvents(orbitMines),
                new VoidDamageEvent(orbitMines.getLobby().getWorld()) {
                    @Override
                    public Location getRespawnLocation(Player player) {
                        return getLobbySpawn();
                    }
                }
        );
    }

    @Override
    protected void registerCommands() {
        Command.getCommand("/fly").unregister();
        Command.getCommand("/tp").unregister();

        new CommandSpawn(this);
        new CommandRegion(this);

        new CommandClaim();
        new CommandCredits();
        new CommandPay();
        new CommandPrismShop();

        new CommandHome();
        new CommandHomes();
        new CommandSetHome();
        new CommandDelHome();

        new CommandWarps(this);
        new CommandMyWarps(this);

        new CommandAccept();

        new CommandTeleport();
        new CommandFly();
        new CommandWorkbench();
        new CommandEnderchest();
        new CommandTpHere();
    }

    @Override
    protected void registerRunnables() {
        new ClaimAchievementRunnable(this);
    }

    @Override
    public void setupNpc(String npcName, Location location) {

    }

    public World getWorld() {
        return world;
    }

    public World getWorld_nether() {
        return world_nether;
    }

    public void setWorld_nether(World world_nether) {
        this.world_nether = world_nether;
    }

    public World getWorld_the_end() {
        return world_the_end;
    }

    public void setWorld_the_end(World world_the_end) {
        this.world_the_end = world_the_end;
    }

    public Location getLobbySpawn() {
        return RandomUtils.randomFrom(spawnLocations);
    }

    public SurvivalSpawn getSpawnTp() {
        return spawnTp;
    }

    public ClaimHandler getClaimHandler() {
        return claimHandler;
    }

    private void setupRegions() {
        if (!Database.get().contains(Table.SERVER_DATA, TableServerData.DATA, new Where(TableServerData.SERVER, server.toString()), new Where(TableServerData.TYPE, "TELEPORTABLE")))
            Database.get().insert(Table.SERVER_DATA, server.toString(), "TELEPORTABLE", Region.TELEPORTABLE + "");
        else
            Region.TELEPORTABLE = Database.get().getInt(Table.SERVER_DATA, TableServerData.DATA, new Where(TableServerData.SERVER, server.toString()), new Where(TableServerData.TYPE, "TELEPORTABLE"));

        List<Integer> generated;
        FileConfiguration configuration = orbitMines.getConfigHandler().get("survival_regions");
        if (configuration.contains("regions"))
            generated = configuration.getIntegerList("regions");
        else
            generated = new ArrayList<>();

        for (int i = 0; i < Region.REGION_COUNT; i++) {
            RegionBuilder builder = new RegionBuilder(world, i);
            builder.build(generated);

            new Region(i, builder.getFixedSpawnLocation(), builder.getInventoryX(), builder.getInventoryY(), builder.isUnderWaterRegion());
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

                        if (claim.getOwner().equals(omp.getUUID())) {
                            new ClaimGUI(Survival.this, claim).open(omp);
                        } else {
                            String name = claim.getOwnerName();
                            new ActionBar(omp, () -> omp.lang("§a§lDit is geclaimed door " + name + "§a§l.", "§a§lThis has been claimed by " + name + "§a§l."), 60).send();

                            Visualization.show(omp, claim, omp.getPlayer().getEyeLocation().getBlockY(), Visualization.Type.CLAIM, omp.getLocation());
                        }
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
                            new ActionBar(omp, () -> omp.lang("§c§lDat zal je eigen claim overlappen.", "§c§lThat will overlap your own claim."), 60).send();
                            Visualization.show(omp, claim, block.getY(), Visualization.Type.INVALID, player.getLocation());
                        }
                    } else {
                        /* In someone else's claim */
                        String name = claim.getOwnerName();
                        new ActionBar(omp, () -> omp.lang("§c§lDat zal " + name + "§c§l's claim overlappen.", "§c§lThat will overlap " + name + "§c§l's claim."), 60).send();
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
                    int remaining;

                    if (omp.getClaimToolType() != Claim.ToolType.WITHOUT_OWNER) {
                        if (newWidth < Claim.MIN_WIDTH || newHeight < Claim.MIN_WIDTH) {
                            /* If event fired twice */
                            if (newWidth != 1 && newHeight != 1)
                                new ActionBar(omp, () -> omp.lang("§c§lEen claim moet minimaal 3x3 zijn.", "§c§lA claim must at least be 3x3."), 60).send();

                            return;
                        }

                        int newArea = newWidth * newHeight;
                        if (newArea < Claim.MIN_AREA) {
                            if (newArea != 1)
                                new ActionBar(omp, () -> omp.lang("§c§lEen claim moet minimaal 3x3 zijn.", "§c§lA claim must at least be 3x3."), 60).send();

                            return;
                        }

                        remaining = omp.getRemainingClaimBlocks() - newArea;
                        if (remaining < 0) {
                            new ActionBar(omp, () -> omp.lang("§c§lJe hebt nog §6§l" + Math.abs(remaining) + " Claimblocks§c§l nodig om dit te claimen.", "§c§lYou need §6§l" + Math.abs(remaining) + " Claimblocks§c§l in order to claim this."), 60).send();
                            return;
                        }
                    } else {
                        owner = null;
                        remaining = 0;
                    }

                    Claim.CreateResult result = claimHandler.createClaim(omp.getWorld(), omp, owner, null, null, omp.getLastClaimToolLocation().getBlockX(), block.getX(), omp.getLastClaimToolLocation().getBlockY(), block.getY(), omp.getLastClaimToolLocation().getBlockZ(), block.getZ());

                    if (!result.isSucceeded()) {
                        new ActionBar(omp, () -> omp.lang("§c§lJouw claim overlapt een andere claim!", "§c§lYour claim overlaps another claim!"), 60).send();

                        if (result.getClaim() != null)
                            Visualization.show(omp, result.getClaim(), block.getY(), Visualization.Type.INVALID, player.getLocation());

                        return;
                    } else {
                        new ActionBar(omp, () -> omp.lang("§a§lSuccesvol een claim gemaakt. Claimblocks over: §6§l" + remaining + "§a§l.", "§a§lSuccessfully created a claim. Available Claimblocks: §6§l" + remaining + "§a§l."), 100).send();
                        Visualization.show(omp, result.getClaim(), block.getY(), Visualization.Type.CLAIM, player.getLocation());

                        omp.setLastClaimToolLocation(null);
                        omp.playSound(Sound.ENTITY_ARROW_HIT_PLAYER);
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

                return omp.lang("§a§l" + (omp.getResizingClaim() == null ? "Claimen" : "Claim aanpassen") + "...        §c§lPos 1: §6§l" + LocationUtils.friendlyString(omp.getLastClaimToolLocation()) + "        §c§lPos 2: §6§lGEEN", "§a§l" + (omp.getResizingClaim() == null ? "Claiming" : "Resizing Claim") + "...        §c§lPos 1: §6§l" + LocationUtils.friendlyString(omp.getLastClaimToolLocation()) + "        §c§lPos 2: §6§lNONE");
            }

            @Override
            public void onEnter(OMPlayer player) {
                super.onEnter(player);
                SurvivalPlayer omp = (SurvivalPlayer) player;

                omp.playSound(Sound.UI_BUTTON_CLICK);

                omp.resetScoreboard();
                omp.setScoreboard(new ClaimScoreboard(orbitMines, omp));
            }

            @Override
            public void onLeave(OMPlayer player) {
                super.onLeave(player);
                SurvivalPlayer omp = (SurvivalPlayer) player;
                omp.setLastClaimToolLocation(null);
                omp.setResizingClaim(null);

                omp.resetScoreboard();
                omp.setScoreboard(new Scoreboard(orbitMines, omp));
            }
        };
    }

    public static class Scoreboard extends DefaultScoreboard {

        public Scoreboard(OrbitMines orbitMines, SurvivalPlayer omp) {
            super(omp,
                    () -> orbitMines.getScoreboardAnimation().get(),
                    () -> "§m--------------",
                    () -> "",
                    () -> "§2§lCredits",
                    () -> " " + NumberUtils.locale(omp.getEarthMoney()),
                    () -> " ",
                    () -> "§9§lClaimblocks",
                    () -> " " + NumberUtils.locale(omp.getRemainingClaimBlocks()),
                    () -> "  "

            );
        }
    }

    public static class ClaimScoreboard extends DefaultScoreboard {

        public ClaimScoreboard(OrbitMines orbitMines, SurvivalPlayer omp) {
            super(omp,
                    () -> orbitMines.getScoreboardAnimation().get(),
                    () -> "§f§m------------------------",
                    () -> "§9§lClaimblocks",
                    () -> " " + NumberUtils.locale(omp.getRemainingClaimBlocks()),
                    () -> "",
                    () -> omp.lang("§6§lLINKER MUISKLIK", "§6§lLEFT CLICK"),
                    () -> omp.lang("§7In claim: Open claim GUI.", "§7In claim: Open claim GUI."),
                    () -> omp.lang("§7Buiten claim: Claim info.", "§7Outside claim: Claim info."),
                    () -> " ",
                    () -> omp.lang("§6§lSHIFT + LINKER MUISKLIK", "§6§lSHIFT + LEFT CLICK"),
                    () -> omp.lang("§7Dichtstbijzijnde claims.", "§7Show nearby claims."),
                    () -> "  ",
                    () -> omp.lang("§6§lRECHTER MUISKLIK", "§6§lRIGHT CLICK"),
                    () -> omp.lang("§7Kies hoeken om te claimen.", "§7Claim by selecting corners."),
                    () -> omp.lang("§7Pas een claim aan door op", "§7Resize an existing claim by"),
                    () -> omp.lang("§7een hoek te klikken.", "§7clicking on a corner.")

            );
        }
    }

    public enum Settings {

        DROPS_UNLOCKED;

    }
}
