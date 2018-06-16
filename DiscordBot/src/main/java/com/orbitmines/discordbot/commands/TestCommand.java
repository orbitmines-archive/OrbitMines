package com.orbitmines.discordbot.commands;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.discordbot.handlers.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class TestCommand extends Command {

    private String[] alias = { "testcmd" };

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
        channel.sendMessage(user.getAsMention() + " Testing Embedded Message (Fields, Thumbnail FullSkin)..").queue();

        EmbedBuilder builder = new EmbedBuilder();
        //new MessageEmbed("https://www.orbitmines.com", "Test Title", "Test Description", EmbedType.IMAGE, OffsetDateTime.now(), 1, thumbnail, provider, authorInfo, videoInfo, footer, imageInfo, fields)

//        builder.setTitle("Title", "https://images-ext-2.discordapp.net/external/RyBiRwUtq8VqdDb6Sm77J303UEziO55Ujqu8FaRCfQ4/https/i.imgur.com/JsgxK3Y.png");
//        builder.setDescription("Description");
//        builder.setAuthor("Author", "https://images-ext-2.discordapp.net/external/RyBiRwUtq8VqdDb6Sm77J303UEziO55Ujqu8FaRCfQ4/https/i.imgur.com/JsgxK3Y.png", "https://images-ext-2.discordapp.net/external/RyBiRwUtq8VqdDb6Sm77J303UEziO55Ujqu8FaRCfQ4/https/i.imgur.com/JsgxK3Y.png");
//        builder.setColor(Color.RED);
//        builder.setFooter("Footer", "https://images-ext-2.discordapp.net/external/RyBiRwUtq8VqdDb6Sm77J303UEziO55Ujqu8FaRCfQ4/https/i.imgur.com/JsgxK3Y.png");
//        builder.setThumbnail("https://images-ext-2.discordapp.net/external/RyBiRwUtq8VqdDb6Sm77J303UEziO55Ujqu8FaRCfQ4/https/i.imgur.com/JsgxK3Y.png");
//        builder.setImage("https://images-ext-2.discordapp.net/external/RyBiRwUtq8VqdDb6Sm77J303UEziO55Ujqu8FaRCfQ4/https/i.imgur.com/JsgxK3Y.png");

        builder.setTitle("Title");
        builder.setDescription("Description");
        builder.setColor(Color.BLUE);
//        builder.setFooter("Footer", "");
        builder.setAuthor("Author");

        builder.addField("Field", "Value", false);

        builder.setImage("https://crafatar.com/renders/head/01a5412b-275b-4f42-aea9-1bef163210b9");

        channel.sendMessage(builder.build()).queue();
    }
}
