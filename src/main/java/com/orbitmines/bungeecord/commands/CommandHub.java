package com.orbitmines.bungeecord.commands;

import com.orbitmines.api.Server;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.cmd.Command;
import net.md_5.bungee.api.event.ChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class CommandHub extends Command {

    private String[] alias = { "/hub", "/lobby" };

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(BungeePlayer omp) {
        return null;
    }

    @Override
    public void dispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        omp.connect(Server.HUB, true);
    }
}
