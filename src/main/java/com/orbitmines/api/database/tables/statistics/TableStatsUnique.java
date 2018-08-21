package com.orbitmines.api.database.tables.statistics;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableStatsUnique extends Table {

    public static final Column TIME = new Column("Time", Column.Type.BIGINT);
    public static final Column UNIQUE = new Column("Unique", Column.Type.INT);

    public TableStatsUnique() {
        super("StatsUnique", TIME, UNIQUE);
    }
}
