package com.orbitmines.bungeecord.handlers;

import com.orbitmines.api.*;
import com.orbitmines.api.database.*;
import com.orbitmines.api.database.Set;
import com.orbitmines.api.database.tables.TablePlayers;
import com.orbitmines.api.database.tables.TableServers;
import com.orbitmines.api.settings.SettingsType;
import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.data.SettingsData;
import com.orbitmines.spigot.api.handlers.data.VoteData;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;
import java.util.concurrent.TimeUnit;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class BungeePlayer {

    private static List<BungeePlayer> players = new ArrayList<>();

    private OrbitMinesBungee bungee;
    private final ProxiedPlayer player;
    private final DisplayName displayName;

    private boolean loggedIn;

    private StaffRank staffRank;
    private VipRank vipRank;
    private Language language;
    private boolean silent;

    protected Map<Data.Type, Data> data;

    private BungeePlayer lastMsg;

    public BungeePlayer(ProxiedPlayer player) {
        bungee = OrbitMinesBungee.getBungee();
        this.player = player;
        this.displayName = new DisplayName(player.getName());

        this.loggedIn = true;

        this.staffRank = StaffRank.NONE;
        this.vipRank = VipRank.NONE;
        this.language = Language.ENGLISH;
        this.silent = false;

        this.data = new HashMap<>();
    }

    /*


        Login / Logout


    */

    public void login() {
        players.add(this);

        if (!Database.get().contains(Table.PLAYERS, TablePlayers.UUID, new Where(TablePlayers.UUID, getUUID().toString()))) {
            Database.get().insert(Table.PLAYERS, Table.PLAYERS.values(getUUID().toString(), getRealName(), staffRank.toString(), vipRank.toString(), language.toString(), SettingsType.ENABLED.toString(), SettingsType.ENABLED.toString(), SettingsType.ENABLED.toString(), silent ? "1" : "0", "0", "0", "null"));
        } else {
            Map<Column, String> values = Database.get().getValues(Table.PLAYERS, new Column[]{
                    TablePlayers.NAME,
                    TablePlayers.STAFFRANK,
                    TablePlayers.VIPRANK,
                    TablePlayers.LANGUAGE,
                    TablePlayers.SILENT,
            }, new Where(TablePlayers.UUID, getUUID().toString()));

            staffRank = StaffRank.valueOf(values.get(TablePlayers.STAFFRANK));
            vipRank = VipRank.valueOf(values.get(TablePlayers.VIPRANK));
            language = Language.valueOf(values.get(TablePlayers.LANGUAGE));
            silent = "1".equals(values.get(TablePlayers.SILENT));

               /* Update Name Change */
            String name = values.get(TablePlayers.NAME);
            if (!name.equals(getRealName()))
                Database.get().update(Table.PLAYERS, new Set(TablePlayers.NAME, getRealName()), new Where(TablePlayers.UUID, getUUID().toString()));

        }

        /* PrefixColor to DisplayName */
        setPrefix(getRankPrefixColor().getChatColor());

        /* Update IP History */
        IP.update(getUUID(), player.getAddress().getHostString());

        updateLastOnline();

        /* Initiate Login */
        if (staffRank == StaffRank.NONE || !bungee.mustLogin(this)) {
            on2FALogin();
            return;
        }

        loggedIn = false;

        bungee.getProxy().getScheduler().schedule(bungee, () -> bungee.getMessageHandler().dataTransfer(PluginMessage.LOGIN_2FA, player, getUUID().toString()), 1, TimeUnit.SECONDS);
    }

    public void logout() {
        /* Clear current server */
        IP ip = IP.update(getUUID(), player.getAddress().getHostString());
        ip.setCurrentServer(null);

        updateLastOnline();

        players.remove(this);
    }

    /* Called when a player joins the network, or successfully logs in with their 2FA code */
    public void on2FALogin() {
        /* Send Title Announcements */
        bungee.getAnnouncementHandler().send(player);
    }

    public void updateLastOnline() {
        IP ip = IP.getIp(getUUID());
        if (ip == null) {
            new IP(getUUID(), player.getAddress().getHostString()).insert();
            return;
        }

        ip.set(player.getAddress().getHostString());
        ip.update();
    }

    /*


        Connection


     */

    public void connect(Server server, boolean notify) {
        ServerInfo serverInfo = bungee.getServer(server);

        if (!isLoggedIn()) {
            return;
        } else if (server.getStatus() == Server.Status.OFFLINE) {
            sendMessage("Server", Color.RED, "§7Die server is " + Server.Status.OFFLINE.getColor().getChatColor() + Server.Status.OFFLINE.getName() + "§7!", "§7That server is " + Server.Status.OFFLINE.getColor().getChatColor() + Server.Status.OFFLINE.getName() + "§7!");
            return;
        } else if (serverInfo == null) {
            sendMessage("Server", Color.RED, "§7Error met het verbinden van die server.", "§7Error while connecting to that server.");
            return;
        } else if (player.getServer().getInfo().getName().equals(serverInfo.getName())) {
            /* Already connected to this server */
            return;
        } else if (server.getMaxPlayers() - serverInfo.getPlayers().size() < 1) {
            sendMessage("Server", Color.RED, "§7Die server is vol!", "§7That server is full!");
            return;
        } else if (!isEligible(StaffRank.DEVELOPER) && server.getStatus() == Server.Status.MAINTENANCE) {
            sendMessage("Server", Color.FUCHSIA, "§7Er word momenteel aan die server gewerkt.", "§7That server is currently under maintenance.");
            return;
        }

        player.connect(serverInfo);

        if (notify)
            sendMessage("Server", Color.LIME, "§7Verbinden met §c" + server.getDisplayName() + "§7...", "§7Connecting to §c" + server.getDisplayName() + "§7...");
    }

    public ServerInfo getFallBackServer() {
        ServerInfo serverInfo = bungee.getServer(Server.HUB);

        /* Try to connect to the Hub */
        if (serverInfo != null && Server.HUB.getStatus() == Server.Status.ONLINE)
            return serverInfo;

        /* Hub server is not online, join a random server */
        List<Map<Column, String>> entries = Database.get().getEntries(Table.SERVERS, TableServers.SERVER, new Where(TableServers.STATUS, Server.Status.ONLINE.toString()));
        if (entries.size() == 0)
            return null;

        return bungee.getServer(Server.valueOf(RandomUtils.randomFrom(entries).get(TableServers.SERVER)));
    }

    /*


        Getters & Setters


     */

    /*
        Player
     */

    public ProxiedPlayer getPlayer() {
        return player;
    }

    /*
        Login
     */

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;

        if (loggedIn)
            on2FALogin();
    }

    /*
        Ranks
     */

    public StaffRank getStaffRank() {
        return staffRank;
    }

    public void setStaffRank(StaffRank staffRank) {
        Database.get().update(Table.PLAYERS, new Set(TablePlayers.STAFFRANK, staffRank.toString()), new Where(TablePlayers.UUID, getUUID().toString()));
        bungee.getMessageHandler().dataTransfer(PluginMessage.UPDATE_RANKS, player, getUUID().toString());

        /* Trigger #updateRanks for subclasses */
        updateRanks();
    }

    public boolean isEligible(StaffRank staffRank) {
        return this.staffRank.ordinal() >= staffRank.ordinal();
    }

    public VipRank getVipRank() {
        return vipRank;
    }

    public void setVipRank(VipRank vipRank) {
        Database.get().update(Table.PLAYERS, new Set(TablePlayers.VIPRANK, vipRank.toString()), new Where(TablePlayers.UUID, getUUID().toString()));
        bungee.getMessageHandler().dataTransfer(PluginMessage.UPDATE_RANKS, player, getUUID().toString());

        /* Trigger #updateRanks */
        updateRanks();
    }

    public boolean isEligible(VipRank vipRank) {
        return this.vipRank.ordinal() >= vipRank.ordinal() || isEligible(StaffRank.OWNER);
    }

    public void updateRanks() {
        Map<Column, String> values = Database.get().getValues(Table.PLAYERS, new Column[]{
                TablePlayers.STAFFRANK,
                TablePlayers.VIPRANK,
        }, new Where(TablePlayers.UUID, getUUID().toString()));

        staffRank = StaffRank.valueOf(values.get(TablePlayers.STAFFRANK));
        vipRank = VipRank.valueOf(values.get(TablePlayers.VIPRANK));

        /* PrefixColor to DisplayName */
        setPrefix(getRankPrefixColor().getChatColor());
    }

    /*
        Language
     */

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        Database.get().update(Table.PLAYERS, new Set(TablePlayers.LANGUAGE, language.toString()), new Where(TablePlayers.UUID, getUUID().toString()));
        bungee.getMessageHandler().dataTransfer(PluginMessage.UPDATE_LANGUAGE, player, getUUID().toString());

        /* Trigger #updateLanguage */
        updateLanguage();
    }

    public void updateLanguage() {
        this.language = Language.valueOf(Database.get().getString(Table.PLAYERS, TablePlayers.LANGUAGE, new Where(TablePlayers.UUID, getUUID().toString())));
    }

    /*
        Silent
     */

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        Database.get().update(Table.PLAYERS, new Set(TablePlayers.SILENT, silent), new Where(TablePlayers.UUID, getUUID().toString()));
        bungee.getMessageHandler().dataTransfer(PluginMessage.UPDATE_SILENT, player, getUUID().toString());

        /* Trigger #updateSilent */
        updateSilent();
    }

    public void updateSilent() {
        this.silent = Database.get().getBoolean(Table.PLAYERS, TablePlayers.SILENT, new Where(TablePlayers.UUID, getUUID().toString()));
    }

    /*
        Data
     */

    public Data getData(Data.Type type) {
        if (data.containsKey(type))
            return data.get(type);

        Data data;
        switch (type) {

            case VOTES:
                data = new VoteData(getUUID());
                break;
//            case FRIENDS:
//                data = new FriendsData(getUUID());
//                break; -> Not avaiable in Bungee
            case SETTINGS:
                data = new SettingsData(getUUID());
                break;
            default:
                return null;
        }

        data.load();
        this.data.put(type, data);

        return data;
    }

    /*


        DisplayName


     */

    public String getRealName() {
        return displayName.getInitialName();
    }

    public String getName() {
        return getName(false);
    }

    public String getName(boolean onlyName) {
        return displayName.getDisplayName(onlyName);
    }

    public void setName(String name) {
        displayName.setName(name);
    }

    public String getPrefix() {
        return displayName.getPrefix();
    }

    public void setPrefix(String prefix) {
        displayName.setPrefix(prefix);
    }

    public String getSuffix() {
        return displayName.getSuffix();
    }

    public void setSuffix(String suffix) {
        displayName.setSuffix(suffix);
    }


    /*


        Shortcuts


     */

    public void sendMessage(String... messages) {
        sendMessage(new Message(messages));
    }

    public void sendMessage(String prefix, Color prefixColor, String... messages) {
        sendMessage(new Message(prefix, prefixColor, messages));
    }

    public void sendMessage(Message message) {
        if (!isLoggedIn() && message.getPrefix() != null && !message.getPrefix().equals(Message.PREFIX_2FA))
            return;

        player.sendMessage(message.lang(language));
    }

    public String lang(String... messages) {
        return lang(new Message(messages));
    }

    public String lang(String prefix, Color prefixColor, String... messages) {
        return lang(new Message(prefix, prefixColor, messages));
    }

    public String lang(Message message) {
        return message.lang(language);
    }

    public void sendMessage(TextComponent textComponent) {
        if (!isLoggedIn())
            return;

        player.sendMessage(textComponent);
    }

    public UUID getUUID() {
        return player.getUniqueId();
    }

    public Server getServer() {
        return bungee.getServer(player.getServer().getInfo());
    }

    public String getRankPrefix() {
        return staffRank != StaffRank.NONE ? staffRank.getPrefix() : vipRank.getPrefix();
    }

    public String getRankPrefix(Color color) {
        return staffRank != StaffRank.NONE ? staffRank.getPrefix(color) : vipRank.getPrefix(color);
    }

    public String getRankName() {
        return staffRank != StaffRank.NONE ? staffRank.getName() : vipRank.getName();
    }

    public String getRankDisplayName() {
        return staffRank != StaffRank.NONE ? staffRank.getDisplayName() : vipRank.getDisplayName();
    }

    public Color getRankPrefixColor() {
        return staffRank != StaffRank.NONE ? staffRank.getPrefixColor() : vipRank.getPrefixColor();
    }

    public Color getRankChatColor() {
        return staffRank != StaffRank.NONE ? staffRank.getChatColor() : vipRank.getChatColor();
    }

    /*


        BungeePlayer Getters


     */

    public static BungeePlayer getPlayer(ProxiedPlayer player) {
        for (BungeePlayer omp : players) {
            if (omp.getPlayer() == player)
                return omp;
        }
        throw new IllegalStateException();
    }

    public static BungeePlayer getPlayer(String name) {
        for (BungeePlayer omp : players) {
            if (omp.getName(true).equalsIgnoreCase(name))
                return omp;
        }
        throw new IllegalStateException();
    }

    public static BungeePlayer getPlayer(UUID uuid) {
        for (BungeePlayer omp : players) {
            if (omp.getUUID().toString().equals(uuid.toString()))
                return omp;
        }
        throw new IllegalStateException();
    }

    public static List<BungeePlayer> getPlayers() {
        return players;
    }


    public static List<BungeePlayer> getPlayers(Server server) {
        List<BungeePlayer> list = new ArrayList<>();

        for (BungeePlayer bungeePlayer : players) {
            if (bungeePlayer.getServer() == server)
                list.add(bungeePlayer);
        }

        return list;
    }
}
