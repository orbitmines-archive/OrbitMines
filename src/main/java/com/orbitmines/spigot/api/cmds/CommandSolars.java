package com.orbitmines.spigot.api.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;

public class CommandSolars extends Command {

    public CommandSolars() {
        super(CommandLibrary.SOLARS);
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        omp.sendMessage("Solars", Color.BLUE, "§7" + omp.lang("Je hebt", "You've got") + " §e§l" + NumberUtils.locale(omp.getSolars()) + " " + (omp.getSolars() == 1 ? "Solar" : "Solars") + "§7.");
    }
}
