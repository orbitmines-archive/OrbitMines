package com.orbitmines.spigot.servers.minigames.handlers;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.servers.minigames.MiniGame;
import com.orbitmines.spigot.servers.minigames.MiniGameType;
import com.orbitmines.spigot.servers.minigames.MiniGames;
import com.orbitmines.spigot.servers.minigames.handlers.stats.Stats;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Robin on 3/18/2018.
 */
public class MiniGamePlayer extends OMPlayer {

    private static HashMap<UUID, MiniGamePlayer> players = new HashMap<>();

    private MiniGames miniGames;

    private MiniGame miniGame;

    private List<Stats.Data> stats;

    public MiniGamePlayer(Player player, MiniGames miniGames) {
        super(player);
        this.miniGames = miniGames;
        this.stats = new ArrayList<>();
        this.setUpStats();
        players.put(player.getUniqueId(), this);
    }

    @Override
    protected void onLogin() {

    }

    @Override
    protected void onLogout() {
        leave();
    }

    @Override
    public void onVote(int votes) {

    }

    @Override
    public boolean canReceiveVelocity() {
        return false;
    }

    /* MINI-GAME METHODS */
    public boolean leave(){
        if(isInGame()) {
            this.miniGame.leave(this);
            this.miniGame = null;
        }
        return isInGame();
    }

    public boolean isInGame(){
        return miniGame != null;
    }

    public void join(MiniGame miniGame){
        this.miniGame = miniGame;
        this.miniGame.join(this);
    }

    /* STATS METHODS*/
    private void setUpStats(){
        for(MiniGameType miniGameType : miniGames.getTypes()){
            stats.add(new Stats.Data(this.getUUID(), Stats.getStats(miniGameType)));
        }
    }

    public Stats.Data getData(MiniGameType miniGameType){
        for(Stats.Data data : stats){
            if(data.getStats().getType() == miniGameType){
                return data;
            }
        }
        return null;
    }
}
