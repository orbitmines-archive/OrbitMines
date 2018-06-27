package com.orbitmines.api.database.tables;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.utils.DateUtils;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableVotes extends Table {

    public static final Column UUID = new Column("UUID", Column.Type.VARCHAR, 36);
    /* Votes Changes automatically on the restart of the server, servers restart at midnight, and so will switch to the new month table */
    public static final Column VOTES = new Column(DateUtils.getMonth() + DateUtils.getYear(), Column.Type.INT, "0");
    public static final Column TOTAL_VOTES = new Column("TotalVotes", Column.Type.INT);
    public static final Column CACHED_VOTES = new Column("CachedVotes", Column.Type.INT);
    public static final Column VOTE_TIME_STAMPS = new Column("VoteTimeStamps", Column.Type.VARCHAR, 500);

    public TableVotes() {
        super("Votes", UUID, VOTES, TOTAL_VOTES, CACHED_VOTES, VOTE_TIME_STAMPS);
    }

    public static Column getByMonth(String month, int year) {
        return new Column(month + year, Column.Type.INT);
    }
}
