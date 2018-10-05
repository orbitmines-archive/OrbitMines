package com.orbitmines.spigot.api.handlers.cmd;

import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.api.Server;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.discordbot.utils.DiscordSpigotUtils;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.chat.ComponentMessage;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public abstract class Command {

    private static List<Command> commands = new ArrayList<>();

    protected CommandLibrary library;

    public Command(CommandLibrary library) {
        this.library = library;

        commands.add(this);
    }

    public String[] getAlias() {
        return library.getAlias();
    }

    public Server getServer() {
        return library.getServer();
    }

    /* a[0] = '/<command>' */
    public abstract void dispatch(OMPlayer omp, String[] a);

    public void unregister() {
        commands.remove(this);
    }

    public ComponentMessage getHelpMessage(OMPlayer omp) {
        ComponentMessage cM = new ComponentMessage();
        cM.add(new Message("Help", Color.BLUE, ""));
        cM.add("§7" + omp.lang("Gebruik", "Use") + " ");
        cM.add(DiscordSpigotUtils.getCommandMention(omp, library, getAlias()[0]).setChatColor(Color.BLUE.getMd5()));
        return cM;
    }

    public static Command getCommand(String cmd) {
        for (Command command : commands) {
            for (String alias : command.getAlias()) {
                if (cmd.equalsIgnoreCase(alias))
                    return command;
            }
        }

        return null;
    }

    public static List<Command> getCommands() {
        return commands;
    }
}
