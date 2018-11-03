package com.orbitmines.bungeecord.statistics.graphs.date;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Server;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.tables.TablePlayTime;
import com.orbitmines.api.database.tables.statistics.TableStatsPlayTime;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.bungeecord.runnables.BungeeRunnable;
import com.orbitmines.bungeecord.statistics.graphs.Graph;
import com.orbitmines.discordbot.DiscordBot;
import org.jfree.chart.plot.PlotOrientation;

import java.util.concurrent.TimeUnit;

public class PlayTimeTotalGraph extends DateGraphUpdater {

    public PlayTimeTotalGraph() {
        super(DiscordBot.ChannelType.stats_playtime_total, Graph.Type.LINE, BungeeRunnable.TimeUnit.MINUTE, 5);
    }

    @Override
    protected void insert() {
        /* Fix First Entry */
        if (Database.get().getCount(Table.STATS_PLAY_TIME) == 0) {
            Database.get().insert(Table.STATS_PLAY_TIME, System.currentTimeMillis() + "", "0", "0", "0", "0");
        } else {
            /* Insert */
            long HUB = Database.get().getLongSum(Table.PLAY_TIME, TablePlayTime.HUB);
            long SURVIVAL = Database.get().getLongSum(Table.PLAY_TIME, TablePlayTime.SURVIVAL);
            long KITPVP = Database.get().getLongSum(Table.PLAY_TIME, TablePlayTime.KITPVP);
            long total = HUB + SURVIVAL + KITPVP;

            Database.get().insert(Table.STATS_PLAY_TIME, System.currentTimeMillis() + "", total + "", HUB + "", SURVIVAL + "", KITPVP + "");
        }
    }

    @Override
    protected DateGraph newInstance(DateGraph.Type type) {
        return new Instance(this.channelType, this.type, type);
    }

    @Override
    protected Table getTable() {
        return Table.STATS_PLAY_TIME;
    }

    public class Instance extends DateGraph {

        public Instance(DiscordBot.ChannelType channelType, Graph.Type graphType, Type type) {
            super(channelType, graphType, type, "Total Time Played", "Hours", PlotOrientation.VERTICAL, Table.STATS_PLAY_TIME, TableStatsPlayTime.TIME, TableStatsPlayTime.TOTAL);

            add(
                new Instance(Server.HUB.toString(), Server.HUB.getColor(), TableStatsPlayTime.HUB) {
                    @Override
                    protected String getDescription() {
                        long count = getCount(last, first);
                        return NumberUtils.locale(count) + " " + (count == 1 ? "Hour" : "Hours");
                    }
                },
                new Instance(Server.SURVIVAL.toString(), Server.SURVIVAL.getColor(), TableStatsPlayTime.SURVIVAL) {
                    @Override
                    protected String getDescription() {
                        long count = getCount(last, first);
                        return NumberUtils.locale(count) + " " + (count == 1 ? "Hour" : "Hours");
                    }
                },
                new Instance(Server.KITPVP.toString(), Server.KITPVP.getColor(), TableStatsPlayTime.KITPVP) {
                    @Override
                    protected String getDescription() {
                        long count = getCount(last, first);
                        return NumberUtils.locale(count) + " " + (count == 1 ? "Hour" : "Hours");
                    }
                }
            );
        }

        @Override
        protected long getCount(long count, long prevCount) {
            return (int) ((count - prevCount) / TimeUnit.HOURS.toSeconds(1));
        }

        @Override
        protected Instance setupMain(Column mainCountColumn) {
            return new Instance(type.getThisStatus(), Color.SILVER, mainCountColumn) {
                @Override
                protected String getDescription() {
                    long count = getCount(last, first);
                    return NumberUtils.locale(count) + " " + (count == 1 ? "Hour" : "Hours");
                }
            };
        }

        @Override
        protected Instance setupLast(Column mainCountColumn) {
            return null;
        }
    }
}
