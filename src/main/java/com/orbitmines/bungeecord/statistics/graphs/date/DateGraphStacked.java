package com.orbitmines.bungeecord.statistics.graphs.date;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.bungeecord.statistics.graphs.Graph;
import com.orbitmines.discordbot.DiscordBot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.xy.XYDataset;

import java.util.Date;
import java.util.Map;

public abstract class DateGraphStacked extends DateGraph {

    public DateGraphStacked(DiscordBot.ChannelType channelType, Graph.Type graphType, Type type, String title, String yAxisLabel, PlotOrientation orientation, Table table, Column timeColumn, Column countColumn) {
        super(channelType, graphType, type, title, yAxisLabel, orientation, table, timeColumn, countColumn);
    }


    @Override
    protected XYDataset getDataset() {
        TimeTableXYDataset dataset = new TimeTableXYDataset();

        for (DateGraph.Instance instance : instances) {
            instance.generate(dataset);
        }

        return dataset;
    }

    public abstract class Instance extends DateGraph.Instance {

        public Instance(String seriesName, Color color, Column countColumn) {
            super(seriesName, color, countColumn);
        }

        @Override
        public void generate(XYDataset xyDataset) {
            TimeTableXYDataset dataset = (TimeTableXYDataset) xyDataset;

            /* Reset all Values */
            top = 0L;
            topCount = 0;
            average = 0;

            first = null;

            entryCount = 0;
            entrySum = 0;

            lastTime = null;
            last = 0;

            /* Setup Series */
            int prevCount = 0;
            for (Map<Column, String> entry : Database.get().getEntries(table, new Where(Where.Operator.GREATER_THAN_OR_EQUAL, timeColumn, getMinimum()), new Where(Where.Operator.LESSER_THAN_OR_EQUAL, timeColumn, getMaximum()))) {
                long millis = Long.parseLong(entry.get(timeColumn));
                int loaded = Integer.parseInt(entry.get(countColumn));

                /* First Time */
                if (firstTime == null) {
                    firstTime = millis;
                    first = loaded;
                }

                int count = getCount(loaded, prevCount);

                dataset.add(new Millisecond(new Date(getMillis(millis))), count, seriesName);

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

                /* Last Time */
                lastTime = millis;
                last = loaded;
            }

            /* Average */
            if (entryCount != 0)
                average = (int) (entrySum / entryCount);
        }
    }
}
