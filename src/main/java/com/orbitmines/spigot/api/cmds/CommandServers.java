package com.orbitmines.spigot.api.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;

public class CommandServers extends Command {

    private final OrbitMines orbitMines;

    public CommandServers(OrbitMines orbitMines) {
        super(CommandLibrary.SERVERS);

        this.orbitMines = orbitMines;
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        orbitMines.getServerSelectors().get(omp.getLanguage()).open(omp);
    }
}
