package com.orbitmines.spigot.api.runnables;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public abstract class PlayerRunnable {

    private Map<Long, List<PlayerRunnable>> playerRunnables = new HashMap<>();

    protected OrbitMines orbitMines;
    private SpigotRunnable.Time time;
    private BukkitTask task;

    public PlayerRunnable(SpigotRunnable.TimeUnit timeUnit, int amount) {
        this.orbitMines = OrbitMines.getInstance();
        this.time = new SpigotRunnable.Time(timeUnit, amount);

        start();
    }

    public abstract void run(OMPlayer omp);

    public SpigotRunnable.TimeUnit getTimeUnit() {
        return time.getTimeUnit();
    }

    public int getAmount() {
        return time.getAmount();
    }

    public void cancel() {
        if (!playerRunnables.containsKey(time.getTicks()))
            return;

        if (task != null && playerRunnables.get(time.getTicks()).size() == 1) {
            task.cancel();
            playerRunnables.remove(time.getTicks());
            return;
        }

        playerRunnables.get(time.getTicks()).remove(this);
    }

    private void start() {
        long ticks = time.getTicks();

        if (playerRunnables.containsKey(ticks)) {
            playerRunnables.get(ticks).add(this);
            return;
        }

        playerRunnables.put(ticks, new ArrayList<>(Collections.singletonList(this)));

        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                List<PlayerRunnable> runnables = playerRunnables.get(time.getTicks());
                for (OMPlayer omp : OMPlayer.getPlayers()) {
                    for (PlayerRunnable runnable : runnables) {
                        runnable.run(omp);
                    }
                }
            }
        }.runTaskTimer(orbitMines, 0, time.getTicks());
    }

    public boolean isRunning() {
        return playerRunnables.containsKey(time.getTicks()) && playerRunnables.get(time.getTicks()).contains(this);
    }
}
