package com.orbitmines.discordbot.handlers;

import com.orbitmines.discordbot.utils.BotToken;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

public abstract class Command {

    public final static String PREFIX = "!";

    private static List<Command> commands = new ArrayList<>();

    private final BotToken token;

    public Command(BotToken token) {
        commands.add(this);

        this.token = token;
    }

    public abstract String[] getAlias();

    public abstract String getHelp();

    /* a[0] = '!<command>' */
    public abstract void dispatch(MessageReceivedEvent event, User user, MessageChannel channel, Message msg, String[] a);

    public BotToken getToken() {
        return token;
    }

    public void unregister() {
        commands.remove(this);
    }

    public static Command getCommand(BotToken token, String cmd) {
        for (Command command : commands) {
            if (command.getToken() != token)
                continue;

            for (String alias : command.getAlias()) {
                if (cmd.equalsIgnoreCase(PREFIX + alias))
                    return command;
            }
        }

        return null;
    }

    public static List<Command> getCommands() {
        return commands;
    }
}
