package com.orbitmines.api.database.tables.statistics;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableStatsOnline extends Table {

    public static final Column TIME = new Column("Time", Column.Type.BIGINT);
    public static final Column TOTAL = new Column("Total", Column.Type.INT);
    public static final Column OWNER = new Column("OWNER", Column.Type.INT);
    public static final Column ADMIN = new Column("ADMIN", Column.Type.INT);
    public static final Column DEVELOPER = new Column("DEVELOPER", Column.Type.INT);
    public static final Column MODERATOR = new Column("MODERATOR", Column.Type.INT);
    public static final Column BUILDER = new Column("BUILDER", Column.Type.INT);
    public static final Column EMERALD = new Column("EMERALD", Column.Type.INT);
    public static final Column DIAMOND = new Column("DIAMOND", Column.Type.INT);
    public static final Column GOLD = new Column("GOLD", Column.Type.INT);
    public static final Column IRON = new Column("IRON", Column.Type.INT);
    public static final Column USER = new Column("USER", Column.Type.INT);

    public TableStatsOnline() {
        super("StatsOnline", TIME, TOTAL, OWNER, ADMIN, DEVELOPER, MODERATOR, BUILDER, EMERALD, DIAMOND, GOLD, IRON, USER);
    }
}
