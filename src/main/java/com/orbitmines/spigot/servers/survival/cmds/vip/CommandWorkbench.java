package com.orbitmines.spigot.servers.survival.cmds.vip;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.VipCommand;

public class CommandWorkbench extends VipCommand {

    public CommandWorkbench() {
        super(CommandLibrary.SURVIVAL_WORKBENCH);
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        omp.getPlayer().openWorkbench(null, true);
    }
}
