package com.orbitmines.spigot.servers.minigames.minigames.cf.phases;

import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardSet;
import com.orbitmines.spigot.servers.minigames.MiniGame;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGamePlayer;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGameType;
import com.orbitmines.spigot.servers.minigames.handlers.phase.DefaultRunningPhase;

/**
 * Created by Robin on 4/5/2018.
 */
public class ChickenFightRunningPhase extends DefaultRunningPhase {

    public ChickenFightRunningPhase(MiniGameType type) {
        super(type);
    }

    @Override
    public void start(MiniGame miniGame) {
        super.start(miniGame);
        //DISGUISE
    }

    @Override
    public void run(MiniGame miniGame) {

    }

    @Override
    public ScoreboardSet getScoreboard(MiniGamePlayer player) {
        return null;
    }
}
