package com.orbitmines.api.database.tables.survival;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableSurvivalClaim extends Table {

    public static final Column ID = new Column("ID", Column.Type.INT, 15);
    public static final Column NAME = new Column("Name", Column.Type.VARCHAR, 20);
    public static final Column CREATED_ON = new Column("CreatedOn", Column.Type.VARCHAR, 32);
    public static final Column CORNER_1 = new Column("Corner1", Column.Type.VARCHAR, 100);
    public static final Column CORNER_2 = new Column("Corner2", Column.Type.VARCHAR, 100);
    public static final Column OWNER = new Column("Owner", Column.Type.VARCHAR, 36);
    public static final Column MEMBERS = new Column("Members", Column.Type.TEXT);
    public static final Column SETTINGS = new Column("Settings", Column.Type.TEXT);
    public static final Column PARENT = new Column("Parent", Column.Type.INT, 15);

    public TableSurvivalClaim() {
        super("SurvivalClaims", ID, NAME, CREATED_ON, CORNER_1, CORNER_2, OWNER, MEMBERS, SETTINGS, PARENT);
    }
}
