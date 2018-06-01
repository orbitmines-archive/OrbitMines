package com.orbitmines.spigot.servers.survival.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.servers.survival.gui.RegionGUI;

public class CommandRegion extends Command {

    private String[] alias = { "/region", "/regions", "/rg" };

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return "(id)";
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        if (a.length == 1) {
            //TODO open closest region as 0,0?
            new RegionGUI().open(omp);
        }
    }
}
