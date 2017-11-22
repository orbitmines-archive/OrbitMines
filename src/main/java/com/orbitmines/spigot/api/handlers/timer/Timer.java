package com.orbitmines.spigot.api.handlers.timer;

import com.orbitmines.api.Cooldown;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import org.bukkit.scheduler.BukkitRunnable;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public abstract class Timer extends Cooldown {

    private final SpigotRunnable.Time interval;

    private SpigotRunnable runnable;
    private long totalTicks;
    private long remainingTicks;
    private int intervalTicks;

    public Timer(SpigotRunnable.Time cooldown) {
        this(cooldown, -1);
    }

    public Timer(SpigotRunnable.Time cooldown, long delay) {
        this(cooldown, cooldown, delay);
    }

    public Timer(SpigotRunnable.Time cooldown, SpigotRunnable.Time interval) {
        this(cooldown, interval, -1);
    }

    public Timer(SpigotRunnable.Time cooldown, SpigotRunnable.Time interval, long delay) {
        /* 20 ticks = 1 second, 1000 millis = 1 second */
        super(cooldown.getTicks() * 50);

        this.interval = interval;
        totalTicks = cooldown.getTicks();
        remainingTicks = totalTicks;
        intervalTicks = 0;

        if (delay == -1) {
            startRunnable();
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                startRunnable();
            }
        }.runTaskLater(OrbitMines.getInstance(), delay);
    }

    /* Called on Finish */
    public abstract void onFinish();

    /* Called every #getInterval, override to use */
    public  void onInterval() {
    }

    public SpigotRunnable.Time getInterval() {
        return interval;
    }

    public SpigotRunnable getRunnable() {
        return runnable;
    }

    /* Between 0 and 1 */
    public float getProgress() {
        return (float) remainingTicks / (float) totalTicks;
    }

    public float getReverseProgress() {
        return ((float) totalTicks - (float) remainingTicks) / (float) totalTicks;
    }

    public long getTotalTicks() {
        return totalTicks;
    }

    private void startRunnable() {
        long ticksPerInterval = interval.getTicks();

        runnable = new SpigotRunnable(SpigotRunnable.TimeUnit.TICK, 1) {
            @Override
            public void run() {
                remainingTicks--;

                if (remainingTicks == 0) {
                    onFinish();
                    cancel();
                    return;
                }

                intervalTicks++;

                if (ticksPerInterval == intervalTicks) {
                    onInterval();

                    intervalTicks = 0;
                }
            }
        };
    }

    public void cancel() {
        runnable.cancel();
    }

    public void finish() {
        remainingTicks = 0;
        onFinish();
        cancel();
    }

    public long getRemainingTicks() {
        return remainingTicks;
    }
}
