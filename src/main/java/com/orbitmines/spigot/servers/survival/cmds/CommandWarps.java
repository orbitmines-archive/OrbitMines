package com.orbitmines.spigot.servers.survival.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.gui.warp.WarpGUI;
import com.orbitmines.spigot.servers.survival.handlers.teleportable.Warp;

public class CommandWarps extends Command {

    private Survival survival;

    public CommandWarps(Survival survival) {
        super(CommandLibrary.SURVIVAL_WARP);

        this.survival = survival;
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        if (a.length == 1) {
            new WarpGUI(survival).open(omp);
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < a.length; i++) {
            if (i != 1)
                stringBuilder.append(" ");

            stringBuilder.append(a[i]);
        }
        String warpName = stringBuilder.toString();

        Warp warp = Warp.getWarp(warpName);
        if (warp == null) {
            omp.sendMessage("Warp", Color.RED, "Die Warp bestaat niet!", "That Warp doesn't exist.");
            return;
        }

        warp.teleport(omp);
    }
}
