package com.orbitmines.spigot.api.cmds.moderator;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.StaffCommand;

public class CommandOpMode extends StaffCommand {

    public CommandOpMode() {
        super(CommandLibrary.OPMODE);
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        omp.setOpMode(!omp.isOpMode());

        if (omp.isOpMode())
            omp.sendMessage("Op", Color.LIME, "§7Je §4§lOP Mode§7 staat nu aan.", "§7Your §4§lOP Mode§7 has been enabled.");
        else
            omp.sendMessage("Op", Color.LIME, "§7Je §4§lOP Mode§7 staat nu uit.", "§7Your §4§lOP Mode§7 has been disabled.");
    }
}
