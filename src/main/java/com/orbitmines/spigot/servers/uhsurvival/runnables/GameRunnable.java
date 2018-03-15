package com.orbitmines.spigot.servers.uhsurvival.runnables;

import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import com.orbitmines.spigot.servers.uhsurvival.UHSurvival;
import com.orbitmines.spigot.servers.uhsurvival.runnables.runnable.FoodRunnable;
import com.orbitmines.spigot.servers.uhsurvival.runnables.runnable.LootChestRunnable;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 3/10/2018.
 */
public class GameRunnable extends SpigotRunnable {

    private List<Runnable> runnables;
    private static UHSurvival uhSurvival;

    public GameRunnable(UHSurvival uhSurvival) {
        super(TimeUnit.SECOND, 1);
        this.uhSurvival = uhSurvival;
        this.runnables = new ArrayList<>();
        addRunnable(new LootChestRunnable(World.WORLD.getMap().getWarzone()));
        addRunnable(new FoodRunnable());
    }

    @Override
    public void run() {
        for(Runnable runnable : runnables){
            runnable.run();
        }
    }

    public void addRunnable(Runnable runnable){
        this.runnables.add(runnable);
    }

    public static abstract class Runnable {

        public UHSurvival uhSurvival;

        private int currentSeconds;
        private int maxSeconds;

        public Runnable(int maxSeconds){
            this.uhSurvival = GameRunnable.uhSurvival;
            this.currentSeconds = 0;
            this.maxSeconds = maxSeconds;
        }

        private void reset(){
            this.currentSeconds = maxSeconds;
        }

        public int getCurrentSeconds() {
            return currentSeconds;
        }

        public boolean isZero(){
            return currentSeconds == 0;
        }

        public void tick(int ticks){
            currentSeconds =- ticks;
            if(currentSeconds == 0){
                reset();
            }
        }

        public UHSurvival getInstance() {
            return uhSurvival;
        }

        public abstract void run();
    }
}
