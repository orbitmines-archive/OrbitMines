package com.orbitmines.spigot.servers.survival.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Server;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.servers.survival.gui.SurvivalPrismSolarShopGUI;

public class CommandPrismShop extends Command {

    private String[] alias = { "/prismshop", "/prismsshop" };

    public CommandPrismShop() {
        super(Server.SURVIVAL);
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
        new SurvivalPrismSolarShopGUI().open(omp);
    }
}
