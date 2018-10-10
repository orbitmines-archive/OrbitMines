package com.orbitmines.spigot.servers.kitpvp.runnables;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Language;
import com.orbitmines.api.utils.TimeUtils;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import com.orbitmines.spigot.api.utils.WorldUtils;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class MapResetSignRunnable extends SpigotRunnable {

    private List<Location> signLocations;

    public MapResetSignRunnable(List<Location> signLocations) {
        super(TimeUnit.SECOND, 1);

        this.signLocations = signLocations;
    }

    @Override
    public void run() {
        String[] lines = {"", "§lMap Reset", TimeUtils.fromTimeStamp(KitPvPMap.TIMER.getRemainingTicks() * 50, Language.ENGLISH), ""};

        for (Location location : signLocations) {
            for (Player player : WorldUtils.getNearbyPlayers(location, 16)) {
                player.sendSignChange(location, lines);
            }
        }
    }
}
