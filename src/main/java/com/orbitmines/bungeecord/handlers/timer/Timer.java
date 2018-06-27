package com.orbitmines.bungeecord.handlers.timer;

import com.orbitmines.api.Cooldown;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.runnables.BungeeRunnable;
import net.md_5.bungee.api.ProxyServer;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public abstract class Timer extends Cooldown {

    private final BungeeRunnable.Time interval;

    private BungeeRunnable runnable;
    private long totalSeconds;
    private long remainingSeconds;
    private int intervalSeconds;

    public Timer(BungeeRunnable.Time cooldown) {
        this(cooldown, -1);
    }

    public Timer(BungeeRunnable.Time cooldown, long delay) {
        this(cooldown, cooldown, delay);
    }

    public Timer(BungeeRunnable.Time cooldown, BungeeRunnable.Time interval) {
        this(cooldown, interval, -1);
    }

    public Timer(BungeeRunnable.Time cooldown, BungeeRunnable.Time interval, long delay) {
        /* 20 ticks = 1 second, 1000 millis = 1 second */
        super(cooldown.getSeconds() * 1000);

        this.interval = interval;
        totalSeconds = cooldown.getSeconds();
        remainingSeconds = totalSeconds;
        intervalSeconds = 0;

        if (delay == -1) {
            startRunnable();
            return;
        }

        ProxyServer.getInstance().getScheduler().schedule(OrbitMinesBungee.getBungee(), this::startRunnable, delay, java.util.concurrent.TimeUnit.SECONDS);
    }

    /* Called on Finish */
    public abstract void onFinish();

    /* Called every #getInterval, override to use */
    public  void onInterval() {
    }

    public BungeeRunnable.Time getInterval() {
        return interval;
    }

    public BungeeRunnable getRunnable() {
        return runnable;
    }

    /* Between 0 and 1 */
    public float getProgress() {
        return (float) remainingSeconds / (float) totalSeconds;
    }

    public float getReverseProgress() {
        return ((float) totalSeconds - (float) remainingSeconds) / (float) totalSeconds;
    }

    public long getTotalSeconds() {
        return totalSeconds;
    }

    private void startRunnable() {
        long secondsPerInterval = interval.getSeconds();

        runnable = new BungeeRunnable(BungeeRunnable.TimeUnit.SECOND, 1) {
            @Override
            public void run() {
                remainingSeconds--;

                if (remainingSeconds == 0) {
                    onFinish();
                    cancel();
                    return;
                }

                intervalSeconds++;

                if (secondsPerInterval == intervalSeconds) {
                    onInterval();

                    intervalSeconds = 0;
                }
            }
        };
    }

    public void cancel() {
        runnable.cancel();
    }

    public void finish() {
        remainingSeconds = 0;
        onFinish();
        cancel();
    }

    public long getRemainingSeconds() {
        return remainingSeconds;
    }
}
