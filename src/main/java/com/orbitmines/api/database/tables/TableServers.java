package com.orbitmines.api.database.tables;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableServers extends Table {

    public static final Column IP = new Column("IP", Column.Type.VARCHAR, 15);
    public static final Column PORT = new Column("Port", Column.Type.SMALLINT);
    public static final Column SERVER = new Column("Server", Column.Type.VARCHAR, 32);
    public static final Column STATUS = new Column("Status", Column.Type.VARCHAR, 32);
    public static final Column PLAYERS = new Column("Players", Column.Type.INT);
    public static final Column MAX_PLAYERS = new Column("MaxPlayers", Column.Type.INT);
    public static final Column LAST_UPDATE = new Column("LastUpdate", Column.Type.BIGINT);

    public TableServers() {
        super("Servers", IP, PORT, SERVER, STATUS, PLAYERS, MAX_PLAYERS, LAST_UPDATE);
    }

    public String[] values(String ip, String port,String server, String status, String players, String maxPlayers, String lastUpdate) {
        return new String[] { ip, port, server, status, players, maxPlayers, lastUpdate };
    }
}
