package com.orbitmines.spigot.api.handlers.leaderboard;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.npc.Hologram;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public abstract class PlayerLeaderBoard extends LeaderBoard {

    private Map<OMPlayer, Hologram> holograms;

    public PlayerLeaderBoard(Location location) {
        super(location);

        hologram.delete();

        holograms = new HashMap<>();
    }

    public abstract void setupLines(OMPlayer omp, Hologram hologram);

    @Override
    public void update() {
        for (OMPlayer omp : holograms.keySet()) {
            update(omp);
        }
    }

    private void update(OMPlayer omp) {
        Hologram hologram = holograms.get(omp);
        hologram.clearLines();

        setupLines(omp, hologram);

        hologram.create(omp.getPlayer());
    }

    public void onLogin(OMPlayer omp) {
        holograms.put(omp, new Hologram(location, true));
        update(omp);
    }

    public void onLogout(OMPlayer omp) {
        holograms.remove(omp);
    }
}
