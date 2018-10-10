package com.orbitmines.bungeecord.statistics.graphs.date;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Server;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.statistics.TableStatsTPS;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.bungeecord.runnables.BungeeRunnable;
import com.orbitmines.bungeecord.statistics.graphs.Graph;
import com.orbitmines.discordbot.DiscordBot;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class TPSGraph extends DateGraphUpdater {

    private final Server server;

    public TPSGraph(DiscordBot.ChannelType channelType, Server server) {
        super(channelType, Graph.Type.LINE, BungeeRunnable.TimeUnit.MINUTE, 5);

        this.server = server;
    }

    @Override
    protected void insert() {
        //
    }

    @Override
    protected DateGraph newInstance(DateGraph.Type type) {
        return new Instance(this.channelType, this.type, type);
    }

    @Override
    protected Table getTable() {
        return Table.STATS_TPS;
    }

    public class Instance extends DateGraph {

        public Instance(DiscordBot.ChannelType channelType, Graph.Type graphType, Type type) {
            super(channelType, graphType, type, server.getName() + " TPS", "Ticks/sec", PlotOrientation.VERTICAL, Table.STATS_TPS, TableStatsTPS.TIME, TableStatsTPS.TPS);

            add(
                new Instance(type.getName() + " Upper TPS", Color.LIME, null) {
                    @Override
                    protected String getDescription() {
                        Instance main = getMainInstance();

                        return NumberUtils.locale(toTPS(main.topCount)) + " TPS / " + DateUtils.FORMAT.format(main.top);
                    }

                    @Override
                    public void generate(XYDataset xyDataset) {
                        TimeSeriesCollection dataset = (TimeSeriesCollection) xyDataset;

                        Instance main = getMainInstance();

                        TimeSeries series = new TimeSeries(seriesName);

                        add(series, new Millisecond(new Date(main.getMinimum())), main.topCount);
                        add(series, new Millisecond(new Date(main.getMaximum())), main.topCount);

                        dataset.addSeries(series);
                    }

                    @Override
                    public void add(TimeSeries series, Millisecond date, long count) {
                        series.add(date, toTPS(count));
                    }

                    @Override
                    public List<Map<Column, String>> getEntries() {
                        return Database.get().getEntries(table, new Where(TableStatsTPS.SERVER, server.toString()), new Where(Where.Operator.GREATER_THAN_OR_EQUAL, timeColumn, getMinimum()), new Where(Where.Operator.LESSER_THAN_OR_EQUAL, timeColumn, getMaximum()));
                    }
                },
                new Instance(type.getName() + " Average", Color.YELLOW, null) {
                    @Override
                    protected String getDescription() {
                        Instance main = getMainInstance();

                        return NumberUtils.locale(toTPS(main.average)) + " TPS";
                    }

                    @Override
                    public void generate(XYDataset xyDataset) {
                        TimeSeriesCollection dataset = (TimeSeriesCollection) xyDataset;

                        Instance main = getMainInstance();

                        TimeSeries series = new TimeSeries(seriesName);

                        add(series, new Millisecond(new Date(main.getMinimum())), main.average);
                        add(series, new Millisecond(new Date(main.getMaximum())), main.average);

                        dataset.addSeries(series);
                    }

                    @Override
                    public void add(TimeSeries series, Millisecond date, long count) {
                        series.add(date, toTPS(count));
                    }

                    @Override
                    public List<Map<Column, String>> getEntries() {
                        return Database.get().getEntries(table, new Where(TableStatsTPS.SERVER, server.toString()), new Where(Where.Operator.GREATER_THAN_OR_EQUAL, timeColumn, getMinimum()), new Where(Where.Operator.LESSER_THAN_OR_EQUAL, timeColumn, getMaximum()));
                    }
                },
                new Instance(type.getName() + " Lower TPS", Color.MAROON, null) {
                    @Override
                    protected String getDescription() {
                        Instance main = getMainInstance();

                        return NumberUtils.locale(toTPS(main.bottomCount)) + " TPS / " + DateUtils.FORMAT.format(main.bottom);
                    }

                    @Override
                    public void generate(XYDataset xyDataset) {
                        TimeSeriesCollection dataset = (TimeSeriesCollection) xyDataset;

                        Instance main = getMainInstance();

                        TimeSeries series = new TimeSeries(seriesName);

                        add(series, new Millisecond(new Date(main.getMinimum())), main.bottomCount);
                        add(series, new Millisecond(new Date(main.getMaximum())), main.bottomCount);

                        dataset.addSeries(series);
                    }

                    @Override
                    public void add(TimeSeries series, Millisecond date, long count) {
                        series.add(date, toTPS(count));
                    }

                    @Override
                    public List<Map<Column, String>> getEntries() {
                        return Database.get().getEntries(table, new Where(TableStatsTPS.SERVER, server.toString()), new Where(Where.Operator.GREATER_THAN_OR_EQUAL, timeColumn, getMinimum()), new Where(Where.Operator.LESSER_THAN_OR_EQUAL, timeColumn, getMaximum()));
                    }
                }
            );
        }

        protected Instance setupMain(Column mainCountColumn) {
            return new Instance(type.getThisStatus(), server.getColor(), mainCountColumn) {
                @Override
                protected String getDescription() {
                    return null;
                }

                @Override
                public void add(TimeSeries series, Millisecond date, long count) {
                    series.add(date, toTPS(count));
                }

                @Override
                public List<Map<Column, String>> getEntries() {
                    return Database.get().getEntries(table, new Where(TableStatsTPS.SERVER, server.toString()), new Where(Where.Operator.GREATER_THAN_OR_EQUAL, timeColumn, getMinimum()), new Where(Where.Operator.LESSER_THAN_OR_EQUAL, timeColumn, getMaximum()));
                }
            };
        }

        @Override
        protected ValueAxis[] getRange() {
            ValueAxis[] range = super.getRange();

            NumberAxis axis = (NumberAxis) range[0];
            axis.setLowerBound(toTPS(getMainInstance().bottomCount));
            axis.setUpperBound(toTPS(getMainInstance().topCount));

            return range;
        }

        protected Instance setupLast(Column mainCountColumn) {
            return null;
        }

        private double toTPS(long count) {
            return ((double) count) / 10000;
        }
    }
}
