package com.orbitmines.spigot.servers.survival.handlers.teleportable;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.Teleportable;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import org.bukkit.Location;

public class SurvivalSpawn extends Teleportable {

    private final Survival survival;

    public SurvivalSpawn(Survival survival) {
        this.survival = survival;
    }

    @Override
    public Location getLocation() {
        return survival.getLobbySpawn();
    }

    @Override
    public int getDuration(OMPlayer omp) {
        return 3;
    }

    @Override
    public Color getColor() {
        return Color.LIME;
    }

    @Override
    public String getName() {
        return "Spawn";
    }

    @Override
    public void onTeleport(OMPlayer player, Location from, Location to) {
        SurvivalPlayer omp = (SurvivalPlayer) player;
        omp.setBackLocation(from);
    }
}
