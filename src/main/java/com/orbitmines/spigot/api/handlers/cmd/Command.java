package com.orbitmines.spigot.api.handlers.cmd;

import com.orbitmines.spigot.api.handlers.OMPlayer;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public abstract class Command {

    private static List<Command> commands = new ArrayList<>();

    public Command() {
        commands.add(this);
    }

    public abstract String[] getAlias();

    public abstract String getHelp(OMPlayer omp);

    /* a[0] = '/<command>' */
    public abstract void dispatch(OMPlayer omp, String[] a);

    public void unregister() {
        commands.remove(this);
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
