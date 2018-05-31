package com.orbitmines.api.database.tables;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TablePlayTime extends Table {

    public static final Column UUID = new Column("UUID", Column.Type.VARCHAR, 36);
    public static final Column KITPVP = new Column("KITPVP", Column.Type.BIGINT);
    public static final Column PRISON = new Column("PRISON", Column.Type.BIGINT);
    public static final Column CREATIVE = new Column("CREATIVE", Column.Type.BIGINT);
    public static final Column HUB = new Column("HUB", Column.Type.BIGINT);
    public static final Column SURVIVAL = new Column("SURVIVAL", Column.Type.BIGINT);
    public static final Column SKYBLOCK = new Column("SKYBLOCK", Column.Type.BIGINT);
    public static final Column FOG = new Column("FOG", Column.Type.BIGINT);
    public static final Column MINIGAMES = new Column("MINIGAMES", Column.Type.BIGINT);
    public static final Column UHSURVIVAL = new Column("UHSURVIVAL", Column.Type.BIGINT);

    public TablePlayTime() {
        super("PlayTime", UUID, KITPVP, PRISON, CREATIVE, HUB, SURVIVAL, SKYBLOCK, FOG, MINIGAMES, UHSURVIVAL);
    }
}
