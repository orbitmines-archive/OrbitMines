package com.orbitmines.spigot.servers.survival;

import com.google.common.io.ByteArrayDataInput;
import com.orbitmines.api.PluginMessage;
import com.orbitmines.api.Server;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.TableServerData;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.OrbitMinesServer;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.PluginMessageHandler;
import com.orbitmines.spigot.api.handlers.PreventionSet;
import com.orbitmines.spigot.api.handlers.scoreboard.DefaultScoreboard;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.region.Region;
import com.orbitmines.spigot.servers.survival.handlers.region.RegionBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class Survival extends OrbitMinesServer {

    private World world;

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

        setupRegions();
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

    private void setupRegions() {
        Region.TELEPORTABLE = Database.get().getInt(Table.SERVER_DATA, TableServerData.DATA, new Where(TableServerData.TYPE, "TELEPORTABLE"));

        for (int i = 0; i < Region.REGION_COUNT; i++) {
            RegionBuilder builder = new RegionBuilder(world, i);
            builder.build();

            new Region(i, builder.getFixedSpawnLocation(), builder.getInventoryX(), builder.getInventoryY());
        }
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
