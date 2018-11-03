package com.orbitmines.api.database.tables.statistics;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableStatsPlayTime extends Table {

    public static final Column TIME = new Column("Time", Column.Type.BIGINT);
    public static final Column TOTAL = new Column("Total", Column.Type.BIGINT);
    public static final Column HUB = new Column("HUB", Column.Type.BIGINT);
    public static final Column SURVIVAL = new Column("SURVIVAL", Column.Type.BIGINT);
    public static final Column KITPVP = new Column("KITPVP", Column.Type.BIGINT);

    public TableStatsPlayTime() {
        super("StatsPlayTime", TIME, TOTAL, HUB, SURVIVAL, KITPVP);
    }
}
