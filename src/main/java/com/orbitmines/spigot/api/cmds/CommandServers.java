package com.orbitmines.spigot.api.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;

public class CommandServers extends Command {

    private String[] alias = { "/servers", "/serverselector" };

    private final OrbitMines orbitMines;

    public CommandServers(OrbitMines orbitMines) {
        super(null);

        this.orbitMines = orbitMines;
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
        orbitMines.getServerSelectors().get(omp.getLanguage()).open(omp);
    }
}
