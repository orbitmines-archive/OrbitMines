package com.orbitmines.spigot.api.handlers.chat;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardString;
import com.orbitmines.spigot.api.handlers.timer.Timer;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;

import java.util.HashMap;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class ActionBar {

    private Map<OMPlayer, ActionBar> actionBars = new HashMap<>();

    private OrbitMines orbitMines;
    private final ScoreboardString message;
    private OMPlayer player;
    private long stay;

    private Timer timer;

    public ActionBar(OMPlayer player, ScoreboardString message, long stay) {
        this.orbitMines = OrbitMines.getInstance();
        this.player = player;
        this.message = message;
        this.stay = stay;
    }

    public ScoreboardString getMessage() {
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

        timer = new Timer(new SpigotRunnable.Time(SpigotRunnable.TimeUnit.TICK, (int) stay), new SpigotRunnable.Time(SpigotRunnable.TimeUnit.TICK, 1)) {

            @Override
            public void onInterval() {
                if (!player.getPlayer().isOnline()) {
                    stop();
                    return;
                }

                /* If another actionbar stopped */
                if (!actionBars.containsKey(player))
                    actionBars.put(player, ActionBar.this);

                /* Check if most recent actionbar is this actionbar */
                if (actionBars.get(player) == ActionBar.this)
                    orbitMines.getNms().actionBar().send(player.getPlayer(), ActionBar.this);
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

        if (timer != null)
            timer.cancel();
    }

    public void forceStop() {
        stop();

        orbitMines.getNms().actionBar().send(player.getPlayer(), new ActionBar(player, () -> "", 1));
    }
}
