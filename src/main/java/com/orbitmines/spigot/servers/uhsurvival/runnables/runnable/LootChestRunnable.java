package com.orbitmines.spigot.servers.uhsurvival.runnables.runnable;

import com.orbitmines.spigot.servers.uhsurvival.handlers.map.warzone.Warzone;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.warzone.lootchest.LootChest;
import com.orbitmines.spigot.servers.uhsurvival.runnables.GameRunnable;

/**
 * Created by Robin on 3/14/2018.
 */
public class LootChestRunnable extends GameRunnable.Runnable {

    private Warzone warzone;

    public LootChestRunnable(Warzone warzone){
        super(3600);
        this.warzone = warzone;
    }

    @Override
    public void run() {
        int minutes = getCurrentSeconds() / 60;
        if(isZero()){
            warzone.addChest();
        } else {
            //TODO: BROADCAST!
        }
        this.tick(1);
        for(LootChest lootChest : warzone.getChests()){
            if(!lootChest.hasTimeLeft()){
                warzone.removeLootChest(lootChest);
            } else {
                lootChest.tick(1);
            }
        }
    }
}
