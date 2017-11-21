package com.orbitmines.api.database.tables;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableServers extends Table {

    public static final Column SERVER = new Column("Server", Column.Type.VARCHAR, 32);
    public static final Column STATUS = new Column("Status", Column.Type.VARCHAR, 32);
    public static final Column PLAYERS = new Column("Players", Column.Type.INT);
    public static final Column MAX_PLAYERS = new Column("MaxPlayers", Column.Type.INT);

    public TableServers() {
        super("Servers", SERVER, STATUS, PLAYERS, MAX_PLAYERS);
    }
}
