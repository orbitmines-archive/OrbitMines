package com.orbitmines.spigot.servers.uhsurvival.runnables;

import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.warzone.Warzone;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.warzone.lootchest.LootChest;

/**
 * Created by Robin on 3/10/2018.
 */
public class LootChestRunnable extends SpigotRunnable {

    private int time = 3600;
    private Warzone warzone;

    public LootChestRunnable(Warzone warzone) {
        super(TimeUnit.SECOND, 1);
        if(warzone == null) throw new IllegalStateException("Warzone cannot be null!");
        this.warzone = warzone;
    }

    @Override
    public void run() {
        time--;
        if(time % 3600 == 0){
            //TODO: ADD BROADCAST MESSAGES!
        } else if(time % 900 == 0){

        } else if(time % 300 == 0){

        } else if(time <= 180 && time % 60 == 0){

        } else if(time <= 5){

        } else if(time == 0){
            warzone.addChest();
            this.time = 3600;
        }
        for(LootChest lootChest : warzone.getChests()){
            lootChest.tick(1);
            if(!lootChest.hasTimeLeft()){
                warzone.removeLootChest(lootChest);
            }
        }
    }
}
