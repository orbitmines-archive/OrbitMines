package com.orbitmines.api.database.tables;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TablePlayers extends Table {

    public static final Column UUID = new Column("UUID", Column.Type.VARCHAR, 36);
    public static final Column NAME = new Column("Name", Column.Type.VARCHAR, 16);
    public static final Column STAFFRANK = new Column("StaffRank", Column.Type.VARCHAR, 32);
    public static final Column VIPRANK = new Column("VipRank", Column.Type.VARCHAR, 32);
    public static final Column LANGUAGE = new Column("Language", Column.Type.INT);
    public static final Column SILENT = new Column("Silent", Column.Type.TINYINT, 1);
    public static final Column POINTS = new Column("Points", Column.Type.INT);
    public static final Column TOKENS = new Column("Tokens", Column.Type.INT);
    public static final Column MONTHLYBONUS = new Column("MonthlyBonus", Column.Type.TINYINT, 1);
    public static final Column VOTES = new Column("Votes", Column.Type.INT);
    public static final Column TOTAL_VOTES = new Column("TotalVotes", Column.Type.INT);
    public static final Column CACHED_VOTES = new Column("CachedVotes", Column.Type.VARCHAR, 500);

    public TablePlayers() {
        super("Players", UUID, NAME, STAFFRANK, VIPRANK, LANGUAGE, SILENT, POINTS, TOKENS, MONTHLYBONUS, VOTES, TOTAL_VOTES, CACHED_VOTES);
    }
}
