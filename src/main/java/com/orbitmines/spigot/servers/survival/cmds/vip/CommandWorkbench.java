package com.orbitmines.spigot.servers.survival.cmds.vip;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Server;
import com.orbitmines.api.VipRank;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.VipCommand;

public class CommandWorkbench extends VipCommand {

    private String[] alias = { "/workbench", "/wb" };

    public CommandWorkbench() {
        super(Server.SURVIVAL, VipRank.GOLD);
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
    public void onDispatch(OMPlayer omp, String[] a) {
        omp.getPlayer().openWorkbench(null, true);
    }
}
