package com.orbitmines.api.database.tables;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableDiscord extends Table {

    public static final Column UUID = new Column("UUID", Column.Type.VARCHAR, 36);
    public static final Column ID = new Column("Id", Column.Type.BIGINT);

    public TableDiscord() {
        super("Discord", UUID, ID);
    }
}
