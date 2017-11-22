package com.orbitmines.api.database.tables;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class Table2FA extends Table {

    public static final Column UUID = new Column("UUID", Column.Type.VARCHAR, 36);
    public static final Column SECRET = new Column("Secret", Column.Type.TINYTEXT);

    public Table2FA() {
        super("2FA", UUID, SECRET);
    }

    public String[] values(String uuid, String secret) {
        return new String[] { uuid, secret };
    }
}
