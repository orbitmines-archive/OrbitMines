package com.orbitmines.api;

import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.TablePlayers;
import com.orbitmines.api.punishment.PunishmentHandler;
import com.orbitmines.api.utils.uuid.UUIDUtils;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.utils.BotToken;
import net.dv8tion.jda.core.entities.User;

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

    //TODO When player disguises are added; this should return the preferred display name.
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

    public Language getLanguage() {
        return Language.valueOf(Database.get().getString(Table.PLAYERS, TablePlayers.LANGUAGE, new Where(TablePlayers.UUID, getUUID().toString())));
    }

    public String getFirstLogin() {
        return Database.get().getString(Table.PLAYERS, TablePlayers.FIRST_LOGIN, new Where(TablePlayers.UUID, getUUID().toString()));
    }

    public Long getDiscordId() {
        return DiscordBot.getInstance().getLinkedId(uuid);
    }

    public User getDiscordUser(BotToken token) {
        return DiscordBot.getInstance().getUserById(token, getDiscordId());
    }

    public String getLastOnline() {
        updateIP();
        ip.updateLastLogin();

        return ip == null ? null : ip.getLastLogin();
    }

    public String getLastOnlineInTimeUnit(Language language) {
        updateIP();
        ip.updateLastLogin();

        return ip == null ? null : ip.getLastLoginInTimeUnit(language);
    }

    public Server getServer() {
        updateIP();

        /* If we somehow can't receive the player's IP address we return null */
        if (ip == null)
            return null;

        ip.updateCurrentServer();

        if (ip.getCurrentServer() != null) {
            return Server.valueOf(ip.getCurrentServer());
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

    public String getRankPrefix() {
        StaffRank staffRank = getStaffRank();
        return staffRank != StaffRank.NONE ? staffRank.getPrefix() : getVipRank().getPrefix();
    }

    public String getRankPrefix(Color color) {
        StaffRank staffRank = getStaffRank();
        return staffRank != StaffRank.NONE ? staffRank.getPrefix(color) : getVipRank().getPrefix(color);
    }

    public String getRankName() {
        StaffRank staffRank = getStaffRank();
        return staffRank != StaffRank.NONE ? staffRank.getName() : getVipRank().getName();
    }

    public String getRankDisplayName() {
        StaffRank staffRank = getStaffRank();
        return staffRank != StaffRank.NONE ? staffRank.getDisplayName() : getVipRank().getDisplayName();
    }

    public Color getRankPrefixColor() {
        StaffRank staffRank = getStaffRank();
        return staffRank != StaffRank.NONE ? staffRank.getPrefixColor() : getVipRank().getPrefixColor();
    }

    public Color getRankChatColor() {
        StaffRank staffRank = getStaffRank();
        return staffRank != StaffRank.NONE ? staffRank.getChatColor() : getVipRank().getChatColor();
    }

    public static CachedPlayer getPlayer(UUID uuid) {
        if (cached.containsKey(uuid))
            return cached.get(uuid);

        return Database.get().contains(Table.PLAYERS, TablePlayers.UUID, new Where(TablePlayers.UUID, uuid.toString())) ? new CachedPlayer(uuid) : null;
    }

    public static CachedPlayer getPlayer(String playerName) {
        UUID uuid = UUIDUtils.getUUIDFromDatabase(playerName);

        return uuid != null ? getPlayer(uuid) : null;
    }
}
