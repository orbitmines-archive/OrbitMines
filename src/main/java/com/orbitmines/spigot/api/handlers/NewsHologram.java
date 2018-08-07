package com.orbitmines.spigot.api.handlers;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.Set;
import com.orbitmines.api.database.tables.TableServerData;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.npc.Hologram;
import com.orbitmines.spigot.api.utils.Serializer;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsHologram {

    private static HashMap<String, NewsHologram> holograms = new HashMap<>();

    private OrbitMines orbitMines;
    private String name;
    private Hologram hologram;

    public NewsHologram(String name) {
        this(name, null);
    }

    public NewsHologram(String name, Location location) {
        orbitMines = OrbitMines.getInstance();
        holograms.put(name, this);

        this.name = name;

        if (!loadedFromDatabase()) {
            if (location == null)
                throw new NullPointerException();

            this.hologram = new Hologram(location, 1.75, Hologram.Face.UP);
            this.hologram.addLine(() -> "§a§l" + name, false);

            insert();
        }

        this.hologram.create();
    }

    public String getName() {
        return name;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public void addLine(String line) {
        this.hologram.addLine(() -> ChatColor.translateAlternateColorCodes('&', line), true);

        update();
    }

    public void setLine(int index, String line) {
        this.hologram.setLine(index, () -> ChatColor.translateAlternateColorCodes('&', line));

        update();
    }

    public void removeLine(int index) {
        this.hologram.removeLine(index);

        if (hologram.getLines().size() != 0) {
            this.hologram.create();
            update();
        } else {
            delete(true);
        }
    }

    public void relocate(Location location) {
        this.hologram.setSpawnLocation(location);

        update();
    }

    public void delete(boolean update) {
        this.hologram.destroy();
        holograms.remove(name);

        if (update)
            remove();
    }

    private void loadHologram(String string) {
        String[] data = string.split("~");

        if (this.hologram == null) {
            this.hologram = new Hologram(Serializer.parseLocation(data[0]), 1.75, Hologram.Face.UP);
        } else {
            this.hologram.setSpawnLocation(Serializer.parseLocation(data[0]));
            this.hologram.removeAllLines();
        }

        for (String line : data[1].split(";")) {
            this.hologram.addLine(() -> ChatColor.translateAlternateColorCodes('&', line), false);
        }
    }

    private void insert() {
        Database.get().insert(Table.SERVER_DATA, orbitMines.getServerHandler().getServer().toString(), getVariableName(), hologram.serialize());
    }

    private void update() {
        Database.get().update(Table.SERVER_DATA, new Set(TableServerData.DATA, hologram.serialize()), new Where(TableServerData.TYPE, getVariableName()));
    }

    private void remove() {
        Database.get().delete(Table.SERVER_DATA, new Where(TableServerData.TYPE, getVariableName()));
    }

    private String getVariableName() {
        return "HOLOGRAM|" + this.name;
    }

    public boolean loadedFromDatabase() {
        if (!Database.get().contains(Table.SERVER_DATA, TableServerData.TYPE, new Where(TableServerData.TYPE, getVariableName())))
            return false;

        String data = Database.get().getString(Table.SERVER_DATA, TableServerData.DATA, new Where(TableServerData.TYPE, getVariableName()));
        loadHologram(data);

        return true;
    }

    public static void setup() {
        List<Map<Column, String>> entries = Database.get().getEntries(Table.SERVER_DATA, TableServerData.TYPE, new Where(TableServerData.SERVER, OrbitMines.getInstance().getServerHandler().getServer().toString()), new Where(Where.Operator.LIKE, TableServerData.TYPE, "HOLOGRAM|%"));

        for (Map<Column, String> entry : entries) {
            new NewsHologram(entry.get(TableServerData.TYPE).substring(9));
        }
    }
    
    public static HashMap<String, NewsHologram> getHolograms() {
        return holograms;
    }

    public static NewsHologram getHologram(String name) {
        for (NewsHologram hologram : holograms.values()) {
            if (hologram.getName().equals(name))
                return hologram;
        }
        return null;
    }
}
