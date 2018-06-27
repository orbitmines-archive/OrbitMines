package com.orbitmines.api.database.tables;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TablePeriodLoot extends Table {

    public static final Column UUID = new Column("UUID", Column.Type.VARCHAR, 36);
    public static final Column DAILY = new Column("Daily", Column.Type.BIGINT);
    public static final Column MONTHLY = new Column("Monthly", Column.Type.BIGINT);
    public static final Column MONTHLY_VIP = new Column("MonthlyVIP", Column.Type.BIGINT);


    public TablePeriodLoot() {
        super("PeriodLoot", UUID, DAILY, MONTHLY, MONTHLY_VIP);
    }
}
