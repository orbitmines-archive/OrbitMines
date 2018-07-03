package com.orbitmines.bungeecord.handlers.cmd;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;

public abstract class ConsoleCommand extends Command {

    private static List<ConsoleCommand> commands = new ArrayList<>();

    public ConsoleCommand(String name) {
        super(name);

        commands.add(this);
    }

    public void unregister() {
        commands.remove(this);
    }

    public static ConsoleCommand getCommand(String cmd) {
        for (ConsoleCommand command : commands) {
            if (cmd.equalsIgnoreCase(command.getName()))
                return command;
        }

        return null;
    }

    public static List<ConsoleCommand> getCommands() {
        return commands;
    }
}
