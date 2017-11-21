package com.orbitmines.api.database;

import com.orbitmines.api.database.tables.TablePlayers;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class Table {

    public static List<Table> ALL = new ArrayList<>();

    public static TablePlayers PLAYERS;

    static {
        PLAYERS = new TablePlayers();
    }

    private final String name;
    private final Column[] columns;

    public Table(String name, Column... columns) {
        ALL.add(this);

        this.name = name;
        this.columns = columns;
    }

    public Column[] getColumns() {
        return columns;
    }

    @Override
    public String toString() {
        return name;
    }

    public String values(String... values) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" VALUES ('");

        for (int i = 0; i < values.length; i++) {
            if (i != 0)
                stringBuilder.append("','");

            stringBuilder.append(values[i]);
        }

        stringBuilder.append("')");

        return stringBuilder.toString();
    }
}
