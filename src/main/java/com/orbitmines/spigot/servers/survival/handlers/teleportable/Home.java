package com.orbitmines.spigot.servers.survival.handlers.teleportable;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.database.*;
import com.orbitmines.api.database.tables.survival.TableSurvivalHomes;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.Teleportable;
import com.orbitmines.spigot.api.utils.Serializer;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Home extends Teleportable {

    public static Color COLOR = Color.ORANGE;
    public static int MAX_CHARACTERS = 40;

    private final UUID uuid;
    private String name;
    private Location location;

    public Home(UUID uuid, String name, Location location) {
        this.uuid = uuid;
        this.name = name;
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public int getDuration(OMPlayer omp) {
        return 3;
    }

    @Override
    public Color getColor() {
        return COLOR;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void onTeleport(OMPlayer player, Location from, Location to) {
        SurvivalPlayer omp = (SurvivalPlayer) player;
        omp.setBackLocation(from);
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setName(String name) {
        Database.get().update(Table.SURVIVAL_HOMES, new Set(TableSurvivalHomes.NAME, name), new Where(TableSurvivalHomes.UUID, uuid.toString()), new Where(TableSurvivalHomes.NAME, this.name));

        this.name = name;
    }

    public void setLocation(Location location) {
        this.location = location;

        Database.get().update(Table.SURVIVAL_HOMES, new Set(TableSurvivalHomes.LOCATION, Serializer.serialize(location)), new Where(TableSurvivalHomes.UUID, uuid.toString()), new Where(TableSurvivalHomes.NAME, this.name));
    }

    public void delete() {
        SurvivalPlayer omp = SurvivalPlayer.getPlayer(uuid);
        if (omp != null) { /* If Online */
            omp.getHomes().remove(this);
            omp.sendMessage("Home", Color.LIME, "§7Je home is verwijderd! (" + COLOR.getChatColor() + this.name + "§7)", "§7Removed your home! (" + COLOR.getChatColor() + this.name + "§7)");
        }

        Database.get().delete(Table.SURVIVAL_HOMES, new Where(TableSurvivalHomes.UUID, uuid.toString()), new Where(TableSurvivalHomes.NAME, this.name));
    }

    public static Home createHome(UUID uuid, String name, Location location) {
        Database.get().insert(Table.SURVIVAL_HOMES, uuid.toString(), name, Serializer.serialize(location));

        return new Home(uuid, name, location);
    }

    public static List<Home> getHomesFor(UUID uuid) {
        List<Home> homes = new ArrayList<>();

        List<Map<Column, String>> entries = Database.get().getEntries(Table.SURVIVAL_HOMES, new Where(TableSurvivalHomes.UUID, uuid.toString()));

        for (Map<Column, String> entry : entries) {
            Home home = new Home(uuid, entry.get(TableSurvivalHomes.NAME), Serializer.parseLocation(entry.get(TableSurvivalHomes.LOCATION)));
            homes.add(home);
        }

        return homes;
    }

    public static int getHomeCount(UUID uuid) {
        return Database.get().getCount(Table.SURVIVAL_HOMES, new Where(TableSurvivalHomes.UUID, uuid.toString()));
    }

    public static List<Home> getAllHomes() {
        List<Home> homes = new ArrayList<>();

        List<Map<Column, String>> entries = Database.get().getEntries(Table.SURVIVAL_HOMES);

        for (Map<Column, String> entry : entries) {
            Home home = new Home(UUID.fromString(entry.get(TableSurvivalHomes.UUID)), entry.get(TableSurvivalHomes.NAME), Serializer.parseLocation(entry.get(TableSurvivalHomes.LOCATION)));
            homes.add(home);
        }

        return homes;
    }
}
