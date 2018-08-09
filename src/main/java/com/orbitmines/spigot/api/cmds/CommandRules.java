package com.orbitmines.spigot.api.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.servers.hub.Hub;

public class CommandRules extends Command {

    private String[] alias = { "/rules" };

    private final OrbitMines orbitMines;

    public CommandRules(OrbitMines orbitMines) {
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
        orbitMines.getNms().customItem().openBook(omp.getPlayer(), Hub.RULE_BOOK.get(omp.getLanguage()));
    }
}
