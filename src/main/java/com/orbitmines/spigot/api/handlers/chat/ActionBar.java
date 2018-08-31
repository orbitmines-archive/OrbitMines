package com.orbitmines.spigot.api.handlers.chat;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardString;
import com.orbitmines.spigot.api.handlers.timer.Timer;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class ActionBar {

    private static final Map<OMPlayer, List<ActionBar>> actionBars = new HashMap<>();

    private OrbitMines plugin;
    private final ScoreboardString message;
    private OMPlayer player;
    private long stay;

    private Timer timer;

    public ActionBar(OMPlayer player, ScoreboardString message, long stay) {
        this.plugin = OrbitMines.getInstance();
        this.player = player;
        this.message = message;
        this.stay = stay;
    }

    /* @Override to use */
    public void onRun() { }

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
        start();
    }

    public ActionBar copy() {
        return new ActionBar(player, message, stay);
    }

    public ActionBar copy(OMPlayer player) {
        return new ActionBar(player, message, stay);
    }

    private void start() {
        if (!actionBars.containsKey(player))
            actionBars.put(player, new ArrayList<>());

        actionBars.get(player).add(this);

        timer = new Timer(new SpigotRunnable.Time(SpigotRunnable.TimeUnit.TICK, (int) stay), new SpigotRunnable.Time(SpigotRunnable.TimeUnit.TICK, 1)) {

            @Override
            public void onInterval() {
                if (!player.getPlayer().isOnline()) {
                    stop();
                    return;
                }

                List<ActionBar> list = actionBars.get(player);

                /* Check if most recent actionbar is this actionbar */
                if (list.get(list.size() -1) == ActionBar.this) {
                    plugin.getNms().actionBar().send(Collections.singletonList(player.getPlayer()), message.getString());
                    onRun();
                }
            }

            @Override
            public void onFinish() {
                stop();
            }
        };
    }

    public void stop() {
        if (actionBars.get(player).size() == 1)
            actionBars.remove(player);
        else
            actionBars.get(player).remove(this);

        if (timer != null)
            timer.cancel();
    }

    public void forceStop() {
        stop();

        if (!actionBars.containsKey(player))
            /* Clear ActionBar, if we no longer need to display any action bar */
            plugin.getNms().actionBar().send(Collections.singletonList(player.getPlayer()), "");
    }
}
