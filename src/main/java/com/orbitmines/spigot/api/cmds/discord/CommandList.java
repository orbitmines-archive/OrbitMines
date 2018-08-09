package com.orbitmines.spigot.api.cmds.discord;

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
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandList extends Command {

    private String[] alias = { "list" };

    private DiscordBot discord;

    public CommandList(DiscordBot discord, BotToken token) {
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
    public void dispatch(MessageReceivedEvent event, User user, MessageChannel channel, Message msg, String[] a) {
        if (!channel.getName().equals(discord.getChannelFor(token).getName()))
            return;

        int players = OMPlayer.getPlayers().size();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < players; i++) {
            OMPlayer omp = OMPlayer.getPlayers().get(i);

            if (i == 0)
                stringBuilder.append(" » ");
            else
                stringBuilder.append(", ");

            stringBuilder.append(DiscordSpigotUtils.getDisplay(discord, token, omp));
        }

        Server server = token.getServer();

        channel.sendMessage("There are currently **" + players + " " + (players == 1 ? "Player" : "Players") + "** online in " + discord.getEmote(token, DiscordBot.CustomEmote.from(server)) + "**" + server.getName() + "**" + stringBuilder).queue();
    }
}
