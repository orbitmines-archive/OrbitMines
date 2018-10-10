package com.orbitmines.api.database.tables.statistics;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableStatsTPS extends Table {

    public static final Column TIME = new Column("Time", Column.Type.BIGINT);
    public static final Column SERVER = new Column("Server", Column.Type.VARCHAR, 32);
    public static final Column TPS = new Column("TPS", Column.Type.BIGINT);

    public TableStatsTPS() {
        super("StatsTPS", TIME, SERVER, TPS);
    }
}
