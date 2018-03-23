package com.orbitmines.spigot.servers.minigames.utils;

/**
 * Created by Robin on 3/19/2018.
 */
public enum GameState {

    LOBBY(2* 60),
    WARM_UP(10),
    RUNNING(10 * 60),
    ENDING(20),
    RESTARTING(10);

    private int seconds;

    GameState(int seconds){
        this.seconds = seconds;
    }

    public int getSeconds() {
        return seconds;
    }
}
