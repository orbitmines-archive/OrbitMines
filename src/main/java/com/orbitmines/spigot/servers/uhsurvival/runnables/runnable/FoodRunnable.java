package com.orbitmines.spigot.servers.uhsurvival.runnables.runnable;

import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.profile.PlayerProfile;
import com.orbitmines.spigot.servers.uhsurvival.runnables.GameRunnable;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.FoodType;

/**
 * Created by Robin on 3/14/2018.
 */
public class FoodRunnable extends GameRunnable.Runnable {

    public FoodRunnable() {
        super(270);
    }

    @Override
    public void run() {
        for (UHPlayer uhPlayer : UHPlayer.getUHPlayers().values()) {
            if (uhPlayer != null) {
                PlayerProfile profile = uhPlayer.getProfile();
                for (FoodType type : FoodType.values()) {
                    if (getCurrentSeconds() % type.getSeconds() == 0) {
                        profile.removeFood(type, 1);
                    }
                }
                if (getCurrentSeconds() % 3 == 0) {
                    profile.removeWater(1);
                    uhSurvival.getFoodManager().getWater().output(uhPlayer);
                }
            }
        }
        this.tick(1);
    }
}
