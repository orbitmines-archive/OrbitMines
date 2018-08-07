package com.orbitmines.spigot.api.cmds.moderator;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.StaffRank;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.StaffCommand;

public class CommandOpMode extends StaffCommand {

    private String[] alias = { "/opmode" };

    public CommandOpMode() {
        super(StaffRank.MODERATOR);
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
        omp.setOpMode(!omp.isOpMode());

        if (omp.isOpMode())
            omp.sendMessage("Op", Color.LIME, "§7Je §4§lOP Mode§7 staat nu aan.", "§7Your §4§lOP Mode§7 has been enabled.");
        else
            omp.sendMessage("Op", Color.LIME, "§7Je §4§lOP Mode§7 staat nu uit.", "§7Your §4§lOP Mode§7 has been disabled.");
    }
}
