package com.orbitmines.spigot.api.handlers.leaderboard.hologram;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.leaderboard.LeaderBoard;
import com.orbitmines.spigot.api.handlers.npc.Hologram;
import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardString;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public abstract class PlayerHologramLeaderBoard extends LeaderBoard {
    //TODO FIX AFTER SOME TIME APPEARING
    private Map<OMPlayer, Hologram> holograms;

    public PlayerHologramLeaderBoard(Location location) {
        super(location);

        holograms = new HashMap<>();
    }

    public abstract ScoreboardString[] getLines(OMPlayer omp);

    @Override
    public void update() {
        for (Hologram hologram : holograms.values()) {
            hologram.update();
        }
    }

    public void onLogin(OMPlayer omp) {
        Hologram hologram = new Hologram(location, 0.5, Hologram.Face.UP);
        hologram.create(omp.getPlayer());

        holograms.put(omp, hologram);

        for (ScoreboardString string : getLines(omp)) {
            hologram.addLine(string, true);
        }
    }

    public void onLogout(OMPlayer omp) {
        if (!holograms.containsKey(omp))
            return;

        /* Destroy hologram on logout, cause the hologram was only meant for this player */
        holograms.get(omp).destroy();

        holograms.remove(omp);
    }
}
