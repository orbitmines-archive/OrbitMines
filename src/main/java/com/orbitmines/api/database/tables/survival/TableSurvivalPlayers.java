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
    public static final Column BACK_LOCATION = new Column("BackLocation", Column.Type.VARCHAR, 100);
    public static final Column BACK_CHARGES = new Column("BackCharges", Column.Type.INT);
    public static final Column BED_ENTER = new Column("LastBedEnter", Column.Type.BIGINT);
    public static final Column LOGOUT_LOCATION = new Column("LogoutLocation", Column.Type.VARCHAR, 100);
    public static final Column LOGOUT_FLY = new Column("LogoutFly", Column.Type.TINYINT, 1);
    public static final Column EXTRA_HOMES = new Column("ExtraHomes", Column.Type.INT);
    public static final Column WARP_SLOT_SHOP = new Column("WarpSlotShop", Column.Type.TINYINT, 1);
    public static final Column WARP_SLOT_PRISMS = new Column("WarpSlotPrisms", Column.Type.TINYINT, 1);
    public static final Column FAVORITE_WARPS = new Column("FavoriteWarps", Column.Type.TEXT);

    public TableSurvivalPlayers() {
        super("SurvivalPlayers", UUID, EARTH_MONEY, CLAIM_BLOCKS, BACK_LOCATION, BACK_CHARGES, BED_ENTER, LOGOUT_LOCATION, LOGOUT_FLY, EXTRA_HOMES, WARP_SLOT_SHOP, WARP_SLOT_PRISMS, FAVORITE_WARPS);
    }
}
