package com.orbitmines.discordbot.commands;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Server;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.handlers.Command;
import com.orbitmines.discordbot.utils.BotToken;
import com.orbitmines.discordbot.utils.DiscordSpigotUtils;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Collection;

public class CommandListServer extends Command {

    private String[] alias = { "list" };

    private DiscordBot discord;

    public CommandListServer(DiscordBot discord, BotToken token) {
        super(token, "Display the current players online.");
        this.discord = discord;
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
        return false;
    }

    @Override
    public void dispatch(MessageReceivedEvent event, User user, MessageChannel ch, Message msg, String[] a) {
        TextChannel channel = event.getTextChannel();

        BotToken token = BotToken.from(channel);

        if (token == this.token)
            send(channel, token.getServer());
    }

    private void send(TextChannel channel, Server server) {
        Collection<OMPlayer> list = OMPlayer.getPlayers();
        int players = list.size();

        StringBuilder stringBuilder = new StringBuilder();

        boolean first = true;
        for (OMPlayer omp : list) {
            if (first)
                stringBuilder.append(" » ");
            else
                stringBuilder.append(", ");

            stringBuilder.append(DiscordSpigotUtils.getDisplay(discord, token, omp));

            if (first)
                first = false;
        }

        channel.sendMessage("There " + (players == 1 ? "is" : "are") + " currently **" + players + " " + (players == 1 ? "Player" : "Players") + "** online in the" + discord.getEmote(token, DiscordBot.CustomEmote.from(server)).getAsMention() + "**" + server.getName() + "** server" + stringBuilder).queue();
    }
}
