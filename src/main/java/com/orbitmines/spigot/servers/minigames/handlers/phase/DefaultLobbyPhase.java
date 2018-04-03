package com.orbitmines.spigot.servers.minigames.handlers.phase;

import com.orbitmines.spigot.api.handlers.OrbitMinesMap;
import com.orbitmines.spigot.api.handlers.PreventionSet;
import com.orbitmines.spigot.api.handlers.scoreboard.DefaultScoreboard;
import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardSet;
import com.orbitmines.spigot.servers.minigames.MiniGame;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGamePlayer;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGameType;
import com.orbitmines.spigot.servers.minigames.handlers.team.Team;
import com.orbitmines.spigot.servers.minigames.utils.GameState;

/**
 * Created by Robin on 3/29/2018.
 */
public abstract class DefaultLobbyPhase extends MiniGamePhase {

    public DefaultLobbyPhase(MiniGameType miniGame, GameState state, int duration) {
        super(miniGame, state, duration);
    }

    @Override
    public void start(MiniGame miniGame) {
        miniGame.generateMap();

        OrbitMinesMap map = miniGame.getMap();

        prevent(map.getWorld(), PreventionSet.Prevention.values());
    }

    @Override
    public void onEnd(MiniGame miniGame) {
        super.onEnd(miniGame);

        for(MiniGamePlayer player : miniGame.getPlayers()){
            player.clearFullInventory();
            player.clearPotionEffects();

            //TODO: DISABLE COSMETICS!
        }

        Team.generateTeams(miniGame);

        for(Team team : miniGame.getTeams()){
            team.teleport(null);
        }
    }

    @Override
    public ScoreboardSet getScoreboard(MiniGamePlayer player) {
        return new DefaultScoreboard(player, () -> "ScoreboardTitle",
                () -> "kek",
                () -> "nuhh");
        //TODO!
    }
}
