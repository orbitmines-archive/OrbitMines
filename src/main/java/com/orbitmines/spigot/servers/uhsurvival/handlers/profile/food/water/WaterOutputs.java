package com.orbitmines.spigot.servers.uhsurvival.handlers.profile.food.water;

import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.profile.PlayerProfile;

/**
 * Created by Robin on 3/18/2018.
 */
class WaterOutputs {

    WaterOutputs(Water water){
        water.registerOutput(new WaterOutput_1());
        water.registerOutput(new WaterOutput_2());
        water.registerOutput(new WaterOutput_3());
        water.registerOutput(new WaterOutput_4());
    }

    private class WaterOutput_1 extends Water.WaterOutput {

        private WaterOutput_1() {
            super(30, 20);
        }

        @Override
        public void output(UHPlayer player) {
            player.getProfile().setWaterSpeed(0.085F);
        }
    }

    private class WaterOutput_2 extends Water.WaterOutput {

        private WaterOutput_2() {
            super(20, 5);
        }

        @Override
        public void output(UHPlayer player) {
            player.getProfile().setWaterSpeed(0.055F);
        }
    }

    private class WaterOutput_3 extends Water.WaterOutput {

        private WaterOutput_3(){
            super(5,0);
        }

        @Override
        public void output(UHPlayer player) {
            player.getProfile().setWaterSpeed(0.01F);
            player.getPlayer().damage(5.5 - player.getProfile().getWater());
        }
    }

    private class WaterOutput_4 extends Water.WaterOutput {

        private WaterOutput_4(){
            super(100, 30);
        }

        @Override
        public void output(UHPlayer player) {
            player.getProfile().setWaterSpeed(PlayerProfile.NORMAL_SPEED);
        }
    }
}
