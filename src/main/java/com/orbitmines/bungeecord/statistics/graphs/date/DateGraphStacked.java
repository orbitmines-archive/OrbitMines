package com.orbitmines.bungeecord.statistics.graphs.date;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

@Deprecated
public abstract class DateGraphStacked {
//    extends DateGraph {
//
//    public DateGraphStacked(DiscordBot.ChannelType channelType, Graph.Type graphType, Type type, String title, String yAxisLabel, PlotOrientation orientation, Table table, Column timeColumn, Column countColumn) {
//        super(channelType, graphType, type, title, yAxisLabel, orientation, table, timeColumn, countColumn);
//    }
//
//
//    @Override
//    protected XYDataset getDataset() {
//        TimeTableXYDataset dataset = new TimeTableXYDataset();
//
//        for (DateGraph.Instance instance : instances) {
//            instance.generate(dataset);
//        }
//
//        return dataset;
//    }
//
//    public abstract class Instance extends DateGraph.Instance {
//
//        public Instance(String seriesName, Color color, Column countColumn) {
//            super(seriesName, color, countColumn);
//        }
//
//        @Override
//        public void generate(XYDataset xyDataset) {
//            TimeTableXYDataset dataset = (TimeTableXYDataset) xyDataset;
//
//            /* Reset all Values */
//            top = 0L;
//            topCount = 0L;
//            average = 0L;
//
//            first = 0L;
//
//            entryCount = 0L;
//            entrySum = 0L;
//
//            lastTime = 0L;
//            last = 0L;
//
//            /* Setup Series */
//            long prevCount = 0;
//            for (Map<Column, String> entry : Database.get().getEntries(table, new Where(Where.Operator.GREATER_THAN_OR_EQUAL, timeColumn, getMinimum()), new Where(Where.Operator.LESSER_THAN_OR_EQUAL, timeColumn, getMaximum()))) {
//                long millis = Long.parseLong(entry.get(timeColumn));
//                long loaded = Long.parseLong(entry.get(countColumn));
//
//                /* First Time */
//                if (firstTime == 0L) {
//                    firstTime = millis;
//                    first = loaded;
//                }
//
//                long count = getCount(loaded, prevCount);
//
//                dataset.add(new Millisecond(new Date(getMillis(millis))), count, seriesName);
//
//                /* Record */
//                if (top == 0L || count >= topCount) {
//                    top = millis;
//                    topCount = count;
//                }
//
//                /* Average */
//                entryCount += 1L;
//                entrySum += count;
//
//                /* Prev Count */
//                prevCount = loaded;
//
//                /* Last Time */
//                lastTime = millis;
//                last = loaded;
//            }
//
//            /* Average */
//            if (entryCount != 0)
//                average = (entrySum / entryCount);
//        }
//    }
}
