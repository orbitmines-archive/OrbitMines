package com.orbitmines.spigot.api.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.servers.hub.gui.SettingsGUI;

public class CommandSettings extends Command {

    public CommandSettings() {
        super(CommandLibrary.SETTINGS);
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        new SettingsGUI().open(omp);
    }
}
