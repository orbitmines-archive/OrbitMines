package com.orbitmines.api.database.tables.kitpvp;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableKitPvPPlayers extends Table {

    public static final Column UUID = new Column("UUID", Column.Type.VARCHAR, 36);
    public static final Column COINS = new Column("Coins", Column.Type.INT);

    public TableKitPvPPlayers() {
        super("KitPvPPlayers", UUID, COINS);
    }

    public String[] values(String uuid, String coins) {
        return new String[] { uuid, coins };
    }
}
