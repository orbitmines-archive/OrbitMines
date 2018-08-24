package com.orbitmines.bungeecord.statistics.graphs.date;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.bungeecord.runnables.BungeeRunnable;
import com.orbitmines.bungeecord.statistics.graphs.Graph;
import com.orbitmines.bungeecord.statistics.graphs.GraphUpdater;
import com.orbitmines.discordbot.DiscordBot;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;
import java.util.Map;

public abstract class DateGraphUpdater extends GraphUpdater {

    public DateGraphUpdater(DiscordBot.ChannelType channelType, Graph.Type type, BungeeRunnable.TimeUnit timeUnit, int count) {
        super(channelType, timeUnit, count, type);
    }

    protected abstract void insert();

    protected abstract DateGraph newInstance(DateGraph.Type type);

    protected abstract Table getTable();

    @Override
    protected void update(TextChannel channel) {
        insert();

        /* Remove redudancy; If the last 3 entries are the same, remove the middle one */
        Table table = getTable();
        if (table != null) {
            List<Map<Column, String>> entries = Database.get().getEntries(table);
            int size = entries.size();

            if (size >= 3) {
                boolean redundant = Database.get().entryEquals(table.getColumns()[0], entries.get(size - 1), entries.get(size - 2), entries.get(size - 3));

                if (redundant)
                    Database.get().delete(table, new Where(table.getColumns()[0], Long.parseLong(entries.get(size - 2).get(table.getColumns()[0]))));
            }
        }

        /* Update */
        for (DateGraph.Type type : DateGraph.Type.values()) {
            newInstance(type).generate(channelType.toString() + "_" + type.getName(), 400, 300);
        }
    }
}
