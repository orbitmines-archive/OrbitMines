package com.orbitmines.api.database.tables.uhsurvival;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Robin on 3/4/2018.
 */
public class TableUHPlayers extends Table {

    public static final Column UUID = new Column("UUID", Column.Type.VARCHAR, 36);
    public static final Column WATER = new Column("Water", Column.Type.INT, 3);
    public static final Column FOOD = new Column("Food", Column.Type.LONGTEXT);
    public static final Column BANNED_DATE = new Column("BannedDate", Column.Type.DATE);

    public TableUHPlayers(){
        super("UHPlayers", UUID, WATER, FOOD, BANNED_DATE);
    }

    public String[] values(String uuid, int water, String food, Date date) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy:MM:dd-HH:mm:ss");
        String d = date == null ? "null" : sf.format(date);
        return new String[]{uuid, water + "", food, d};
    }
}
