package com.orbitmines.spigot.servers.minigames.handlers.phase;

import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardSet;
import com.orbitmines.spigot.servers.minigames.MiniGame;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGamePlayer;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGameType;
import com.orbitmines.spigot.servers.minigames.handlers.team.Team;
import com.orbitmines.spigot.servers.minigames.utils.GameState;
import org.bukkit.Sound;

import java.util.HashMap;

/**
 * Created by Robin on 4/3/2018.
 */
public abstract class DefaultEndingPhase extends MiniGamePhase {

    public DefaultEndingPhase(MiniGameType miniGame) {
        super(miniGame, GameState.ENDING, 15);
    }

    @Override
    public void start(MiniGame miniGame) {
        HashMap<Team, Integer> winners = getWinners(miniGame);
        for(Team team : winners.keySet()){
            if(team != null){
                Integer place = winners.get(team);
                team.gainReward(miniGame.getType().getPlace(place));
            }
        }

        //TODO: BROADCAST WHO ARE WINNERS
    }

    @Override
    public void run(MiniGame miniGame) {
        if(miniGame.getTime() < 3){
            for(Team team : miniGame.getTeams()){
                team.playSound(Sound.UI_BUTTON_CLICK, 2,2);
            }
        }
    }

    @Override
    public void onEnd(MiniGame miniGame) {
        super.onEnd(miniGame);

        miniGame.reset();
    }

    @Override
    public ScoreboardSet getScoreboard(MiniGamePlayer player) {
        return null;
    }

    protected abstract HashMap<Team, Integer> getWinners(MiniGame miniGame);
}
