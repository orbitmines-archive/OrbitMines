package com.orbitmines.spigot.api.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.servers.hub.Hub;

public class CommandRules extends Command {

    private final OrbitMines orbitMines;

    public CommandRules(OrbitMines orbitMines) {
        super(CommandLibrary.RULES);

        this.orbitMines = orbitMines;
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        orbitMines.getNms().customItem().openBook(omp.getPlayer(), Hub.RULE_BOOK.get(omp.getLanguage()));
    }
}
