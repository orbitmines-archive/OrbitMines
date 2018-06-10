package com.orbitmines.spigot.api.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;

public class CommandSolars extends Command {

    private String[] alias = { "/solars" };

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
        omp.sendMessage("Solars", Color.BLUE, "§7" + omp.lang("Je hebt", "You've got") + " §e§l" + omp.getSolars() + " " + (omp.getSolars() == 1 ? "Solar" : "Solars") + "§7.");
    }
}
