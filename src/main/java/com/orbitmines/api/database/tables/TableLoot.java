package com.orbitmines.api.database.tables;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableLoot extends Table {

    public static final Column UUID = new Column("UUID", Column.Type.VARCHAR, 36);
    public static final Column LOOT = new Column("Loot", Column.Type.VARCHAR, 32);
    public static final Column RARITY = new Column("Rarity", Column.Type.VARCHAR, 32);
    public static final Column COUNT = new Column("Count", Column.Type.INT);
    public static final Column DESCRIPTION = new Column("Description", Column.Type.TEXT);

    public TableLoot() {
        super("Loot", UUID, LOOT, RARITY, COUNT, DESCRIPTION);
    }
}
