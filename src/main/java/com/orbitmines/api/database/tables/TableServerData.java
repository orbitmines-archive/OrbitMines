package com.orbitmines.api.database.tables;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableServerData extends Table {

    public static final Column SERVER = new Column("Server", Column.Type.VARCHAR, 32);
    public static final Column TYPE = new Column("Type", Column.Type.VARCHAR, 32);
    public static final Column DATA = new Column("Data", Column.Type.VARCHAR, 1000);

    public TableServerData() {
        super("ServerData", SERVER, TYPE, DATA);
    }

    public String[] values(String gameMode, String type, String data) {
        return new String[] { gameMode, type, data };
    }
}
