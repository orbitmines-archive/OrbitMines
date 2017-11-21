package com.orbitmines.spigot.runnables;

import com.madblock.spigot.api.handlers.OMPlayer;
import com.madblock.spigot.api.runnables.SpigotRunnable;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class ScoreboardRunnable extends SpigotRunnable {

    public ScoreboardRunnable() {
        super(SpigotRunnable.TimeUnit.TICK, 5);
    }

    @Override
    public void run() {
        madBlock.getScoreboardAnimation().next();

        for (OMPlayer omp : OMPlayer.getPlayers()) {
            mbp.updateScoreboard();
        }
    }
}
