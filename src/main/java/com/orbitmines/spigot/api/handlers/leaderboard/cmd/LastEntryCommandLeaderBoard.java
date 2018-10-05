package com.orbitmines.spigot.api.handlers.leaderboard.cmd;

import com.orbitmines.api.Color;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.utils.CommandLibrary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/*
 * OrbitMines - @author Fadi Shawki - 29-7-2017
 */
public abstract class LastEntryCommandLeaderBoard extends DefaultCommandLeaderBoard {

    public LastEntryCommandLeaderBoard(String name, Color color, CommandLibrary library, int size, Table table, Column uuidColumn, Column column, Where... wheres) {
        super(name, color, library, size, table, uuidColumn, column, wheres);
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
