package com.orbitmines.discordbot.commands;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.tables.TableVotes;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.bungeecord.runnables.BungeeRunnable;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.handlers.Command;
import com.orbitmines.discordbot.utils.BotToken;
import com.orbitmines.discordbot.utils.DiscordUtils;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CommandTopVoters extends Command {

    private String[] alias = { "topvoters" };

    private final DiscordBot discord;

    protected List<Map<Column, String>> ordered;
    protected int size;

    public CommandTopVoters(DiscordBot discord) {
        super(BotToken.DEFAULT, "Display the Top 5 Voters of " + DateUtils.getMonth() +".");

        this.discord = discord;

        this.ordered = new ArrayList<>();
        this.size = 5;

        if (!DiscordBot.getInstance().isBungee())
            return;

        new BungeeRunnable(BungeeRunnable.TimeUnit.MINUTE, 10) {
            @Override
            public void run() {
                ordered.clear();
                /* Update top {size} players */
                List<Map<Column, String>> entries = Database.get().getEntries(Table.VOTES, new Column[] { TableVotes.UUID, TableVotes.VOTES });

                ordered.addAll(entries);
                ordered.sort((m1, m2) -> Integer.parseInt(m2.get(TableVotes.VOTES)) - Integer.parseInt(m1.get(TableVotes.VOTES)));

                if (ordered.size() > size)
                    ordered = ordered.subList(0, size);
            }
        };
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
        StringBuilder message = new StringBuilder(user.getAsMention() + " **TOP VOTERS OF " + DateUtils.getMonth().toUpperCase() + " " + DateUtils.getYear() + "**");

        for (int i = 0; i < size; i++) {
            if (ordered.size() < i + 1)
                continue;

            message.append("\n");

            Map<Column, String> entry = ordered.get(i);

            UUID uuid = UUID.fromString(entry.get(TableVotes.UUID));
            int count = Integer.parseInt(entry.get(TableVotes.VOTES));

            DiscordBot.CustomRole customRole;
            switch (i) {
                case 0:
                    customRole = DiscordBot.CustomRole.TOP_VOTER_1;
                    break;
                case 1:
                    customRole = DiscordBot.CustomRole.TOP_VOTER_2;
                    break;
                case 2:
                    customRole = DiscordBot.CustomRole.TOP_VOTER_3;
                    break;
                case 3:
                    customRole = DiscordBot.CustomRole.TOP_VOTER_4;
                    break;
                case 4:
                    customRole = DiscordBot.CustomRole.TOP_VOTER_5;
                    break;
                default:
                    customRole = DiscordBot.CustomRole.TOP_VOTER_5;
                    break;
            }

            message.append(discord.getRole(token, customRole).getAsMention()).append(" ").append(DiscordUtils.getDisplay(discord, token, uuid)).append(" with **").append(count).append(" ").append(count == 1 ? "Vote" : "Votes").append("**");
        }

        channel.sendMessage(message.toString()).queue();
    }
}
