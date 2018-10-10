package com.orbitmines.bungeecord.statistics;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Server;
import com.orbitmines.bungeecord.statistics.graphs.date.*;
import com.orbitmines.discordbot.DiscordBot;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Statistics {

    public static final Color TRANSPARENT = new Color(0xFF, 0xFF, 0xFF, 0);
    public static final Color GRID_COLOR = new Color(85, 85, 85);
    public static final Color TEXT_COLOR = new Color(200, 200, 200);

    private final OnlinePlayerGraph onlinePlayerGraph;
    private final OnlinePlayerByRankGraph onlinePlayerByRankGraph;

    private final UniquePlayerGraph uniquePlayerGraph;

    private final VotesTotalGraph votesTotalGraph;
    private final VotesMonthlyGraph votesMonthlyGraph;

    private final PlayTimeTotalGraph playTimeTotalGraph;

    private final Map<Server, TPSGraph> tpsGraphs;

    public Statistics() {
        onlinePlayerGraph = new OnlinePlayerGraph();
        onlinePlayerByRankGraph = new OnlinePlayerByRankGraph();

        uniquePlayerGraph = new UniquePlayerGraph();

        votesTotalGraph = new VotesTotalGraph();
        votesMonthlyGraph = new VotesMonthlyGraph();

        playTimeTotalGraph = new PlayTimeTotalGraph();

        tpsGraphs = new HashMap<>();
        tpsGraphs.put(Server.HUB, new TPSGraph(DiscordBot.ChannelType.stats_tps_hub, Server.HUB));
        tpsGraphs.put(Server.SURVIVAL, new TPSGraph(DiscordBot.ChannelType.stats_tps_survival, Server.SURVIVAL));
    }

    public OnlinePlayerGraph getOnlinePlayerGraph() {
        return onlinePlayerGraph;
    }

    public UniquePlayerGraph getUniquePlayerGraph() {
        return uniquePlayerGraph;
    }

    public OnlinePlayerByRankGraph getOnlinePlayerByRankGraph() {
        return onlinePlayerByRankGraph;
    }

    public VotesTotalGraph getVotesTotalGraph() {
        return votesTotalGraph;
    }

    public VotesMonthlyGraph getVotesMonthlyGraph() {
        return votesMonthlyGraph;
    }

    public PlayTimeTotalGraph getPlayTimeTotalGraph() {
        return playTimeTotalGraph;
    }

    public Map<Server, TPSGraph> getTpsGraphs() {
        return tpsGraphs;
    }

    public void update() {
        onlinePlayerGraph.update();
        onlinePlayerByRankGraph.update();

        uniquePlayerGraph.update();

        votesTotalGraph.update();
        votesMonthlyGraph.update();

        playTimeTotalGraph.update();

        for (Server server : tpsGraphs.keySet()) {
            tpsGraphs.get(server).update();
        }
    }
}
