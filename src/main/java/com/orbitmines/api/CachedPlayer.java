package com.orbitmines.api;

import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.TablePlayers;
import com.orbitmines.api.utils.uuid.UUIDUtils;
import com.orbitmines.bungeecord.OrbitMinesBungee;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class CachedPlayer {

    private static Map<UUID, CachedPlayer> cached = new HashMap<>();

    private UUID uuid;
    private String playerName;

    private IP ip;

    public CachedPlayer(UUID uuid) {
        cached.put(uuid, this);

        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getPlayerName() {
        if (playerName == null)
            playerName = Database.get().getString(Table.PLAYERS, TablePlayers.NAME, new Where(TablePlayers.UUID, getUUID().toString()));

        return playerName;
    }

    public StaffRank getStaffRank() {
        return StaffRank.valueOf(Database.get().getString(Table.PLAYERS, TablePlayers.STAFFRANK, new Where(TablePlayers.UUID, getUUID().toString())));
    }

    public VipRank getVipRank() {
        return VipRank.valueOf(Database.get().getString(Table.PLAYERS, TablePlayers.VIPRANK, new Where(TablePlayers.UUID, getUUID().toString())));
    }

    public String getLastOnline() {
        updateIP();

        return ip == null ? null : ip.getLastLogin();
    }

    public String getLastOnlineInTimeUnit() {
        updateIP();

        return ip == null ? null : ip.getLastLoginInTimeUnit();
    }

    public Server getServer() {
        updateIP();

        ip.updateCurrentServer();

        if (ip.getCurrentServer() != null) {
            return OrbitMinesBungee.getBungee().getServer(ip.getCurrentServer());
        } else {
            /* Not Online, update last online */
            ip.update();
            return null;
        }
    }

    private void updateIP() {
        if (ip == null)
            ip = IP.getIp(uuid);
    }

    public static CachedPlayer getPlayer(UUID uuid) {
        if (cached.containsKey(uuid))
            return cached.get(uuid);

        return new CachedPlayer(uuid);
    }

    public static CachedPlayer getPlayer(String playerName) {
        UUID uuid = UUIDUtils.getUUIDFromDatabase(playerName);

        return uuid != null ? getPlayer(uuid) : null;
    }
}
