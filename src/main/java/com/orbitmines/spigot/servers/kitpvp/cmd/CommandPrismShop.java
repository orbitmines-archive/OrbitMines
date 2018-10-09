package com.orbitmines.spigot.servers.kitpvp.cmd;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.servers.kitpvp.handlers.gui.KitPvPPrismSolarShopGUI;

public class CommandPrismShop extends Command {

    public CommandPrismShop() {
        super(CommandLibrary.KITPVP_PRISMSHOP);
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        new KitPvPPrismSolarShopGUI().open(omp);
    }
}
