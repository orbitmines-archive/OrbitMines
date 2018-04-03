package com.orbitmines.spigot.servers.minigames.handlers.phase;

import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardSet;
import com.orbitmines.spigot.servers.minigames.MiniGame;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGamePlayer;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGameType;
import com.orbitmines.spigot.servers.minigames.utils.GameState;

/**
 * Created by Robin on 4/3/2018.
 */
public abstract class DefaultRunningPhase extends MiniGamePhase {

    public DefaultRunningPhase(MiniGameType type) {
        super(type, GameState.RUNNING, 15 * 60);
    }

    @Override
    public void start(MiniGame miniGame) {
        for(MiniGamePlayer player : miniGame.getPlayers()){
            if(!player.isSpectator()){
                player.clearFreeze();
            }
        }
    }

    @Override
    public ScoreboardSet getScoreboard(MiniGamePlayer player) {
        return null;
    }
}
