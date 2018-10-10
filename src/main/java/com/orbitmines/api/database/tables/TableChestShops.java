package com.orbitmines.api.database.tables;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableChestShops extends Table {

    public static final Column SERVER = new Column("Server", Column.Type.VARCHAR, 32);
    public static final Column ID = new Column("Id", Column.Type.BIGINT);
    public static final Column OWNER = new Column("Owner", Column.Type.VARCHAR, 36);
    public static final Column LOCATION = new Column("Location", Column.Type.VARCHAR, 100);
    public static final Column MATERIAL = new Column("MaterialId", Column.Type.VARCHAR, 32);
    public static final Column TYPE = new Column("Type", Column.Type.VARCHAR, 32);
    public static final Column AMOUNT = new Column("Amount", Column.Type.SMALLINT);
    public static final Column PRICE = new Column("Price", Column.Type.INT);

    public TableChestShops() {
        super("ChestShops", SERVER, ID, OWNER, LOCATION, MATERIAL, TYPE, AMOUNT, PRICE);
    }
}
