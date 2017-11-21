package com.orbitmines.api.handlers;

import com.madblock.api.Rank;
import com.madblock.api.VipRank;
import com.madblock.api.database.Database;
import com.madblock.api.database.Table;
import com.madblock.api.database.Where;
import com.madblock.api.database.tables.stats.TablePlayers;
import com.madblock.api.handlers.punishment.PunishmentHandler;
import com.madblock.api.utils.UUIDUtils;

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

    public Rank getRank() {
        return Rank.valueOf(Database.get().getString(Table.PLAYERS, TablePlayers.STAFFRANK, new Where(TablePlayers.UUID, getUUID().toString())));
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

    public GameServer getServer() {
        updateIP();

        ip.updateCurrentServer();

        if (ip.getCurrentServer() != null) {
            return GameServer.getServer(ip.getCurrentServer());
        } else {
            /* Not Online, update last online */
            ip.update();
            return null;
        }
    }

    public PunishmentHandler getPunishmentHandler() {
        return PunishmentHandler.getHandler(uuid);
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
