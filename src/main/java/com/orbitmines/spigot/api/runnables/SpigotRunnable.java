package com.orbitmines.spigot.api.runnables;

import com.orbitmines.spigot.OrbitMines;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public abstract class SpigotRunnable {

    private Map<Long, List<SpigotRunnable>> runnables = new HashMap<>();

    protected OrbitMines orbitMines;
    private Time time;
    private BukkitTask task;

    public SpigotRunnable(TimeUnit timeUnit, int amount) {
        this(timeUnit, amount, Time.ZERO, true);
    }
    public SpigotRunnable(TimeUnit timeUnit, int amount, boolean start) {
        this(timeUnit, amount, Time.ZERO, start);
    }

    public SpigotRunnable(TimeUnit timeUnit, int amount, Time delay) {
        this(new Time(timeUnit, amount), delay, true);
    }

    public SpigotRunnable(TimeUnit timeUnit, int amount, Time delay, boolean start) {
        this(new Time(timeUnit, amount), delay, start);
    }

    public SpigotRunnable(Time time) {
        this(time, true);
    }

    public SpigotRunnable(Time time, boolean start) {
        this(time, Time.ZERO, start);
    }

    public SpigotRunnable(Time time, Time delay) {
        this(time, delay, true);
    }

    public SpigotRunnable(Time time, Time delay, boolean start) {
        this.orbitMines = OrbitMines.getInstance();
        this.time = time;

        if (start)
            start(delay);
    }

    public abstract void run();

    public TimeUnit getTimeUnit() {
        return time.getTimeUnit();
    }

    public int getAmount() {
        return time.getAmount();
    }

    public void cancel() {
        if (!runnables.containsKey(time.getTicks()))
            return;

        if (task != null && runnables.get(time.getTicks()).size() == 1) {
            task.cancel();
            runnables.remove(time.getTicks());
            return;
        }

        runnables.get(time.getTicks()).remove(this);
    }

    public void start() {
        start(Time.ZERO);
    }

    public void start(Time delay) {
        long ticks = time.getTicks();

        if (runnables.containsKey(ticks)) {
            runnables.get(ticks).add(this);
            return;
        }

        runnables.put(ticks, new ArrayList<>(Collections.singletonList(this)));

        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                for (SpigotRunnable runnable : runnables.get(time.getTicks())) {
                    runnable.run();
                }
            }
        }.runTaskTimer(orbitMines, delay.getTicks(), time.getTicks());
    }

    public boolean isRunning() {
        return runnables.containsKey(time.getTicks()) && runnables.get(time.getTicks()).contains(this);
    }

    public static class Time {

        public final static Time ZERO = new Time(TimeUnit.TICK, 0);

        private TimeUnit timeUnit;
        private int amount;

        public Time(TimeUnit timeUnit, int amount) {
            this.timeUnit = timeUnit;
            this.amount = amount;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }

        public int getAmount() {
            return amount;
        }

        public long getTicks() {
            return timeUnit.getTicks() * amount;
        }

        public boolean equals(Time time) {
            return getTicks() == time.getTicks();
        }
    }

    public enum TimeUnit {

        TICK(1),
        SECOND(20),
        MINUTE(1200),
        HOUR(72000);

        private long ticks;

        TimeUnit(long ticks) {
            this.ticks = ticks;
        }

        public long getTicks() {
            return ticks;
        }
    }
}
