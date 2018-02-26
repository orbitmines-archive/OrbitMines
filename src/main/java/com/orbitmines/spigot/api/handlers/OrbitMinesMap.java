package com.orbitmines.spigot.api.handlers;

import com.orbitmines.api.Server;
import com.orbitmines.api.database.*;
import com.orbitmines.api.database.tables.TableMaps;
import com.orbitmines.api.database.tables.TableServers;
import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.spigot.api.datapoints.DataPointHandler;
import com.orbitmines.spigot.api.datapoints.DataPointLoader;
import com.orbitmines.spigot.api.handlers.worlds.WorldLoader;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class OrbitMinesMap {

    private final String worldName;
    private final WorldLoader.Type worldGenerator;
    private String name;
    private Type type;
    private Server server;
    private boolean enabled;
    private String authors;

    private World world;
    private DataPointHandler handler;

    public OrbitMinesMap(String worldName, WorldLoader.Type worldGenerator, String name, Type type, Server server, boolean enabled, String authors) {
        this.worldName = worldName;
        this.worldGenerator = worldGenerator;
        this.name = name;
        this.type = type;
        this.server = server;
        this.enabled = enabled;
        this.authors = authors;
    }

    public String getWorldName() {
        return worldName;
    }

    public WorldLoader.Type getWorldGenerator() {
        return worldGenerator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

        updateToDatabase();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;

        updateToDatabase();
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;

        updateToDatabase();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        updateToDatabase();
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;

        updateToDatabase();
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public DataPointHandler getHandler() {
        return handler;
    }

    public void setHandler(DataPointHandler handler) {
        this.handler = handler;
    }

    public void setupDataPoints() {
        /* Clear previous setups */
        handler.clearDataPoints();

        DataPointLoader loader = new DataPointLoader(world, handler.getAsMap());
        loader.load();

        handler.setup();
    }

    public void updateToDatabase() {
        Database.get().update(TableServers.MAPS, new Set[] {

                new Set(TableMaps.NAME, name),
                new Set(TableMaps.TYPE, type.toString()),
                new Set(TableMaps.SERVER, server.toString()),
                new Set(TableMaps.ENABLED, enabled),
                new Set(TableMaps.AUTHORS, authors),

        }, new Where(TableMaps.WORLD_NAME, worldName));
    }

    public static OrbitMinesMap getLobby(Server server) {
        /* Get lobby for gamemode */
        Map<Column, String> values = Database.get().getValues(Table.MAPS, new Column[] {

                TableMaps.WORLD_NAME,
                TableMaps.WORLD_GENERATOR,
                TableMaps.NAME,
                TableMaps.AUTHORS

        }, new Where(TableMaps.TYPE, Type.LOBBY.toString()), new Where(TableMaps.SERVER, server.toString()), new Where(TableMaps.ENABLED, true));

        return new OrbitMinesMap(
                values.get(TableMaps.WORLD_NAME),
                WorldLoader.Type.valueOf(values.get(TableMaps.WORLD_GENERATOR)),
                values.get(TableMaps.NAME),
                Type.LOBBY,
                server,
                true,
                values.get(TableMaps.AUTHORS)
        );
    }

    /* if map = null: random map */
    public static OrbitMinesMap getRandomMap(Server server, OrbitMinesMap previous) {
        /* Get all enabled gamemaps for gamemode */
        List<Map<Column, String>> entries = Database.get().getEntries(Table.MAPS, new Column[] {

                TableMaps.WORLD_NAME,
                TableMaps.WORLD_GENERATOR,
                TableMaps.NAME,
                TableMaps.AUTHORS

        }, new Where(TableMaps.TYPE, Type.GAMEMAP.toString()), new Where(TableMaps.SERVER, server.toString()), new Where(TableMaps.ENABLED, true));

        Map<Column, String> selected;

        if (entries.size() == 1) {
            /* Only One Map */
            selected = entries.get(0);
        } else {
            /* Previous Map is not null, so we rotate maps */
            if (previous != null) {
                for (Map<Column, String> entry : new ArrayList<>(entries)) {
                    if (entry.get(TableMaps.WORLD_NAME).equals(previous.getWorldName()))
                        entries.remove(entry);
                }
            }

            selected = RandomUtils.randomFrom(entries);
        }

        return new OrbitMinesMap(
                selected.get(TableMaps.WORLD_NAME),
                WorldLoader.Type.valueOf(selected.get(TableMaps.WORLD_GENERATOR).toUpperCase()),
                selected.get(TableMaps.NAME),
                Type.GAMEMAP,
                server,
                true,
                selected.get(TableMaps.AUTHORS)
        );
    }

    public enum Type {

        LOBBY,
        GAMEMAP;

    }
}
