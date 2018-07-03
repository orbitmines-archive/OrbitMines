package com.orbitmines.api.database.tables;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
 * OrbitMines - @author Fadi Shawki - 16-6-2018
 */
public class TableReports extends Table {

    public static final Column UUID = new Column("UUID", Column.Type.VARCHAR, 36);
    public static final Column REPORTED_SERVER = new Column("Server", Column.Type.VARCHAR, 32);
    public static final Column REPORTED_ON = new Column("ReportedOn", Column.Type.VARCHAR, 32);
    public static final Column REPORTED_BY = new Column("ReportedBy", Column.Type.VARCHAR, 36);
    public static final Column REASON = new Column("Reason", Column.Type.VARCHAR, 128);

    public TableReports() {
        super("Reports", UUID, REPORTED_SERVER, REPORTED_ON, REPORTED_BY, REASON);
    }
}
