package com.orbitmines.spigot.servers.survival.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Server;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.servers.survival.Survival;

public class CommandSpawn extends Command {

    private String[] alias = { "/spawn" };

    private final Survival survival;

    public CommandSpawn(Survival survival) {
        super(Server.SURVIVAL);

        this.survival = survival;
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return null;
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        if (omp.getWorld() == survival.getOrbitMines().getLobby().getWorld())
            omp.getPlayer().teleport(survival.getSpawnTp().getLocation());
        else
            survival.getSpawnTp().teleport(omp);
    }
}
