package com.orbitmines.discordbot.handlers;

import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.utils.BotToken;
import com.orbitmines.spigot.api.cmds.discord.CommandList;
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

    protected final BotToken token;
    protected final String description;

    public Command(BotToken token, String description) {
        commands.add(this);

        this.token = token;
        this.description = description;
    }

    public abstract String[] getAlias();

    public abstract String getHelp();

    /* a[0] = '!<command>' */
    public abstract void dispatch(MessageReceivedEvent event, User user, MessageChannel channel, Message msg, String[] a);

    public BotToken getToken() {
        return token;
    }

    public String getDescription() {
        return description;
    }

    public String getHelpMessage() {
        String firstCmd = getAlias()[0];
        String help = getHelp();

        if (help == null)
            help = "";
        else
            help = " " + help;

        return PREFIX + firstCmd + help;
    }

    public void unregister() {
        commands.remove(this);
    }

    public static Command getCommand(BotToken token, String cmd) {
        for (Command command : commands) {
            if (command.getToken() != token || token == BotToken.DEFAULT && !DiscordBot.getInstance().isBungee() && !(command instanceof CommandList))
                continue;

            for (String alias : command.getAlias()) {
                if (cmd.equalsIgnoreCase(PREFIX + alias))
                    return command;
            }
        }

        return null;
    }

    public static Command getCommandFromName(String cmd) {
        for (Command command : commands) {
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
