package com.orbitmines.api.database.tables;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableMaps extends Table {

    public static final Column WORLD_NAME = new Column("WorldName", Column.Type.VARCHAR, 32);
    public static final Column WORLD_GENERATOR = new Column("WorldGenerator", Column.Type.VARCHAR, 32);
    public static final Column NAME = new Column("Name", Column.Type.VARCHAR, 32);
    public static final Column TYPE = new Column("Type", Column.Type.VARCHAR, 32);
    public static final Column SERVER = new Column("Server", Column.Type.VARCHAR, 32);
    public static final Column ENABLED = new Column("Enabled", Column.Type.TINYINT, 1);
    public static final Column AUTHORS = new Column("Authors", Column.Type.VARCHAR, 200);

    public TableMaps() {
        super("Maps", WORLD_NAME, WORLD_GENERATOR, NAME, TYPE, SERVER, ENABLED, AUTHORS);
    }
}
