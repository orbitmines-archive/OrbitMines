package com.orbitmines.api.database.tables;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableIPs extends Table {

    public static final Column UUID = new Column("UUID", Column.Type.VARCHAR, 36);
    public static final Column CURRENT_SERVER = new Column("CurrentServer", Column.Type.VARCHAR, 32);
    public static final Column LAST_IP = new Column("LastIp", Column.Type.VARCHAR, 32);
    public static final Column LAST_LOGIN = new Column("LastLogin", Column.Type.DATETIME);
    public static final Column HISTORY = new Column("History", Column.Type.TEXT); /* 3000 IPs */

    public TableIPs() {
        super("IPs", UUID, CURRENT_SERVER, LAST_IP, LAST_LOGIN, HISTORY);
    }

    public String[] values(String uuid, String currentServer, String lastIp, String lastLogin, String history) {
        return new String[] { uuid, currentServer, lastIp, lastLogin, history };
    }
}
