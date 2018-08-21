package com.orbitmines.api.database.tables.statistics;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableStatsVotes extends Table {

    public static final Column TIME = new Column("Time", Column.Type.BIGINT);
    public static final Column TOTAL = new Column("Total", Column.Type.INT);
    public static final Column MONTHLY = new Column("Monthly", Column.Type.INT);

    public TableStatsVotes() {
        super("StatsVotes", TIME, TOTAL, MONTHLY);
    }
}
