package com.orbitmines.api.database.tables.survival;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;
import com.orbitmines.spigot.servers.survival.handlers.teleportable.Warp;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableSurvivalWarps extends Table {

    public static final Column ID = new Column("ID", Column.Type.INT, 15);
    public static final Column UUID = new Column("UUID", Column.Type.VARCHAR, 36);
    public static final Column NAME = new Column("Name", Column.Type.VARCHAR, Warp.MAX_CHARACTERS);
    public static final Column ENABLED = new Column("Enabled", Column.Type.TINYINT, 1);
    public static final Column TYPE = new Column("Type", Column.Type.VARCHAR, 32);
    public static final Column ICON_ID = new Column("IconID", Column.Type.INT, 8);
    public static final Column LOCATION = new Column("Location", Column.Type.VARCHAR, 100);

    public TableSurvivalWarps() {
        super("SurvivalWarps", ID, UUID, NAME, ENABLED, TYPE, ICON_ID, LOCATION);
    }
}
