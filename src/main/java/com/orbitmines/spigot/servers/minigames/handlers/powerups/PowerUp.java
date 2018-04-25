package com.orbitmines.spigot.servers.minigames.handlers.powerups;

import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.spigot.servers.minigames.MiniGame;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGamePlayer;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGameType;

/**
 * Created by Robin on 4/5/2018.
 */
public abstract class PowerUp {

    private MiniGameType type;

    private String name;
    private double chance;

    public PowerUp(MiniGameType type, String name, double chance){
        this.name = name;
        this.chance = chance;
        this.type = type;
    }

    /** ABSTRACT METHODS */
    public abstract boolean canBeSpawned();

    public abstract void pickedUp(MiniGamePlayer player, MiniGame miniGame);

    /** BOOLEANS */
    public boolean canSpawn(){
        return RandomUtils.chance((int) chance);
    }

    /** GETTERS */
    public String getName() {
        return name;
    }

    public double getChance() {
        return chance;
    }

    public MiniGameType getType() {
        return type;
    }
}
