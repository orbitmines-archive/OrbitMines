package com.orbitmines.discordbot.commands;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.ServerList;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.handlers.Command;
import com.orbitmines.discordbot.utils.BotToken;
import com.orbitmines.discordbot.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandVote extends Command {

    private String[] alias = { "vote" };

    private final DiscordBot bot;

    public CommandVote(DiscordBot bot) {
        super(BotToken.DEFAULT, "Display all vote links.");

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
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("OrbitMines Vote Links");
        builder.setColor(ColorUtils.from(Color.BLUE));

        int i = 1;
        for (ServerList serverList : ServerList.values()) {
            builder.addField(i + ". " + serverList.getDisplayName(), serverList.getUrl(), false);
            i++;
        }

        builder.setThumbnail(DiscordBot.Images.PRISMARINE_SHARD.getUrl());

        channel.sendMessage(builder.build()).queue();
    }
}
