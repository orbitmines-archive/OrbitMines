package com.orbitmines.bungeecord.statistics.graphs;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.statistics.Statistics;
import com.orbitmines.bungeecord.statistics.graphs.date.DateGraph;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.utils.BotToken;
import net.dv8tion.jda.core.entities.TextChannel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.TableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public abstract class Graph {

    protected final OrbitMinesBungee bungee;

    protected final DiscordBot.ChannelType channelType;
    protected final Type graphType;

    protected final String title;
    protected final String xAxisLabel;
    protected final String yAxisLabel;
    protected final PlotOrientation orientation;

    public Graph(DiscordBot.ChannelType channelType, Type graphType, String title, String xAxisLabel, String yAxisLabel, PlotOrientation orientation) {
        this.bungee = OrbitMinesBungee.getBungee();

        this.channelType = channelType;
        this.graphType = graphType;
        this.title = title;
        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
        this.orientation = orientation;
    }

    protected abstract void onGenerate();

    protected abstract void setup(XYPlot plot);

    protected abstract void afterFile(TextChannel channel);

    protected abstract XYDataset getDataset();

    protected abstract ValueAxis[] getDomain();

    protected abstract ValueAxis[] getRange();

    public File generate(String fileName, int width, int height) {
        onGenerate();

        XYDataset dataset = getDataset();

        //TODO Other than XYLineChart

        /* Chart */
        JFreeChart chart = graphType.createChart(this, title, xAxisLabel, yAxisLabel, dataset, orientation, true, false, false);
        ChartUtilities.applyCurrentTheme(chart);

        chart.setBackgroundPaint(Statistics.TRANSPARENT);
        chart.getTitle().setPaint(Statistics.TEXT_COLOR);

        /* Plot */
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Statistics.TRANSPARENT);
        plot.setOutlineVisible(false);
        setup(plot);

        Font defaultLabelFont = plot.getDomainAxis().getLabelFont();
        double defaultLabelAngle = plot.getDomainAxis().getLabelAngle();
        RectangleInsets defaultLabelRectangleInsets = plot.getDomainAxis().getLabelInsets();

        /* Domain */
        ValueAxis[] domain = getDomain();
        for (ValueAxis axis : domain) {
            axis.setLabel(xAxisLabel);
            axis.setLabelFont(defaultLabelFont);
            axis.setLabelAngle(defaultLabelAngle);
            axis.setLabelInsets(defaultLabelRectangleInsets);

            axis.setLabelPaint(Statistics.TEXT_COLOR);
            axis.setTickLabelPaint(Statistics.TEXT_COLOR);
        }
        plot.setDomainAxes(domain);
        plot.setDomainGridlinePaint(Statistics.GRID_COLOR);
        plot.setDomainGridlineStroke(new BasicStroke());

        /* Range */
        ValueAxis[] range = getRange();
        for (ValueAxis axis : range) {
            axis.setLabel(yAxisLabel);
            axis.setLabelFont(defaultLabelFont);
            axis.setLabelAngle(defaultLabelAngle);
            axis.setLabelInsets(defaultLabelRectangleInsets);

            axis.setLabelPaint(Statistics.TEXT_COLOR);
            axis.setTickLabelPaint(Statistics.TEXT_COLOR);
        }
        plot.setRangeAxes(range);
        plot.setRangeGridlinePaint(Statistics.GRID_COLOR);
        plot.setRangeGridlineStroke(new BasicStroke());

        /* Legend */
        LegendTitle legend = chart.getLegend();
        legend.setVisible(false);
//        legend.setBackgroundPaint(Statistics.TRANSPARENT);
//        legend.setItemPaint(Statistics.TEXT_COLOR);
//        legend.setFrame(BlockBorder.NONE);
//        legend.setItemLabelPadding(new RectangleInsets(5.0, 2.0, 10.0, 500));
//        legend.setPadding(new RectangleInsets(0.0, 20.0, 0.0, 0.0));

        File file = new File("stats/" + fileName + ".png");
        try {
            ChartUtilities.saveChartAsPNG(file, chart, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextChannel channel = bungee.getDiscord().getChannel(BotToken.DEFAULT, channelType);
        channel.sendFile(file).queue();

        afterFile(channel);

        return file;
    }

    public enum Type {

        BAR() {
            @Override
            public JFreeChart createChart(Graph graph, String title, String xAxisLabel, String yAxisLabel, XYDataset dataset, PlotOrientation orientation, boolean legend, boolean tooltips, boolean urls) {
                return ChartFactory.createXYBarChart(title, xAxisLabel, graph instanceof DateGraph, yAxisLabel, (IntervalXYDataset) dataset, orientation, legend, tooltips, urls);
            }
        },
        AREA() {
            @Override
            public JFreeChart createChart(Graph graph, String title, String xAxisLabel, String yAxisLabel, XYDataset dataset, PlotOrientation orientation, boolean legend, boolean tooltips, boolean urls) {
                return ChartFactory.createXYAreaChart(title, xAxisLabel, yAxisLabel, dataset, orientation, legend, tooltips, urls);
            }
        },
        STACKED_AREA() {
            @Override
            public JFreeChart createChart(Graph graph, String title, String xAxisLabel, String yAxisLabel, XYDataset dataset, PlotOrientation orientation, boolean legend, boolean tooltips, boolean urls) {
                return ChartFactory.createStackedXYAreaChart(title, xAxisLabel, yAxisLabel, (TableXYDataset) dataset, orientation, legend, tooltips, urls);
            }
        },
        LINE() {
            @Override
            public JFreeChart createChart(Graph graph, String title, String xAxisLabel, String yAxisLabel, XYDataset dataset, PlotOrientation orientation, boolean legend, boolean tooltips, boolean urls) {
                return ChartFactory.createXYLineChart(title, xAxisLabel, yAxisLabel, dataset, orientation, legend, tooltips, urls);
            }
        },
        STEP() {
            @Override
            public JFreeChart createChart(Graph graph, String title, String xAxisLabel, String yAxisLabel, XYDataset dataset, PlotOrientation orientation, boolean legend, boolean tooltips, boolean urls) {
                return ChartFactory.createXYStepChart(title, xAxisLabel, yAxisLabel, dataset, orientation, legend, tooltips, urls);
            }
        },
        STEP_AREA() {
            @Override
            public JFreeChart createChart(Graph graph, String title, String xAxisLabel, String yAxisLabel, XYDataset dataset, PlotOrientation orientation, boolean legend, boolean tooltips, boolean urls) {
                return ChartFactory.createXYStepAreaChart(title, xAxisLabel, yAxisLabel, dataset, orientation, legend, tooltips, urls);
            }
        };

        public JFreeChart createChart(Graph graph, String title, String xAxisLabel, String yAxisLabel, XYDataset dataset, PlotOrientation orientation, boolean legend, boolean tooltips, boolean urls) {
            throw new IllegalStateException();
        }
    }
}
