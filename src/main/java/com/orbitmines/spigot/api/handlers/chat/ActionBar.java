package com.orbitmines.spigot.api.handlers.chat;

import com.madblock.spigot.MadBlock;
import com.madblock.spigot.api.handlers.OMPlayer;
import com.madblock.spigot.api.handlers.timer.Timer;
import com.madblock.spigot.api.runnables.SpigotRunnable;

import java.util.HashMap;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class ActionBar {

    private Map<OMPlayer, ActionBar> actionBars = new HashMap<>();

    private MadBlock madblock;
    private String message;
    private OMPlayer player;
    private long stay;

    public ActionBar(OMPlayer player, String message, long stay) {
        this.madblock = MadBlock.getInstance();
        this.player = player;
        this.message = message;
        this.stay = stay;
    }

    public String getMessage() {
        return message;
    }

    public OMPlayer getPlayer() {
        return player;
    }

    public long getStay() {
        return stay;
    }

    public void setStay(int stay) {
        this.stay = stay;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void send() {
        if (!player.isLoggedIn())
            return;

        start();
    }

    public ActionBar copy() {
        return new ActionBar(player, message, stay);
    }

    public ActionBar copy(OMPlayer player) {
        return new ActionBar(player, message, stay);
    }

    private void start() {
        actionBars.put(player, this);

        new Timer(new SpigotRunnable.Time(SpigotRunnable.TimeUnit.TICK, (int) stay), new SpigotRunnable.Time(SpigotRunnable.TimeUnit.TICK, 1)) {

            @Override
            public void onInterval() {
                if (actionBars.get(player) == ActionBar.this)
                    madblock.getNms().actionBar().send(player.getPlayer(), ActionBar.this);
            }

            @Override
            public void onFinish() {
                stop();
            }
        };
    }

    public void stop() {
        if (actionBars.get(player) == this)
            actionBars.remove(player);
    }
}
