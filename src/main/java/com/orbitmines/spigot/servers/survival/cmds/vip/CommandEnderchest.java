package com.orbitmines.spigot.servers.survival.cmds.vip;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Server;
import com.orbitmines.api.VipRank;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.VipCommand;

public class CommandEnderchest extends VipCommand {

    private String[] alias = { "/enderchest" };

    public CommandEnderchest() {
        super(Server.SURVIVAL, VipRank.DIAMOND);
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
        omp.getPlayer().openInventory(omp.getPlayer().getEnderChest());
    }
}
