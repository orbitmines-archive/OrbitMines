package com.orbitmines.api.database.tables.survival;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;
import com.orbitmines.spigot.servers.survival.handlers.teleportable.Home;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableSurvivalHomes extends Table {

    public static final Column UUID = new Column("UUID", Column.Type.VARCHAR, 36);
    public static final Column NAME = new Column("Name", Column.Type.VARCHAR, Home.MAX_CHARACTERS);
    public static final Column LOCATION = new Column("Location", Column.Type.VARCHAR, 100);

    public TableSurvivalHomes() {
        super("SurvivalHomes", UUID, NAME, LOCATION);
    }
}
