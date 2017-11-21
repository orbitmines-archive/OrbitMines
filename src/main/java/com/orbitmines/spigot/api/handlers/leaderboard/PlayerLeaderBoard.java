package com.orbitmines.spigot.api.handlers.leaderboard;

import com.madblock.spigot.api.handlers.OMPlayer;
import com.madblock.spigot.api.handlers.npc.Hologram;
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
            update(mbp);
        }
    }

    private void update(OMPlayer omp) {
        Hologram hologram = holograms.get(mbp);
        hologram.clearLines();

        setupLines(mbp, hologram);

        hologram.create(mbp.getPlayer());
    }

    public void onLogin(OMPlayer omp) {
        holograms.put(mbp, new Hologram(location, true));
        update(mbp);
    }

    public void onLogout(OMPlayer omp) {
        holograms.remove(mbp);
    }
}
