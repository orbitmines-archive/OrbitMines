package com.orbitmines.spigot.servers.uhsurvival2.commands;

import com.orbitmines.api.StaffRank;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.StaffCommand;

public class DungeonCommand extends StaffCommand {

    //TODO!

    public DungeonCommand() {
        super(StaffRank.MODERATOR);
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {

    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return null;
    }
}
