package com.orbitmines.bungeecord.handlers.cmd;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import net.md_5.bungee.api.event.ChatEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class ConsoleCommand {

    private static List<ConsoleCommand> commands = new ArrayList<>();

    public ConsoleCommand() {
        commands.add(this);
    }

    public abstract String[] getAlias();

    /* a[0] = '/<command>' */
    public abstract void dispatch(ChatEvent event, String[] a);

    public void unregister() {
        commands.remove(this);
    }

    public static ConsoleCommand getCommand(String cmd) {
        for (ConsoleCommand command : commands) {
            for (String alias : command.getAlias()) {
                if (cmd.equalsIgnoreCase(alias))
                    return command;
            }
        }

        return null;
    }

    public static List<ConsoleCommand> getCommands() {
        return commands;
    }
}
