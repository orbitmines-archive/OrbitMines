package com.orbitmines.api.database.tables;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableAchievements extends Table {

    public static final Column UUID = new Column("UUID", Column.Type.VARCHAR, 36);
    public static final Column SERVER = new Column("Server", Column.Type.VARCHAR, 32);
    public static final Column ACHIEVEMENT = new Column("Achievement", Column.Type.VARCHAR, 32);
    public static final Column COMPLETED = new Column("Completed", Column.Type.TINYINT);
    public static final Column PROGRESS = new Column("Progress", Column.Type.INT);

    public TableAchievements() {
        super("Achievements", UUID, SERVER, ACHIEVEMENT, COMPLETED, PROGRESS);
    }
}
