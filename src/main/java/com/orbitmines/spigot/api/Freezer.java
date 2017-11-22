package com.orbitmines.spigot.api;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.npc.PlayerFreezer;
import org.bukkit.Location;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public enum Freezer {

    ARMORSTAND_RIDE {
        @Override
        public void freeze(OMPlayer omp, Location location) {
            if (location != null)
                new PlayerFreezer(omp.getPlayer(), location);
            else
                new PlayerFreezer(omp.getPlayer());
        }

        @Override
        public void clearFreeze(OMPlayer omp) {
            PlayerFreezer.getFreezer(omp.getPlayer()).delete();
        }
    },
    MOVE,
    MOVE_AND_JUMP,
    MOVE_AND_LOOK_AROUND;

    public void freeze(OMPlayer omp, Location location) {}

    public void clearFreeze(OMPlayer omp) {}

}
