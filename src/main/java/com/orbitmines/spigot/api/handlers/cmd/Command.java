package com.orbitmines.spigot.api.handlers.cmd;

import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.api.Server;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.chat.ComponentMessage;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public abstract class Command {

    private static List<Command> commands = new ArrayList<>();

    private Server server;

    public Command(Server server) {
        this.server = server;

        commands.add(this);
    }

    public abstract String[] getAlias();

    public abstract String getHelp(OMPlayer omp);

    public Server getServer() {
        return server;
    }

    /* a[0] = '/<command>' */
    public abstract void dispatch(OMPlayer omp, String[] a);

    public void unregister() {
        commands.remove(this);
    }

    public ComponentMessage getHelpMessage(OMPlayer omp) {
        ComponentMessage cM = new ComponentMessage();
        cM.add(new Message("Help", Color.BLUE, ""));
        return getHelpMessage(omp, cM);
    }

    public ComponentMessage getHelpMessage(OMPlayer omp, ComponentMessage cM) {
        String firstCmd = getAlias()[0];
        String help = getHelp(omp);

        if (help == null)
            help = "";
        else
            help = " §7" + help;

        StringBuilder aliases = new StringBuilder();
        if (getAlias().length > 1) {
            aliases.append("\n\n§7Alias: ");

            for (int i = 1; i < getAlias().length; i++) {
                if (i != 1)
                    aliases.append("§7, ");

                aliases.append("§9").append(getAlias()[i]);
            }
        }

        cM.add(new Message("§9" + firstCmd + help), ClickEvent.Action.SUGGEST_COMMAND, new Message(firstCmd + " "), HoverEvent.Action.SHOW_TEXT, new Message("§9" + firstCmd + help + aliases.toString()));

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
