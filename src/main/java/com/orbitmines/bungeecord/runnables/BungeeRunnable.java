package com.orbitmines.bungeecord.runnables;

import com.orbitmines.bungeecord.OrbitMinesBungee;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public abstract class BungeeRunnable {

    private Map<Long, List<BungeeRunnable>> runnables = new HashMap<>();

    protected OrbitMinesBungee bungee;
    private Time time;
    private ScheduledTask task;

    public BungeeRunnable(TimeUnit timeUnit, int amount) {
        this.bungee = OrbitMinesBungee.getBungee();
        this.time = new Time(timeUnit, amount);

        start();
    }

    public abstract void run();

    public TimeUnit getTimeUnit() {
        return time.getTimeUnit();
    }

    public int getAmount() {
        return time.getAmount();
    }

    public void cancel() {
        if (!runnables.containsKey(time.getSeconds()))
            return;

        if (task != null && runnables.get(time.getSeconds()).size() == 1) {
            task.cancel();
            runnables.remove(time.getSeconds());
            return;
        }

        runnables.get(time.getSeconds()).remove(this);
    }

    private void start() {
        long seconds = time.getSeconds();

        if (runnables.containsKey(seconds)) {
            runnables.get(seconds).add(this);
            return;
        }

        runnables.put(seconds, new ArrayList<>(Collections.singletonList(this)));

        task = bungee.getProxy().getScheduler().schedule(bungee, () -> {
            for (BungeeRunnable runnable : runnables.get(time.getSeconds())) {
                runnable.run();
            }
        }, 0, time.getSeconds(), java.util.concurrent.TimeUnit.SECONDS);
    }

    public boolean isRunning() {
        return runnables.containsKey(time.getSeconds()) && runnables.get(time.getSeconds()).contains(this);
    }

    public static class Time {

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

        public long getSeconds() {
            return timeUnit.getSeconds() * amount;
        }

        public boolean equals(Time time) {
            return getSeconds() == time.getSeconds();
        }
    }

    public enum TimeUnit {

        SECOND(1),
        MINUTE(60),
        HOUR(360);

        private long seconds;

        TimeUnit(long seconds) {
            this.seconds = seconds;
        }

        public long getSeconds() {
            return seconds;
        }
    }
}
