package com.orbitmines.spigot.api.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.servers.hub.gui.discordgroup.DiscordGroupGUI;

public class CommandDiscordServer extends Command {

    public CommandDiscordServer() {
        super(CommandLibrary.DISCORDSQUAD);
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        new DiscordGroupGUI().open(omp);
    }
}
