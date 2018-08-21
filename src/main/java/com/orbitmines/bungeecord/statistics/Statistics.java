package com.orbitmines.bungeecord.statistics;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.bungeecord.statistics.graphs.date.*;

import java.awt.*;

public class Statistics {

    public static final Color TRANSPARENT = new Color(0xFF, 0xFF, 0xFF, 0);
    public static final Color GRID_COLOR = new Color(85, 85, 85);
    public static final Color TEXT_COLOR = new Color(200, 200, 200);

    private final OnlinePlayerGraph onlinePlayerGraph;
    private final OnlinePlayerByRankGraph onlinePlayerByRankGraph;

    private final UniquePlayerGraph uniquePlayerGraph;

    private final VotesTotalGraph votesTotalGraph;
    private final VotesMonthlyGraph votesMonthlyGraph;

    public Statistics() {
        onlinePlayerGraph = new OnlinePlayerGraph();
        onlinePlayerByRankGraph = new OnlinePlayerByRankGraph();

        uniquePlayerGraph = new UniquePlayerGraph();

        votesTotalGraph = new VotesTotalGraph();
        votesMonthlyGraph = new VotesMonthlyGraph();
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

    public void update() {
        onlinePlayerGraph.update();
        onlinePlayerByRankGraph.update();

        uniquePlayerGraph.update();

        votesTotalGraph.update();
        votesMonthlyGraph.update();
    }
}
