package com.orbitmines.bungeecord.statistics.graphs.date;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.bungeecord.statistics.graphs.Graph;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.utils.BotToken;
import net.dv8tion.jda.core.entities.TextChannel;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.RangeType;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public abstract class DateGraph extends Graph {

    protected final Type type;

    protected final Table table;
    protected final Column timeColumn;

    protected final List<Instance> instances;

    protected Date maximum;
    protected Date minimum;

    public DateGraph(DiscordBot.ChannelType channelType, Graph.Type graphType, Type type, String title, String yAxisLabel, PlotOrientation orientation, Table table, Column timeColumn, Column mainCountColumn, Instance... instances) {
        super(channelType, graphType, type.name + " " + title, type.name, yAxisLabel, orientation);

        this.type = type;

        this.table = table;
        this.timeColumn = timeColumn;

        this.instances = new ArrayList<>();

        Instance main = setupMain(mainCountColumn);
        if (main != null)
            this.instances.add(main);

        Instance last = setupLast(mainCountColumn);
        if (last != null)
            this.instances.add(last);
    }

    protected Instance setupMain(Column mainCountColumn) {
        return new Instance(type.thisStatus, Color.RED, mainCountColumn) {
            @Override
            protected String getDescription() {
                return null;
            }
        };
    }

    protected Instance setupLast(Column mainCountColumn) {
        /* Add 'Last' Instance */
        if (type != Type.LIFE_TIME)
            return new Instance(type.lastStatus, Color.BLUE, mainCountColumn) {
                @Override
                protected String getDescription() {
                    return null;
                }

                @Override
                public long getMinimum() {
                    return minimum.getTime() - type.millis;
                }

                @Override
                public long getMaximum() {
                    return minimum.getTime();
                }

                @Override
                public long getMillis(long millis) {
                    /* Adjust to graph */
                    return millis + type.getMillis();
                }
            };

        return null;
    }

    public void add(Instance... instances) {
        this.instances.addAll(Arrays.asList(instances));
    }

    public Instance getMainInstance() {
        return instances.get(0);
    }

    protected int getCount(int count, int prevCount) {
        return count;
    }

    @Override
    protected void onGenerate() {
        this.maximum = DateUtils.now();
        this.minimum = type != Type.LIFE_TIME ? new Date(maximum.getTime() - type.millis) : new Date(Long.parseLong(Database.get().getEntries(table, timeColumn).get(0).get(timeColumn)));
    }

    @Override
    protected void setup(XYPlot plot) {
        int index = 0;
        for (Instance instance : instances) {
            plot.getRenderer().setSeriesPaint(index, instance.color.getAwtColor());
            index++;
        }
    }

    @Override
    protected void afterFile(TextChannel channel) {
        for (Instance instance : instances) {
            String description = instance.getDescription();
            channel.sendMessage(bungee.getDiscord().getRole(BotToken.DEFAULT, DiscordBot.CustomRole.valueOf(instance.color.toString())).getAsMention() + " **" + instance.seriesName + "**" + (description == null ? "" : " » " + description)).queue();
        }
    }

    @Override
    protected XYDataset getDataset() {
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        for (Instance instance : instances) {
            instance.generate(dataset);
        }

        return dataset;
    }

    @Override
    protected ValueAxis[] getDomain() {
        DateAxis domain = new DateAxis();

        if (type != Type.LIFE_TIME)
            domain.setDateFormatOverride(type.format);

        domain.setMinimumDate(minimum);
        domain.setMaximumDate(maximum);

        return new ValueAxis[] { domain };
    }

    @Override
    protected ValueAxis[] getRange() {
        NumberAxis range = new NumberAxis();
        range.setNumberFormatOverride(NumberFormat.getIntegerInstance());

        range.setRangeType(RangeType.POSITIVE);

        return new ValueAxis[] { range };
    }

    public enum Type {

        LIFE_TIME("Lifetime", "Lifetime", null, -1, new SimpleDateFormat("MMM:yyyy")),
        YEARLY("Yearly", "This Year", "Last Year", TimeUnit.DAYS.toMillis(365), new SimpleDateFormat("MMM")),
        MONTHLY("Monthly", "This Month", "Last Month", TimeUnit.DAYS.toMillis(30), new SimpleDateFormat("dd")),
        WEEKLY("Weekly", "This Week", "Last Week", TimeUnit.DAYS.toMillis(7), new SimpleDateFormat("EEEE")),
        DAILY("Daily", "Today", "Yesterday", TimeUnit.DAYS.toMillis(1), new SimpleDateFormat("HH:mm"));

        private final String name;
        private final String thisStatus;
        private final String lastStatus;
        private final long millis;
        private final SimpleDateFormat format;

        Type(String name, String thisStatus, String lastStatus, long millis, SimpleDateFormat format) {
            this.name = name;
            this.thisStatus = thisStatus;
            this.lastStatus = lastStatus;
            this.millis = millis;
            this.format = format;
        }

        public String getName() {
            return name;
        }

        public String getThisStatus() {
            return thisStatus;
        }

        public String getLastStatus() {
            return lastStatus;
        }

        public long getMillis() {
            return millis;
        }

        public SimpleDateFormat getFormat() {
            return format;
        }
    }

    public abstract class Instance {

        protected String seriesName;
        protected final Color color;
        protected final Column countColumn;

        protected long top;
        protected int topCount;

        protected Long firstTime;
        protected Integer first;

        protected Long lastZeroOrFirstTime;
        protected Integer lastZeroOrFirst;

        protected Long lastTime;
        protected Integer last;

        protected long entryCount;
        protected long entrySum;
        protected int average;

        public Instance(String seriesName, Color color, Column countColumn) {
            this.seriesName = seriesName;
            this.color = color;
            this.countColumn = countColumn;
        }

        public Instance getMainInstance() {
            return DateGraph.this.getMainInstance();
        }

        public long getMinimum() {
            return DateGraph.this.minimum.getTime();
        }

        public long getMaximum() {
            return DateGraph.this.maximum.getTime();
        }

        public long getMillis(long millis) {
            return millis;
        }

        protected abstract String getDescription();

        public void generate(XYDataset xyDataset) {
            TimeSeriesCollection dataset = (TimeSeriesCollection) xyDataset;

            /* Reset all Values */
            top = 0L;
            topCount = 0;
            average = 0;

            first = null;

            entryCount = 0;
            entrySum = 0;

            lastZeroOrFirstTime = null;
            lastZeroOrFirst = 0;

            lastTime = null;
            last = 0;

            /* Setup Series */
            TimeSeries series = new TimeSeries(seriesName);

            int prevCount = 0;
            for (Map<Column, String> entry : Database.get().getEntries(table, new Where(Where.Operator.GREATER_THAN_OR_EQUAL, timeColumn, getMinimum()), new Where(Where.Operator.LESSER_THAN_OR_EQUAL, timeColumn, getMaximum()))) {
                long millis = Long.parseLong(entry.get(timeColumn));
                int loaded = Integer.parseInt(entry.get(countColumn));

                /* First Time */
                if (firstTime == null) {
                    firstTime = millis;
                    first = loaded;

                    lastZeroOrFirstTime = firstTime;
                    lastZeroOrFirst = first;
                }

                int count = getCount(loaded, prevCount);

                series.add(new Millisecond(new Date(getMillis(millis))), count);

                /* Record */
                if (top == 0L || count >= topCount) {
                    top = millis;
                    topCount = count;
                }

                /* Average */
                entryCount += 1L;
                entrySum += (long) count;

                /* Prev Count */
                prevCount = loaded;

                /* LastZero / Or First */
                if (loaded == 0) {
                    lastZeroOrFirstTime = millis;
                    lastZeroOrFirst = loaded;
                }

                /* Last Time */
                lastTime = millis;
                last = loaded;
            }

            dataset.addSeries(series);

            /* Average */
            if (entryCount != 0)
                average = (int) (entrySum / entryCount);
        }
    }
}
