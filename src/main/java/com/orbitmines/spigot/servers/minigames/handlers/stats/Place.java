package com.orbitmines.spigot.servers.minigames.handlers.stats;

import com.orbitmines.spigot.servers.minigames.handlers.MiniGamePlayer;

/**
 * Created by Robin on 3/20/2018.
 */
public abstract class Place {

    private int place;

    public Place(int place){
        this.place = place;
    }

    public int getPlace() {
        return place;
    }

    public abstract void gainReward(MiniGamePlayer player);
}
