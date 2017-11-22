package com.orbitmines.api.database.tables;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableVotes extends Table {

    public static final Column UUID = new Column("UUID", Column.Type.VARCHAR, 36);
    public static final Column VOTES = new Column("Votes", Column.Type.INT);
    public static final Column TOTAL_VOTES = new Column("TotalVotes", Column.Type.INT);
    public static final Column CACHED_VOTES = new Column("CachedVotes", Column.Type.VARCHAR, 500);
    public static final Column VOTE_TIME_STAMPS = new Column("VoteTimeStamps", Column.Type.VARCHAR, 500);

    public TableVotes() {
        super("Votes", UUID, VOTES, TOTAL_VOTES, CACHED_VOTES, VOTE_TIME_STAMPS);
    }

    public String[] values(String uuid, String votes, String totalVotes, String cachedVotes, String voteTimeStamps) {
        return new String[] { uuid, votes, totalVotes, cachedVotes, voteTimeStamps };
    }
}
