package com.orbitmines.discordbot.commands;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.handlers.Command;
import com.orbitmines.discordbot.utils.BotToken;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandIP extends Command {

    private String[] alias = { "ip" };

    private final DiscordBot bot;

    public CommandIP(DiscordBot bot) {
        super(BotToken.DEFAULT, "Hub.OrbitMines.com");

        this.bot = bot;
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public boolean isBungeeCommand() {
        return true;
    }

    @Override
    public void dispatch(MessageReceivedEvent event, User user, MessageChannel channel, Message msg, String[] a) {
        channel.sendMessage(user.getAsMention() + " **Hub.OrbitMines.com**").queue();
    }
}
