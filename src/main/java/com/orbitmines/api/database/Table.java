package com.orbitmines.api.database;

import com.orbitmines.api.database.tables.*;
import com.orbitmines.api.database.tables.kitpvp.TableKitPvPPlayers;
import com.orbitmines.api.database.tables.survival.TableSurvivalClaim;
import com.orbitmines.api.database.tables.survival.TableSurvivalHomes;
import com.orbitmines.api.database.tables.survival.TableSurvivalPlayers;
import com.orbitmines.api.database.tables.survival.TableSurvivalWarps;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class Table {

    public static List<Table> ALL = new ArrayList<>();

    public static TablePlayers PLAYERS;
    public static TableVotes VOTES;
    public static TableFriends FRIENDS;
    public static TableIPs IPS;
    public static Table2FA _2FA;
    public static TableDonations DONATIONS;
    public static TablePlayTime PLAY_TIME;
    public static TableLoot LOOT;
    public static TablePeriodLoot PERIOD_LOOT;

    public static TableServerData SERVER_DATA;
    public static TableServers SERVERS;
    public static TableMaps MAPS;

    /* Survival */
    public static TableSurvivalPlayers SURVIVAL_PLAYERS;
    public static TableSurvivalClaim SURVIVAL_CLAIM;
    public static TableSurvivalHomes SURVIVAL_HOMES;
    public static TableSurvivalWarps SURVIVAL_WARPS;

    /* KitPvP */
    public static TableKitPvPPlayers KITPVP_PLAYERS;

    static {
        PLAYERS = new TablePlayers();
        VOTES = new TableVotes();
        FRIENDS = new TableFriends();
        IPS = new TableIPs();
        _2FA = new Table2FA();
        DONATIONS = new TableDonations();
        PLAY_TIME = new TablePlayTime();
        LOOT = new TableLoot();
        PERIOD_LOOT = new TablePeriodLoot();

        SERVER_DATA = new TableServerData();
        SERVERS = new TableServers();
        MAPS = new TableMaps();

        SURVIVAL_PLAYERS = new TableSurvivalPlayers();
        SURVIVAL_CLAIM = new TableSurvivalClaim();
        SURVIVAL_HOMES = new TableSurvivalHomes();
        SURVIVAL_WARPS = new TableSurvivalWarps();

        KITPVP_PLAYERS = new TableKitPvPPlayers();
    }

    private final String name;
    private final Column[] columns;

    public Table(String name, Column... columns) {
        ALL.add(this);

        this.name = name;
        this.columns = columns;
    }

    public Column[] getColumns() {
        return columns;
    }

    @Override
    public String toString() {
        return name;
    }

    public String values(String... values) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" VALUES ('");

        for (int i = 0; i < values.length; i++) {
            if (i != 0)
                stringBuilder.append("','");

            stringBuilder.append(values[i]);
        }

        stringBuilder.append("')");

        return stringBuilder.toString();
    }
}
