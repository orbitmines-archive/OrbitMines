package com.orbitmines.spigot.servers.survival.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.gui.warp.WarpSlotsGUI;

public class CommandMyWarps extends Command {

    private Survival survival;

    public CommandMyWarps(Survival survival) {
        super(CommandLibrary.SURVIVAL_MYWARPS);

        this.survival = survival;
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        new WarpSlotsGUI(survival).open(omp);
    }
}
