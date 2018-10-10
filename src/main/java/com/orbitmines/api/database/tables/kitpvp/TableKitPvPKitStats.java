package com.orbitmines.api.database.tables.kitpvp;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableKitPvPKitStats extends Table {

    public static final Column UUID = new Column("UUID", Column.Type.VARCHAR, 36);
    public static final Column KIT_ID = new Column("KitId", Column.Type.BIGINT);
    public static final Column UNLOCKED_LEVEL = new Column("UnlockedLevel", Column.Type.INT);
    public static final Column KILLS = new Column("Kills", Column.Type.INT);
    public static final Column DEATHS = new Column("Deaths", Column.Type.INT);
    public static final Column BEST_STREAK = new Column("BestStreak", Column.Type.INT);

    public TableKitPvPKitStats() {
        super("KitPvPKitStats", UUID, KIT_ID, UNLOCKED_LEVEL, KILLS, DEATHS, BEST_STREAK);
    }
}
