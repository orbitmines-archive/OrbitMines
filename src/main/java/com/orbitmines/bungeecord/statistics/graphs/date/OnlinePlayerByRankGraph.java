package com.orbitmines.bungeecord.statistics.graphs.date;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.tables.statistics.TableStatsOnline;
import com.orbitmines.bungeecord.runnables.BungeeRunnable;
import com.orbitmines.bungeecord.statistics.graphs.Graph;
import com.orbitmines.discordbot.DiscordBot;
import org.jfree.chart.plot.PlotOrientation;

public class OnlinePlayerByRankGraph extends DateGraphUpdater {

    public OnlinePlayerByRankGraph() {
        super(DiscordBot.ChannelType.stats_online_by_rank, Graph.Type.LINE, BungeeRunnable.TimeUnit.MINUTE, 5);
    }

    @Override
    protected void insert() { }

    @Override
    protected DateGraph newInstance(DateGraph.Type type) {
        return new Instance(this.channelType, this.type, type);
    }

    @Override
    protected Table getTable() {
        return Table.STATS_ONLINE;
    }

    public class Instance extends DateGraph {

        public Instance(DiscordBot.ChannelType channelType, Graph.Type graphType, Type type) {
            super(channelType, graphType, type, "Online Players (By Rank)", "Players", PlotOrientation.VERTICAL, Table.STATS_ONLINE, TableStatsOnline.TIME, TableStatsOnline.TOTAL);

            add(new Instance(StaffRank.OWNER.toString(), StaffRank.OWNER.getPrefixColor(), TableStatsOnline.OWNER) { @Override protected String getDescription() { return null; } });
            add(new Instance(StaffRank.ADMIN.toString(), StaffRank.ADMIN.getPrefixColor(), TableStatsOnline.ADMIN) { @Override protected String getDescription() { return null; } });
            add(new Instance(StaffRank.DEVELOPER.toString(), StaffRank.DEVELOPER.getPrefixColor(), TableStatsOnline.DEVELOPER) { @Override protected String getDescription() { return null; } });
            add(new Instance(StaffRank.MODERATOR.toString(), StaffRank.MODERATOR.getPrefixColor(), TableStatsOnline.MODERATOR) { @Override protected String getDescription() { return null; } });
            add(new Instance(StaffRank.BUILDER.toString(), StaffRank.BUILDER.getPrefixColor(), TableStatsOnline.BUILDER) { @Override protected String getDescription() { return null; } });
            add(new Instance(VipRank.EMERALD.toString(), VipRank.EMERALD.getPrefixColor(), TableStatsOnline.EMERALD) { @Override protected String getDescription() { return null; } });
            add(new Instance(VipRank.DIAMOND.toString(), VipRank.DIAMOND.getPrefixColor(), TableStatsOnline.DIAMOND) { @Override protected String getDescription() { return null; } });
            add(new Instance(VipRank.GOLD.toString(), VipRank.GOLD.getPrefixColor(), TableStatsOnline.GOLD) { @Override protected String getDescription() { return null; } });
            add(new Instance(VipRank.IRON.toString(), VipRank.IRON.getPrefixColor(), TableStatsOnline.IRON) { @Override protected String getDescription() { return null; } });
            add(new Instance("USER", VipRank.NONE.getPrefixColor(), TableStatsOnline.USER) { @Override protected String getDescription() { return null; } });
        }

        @Override
        protected Instance setupMain(Column mainCountColumn) {
            return new Instance(type.getThisStatus(), Color.BLACK, mainCountColumn) {
                @Override
                protected String getDescription() {
                    return null;
                }
            };
        }

        @Override
        protected Instance setupLast(Column mainCountColumn) {
            return null;
        }
    }
}
