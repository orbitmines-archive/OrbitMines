package com.orbitmines.api.database.tables;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public class TableDonations extends Table {

    public static final Column UUID = new Column("UUID", Column.Type.VARCHAR, 36);
    public static final Column PACKAGE_ID = new Column("PackageID", Column.Type.INT);
    public static final Column AMOUNT_SPENT = new Column("AmountSpent", Column.Type.INT);
    public static final Column DATE = new Column("Date", Column.Type.DATETIME);

    public TableDonations() {
        super("Donations", UUID, PACKAGE_ID, AMOUNT_SPENT, DATE);
    }

    public String[] values(String uuid, String packageId, String amountSpent, String date) {
        return new String[] { uuid, packageId, amountSpent, date };
    }
}
