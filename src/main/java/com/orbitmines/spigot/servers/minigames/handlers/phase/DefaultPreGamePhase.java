package com.orbitmines.spigot.servers.minigames.handlers.phase;

import com.orbitmines.spigot.api.Freezer;
import com.orbitmines.spigot.api.handlers.PreventionSet;
import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardSet;
import com.orbitmines.spigot.servers.minigames.MiniGame;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGamePlayer;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGameType;
import com.orbitmines.spigot.servers.minigames.handlers.team.Team;
import com.orbitmines.spigot.servers.minigames.utils.GameState;
import org.bukkit.Sound;

/**
 * Created by Robin on 4/3/2018.
 */
public abstract class DefaultPreGamePhase extends MiniGamePhase {


    public DefaultPreGamePhase(MiniGameType miniGame) {
        super(miniGame, GameState.WARM_UP, 30);
    }

    @Override
    public void start(MiniGame miniGame) {
        prevent(miniGame.getMap().getWorld(), PreventionSet.Prevention.values());

        for(MiniGamePlayer player : miniGame.getPlayers()){
            if(!player.isSpectator()){
                player.freeze(Freezer.MOVE);
            }
        }
    }

    @Override
    public void run(MiniGame miniGame) {
        if(miniGame.getTime() < 5){
            for(Team team : miniGame.getTeams()){
                team.playSound(Sound.UI_BUTTON_CLICK, 2, 2);
            }
        }
    }

    @Override
    public void onEnd(MiniGame miniGame) {
        super.onEnd(miniGame);

        miniGame.broadcast("MINI-GAME BEGUN AND SHIT! //TODO!");
    }

    @Override
    public ScoreboardSet getScoreboard(MiniGamePlayer player) {
        return null;
    }
}
