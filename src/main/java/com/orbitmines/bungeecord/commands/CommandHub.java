package com.orbitmines.bungeecord.commands;

import com.orbitmines.api.Server;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.cmd.Command;
import net.md_5.bungee.api.event.ChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class CommandHub extends Command {

    public CommandHub() {
        super(CommandLibrary.HUB);
    }

    @Override
    public void dispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        omp.connect(Server.HUB, true);
    }
}
