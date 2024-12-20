package com.orbitmines.api.database;

import com.orbitmines.api.database.tables.*;
import com.orbitmines.api.database.tables.kitpvp.TableKitPvPKitStats;
import com.orbitmines.api.database.tables.kitpvp.TableKitPvPPlayers;
import com.orbitmines.api.database.tables.statistics.*;
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
    public static TableAchievements ACHIEVEMENTS;
    public static TableDiscord DISCORD;
    public static TableDiscordGroup DISCORD_GROUP;
    public static TableDiscordGroupData DISCORD_GROUP_DATA;
    public static TableIPs IPS;
    public static TablePunishments PUNISHMENTS;
    public static TableReports REPORTS;
    public static Table2FA _2FA;
    public static TableDonations DONATIONS;
    public static TablePlayTime PLAY_TIME;
    public static TableLoot LOOT;
    public static TablePeriodLoot PERIOD_LOOT;

    public static TableChestShops CHEST_SHOPS;

    public static TableServerData SERVER_DATA;
    public static TableServers SERVERS;
    public static TableMaps MAPS;

    public static TableStatsOnline STATS_ONLINE;
    public static TableStatsUnique STATS_UNIQUE;
    public static TableStatsVotes STATS_VOTES;
    public static TableStatsPlayTime STATS_PLAY_TIME;
    public static TableStatsTPS STATS_TPS;

    /* Survival */
    public static TableSurvivalPlayers SURVIVAL_PLAYERS;
    public static TableSurvivalClaim SURVIVAL_CLAIM;
    public static TableSurvivalHomes SURVIVAL_HOMES;
    public static TableSurvivalWarps SURVIVAL_WARPS;

    /* KitPvP */
    public static TableKitPvPPlayers KITPVP_PLAYERS;
    public static TableKitPvPKitStats KITPVP_KIT_STATS;

    static {
        PLAYERS = new TablePlayers();
        VOTES = new TableVotes();
        FRIENDS = new TableFriends();
        ACHIEVEMENTS = new TableAchievements();
        DISCORD = new TableDiscord();
        DISCORD_GROUP = new TableDiscordGroup();
        DISCORD_GROUP_DATA = new TableDiscordGroupData();
        IPS = new TableIPs();
        PUNISHMENTS = new TablePunishments();
        REPORTS = new TableReports();
        _2FA = new Table2FA();
        DONATIONS = new TableDonations();
        PLAY_TIME = new TablePlayTime();
        LOOT = new TableLoot();
        PERIOD_LOOT = new TablePeriodLoot();

        CHEST_SHOPS = new TableChestShops();

        SERVER_DATA = new TableServerData();
        SERVERS = new TableServers();
        MAPS = new TableMaps();

        STATS_ONLINE = new TableStatsOnline();
        STATS_UNIQUE = new TableStatsUnique();
        STATS_VOTES = new TableStatsVotes();
        STATS_PLAY_TIME = new TableStatsPlayTime();
        STATS_TPS = new TableStatsTPS();

        SURVIVAL_PLAYERS = new TableSurvivalPlayers();
        SURVIVAL_CLAIM = new TableSurvivalClaim();
        SURVIVAL_HOMES = new TableSurvivalHomes();
        SURVIVAL_WARPS = new TableSurvivalWarps();

        KITPVP_PLAYERS = new TableKitPvPPlayers();
        KITPVP_KIT_STATS = new TableKitPvPKitStats();
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
