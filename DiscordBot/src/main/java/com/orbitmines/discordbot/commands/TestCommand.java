package com.orbitmines.discordbot.commands;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.handlers.Command;
import com.orbitmines.discordbot.utils.BotToken;
import com.orbitmines.discordbot.utils.SkinLibrary;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.UUID;

public class TestCommand extends Command {

    private String[] alias = { "testcmd" };

    public TestCommand() {
        super(BotToken.DEFAULT);
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
    public void dispatch(MessageReceivedEvent event, User user, MessageChannel channel, Message msg, String[] a) {
        channel.sendMessage(user.getAsMention() + " Testing Emote..").queue();

        Guild guild = DiscordBot.getInstance().getGuild();
        UUID uuid = UUID.fromString("01a5412b-275b-4f42-aea9-1bef163210b9");

        SkinLibrary.setupEmote(guild, uuid);

        channel.sendMessage(SkinLibrary.getEmote(guild, uuid).getAsMention() + " <---").queue();
    }
}
