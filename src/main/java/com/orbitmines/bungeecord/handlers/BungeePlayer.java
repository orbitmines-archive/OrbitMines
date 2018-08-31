package com.orbitmines.bungeecord.handlers;

import com.orbitmines.api.*;
import com.orbitmines.api.database.*;
import com.orbitmines.api.database.Set;
import com.orbitmines.api.database.tables.TablePlayers;
import com.orbitmines.api.database.tables.TableServers;
import com.orbitmines.api.punishment.Punishment;
import com.orbitmines.api.punishment.PunishmentHandler;
import com.orbitmines.api.punishment.offences.Offence;
import com.orbitmines.api.punishment.offences.Severity;
import com.orbitmines.api.settings.Settings;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.handlers.DiscordSquad;
import com.orbitmines.discordbot.utils.*;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.data.*;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;
import java.util.concurrent.TimeUnit;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class BungeePlayer {

    private static Map<UUID, BungeePlayer> players = new HashMap<>();

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
        players.put(getUUID(), this);

        if (!Database.get().contains(Table.PLAYERS, TablePlayers.UUID, new Where(TablePlayers.UUID, getUUID().toString()))) {
            Database.get().insert(Table.PLAYERS, getUUID().toString(), getRealName(), "", staffRank.toString(), vipRank.toString(), DateUtils.FORMAT.format(DateUtils.now()), language.toString(), "1", Settings.PRIVATE_MESSAGES.getDefaultType().toString(), Settings.PLAYER_VISIBILITY.getDefaultType().toString(), Settings.GADGETS.getDefaultType().toString(), Settings.STATS.getDefaultType().toString(), silent ? "1" : "0", "0", "0");

            onFirstLogin();
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
            if (!name.equals(getRealName())) {
                Database.get().update(Table.PLAYERS, new Set(TablePlayers.NAME, getRealName()), new Where(TablePlayers.UUID, getUUID().toString()));
                onNameChange(name, getRealName());
            }
        }

        /* PrefixColor to DisplayName */
        setPrefix(getRankPrefixColor().getChatColor());

        /* Update IP History */
        IP.update(getUUID(), player.getAddress().getHostString());

        updateLastOnline();

        /* Initiate Login */
        if (staffRank == StaffRank.NONE || staffRank == StaffRank.ADMIN || !bungee.mustLogin(this)) {
            on2FALogin();
            return;
        }

        bungee.getProxy().getScheduler().schedule(bungee, () -> {
            bungee.getMessageHandler().dataTransfer(PluginMessage.LOGIN_2FA, player, getUUID().toString());

            if (isMuted())
                muteOnSpigot(true);
        }, 1, TimeUnit.SECONDS);
    }

    public void logout() {
        /* Clear current server */
        IP ip = IP.update(getUUID(), player.getAddress().getHostString());
        ip.setCurrentServer(null);

        updateLastOnline();

        ((PlayTimeData) getData(Data.Type.PLAY_TIME)).stopSession();

        players.remove(getUUID());

        SkinLibrary.deleteExistingEmotes(bungee.getDiscord().getGuild(bungee.getToken()), getUUID());
    }

    /* Called when a player joins the network, or successfully logs in with their 2FA code */
    public void on2FALogin() {
        /* Send Title Announcements */
        bungee.getAnnouncementHandler().send(player);
    }

    private void onNameChange(String previousName, String newName) {
        /* Log Name Change */
        DiscordBot discord = bungee.getDiscord();
        BotToken token = bungee.getToken();
        discord.getChannel(token, DiscordBot.ChannelType.name_change).sendMessage(DiscordUtils.getDisplay(discord, token, staffRank, vipRank, getUUID(), previousName) + " » " + DiscordUtils.getDisplay(discord, token, staffRank, vipRank, getUUID(), newName)).queue();

        /* Update DiscordSquad Category */
        DiscordSquad group = DiscordSquad.getGroup(getUUID());
        if (group != null)
            group.updateCategoryName();
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

    private void onFirstLogin() {
        /* Give Welcome Loot */
        Database.get().insert(Table.LOOT, getUUID().toString(), "SOLARS", Rarity.RARE.toString(), "250", "&a&l&oWelcome to &7&l&oOrbit&8&l&oMines&a&l&o!");

        DiscordBot discord = bungee.getDiscord();
        Guild guild = discord.getGuild(bungee.getToken());

        bungee.getProxy().getScheduler().schedule(bungee, () -> discord.getChannel(bungee.getToken(), DiscordBot.ChannelType.new_players).sendMessage(SkinLibrary.getEmote(guild, getUUID()).getAsMention() + " **" + getName(true) + "** has joined OrbitMines for the first time!").queue(), 1, TimeUnit.SECONDS);
    }

    /*


        Connection


     */

    public void connect(Server server, boolean notify) {
        ServerInfo serverInfo = bungee.getServer(server);

        if (!isLoggedIn()) {
            return;
        } else if (server.getStatus() == Server.Status.OFFLINE) {
            sendMessage("Server", Color.RED, "§7Die server is " + Server.Status.OFFLINE.getDisplayName() + "§7!", "§7That server is " + Server.Status.OFFLINE.getDisplayName() + "§7!");
            return;
        } else if (serverInfo == null) {
            sendMessage("Server", Color.RED, "§7Error met het verbinden van die server.", "§7Error while connecting to that server.");
            return;
        } else if (getServer() == server) {
            sendMessage("Server", Color.BLUE, "§7Je zit al op die server.", "§7You're already on that server.");
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

        /* Update on Discord if Linked */
        DiscordBot discord = bungee.getDiscord();
        User user = discord.getLinkedUser(bungee.getToken(), getUUID());

        if (user != null)
            discord.updateRanks(user);

        DiscordSquad group = DiscordSquad.getGroup(getUUID());
        if (group != null)
            group.updateCategoryName();
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

    public void punish(UUID uuid, Offence offence, Severity severity, String reason) {
        CachedPlayer player = CachedPlayer.getPlayer(uuid);
        String color = player.getRankPrefixColor().getChatColor();

        PunishmentHandler handler = PunishmentHandler.getHandler(uuid);

        if (handler.getActivePunishment(offence.getType()) != null) {
            sendMessage("Mod", Color.RED, color + player.getPlayerName() + " §7heeft al een lopende straf!", color + player.getPlayerName() + " §7has already been punished!");
            return;
        }

        sendMessage("Mod", Color.LIME, "Je hebt " + color + player.getPlayerName() + " §7succesvol een straf opgedragen. (Reden: " + reason + ")", "You have successfully punished " + color + player.getPlayerName() + "§7. (Reason: " + reason + ")");

        Punishment punishment = new Punishment(uuid, offence, severity, handler.getPunishedTo(offence, severity), getUUID(), reason);
        handler.addPunishment(punishment);

        BungeePlayer mbp = BungeePlayer.getPlayer(uuid);

        if (severity == Severity.WARNING) {
            if (mbp != null) {
                mbp.sendMessage("§4§m---------------------------------------------");
                mbp.sendMessage("  §c§l" + mbp.lang("WAARSCHUWING", "WARNING"));
                mbp.sendMessage("     §7" + mbp.lang("Je bent gewaarschuwd door", "You have been warned by") + " " + getRankPrefix() + getName() + "§7.");
                mbp.sendMessage("     §7" + mbp.lang("Overtreding", "Offence") + ": §c" + mbp.lang(offence.getName()));
                mbp.sendMessage("     §7" + mbp.lang("Reden", "Reason") + ": §c" + reason);
                mbp.sendMessage("§4§m---------------------------------------------");
            } else {
                //TODO MESSAGE WHEN BACK ONLINE
            }

            getPunishmentChannel().sendMessage(DiscordUtils.getDisplay(bungee.getDiscord(), bungee.getToken(), player.getUUID()) + " has been WARNED!").queue();
            {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setAuthor("WARNING");
                builder.setDescription("");
                builder.setColor(ColorUtils.from(Color.GRAY));

                builder.addField("Player", punishment.getPunished().getPlayerName(), true);
                builder.addField("Offence", punishment.getOffence().getName().lang(Language.ENGLISH), true);
                builder.addField("Warned By", punishment.getPunishedBy().getPlayerName(), true);
                builder.addField("Reason", punishment.getReason(), true);

                builder.setThumbnail(SkinLibrary.getSkinUrl(SkinLibrary.Type.BODY_3D, player.getUUID()) + "/");

                getPunishmentChannel().sendMessage(builder.build()).queue();
            }
            return;
        }

        switch (offence.getType()) {

            case MUTE:
                if (mbp != null) {
                    mbp.sendMessage("§4§m---------------------------------------------");
                    mbp.sendMessage("  §c§l" + mbp.lang("GEMUTE", "MUTED"));
                    mbp.sendMessage("     §7" + mbp.lang("Je bent gemute door", "You have been muted by") + " " + getRankPrefix() + getName() + "§7.");
                    mbp.sendMessage("     §7" + mbp.lang("Reden", "Reason") + ": §c" + reason);
                    mbp.sendMessage("     §7" + mbp.lang("Duur", "Duration") + ": §c" + (severity.getDuration() == Punishment.Duration.PERMANENT ? "§lPERMANENT" : punishment.getExpireInString(mbp.getLanguage())));
                    mbp.sendMessage("§4§m---------------------------------------------");
                    mbp.muteOnSpigot(true);
                } else {
                    //TODO MESSAGE WHEN BACK ONLINE
                }

                getPunishmentChannel().sendMessage(DiscordUtils.getDisplay(bungee.getDiscord(), bungee.getToken(), player.getUUID()) + " has been MUTED!").queue();

                break;
            case BAN:
                if (mbp != null) {
                    mbp.getPlayer().disconnect(punishment.getBanString(mbp.getLanguage()));
                } else {
                    //TODO MESSAGE WHEN BACK ONLINE
                }

                getPunishmentChannel().sendMessage(DiscordUtils.getDisplay(bungee.getDiscord(), bungee.getToken(), player.getUUID()) + " has been BANNED!").queue();
                break;
        }

        {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setAuthor(offence.getType() == Punishment.Type.MUTE ? "MUTE" : "BAN");
            builder.setDescription("");
            builder.setColor(ColorUtils.from(offence.getType() == Punishment.Type.MUTE ? Color.RED : Color.MAROON));

            builder.addField("Player", punishment.getPunished().getPlayerName(), true);
            builder.addField("Offence", punishment.getOffence().getName().lang(Language.ENGLISH), true);
            builder.addField("Severity", punishment.getSeverity().getName().lang(Language.ENGLISH), true);
            builder.addField("From", punishment.getFromString(DateUtils.FORMAT), true);

            if (punishment.getSeverity().getDuration() != Punishment.Duration.PERMANENT)
                builder.addField("To", punishment.getFromString(DateUtils.FORMAT), true);

            builder.addField("Duration", punishment.getSeverity().getDuration() == Punishment.Duration.PERMANENT ? "PERMANENT" : punishment.getExpireInString(Language.ENGLISH), true);
            builder.addField((offence.getType() == Punishment.Type.MUTE ? "Muted" : "Banned") + " By", punishment.getPunishedBy().getPlayerName(), true);
            builder.addField("Reason", punishment.getReason(), true);

            builder.setThumbnail(SkinLibrary.getSkinUrl(SkinLibrary.Type.BODY_3D, player.getUUID()) + "/");

            getPunishmentChannel().sendMessage(builder.build()).queue();
        }
    }

    public void pardon(UUID uuid, Offence offence, String reason) {
        CachedPlayer player = CachedPlayer.getPlayer(uuid);
        String color = player.getRankPrefixColor().getChatColor();

        PunishmentHandler handler = PunishmentHandler.getHandler(uuid);
        Punishment active = handler.getActivePunishment(offence.getType());

        if (active == null) {
            sendMessage("Mod", Color.BLUE, color + player.getPlayerName() + " §7heeft geen lopende straf meer!", color + player.getPlayerName() + " §7has already been pardoned!");
            return;
        }

        active.pardon(getUUID(), reason);

        sendMessage("Mod", Color.LIME, "Je hebt " + color + player.getPlayerName() + "§7 succesvol ontzegd van zijn/haar straf. (Reden: " + reason + ")", "You have successfully pardoned " + color + player.getPlayerName() + "§7. (Reason: " + reason + ")");

        getPunishmentChannel().sendMessage(DiscordUtils.getDisplay(bungee.getDiscord(), bungee.getToken(), player.getUUID()) + " has been PARDONED!").queue();

        {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setAuthor("PARDON");
            builder.setDescription("");
            builder.setColor(ColorUtils.from(Color.LIME));

            builder.addField("Player", active.getPunished().getPlayerName(), true);
            builder.addField("Offence", active.getOffence().getName().lang(Language.ENGLISH), true);
            builder.addField("Severity", active.getSeverity().getName().lang(Language.ENGLISH), true);
            builder.addField("From", active.getFromString(DateUtils.FORMAT), true);

            if (active.getSeverity().getDuration() != Punishment.Duration.PERMANENT)
                builder.addField("To", active.getFromString(DateUtils.FORMAT), true);

            builder.addField("Would have expired in", active.getSeverity().getDuration() == Punishment.Duration.PERMANENT ? "NEVER" : active.getExpireInString(Language.ENGLISH), true);
            builder.addField((active.getSeverity() == Severity.WARNING ? "Warned" : (offence.getType() == Punishment.Type.MUTE ? "Muted" : "Banned")) + " By", active.getPunishedBy().getPlayerName(), true);
            builder.addField("Reason", active.getReason(), true);
            builder.addField("Pardoned Reason", active.getPardonedReason(), true);
            builder.addField("Pardoned On", active.getPardonedOnString(DateUtils.FORMAT), true);
            builder.addField("Pardoned By", active.getPardonedBy().getPlayerName(), true);

            builder.setThumbnail(SkinLibrary.getSkinUrl(SkinLibrary.Type.BODY_3D, player.getUUID()) + "/");

            getPunishmentChannel().sendMessage(builder.build()).queue();
        }
    }

    private TextChannel getPunishmentChannel() {
        return bungee.getDiscord().getChannel(bungee.getToken(), DiscordBot.ChannelType.punishments);
    }

    public boolean isMuted() {
        return PunishmentHandler.getHandler(getUUID()).getActivePunishment(Punishment.Type.MUTE) != null;
    }

    public void muteOnSpigot(boolean mute) {
        bungee.getMessageHandler().dataTransfer(PluginMessage.MUTE, player, getUUID().toString(), mute + "");
    }

    public BungeePlayer getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(BungeePlayer lastMsg) {
        this.lastMsg = lastMsg;
    }

    public boolean hasLastMsg() {
        if (lastMsg != null && !lastMsg.getPlayer().isConnected()) {
            lastMsg = null;
            return false;
        }
        return lastMsg != null;
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
            case SETTINGS:
                data = new SettingsData(getUUID());
                break;
            case PLAY_TIME:
                data = new PlayTimeData(getUUID());
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
        if (player.getServer() == null)//TODO NULLPOINTER FIX?
            return Server.HUB;

        return bungee.getServer(player.getServer().getInfo());
    }

    public String getRankPrefix() {
        return (staffRank != StaffRank.NONE && staffRank != StaffRank.ADMIN) ? staffRank.getPrefix() : vipRank.getPrefix();
    }

    public String getRankPrefix(Color color) {
        return (staffRank != StaffRank.NONE && staffRank != StaffRank.ADMIN) ? staffRank.getPrefix(color) : vipRank.getPrefix(color);
    }

    public String getRankName() {
        return (staffRank != StaffRank.NONE && staffRank != StaffRank.ADMIN) ? staffRank.getName() : vipRank.getName();
    }

    public String getRankDisplayName() {
        return (staffRank != StaffRank.NONE && staffRank != StaffRank.ADMIN) ? staffRank.getDisplayName() : vipRank.getDisplayName();
    }

    public Color getRankPrefixColor() {
        return (staffRank != StaffRank.NONE && staffRank != StaffRank.ADMIN) ? staffRank.getPrefixColor() : vipRank.getPrefixColor();
    }

    public Color getRankChatColor() {
        return (staffRank != StaffRank.NONE && staffRank != StaffRank.ADMIN) ? staffRank.getChatColor() : vipRank.getChatColor();
    }

    /*


        BungeePlayer Getters


     */

    public static BungeePlayer getPlayer(ProxiedPlayer player) {
        if (players.containsKey(player.getUniqueId()))
            return players.get(player.getUniqueId());

        BungeePlayer omp = new BungeePlayer(player);
        omp.login();
        return omp;
    }

    public static BungeePlayer getPlayer(String name) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);
        if (player == null)
            return null;

        return getPlayer(player);
    }

    public static BungeePlayer getPlayer(UUID uuid) {
        if (players.containsKey(uuid))
            return players.get(uuid);

        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
        if (player == null)
            return null;

        BungeePlayer omp = new BungeePlayer(player);
        omp.login();
        return omp;
    }

    public static Collection<BungeePlayer> getPlayers() {
        return players.values();
    }

    public static List<BungeePlayer> getPlayers(Server server) {
        List<BungeePlayer> list = new ArrayList<>();

        for (BungeePlayer bungeePlayer : players.values()) {
            if (bungeePlayer.getServer() == server)
                list.add(bungeePlayer);
        }

        return list;
    }
}
