package com.orbitmines.spigot.servers.minigames.handlers.phase;

import com.orbitmines.spigot.servers.minigames.MiniGame;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGamePlayer;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGameType;
import com.orbitmines.spigot.servers.minigames.handlers.powerups.PowerUp;
import com.orbitmines.spigot.servers.minigames.utils.GameState;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Robin on 4/3/2018.
 */
public abstract class DefaultRunningPhase extends MiniGamePhase {

    private Set<PowerUp> powerUps;

    public DefaultRunningPhase(MiniGameType type) {
        super(type, GameState.RUNNING, 15 * 60);
        this.powerUps = new HashSet<>();
    }

    @Override
    public void start(MiniGame miniGame) {
        for(MiniGamePlayer player : miniGame.getPlayers()){
            if(!player.isSpectator()){
                player.clearFreeze();
            }
            player.getSelectedKit().giveKit(player);
        }
    }

    @Override
    public void onEnd(MiniGame miniGame) {
        super.onEnd(miniGame);
        for(MiniGamePlayer player : miniGame.getPlayers()){
            player.getSelectedKit().clearKit(player);
        }
    }

    public Set<PowerUp> getPowerUps() {
        return powerUps;
    }

    public void addPowerUp(PowerUp powerUp){
        this.powerUps.add(powerUp);
    }
}
