package com.orbitmines.spigot.servers.kitpvp.handlers;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Server;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.TableMaps;
import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.spigot.api.datapoints.DataPointHandler;
import com.orbitmines.spigot.api.handlers.OrbitMinesMap;
import com.orbitmines.spigot.api.handlers.timer.Timer;
import com.orbitmines.spigot.api.handlers.worlds.WorldLoader;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import com.orbitmines.spigot.api.utils.LocationUtils;
import com.orbitmines.spigot.api.utils.WorldUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class KitPvPMap extends OrbitMinesMap {

    public static KitPvPMap CURRENT;
    public static Timer TIMER;

    private static List<KitPvPMap> maps = new ArrayList<>();

    private List<Location> spawns;
    private List<Location> spectatorSpawns;

    private List<UUID> votes;
    private List<Location> voteSigns;

    //TODO Border Datapoint

    public KitPvPMap(String worldName, WorldLoader.Type worldGenerator, String name, boolean enabled, String authors) {
        super(worldName, worldGenerator, name, Type.GAMEMAP, Server.KITPVP, enabled, authors);

        maps.add(this);

        this.votes = new ArrayList<>();
    }

    public void setup(List<Location> spawns, List<Location> spectatorSpawns, List<Location> voteSigns) {
        this.spawns = spawns;
        this.spectatorSpawns = spectatorSpawns;
        this.voteSigns = voteSigns;

        new SpigotRunnable(SpigotRunnable.TimeUnit.SECOND, 1) {
            @Override
            public void run() {
                updateVoteSigns();
            }
        };
    }

    public void teleport(KitPvPPlayer omp) {
        omp.getPlayer().teleport(RandomUtils.randomFrom(omp.getGameMode() != GameMode.SPECTATOR ? spawns : spectatorSpawns));
    }

    public List<UUID> getVotes() {
        return votes;
    }

    public void updateVoteSigns() {
        for (Location voteSignLoc : voteSigns) {
            for (Player player : WorldUtils.getNearbyPlayers(voteSignLoc, 16)) {
                KitPvPPlayer omp = KitPvPPlayer.getPlayer(player);

                String[] lines = new String[4];

                lines[0] = "§l" + this.getName();
                lines[1] = votes.size() + " " + (votes.size() == 1 ? "Vote" : "Votes");
                lines[2] = "";

                if (getVotedFor(omp.getUUID()) == null)
                    lines[3] = (votes.contains(omp.getUUID()) ? "§2" : "§4") + "§l" + omp.lang("Gevoten", "Voted");
                else
                    lines[3] = "§n" + omp.lang("Klik om te voten", "Click to vote");

                player.sendSignChange(voteSignLoc, lines);

                //TODO PLAY CIRCLE EFFECT
            }
        }
    }

    public static KitPvPMap getVotedFor(UUID uuid) {
        for (KitPvPMap map : maps) {
            if (map.getVotes().contains(uuid))
                return map;
        }
        return null;
    }

    public static KitPvPMap getMap(Location signLocation) {
        for (KitPvPMap map : maps) {
            for (Location loc : map.voteSigns) {
                if (LocationUtils.equals(loc, signLocation))
                    return map;
            }
        }
        return null;
    }

    public static List<KitPvPMap> getMaps() {
        return maps;
    }

    public static void loadMaps(WorldLoader worldLoader) {
        /* Get all enabled gamemaps for gamemode */
        List<Map<Column, String>> entries = Database.get().getEntries(Table.MAPS, new Column[] {

                TableMaps.WORLD_NAME,
                TableMaps.WORLD_GENERATOR,
                TableMaps.NAME,
                TableMaps.AUTHORS

        }, new Where(TableMaps.TYPE, Type.GAMEMAP.toString()), new Where(TableMaps.SERVER, Server.KITPVP.toString()), new Where(TableMaps.ENABLED, true));

        for (Map<Column, String> entry : entries) {
            KitPvPMap map = new KitPvPMap(
                    entry.get(TableMaps.WORLD_NAME),
                    WorldLoader.Type.valueOf(entry.get(TableMaps.WORLD_GENERATOR).toUpperCase()),
                    entry.get(TableMaps.NAME),
                    true,
                    entry.get(TableMaps.AUTHORS)
            );

            World lobbyWorld = worldLoader.fromZip(map.getWorldName(), true, map.getWorldGenerator());
            map.setWorld(lobbyWorld);

            map.setHandler(DataPointHandler.getHandler(Server.KITPVP, Type.GAMEMAP));
        }
    }
}
