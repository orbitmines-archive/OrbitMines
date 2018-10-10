package com.orbitmines.spigot.api.handlers.leaderboard.hologram;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardString;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class LastEntryHologramLeaderboard extends DefaultHologramLeaderBoard {

    public LastEntryHologramLeaderboard(Location location, double yOff, ScoreboardString title, int size, Table table, Column uuidColumn, Column column, Where... wheres) {
        super(location, yOff, title, size, table, uuidColumn, column, wheres);
    }

    public LastEntryHologramLeaderboard(Location location, double yOff, ScoreboardString[] title, int size, Table table, Column uuidColumn, Column column, Where... wheres) {
        super(location, yOff, title, size, table, uuidColumn, column, wheres);
    }

    @Override
    protected List<Map<Column, String>> getOnLeaderBoard(List<Map<Column, String>> entries) {
        List<Map<Column, String>> ordered = new ArrayList<>(entries);

        if (ordered.size() > size)
            ordered = ordered.subList(ordered.size() -size, ordered.size());

        Collections.reverse(ordered);

        return ordered;
    }
}
