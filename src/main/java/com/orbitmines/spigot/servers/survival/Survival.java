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
import com.orbitmines.spigot.api.handlers.scoreboard.DefaultScoreboard;
import com.orbitmines.spigot.api.utils.ConsoleUtils;
import com.orbitmines.spigot.api.utils.Serializer;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.claim.Claim;
import com.orbitmines.spigot.servers.survival.handlers.claim.ClaimHandler;
import com.orbitmines.spigot.servers.survival.handlers.region.Region;
import com.orbitmines.spigot.servers.survival.handlers.region.RegionBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

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

        claimHandler = new ClaimHandler();

        setupRegions();
        setupClaims();
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

            Set<Claim.Settings> settings = new HashSet<>();
            if (!entry.get(TableSurvivalClaim.SETTINGS).equals("")) {
                for (String setting : entry.get(TableSurvivalClaim.SETTINGS).split("\\|")) {
                    settings.add(Claim.Settings.valueOf(setting));
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
}
