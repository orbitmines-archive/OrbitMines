package com.orbitmines.api.database.tables;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
 * OrbitMines - @author Fadi Shawki - 16-6-2018
 */
public class TablePunishments extends Table {

    public static final Column UUID = new Column("UUID", Column.Type.VARCHAR, 36);
    public static final Column OFFENCE = new Column("Offence", Column.Type.VARCHAR, 32);
    public static final Column SEVERITY = new Column("Severity", Column.Type.VARCHAR, 32);
    public static final Column FROM = new Column("From", Column.Type.VARCHAR, 32);
    public static final Column TO = new Column("To", Column.Type.VARCHAR, 32);
    public static final Column PUNISHED_BY = new Column("PunishedBy", Column.Type.VARCHAR, 36);
    public static final Column REASON = new Column("Reason", Column.Type.VARCHAR, 128);
    public static final Column PARDONED = new Column("Pardoned", Column.Type.TINYINT, 1);
    public static final Column PARDONED_ON = new Column("PardonedOn", Column.Type.VARCHAR, 32);
    public static final Column PARDONED_BY = new Column("PardonedBy", Column.Type.VARCHAR, 36);
    public static final Column PARDONED_REASON = new Column("PardonedReason", Column.Type.VARCHAR, 128);

    public TablePunishments() {
        super("Punishments", UUID, OFFENCE, SEVERITY, FROM, TO, PUNISHED_BY, REASON, PARDONED, PARDONED_ON, PARDONED_BY, PARDONED_REASON);
    }
}
