package com.orbitmines.bungeecord.statistics.graphs.date;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.tables.TableVotes;
import com.orbitmines.api.database.tables.statistics.TableStatsVotes;
import com.orbitmines.bungeecord.runnables.BungeeRunnable;
import com.orbitmines.bungeecord.statistics.graphs.Graph;
import com.orbitmines.discordbot.DiscordBot;
import org.jfree.chart.plot.PlotOrientation;

public class VotesTotalGraph extends DateGraphUpdater {

    public VotesTotalGraph() {
        super(DiscordBot.ChannelType.stats_votes_total, Graph.Type.STEP, BungeeRunnable.TimeUnit.MINUTE, 5);
    }

    @Override
    protected void insert() {
        /* Fix First Entry */
        if (Database.get().getCount(Table.STATS_VOTES) == 0) {
            Database.get().insert(Table.STATS_VOTES, System.currentTimeMillis() + "", "0", "0");
        } else {
            /* Insert */
            Database.get().insert(Table.STATS_VOTES, System.currentTimeMillis() + "", Database.get().getLongSum(Table.VOTES, TableVotes.TOTAL_VOTES) + "", Database.get().getLongSum(Table.VOTES, TableVotes.VOTES) + "");
        }
    }

    @Override
    protected DateGraph newInstance(DateGraph.Type type) {
        return new Instance(this.channelType, this.type, type);
    }

    @Override
    protected Table getTable() {
        return Table.STATS_VOTES;
    }

    public class Instance extends DateGraph {

        public Instance(DiscordBot.ChannelType channelType, Graph.Type graphType, Type type) {
            super(channelType, graphType, type, "Total Votes", "Votes", PlotOrientation.VERTICAL, Table.STATS_VOTES, TableStatsVotes.TIME, TableStatsVotes.TOTAL);
        }

        @Override
        protected int getCount(int count, int prevCount) {
            return count - getMainInstance().first;
        }

        @Override
        protected Instance setupMain(Column mainCountColumn) {
            return new Instance(type.getThisStatus(), Color.BLUE, mainCountColumn) {
                @Override
                protected String getDescription() {
                    int count = getCount(last, 0);
                    return count + " " + (count == 1 ? "Vote" : "Votes");
                }
            };
        }

        @Override
        protected Instance setupLast(Column mainCountColumn) {
            /* Add 'Last' Instance */
            if (type != Type.LIFE_TIME)
                return new Instance(type.getLastStatus(), Color.TEAL, mainCountColumn) {
                    @Override
                    protected String getDescription() {
                        int count = getCount(last, 0);
                        return count + " " + (count == 1 ? "Vote" : "Votes");
                    }

                    @Override
                    public long getMinimum() {
                        return minimum.getTime() - type.getMillis();
                    }

                    @Override
                    public long getMaximum() {
                        return minimum.getTime();
                    }
                };

            return null;
        }
    }
}
