package com.orbitmines.spigot.servers.survival.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.servers.survival.Survival;

public class CommandSpawn extends Command {

    private final Survival survival;

    public CommandSpawn(Survival survival) {
        super(CommandLibrary.SURVIVAL_SPAWN);

        this.survival = survival;
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        if (omp.getWorld() == survival.getOrbitMines().getLobby().getWorld())
            omp.getPlayer().teleport(survival.getSpawnTp().getLocation());
        else
            survival.getSpawnTp().teleport(omp);
    }
}
