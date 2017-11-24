package com.orbitmines.api.database.tables.survival;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableSurvivalPlayers extends Table {

    public static final Column UUID = new Column("UUID", Column.Type.VARCHAR, 36);
    public static final Column EARTH_MONEY = new Column("EarthMoney", Column.Type.INT);
    public static final Column CLAIM_BLOCKS = new Column("ClaimBlocks", Column.Type.INT);

    public TableSurvivalPlayers() {
        super("SurvivalPlayers", UUID, EARTH_MONEY, CLAIM_BLOCKS);
    }

    public String[] values(String uuid, String earthMoney, String claimBlocks) {
        return new String[] { uuid, earthMoney, claimBlocks };
    }
}
