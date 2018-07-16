package com.orbitmines.api.database.tables;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.settings.Settings;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TablePlayers extends Table {

    public static final Column UUID = new Column("UUID", Column.Type.VARCHAR, 36);
    public static final Column NAME = new Column("Name", Column.Type.VARCHAR, 16);
    public static final Column STAFFRANK = new Column("StaffRank", Column.Type.VARCHAR, 32);
    public static final Column VIPRANK = new Column("VipRank", Column.Type.VARCHAR, 32);
    public static final Column FIRST_LOGIN = new Column("FirstLogin", Column.Type.DATETIME);
    public static final Column LANGUAGE = new Column("Language", Column.Type.VARCHAR, 32);
    public static final Column SETTINGS_PRIVATE_MESSAGES = new Column("PrivateMessages", Column.Type.VARCHAR, Settings.PRIVATE_MESSAGES.getDefaultType().toString(), 32);
    public static final Column SETTINGS_PLAYER_VISIBILITY = new Column("PlayerVisibility", Column.Type.VARCHAR, Settings.PLAYER_VISIBILITY.getDefaultType().toString(), 32);
    public static final Column SETTINGS_GADGETS = new Column("Gadgets", Column.Type.VARCHAR, Settings.GADGETS.getDefaultType().toString(), 32);
    public static final Column SETTINGS_STATS = new Column("Stats", Column.Type.VARCHAR, Settings.STATS.getDefaultType().toString(), 32);
    public static final Column SILENT = new Column("Silent", Column.Type.TINYINT, 1);
    public static final Column SOLARS = new Column("Solars", Column.Type.INT);
    public static final Column PRISMS = new Column("Prisms", Column.Type.INT);

    public TablePlayers() {
        super("Players", UUID, NAME, STAFFRANK, VIPRANK, FIRST_LOGIN, LANGUAGE, SETTINGS_PRIVATE_MESSAGES, SETTINGS_PLAYER_VISIBILITY, SETTINGS_GADGETS, SETTINGS_STATS, SILENT, SOLARS, PRISMS);
    }
}
