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
import com.orbitmines.spigot.api.handlers.OrbitMinesMap;
import com.orbitmines.spigot.api.handlers.worlds.WorldLoader;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KitPvPMap extends OrbitMinesMap {

    private static List<KitPvPMap> maps = new ArrayList<>();

    private List<Location> spawns;
    private List<Location> spectatorSpawns;

    //TODO Border Datapoint

    public KitPvPMap(String worldName, WorldLoader.Type worldGenerator, String name, boolean enabled, String authors) {
        super(worldName, worldGenerator, name, Type.GAMEMAP, Server.KITPVP, enabled, authors);

        maps.add(this);

        this.spawns = new ArrayList<>();
        this.spectatorSpawns = new ArrayList<>();
    }

    public List<Location> getSpawns() {
        return spawns;
    }

    public List<Location> getSpectatorSpawns() {
        return spectatorSpawns;
    }

    public static List<KitPvPMap> getMaps() {
        return maps;
    }

    public static void loadMaps() {
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

            //TODO setupDatapoints
        }
    }
}
