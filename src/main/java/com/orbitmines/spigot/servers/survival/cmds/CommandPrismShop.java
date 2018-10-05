package com.orbitmines.spigot.servers.survival.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.servers.survival.gui.SurvivalPrismSolarShopGUI;

public class CommandPrismShop extends Command {

    public CommandPrismShop() {
        super(CommandLibrary.SURVIVAL_PRISMSHOP);
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        new SurvivalPrismSolarShopGUI().open(omp);
    }
}
