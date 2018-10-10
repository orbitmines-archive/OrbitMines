package com.orbitmines.bungeecord.statistics.graphs;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.bungeecord.runnables.BungeeRunnable;
import com.orbitmines.bungeecord.statistics.StatisticUpdater;
import com.orbitmines.discordbot.DiscordBot;

public abstract class GraphUpdater extends StatisticUpdater {

    protected final Graph.Type type;

    public GraphUpdater(DiscordBot.ChannelType channelType, BungeeRunnable.TimeUnit timeUnit, int count, Graph.Type type) {
        super(channelType, timeUnit, count);
        this.type = type;
    }
}
