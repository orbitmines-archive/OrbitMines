package com.orbitmines.spigot.servers.minigames.handlers;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.servers.minigames.MiniGame;
import com.orbitmines.spigot.servers.minigames.MiniGames;
import com.orbitmines.spigot.servers.minigames.handlers.stats.Stats;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 3/27/2018.
 */
public class MiniGamePlayer extends OMPlayer {

    private MiniGames miniGames;

    private MiniGame miniGame;

    private List<Stats.Data> stats;

    private boolean spectator;

    public MiniGamePlayer(MiniGames miniGames, Player player) {
        super(player);
        this.miniGames = miniGames;
        this.stats = new ArrayList<>();
        this.spectator = false;
    }

    @Override
    protected void onLogin() {

    }

    @Override
    protected void onLogout() {

    }

    @Override
    public void onVote(int votes) {

    }

    @Override
    public boolean canReceiveVelocity() {
        return false;
    }

    /** MINI-GAME METHODS */
    public boolean isInGame(){
        return miniGame != null;
    }

    public void join(MiniGame miniGame){
        this.miniGame = miniGame;
    }

    public MiniGame getGame() {
        return miniGame;
    }

    public void leave(){
        this.miniGame = null;
    }

    public boolean isSpectator(){
        return spectator;
    }

    public void setSpectator(boolean spectator){
        this.spectator = spectator;
        if(spectator){

        }
    }

    /** STATS METHODS */
    public Stats.Data getData(MiniGameType type){
        for(Stats.Data data : stats){
            if(data.getStats().getType() == type){
                return data;
            }
        }
        return null;
    }
}
