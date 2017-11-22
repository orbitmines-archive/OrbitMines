package com.orbitmines.spigot.runnables;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class ScoreboardRunnable extends SpigotRunnable {

    public ScoreboardRunnable() {
        super(SpigotRunnable.TimeUnit.TICK, 5);
    }

    @Override
    public void run() {
        orbitMines.getScoreboardAnimation().next();

        for (OMPlayer omp : OMPlayer.getPlayers()) {
            omp.updateScoreboard();
        }
    }
}
