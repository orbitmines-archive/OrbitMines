package com.orbitmines.api.database.tables.kitpvp;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableKitPvPPlayers extends Table {

    public static final Column UUID = new Column("UUID", Column.Type.VARCHAR, 36);
    public static final Column COINS = new Column("Coins", Column.Type.INT);
    public static final Column EXPERIENCE = new Column("Experience", Column.Type.INT);
    public static final Column KILLS = new Column("Kills", Column.Type.INT);
    public static final Column DEATHS = new Column("Deaths", Column.Type.INT);

    public TableKitPvPPlayers() {
        super("KitPvPPlayers", UUID, COINS, EXPERIENCE, KILLS, DEATHS);
    }
}
