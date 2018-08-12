package com.orbitmines.discordbot.commands;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Server;
import com.orbitmines.api.StaffRank;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.handlers.Command;
import com.orbitmines.discordbot.handlers.DiscordGroup;
import com.orbitmines.discordbot.utils.BotToken;
import com.orbitmines.discordbot.utils.DiscordBungeeUtils;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandList extends Command {

    private String[] alias = { "list" };

    private DiscordBot discord;

    public CommandList(DiscordBot discord) {
        super(BotToken.DEFAULT, "Display the current players online.");
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
        return true;
    }

    @Override
    public void dispatch(MessageReceivedEvent event, User user, MessageChannel ch, Message msg, String[] a) {
        TextChannel channel = event.getTextChannel();

        BotToken token = BotToken.from(channel);

        if (token != null)
            /* Handled in CommandListServer */
            return;

        DiscordGroup group = DiscordGroup.getGroup(channel);

        if (group != null) {
            send(channel, group.getName(), group.getPlayers());
            return;
        }

        if (channel.getName().equals(DiscordBot.ChannelType.staff.toString())) {
            List<BungeePlayer> staff = new ArrayList<>();

            for (BungeePlayer omp : BungeePlayer.getPlayers()) {
                if (omp.isEligible(StaffRank.MODERATOR))
                    staff.add(omp);
            }

            send(channel, "staff", staff);
            return;
        }

        channel.sendMessage(user.getAsMention() + " You can't use **!list** here. Try " + discord.getChannelFor(discord.getGuild(this.token), BotToken.SURVIVAL).getAsMention() + ".").queue();
    }

    private void send(TextChannel channel, String name, List<BungeePlayer> players) {
        Map<Server, List<BungeePlayer>> map = new HashMap<>();

        /* sort */
        for (BungeePlayer omp : players) {
            Server server = omp.getServer();

            if (server == null)
                continue;

            if (!map.containsKey(server))
                map.put(server, new ArrayList<>());

            map.get(server).add(omp);
        }

        channel.sendMessage("There " + (players.size() == 1 ? "is" : "are") + " currently **" + players.size() + " " + (players.size() == 1 ? "Player " : "Players") + "** online in **" + name + "**").queue();

        for (Server server : map.keySet()) {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(discord.getEmote(token, DiscordBot.CustomEmote.from(server)).getAsMention());
            stringBuilder.append("**").append(server.getName()).append("(").append(map.get(server).size()).append(")").append("**");

            for (int i = 0; i < map.get(server).size(); i++) {
                BungeePlayer omp = map.get(server).get(i);

                if (i == 0)
                    stringBuilder.append(" » ");
                else
                    stringBuilder.append(", ");

                stringBuilder.append(DiscordBungeeUtils.getDisplay(discord, token, omp));
            }

            channel.sendMessage(stringBuilder.toString()).queue();
        }
    }
}
