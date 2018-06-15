package com.orbitmines.spigot.api.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;

public class CommandPrisms extends Command {

    private String[] alias = { "/prisms" };

    public CommandPrisms() {
        super(null);
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
        omp.sendMessage("Prisms", Color.BLUE, "§7" + omp.lang("Je hebt", "You've got") + " §9§l" + NumberUtils.locale(omp.getPrisms()) + " " + (omp.getPrisms() == 1 ? "Prism" : "Prisms") + "§7.");
    }
}
