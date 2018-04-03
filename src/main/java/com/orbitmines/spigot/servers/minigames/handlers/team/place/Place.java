package com.orbitmines.spigot.servers.minigames.handlers.team.place;

import com.orbitmines.spigot.servers.minigames.handlers.MiniGamePlayer;

/**
 * Created by Robin on 3/27/2018.
 */
public abstract class Place {

    public static final int FIRST = 1;
    public static final int SECOND = 2;
    public static final int THIRD = 3;

    private int place;

    public Place(int place){
        this.place = place;
    }

    public int getPlace() {
        return place;
    }

    public abstract void gainReward(MiniGamePlayer player);
}
