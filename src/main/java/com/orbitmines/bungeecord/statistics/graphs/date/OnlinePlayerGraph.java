package com.orbitmines.bungeecord.statistics.graphs.date;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.tables.statistics.TableStatsOnline;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.runnables.BungeeRunnable;
import com.orbitmines.bungeecord.statistics.graphs.Graph;
import com.orbitmines.discordbot.DiscordBot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnlinePlayerGraph extends DateGraphUpdater {

    public OnlinePlayerGraph() {
        super(DiscordBot.ChannelType.stats_online, Graph.Type.LINE, BungeeRunnable.TimeUnit.MINUTE, 5);
    }

    @Override
    protected void insert() {
        List<BungeePlayer> players = BungeePlayer.getPlayers();

        Map<StaffRank, Integer> staffCount = new HashMap<>();
        Map<VipRank, Integer> vipCount = new HashMap<>();

        for (BungeePlayer player : players) {
            if (player.getStaffRank() != StaffRank.NONE) {
                if (!staffCount.containsKey(player.getStaffRank()))
                    staffCount.put(player.getStaffRank(), 1);
                else
                    staffCount.put(player.getStaffRank(), staffCount.get(player.getStaffRank()) + 1);
            } else {
                if (!vipCount.containsKey(player.getVipRank()))
                    vipCount.put(player.getVipRank(), 1);
                else
                    vipCount.put(player.getVipRank(), vipCount.get(player.getVipRank()) + 1);
            }
        }

        /* Insert */
        Database.get().insert(Table.STATS_ONLINE, System.currentTimeMillis() + "",
                players.size() + "",
                staffCount.getOrDefault(StaffRank.OWNER, 0) + "",
                staffCount.getOrDefault(StaffRank.ADMIN, 0) + "",
                staffCount.getOrDefault(StaffRank.DEVELOPER, 0) + "",
                staffCount.getOrDefault(StaffRank.MODERATOR, 0) + "",
                staffCount.getOrDefault(StaffRank.BUILDER, 0) + "",
                vipCount.getOrDefault(VipRank.EMERALD, 0) + "",
                vipCount.getOrDefault(VipRank.DIAMOND, 0) + "",
                vipCount.getOrDefault(VipRank.GOLD, 0) + "",
                vipCount.getOrDefault(VipRank.IRON, 0) + "",
                vipCount.getOrDefault(VipRank.NONE, 0) + ""
        );
    }

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
            super(channelType, graphType, type, "Online Players", "Players", PlotOrientation.VERTICAL, Table.STATS_ONLINE, TableStatsOnline.TIME, TableStatsOnline.TOTAL);

            add(
                new DateGraph.Instance(type.getName() + " Record", Color.LIME, null) {
                    @Override
                    protected String getDescription() {
                        DateGraph.Instance main = getMainInstance();

                        return NumberUtils.locale(main.topCount) + " " + (main.topCount == 1 ? "Player" : "Players") + " / " + DateUtils.FORMAT.format(main.top);
                    }

                    @Override
                    public void generate(XYDataset xyDataset) {
                        TimeSeriesCollection dataset = (TimeSeriesCollection) xyDataset;

                        DateGraph.Instance main = getMainInstance();

                        TimeSeries series = new TimeSeries(seriesName);

                        series.add(new Millisecond(new Date(main.getMinimum())), main.topCount);
                        series.add(new Millisecond(new Date(main.getMaximum())), main.topCount);

                        dataset.addSeries(series);
                    }
                },
                new DateGraph.Instance(type.getName() + " Average", Color.YELLOW, null) {
                    @Override
                    protected String getDescription() {
                        DateGraph.Instance main = getMainInstance();

                        return NumberUtils.locale(main.average) + " " + (main.average == 1 ? "Player" : "Players");
                    }

                    @Override
                    public void generate(XYDataset xyDataset) {
                        TimeSeriesCollection dataset = (TimeSeriesCollection) xyDataset;

                        DateGraph.Instance main = getMainInstance();

                        TimeSeries series = new TimeSeries(seriesName);

                        series.add(new Millisecond(new Date(main.getMinimum())), main.average);
                        series.add(new Millisecond(new Date(main.getMaximum())), main.average);

                        dataset.addSeries(series);
                    }
                }
            );
        }
    }
}
