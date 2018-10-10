package com.orbitmines.bungeecord.handlers.cmd;

import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.chat.ComponentMessage;
import com.orbitmines.discordbot.utils.DiscordBungeeUtils;
import net.md_5.bungee.api.event.ChatEvent;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public abstract class Command {

    private static List<Command> commands = new ArrayList<>();

    protected final CommandLibrary library;

    public Command(CommandLibrary library) {
        commands.add(this);

        this.library = library;
    }

    public String[] getAlias() {
        return library.getAlias();
    }

    /* a[0] = '/<command>' */
    public abstract void dispatch(ChatEvent event, BungeePlayer omp, String[] a);

    public void unregister() {
        commands.remove(this);
    }

    public ComponentMessage getHelpMessage(BungeePlayer omp) {
        ComponentMessage cM = new ComponentMessage();
        cM.add(new Message("Help", Color.BLUE, ""));
        cM.add("§7" + omp.lang("Gebruik", "Use") + " ");
        cM.add(DiscordBungeeUtils.getCommandMention(omp, library, getAlias()[0]).setChatColor(Color.BLUE.getMd5()));
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
