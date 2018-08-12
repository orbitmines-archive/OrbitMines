package com.orbitmines.discordbot.commands;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.handlers.Command;
import com.orbitmines.discordbot.utils.BotToken;
import com.orbitmines.discordbot.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandHelp extends Command {

    private String[] alias = { "help" };

    private final DiscordBot bot;

    public CommandHelp(DiscordBot bot) {
        super(BotToken.DEFAULT, "Display all commands.");

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
        builder.setAuthor("OrbitMines Discord Commands");
        builder.setColor(ColorUtils.from(Color.BLUE));

        for (Command command : getCommands()) {
            if (command instanceof CommandHelp || command instanceof CommandListServer)
                continue;

            builder.addField(command.getHelpMessage(), command.getDescription(), false);
        }

        builder.setThumbnail(DiscordBot.Images.ORBITMINES_ICON.getUrl());

        channel.sendMessage(builder.build()).queue();
    }
}
