package com.orbitmines.bungeecord.statistics;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.runnables.BungeeRunnable;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.utils.BotToken;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.TextChannel;

public abstract class StatisticUpdater {

    protected final DiscordBot.ChannelType channelType;
    private final TextChannel channel;

    public StatisticUpdater(DiscordBot.ChannelType channelType, BungeeRunnable.TimeUnit timeUnit, int count) {
       this.channelType = channelType;
        channel = OrbitMinesBungee.getBungee().getDiscord().getChannel(BotToken.DEFAULT, channelType);

        new BungeeRunnable(timeUnit, count) {
            @Override
            public void run() {
                MessageHistory history = new MessageHistory(channel);

                history.retrievePast(100).queue((retrieved) -> {
                    if (retrieved.size() == 0) {
                        update(channel);
                        return;
                    }

                    channel.deleteMessages(retrieved).queue((channel1) -> update(channel));
                });
            }
        };
    }

    protected abstract void update(TextChannel channel);

    public void update() {
        update(channel);
    }
}
