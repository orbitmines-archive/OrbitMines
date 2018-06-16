package com.orbitmines.bungeecord.commands;

import com.orbitmines.api.Color;
import com.orbitmines.api.Server;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.cmd.Command;
import net.md_5.bungee.api.event.ChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class CommandServer extends Command {

    private String[] alias = { "/server" };

    private OrbitMinesBungee bungee;

    public CommandServer(OrbitMinesBungee bungee) {
        this.bungee = bungee;
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(BungeePlayer omp) {
        return "(server)";
    }

    @Override
    public void dispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        if (a.length == 2) {
            Server server = bungee.getServer(a[1]);

            if (server != null)
                omp.connect(server, true);
            else
                omp.sendMessage("Server", Color.RED, "§7De server '§c" + a[1] + "§7' bestaat niet.", "§7Server '§c" + a[1] + "§7' does not exist.");
        } else {
            omp.sendMessage("Server", Color.BLUE, "§7Je bent momenteel verbonden met " + omp.getServer().getDisplayName() + "§7.", "§7You are currently connected to " + omp.getServer().getDisplayName() + "§7.");
        }
    }
}
