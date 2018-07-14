package com.orbitmines.spigot.api.handlers;

import com.orbitmines.api.Color;
import com.orbitmines.api.*;
import com.orbitmines.api.Server;
import com.orbitmines.api.database.*;
import com.orbitmines.api.database.Set;
import com.orbitmines.api.database.tables.TablePlayers;
import com.orbitmines.api.settings.Settings;
import com.orbitmines.api.settings.SettingsType;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.utils.SkinLibrary;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.Freezer;
import com.orbitmines.spigot.api.Loot;
import com.orbitmines.spigot.api.handlers.chat.Title;
import com.orbitmines.spigot.api.handlers.data.*;
import com.orbitmines.spigot.api.handlers.itembuilders.PotionBuilder;
import com.orbitmines.spigot.api.handlers.itemhandlers.ItemHover;
import com.orbitmines.spigot.api.handlers.kit.KitInteractive;
import com.orbitmines.spigot.api.handlers.leaderboard.LeaderBoard;
import com.orbitmines.spigot.api.handlers.leaderboard.hologram.PlayerHologramLeaderBoard;
import com.orbitmines.spigot.api.handlers.npc.MobNpc;
import com.orbitmines.spigot.api.handlers.npc.Npc;
import com.orbitmines.spigot.api.handlers.npc.PersonalisedMobNpc;
import com.orbitmines.spigot.api.handlers.npc.PlayerFreezer;
import com.orbitmines.spigot.api.handlers.scoreboard.OMScoreboard;
import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardSet;
import com.orbitmines.spigot.api.handlers.timer.Timer;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalData;
import net.dv8tion.jda.core.entities.Guild;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public abstract class OMPlayer {

    protected static List<OMPlayer> players = new ArrayList<>();

    protected OrbitMines orbitMines;
    protected final Player player;
    protected final DisplayName displayName;

    private boolean loggedIn;

    private boolean opMode;

    protected StaffRank staffRank;
    protected VipRank vipRank;
    protected Language language;
    protected boolean muted;
    protected boolean silent;
    protected int solars;
    protected int prisms;

    protected String afk;

    protected OMScoreboard scoreboard;
    protected GUI lastInventory;
    protected KitInteractive lastInteractiveKit;
    protected ItemHover currentHover;

    protected Teleportable teleportingTo;
    protected Timer teleportingTimer;

    protected Map<Cooldown, Long> cooldowns;
    protected Map<Data.Type, Data> data;

    private Freezer freezer;

    public OMPlayer(Player player) {
        orbitMines = OrbitMines.getInstance();
        this.player = player;
        this.displayName = new DisplayName(player.getName());

        this.loggedIn = true;

        this.opMode = false;

        this.staffRank = StaffRank.NONE;
        this.vipRank = VipRank.NONE;
        this.language = Language.ENGLISH;
        this.muted = false;
        this.silent = false;
        this.solars = 0;
        this.prisms = 0;

        this.scoreboard = new OMScoreboard(this);

        this.cooldowns = new HashMap<>();
        this.data = new HashMap<>();
    }

    /* Called after the player logs in */
    protected abstract void onLogin();

    /* Called before the player logs out */
    protected abstract void onLogout();

    /* Called when a player receives knock back, example: gadgets */
    public abstract boolean canReceiveVelocity();

    /*


        Login / Logout


     */

    public void login() {
        players.add(this);

        /* Update Players */
        orbitMines.getServerHandler().getServer().setPlayers(Bukkit.getOnlinePlayers().size());

        if (!Database.get().contains(Table.PLAYERS, TablePlayers.UUID, new Where(TablePlayers.UUID, getUUID().toString()))) {
            Database.get().insert(Table.PLAYERS, getUUID().toString(), getRealName(), staffRank.toString(), vipRank.toString(), DateUtils.FORMAT.format(DateUtils.now()), language.toString(), SettingsType.ENABLED.toString(), SettingsType.ENABLED.toString(), SettingsType.ENABLED.toString(), silent ? "1" : "0", solars + "", prisms + "");
        } else {
            Map<Column, String> values = Database.get().getValues(Table.PLAYERS, new Column[]{
                    TablePlayers.STAFFRANK,
                    TablePlayers.VIPRANK,
                    TablePlayers.LANGUAGE,
                    TablePlayers.SILENT,
                    TablePlayers.SOLARS,
                    TablePlayers.PRISMS,
            }, new Where(TablePlayers.UUID, getUUID().toString()));

            staffRank = StaffRank.valueOf(values.get(TablePlayers.STAFFRANK));
            vipRank = VipRank.valueOf(values.get(TablePlayers.VIPRANK));
            language = Language.valueOf(values.get(TablePlayers.LANGUAGE));
            silent = "1".equals(values.get(TablePlayers.SILENT));
            solars = Integer.parseInt(values.get(TablePlayers.SOLARS));
            prisms = Integer.parseInt(values.get(TablePlayers.PRISMS));
        }

        /* Set Op */
        player.setOp(false);

        /* PrefixColor to DisplayName */
        setPrefix(getRankPrefixColor().getChatColor());

        /* Default TabList */
        defaultTabList();

        /* Spawn Leaderboards */
        for (LeaderBoard leaderBoard : LeaderBoard.getLeaderBoards()) {
            if (leaderBoard instanceof PlayerHologramLeaderBoard)
                ((PlayerHologramLeaderBoard) leaderBoard).onLogin(this);
        }

        /* Spawn Personalised MobNpcs */
        for (MobNpc npc : MobNpc.getMobNpcs()) {
            if (npc instanceof PersonalisedMobNpc)
                ((PersonalisedMobNpc) npc).onLogin(this);
        }

        /* Initiate server login */
        onLogin();

        /* Join Message */
        if (silent) {
            orbitMines.broadcast(StaffRank.MODERATOR, " §a» " + getName() + "§a is gejoind. §7§o[Silent]", " §a» " + getName() + "§a has joined. §7§o[Silent]");
        } else {
            orbitMines.broadcast(" §a» " + getName() + "§a is gejoind.", " §a» " + getName() + "§a has joined.");

            DiscordBot discord = orbitMines.getServerHandler().getDiscord();
            Guild guild = discord.getGuild(orbitMines.getServerHandler().getToken());
            orbitMines.getServerHandler().getDiscordChannel().sendMessage(guild.getRolesByName("»", true).get(0).getAsMention() + " " + SkinLibrary.getEmote(guild, getUUID()).getAsMention() + orbitMines.getServerHandler().getDiscordRankPrefix(this) + " **" + getName(true) + "** has joined.").queue();
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline())
                    return;

                /* Hide other npcs that shouldn't be visible */
                for (Npc npc : Npc.getNpcs()) {
                    if (npc.getWatchers() != null && !npc.getWatchers().contains(player))
                        npc.hideFor(player);
                }

                /* Update current server */
                IP ip = IP.getIp(player.getUniqueId());
                ip.updateCurrentServer();

                /* Just joined the network, send message to all favorite friends. */
                if (ip.getCurrentServer() == null)
                    ((FriendsData) getData(Data.Type.FRIENDS)).sendJoinMessage(OMPlayer.this);

                ip.setCurrentServer(orbitMines.getServerHandler().getServer());

                orbitMines.getServerHandler().getMessageHandler().dataTransfer(PluginMessage.SERVER_SWITCH, getUUID().toString(), orbitMines.getServerHandler().getServer().toString());

                if (isLoggedIn())
                    on2FALogin();
            }
        }.runTaskLater(orbitMines, 10);
    }

    public void logout() {
        /* Initiate server logout */
        onLogout();

        /* Quit Message */
        if (silent) {
            orbitMines.broadcast(StaffRank.MODERATOR, " §c« " + getName() + "§c is weggegaan. §7§o[Silent]", " §c« " + getName() + "§c has left. §7§o[Silent]");
        } else {
            orbitMines.broadcast(" §c« " + getName() + "§c is weggegaan.", " §c« " + getName() + "§c has left.");

            DiscordBot discord = orbitMines.getServerHandler().getDiscord();
            Guild guild = discord.getGuild(orbitMines.getServerHandler().getToken());
            orbitMines.getServerHandler().getDiscordChannel().sendMessage(guild.getRolesByName("«", true).get(0).getAsMention() + " " + SkinLibrary.getEmote(guild, getUUID()).getAsMention() + orbitMines.getServerHandler().getDiscordRankPrefix(this) + " **" + getName(true) + "** has left.").queue();
        }

        orbitMines.getServerHandler().getServer().setPlayers(Bukkit.getOnlinePlayers().size() -1);

        /* Cancel 2FA Timeout */
        orbitMines.get2FA().onLogout(this);

        /* Destroy Leaderboards */
        for (LeaderBoard leaderBoard : LeaderBoard.getLeaderBoards()) {
            if (leaderBoard instanceof PlayerHologramLeaderBoard)
                ((PlayerHologramLeaderBoard) leaderBoard).onLogout(this);
        }

        /* Destroy Personalised MobNpcs */
        for (MobNpc npc : MobNpc.getMobNpcs()) {
            if (npc instanceof PersonalisedMobNpc)
                ((PersonalisedMobNpc) npc).onLogout(this);
        }

        /* Remove PlayerFreezer */
        PlayerFreezer freezer = PlayerFreezer.getFreezer(player);
        if (freezer != null)
            freezer.destroy();

        /* Leave Hover */
        if (currentHover != null)
            currentHover.leave(this);

        if (teleportingTo != null)
            teleportingTimer.cancel();

        players.remove(this);
    }

    public void defaultTabList() {
        orbitMines.getNms().tabList().send(Collections.singletonList(player), "\n§8§lOrbit§7§lMines\n" + orbitMines.getServerHandler().getServer().getDisplayName() + "\n", "\n    §7Website: §6§lwww.orbitmines.com§r    \n    §7" + lang("Winkel", "Shop") + ": §3§lorbitmines.buycraft.net§r    \n    §7Twitter: §b§l@OrbitMines§r    \n\n    §7Vote: §9§l/vote§r    \n");
    }

    public void on2FALogin() {
        sendMessage("§7§m----------------------------------------");
        sendMessage(" §8§lOrbit§7§lMines §8- " + orbitMines.getServerHandler().getServer().getDisplayName());
        sendMessage(" ");
        sendMessage(" §7Website: §6§lwww.orbitmines.com");
        sendMessage(" §7" + lang("Winkel", "Shop") + ": §3§lorbitmines.buycraft.net");
        sendMessage(" §7Twitter: §b§l@OrbitMines");
        sendMessage(" §7" + lang("Voten", "Vote") + ": §9§l/vote");
        sendMessage(" ");
        sendMessage("§7§m----------------------------------------");

        checkCachedVotes();
    }

    /*


        Vote


     */

    public void vote(int votes) {
        ((LootData) getData(Data.Type.LOOT)).add(Loot.PRISMS, Rarity.COMMON, "§9§l§oVote Rewards", 250 * votes);

        String reward = "§9§l" + NumberUtils.locale(250 * votes) + " Prisms";

        sendMessage("");
        sendMessage("Vote", Color.BLUE, "§7Je hebt " + reward + "§7 gekregen in je §2/loot§7.", "§7You have received " + reward + "§7 in your §2/loot§7.");
        playSound(Sound.ENTITY_ARROW_HIT_PLAYER);

        orbitMines.broadcast(this, "Vote", Color.BLUE,
                getName() + " §7heeft gevoten met §9/vote§7 en kreeg " + reward + "§7.",
                getName() + " §7voted with §9/vote§7 and received " + reward + "§7.");
    }

    public void checkCachedVotes() {
        VoteData data = (VoteData) getData(Data.Type.VOTES);
        data.updateCache();

        int votes = data.getCachedVotes();

        if (votes == 0)
            return;

        data.clearCache();

        vote(votes);

        /*
                Monthly Vote Achievement
         */

        if (data.getVotes() >= TopVoterReward.MONTHLY_ACHIEVEMENT_VOTES && (data.getVotes() - votes) < TopVoterReward.MONTHLY_ACHIEVEMENT_VOTES)
            ((LootData) getData(Data.Type.LOOT)).add(Loot.PRISMS, Rarity.UNCOMMON, "§a§l§oMonthly Achievement " + DateUtils.getMonth() + " " + DateUtils.getYear(), TopVoterReward.MONTHLY_ACHIEVEMENT_PRISMS);

        //
    }

    /*


        Connection


     */

    public void connect(Server server, boolean notify) {
        orbitMines.getServerHandler().getMessageHandler().dataTransfer(PluginMessage.CONNECT, getUUID().toString(), server.getName(), notify + "");
    }

    public void kick() {
        orbitMines.getServerHandler().getMessageHandler().dataTransfer(PluginMessage.KICK, getUUID().toString());
    }

    public void kick(String string) {
        orbitMines.getServerHandler().getMessageHandler().dataTransfer(PluginMessage.KICK, getUUID().toString(), string.replaceAll("§", "&"));
    }

    /*


        Getters & Setters


     */

    /*
        Player
     */

    public Player getPlayer() {
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
    }

    /*
        OpMode
     */

    public boolean isOpMode() {
        return opMode;
    }

    public void setOpMode(boolean opMode) {
        this.opMode = opMode;
        this.player.setOp(opMode);
    }

    /*
        Ranks
     */

    public StaffRank getStaffRank() {
        return staffRank;
    }

    public void setStaffRank(StaffRank staffRank) {
        Title t = new Title(new Message(""), new Message("§7Je bent nu een " + staffRank.getDisplayName() + "§7!", "§7You are now " + (staffRank == StaffRank.OWNER ? "an" : "a") + " " + staffRank.getDisplayName() + "§7!"), 20, 80, 20);
        t.send(this);

        Database.get().update(Table.PLAYERS, new Set(TablePlayers.STAFFRANK, staffRank.toString()), new Where(TablePlayers.UUID, getUUID().toString()));
        orbitMines.getServerHandler().getMessageHandler().dataTransfer(PluginMessage.UPDATE_RANKS, getUUID().toString());

        /* Trigger #updateRanks for subclasses */
        updateRanks();
    }

    public boolean isEligible(StaffRank staffRank) {
        return this.staffRank.ordinal() >= staffRank.ordinal() || opMode;
    }

    public VipRank getVipRank() {
        return vipRank;
    }

    public void setVipRank(VipRank vipRank) {
        Title t = new Title(new Message(""), new Message("§7Je bent nu een " + vipRank.getDisplayName() + "§7!", "§7You are now " + (vipRank == VipRank.EMERALD || vipRank == VipRank.IRON ? "an" : "a") + " " + vipRank.getDisplayName() + "§7!"), 20, 80, 20);
        t.send(this);

        Database.get().update(Table.PLAYERS, new Set(TablePlayers.VIPRANK, vipRank.toString()), new Where(TablePlayers.UUID, getUUID().toString()));
        orbitMines.getServerHandler().getMessageHandler().dataTransfer(PluginMessage.UPDATE_RANKS, getUUID().toString());

        /* Trigger #updateRanks */
        updateRanks();
    }

    public boolean isEligible(VipRank vipRank) {
        return this.vipRank.ordinal() >= vipRank.ordinal() || opMode;
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
        orbitMines.getServerHandler().getMessageHandler().dataTransfer(PluginMessage.UPDATE_LANGUAGE, getUUID().toString());

        /* Trigger #updateLanguage */
        updateLanguage();
    }

    public void updateLanguage() {
        this.language = Language.valueOf(Database.get().getString(Table.PLAYERS, TablePlayers.LANGUAGE, new Where(TablePlayers.UUID, getUUID().toString())));
        
        defaultTabList();
    }

    /*
        Muted
     */

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    /*
        Silent
     */

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        Database.get().update(Table.PLAYERS, new Set(TablePlayers.SILENT, silent), new Where(TablePlayers.UUID, getUUID().toString()));
        orbitMines.getServerHandler().getMessageHandler().dataTransfer(PluginMessage.UPDATE_SILENT, getUUID().toString());

        /* Trigger #updateSilent */
        updateSilent();
    }

    public void updateSilent() {
        this.silent = Database.get().getBoolean(Table.PLAYERS, TablePlayers.SILENT, new Where(TablePlayers.UUID, getUUID().toString()));
    }

    /*
        Solars
     */

    public int getSolars() {
        return solars;
    }

    public void addSolars(int amount) {
        solars += amount;

        updateSolars();
    }

    public void removeSolars(int amount) {
        solars -= amount;

        updateSolars();
    }

    private void updateSolars() {
        Database.get().update(Table.PLAYERS, new Set(TablePlayers.SOLARS, this.solars), new Where(TablePlayers.UUID, getUUID().toString()));
    }

    /*
        Prisms
     */

    public int getPrisms() {
        return prisms;
    }

    public void addPrisms(int amount) {
        prisms += amount;

        updatePrisms();
    }

    public void removePrisms(int amount) {
        prisms -= amount;

        updatePrisms();
    }

    private void updatePrisms() {
        Database.get().update(Table.PLAYERS, new Set(TablePlayers.PRISMS, this.prisms), new Where(TablePlayers.UUID, getUUID().toString()));
    }

    /*
        Afk
     */

    public boolean isAfk() {
        return afk != null;
    }

    public String getAfk() {
        return afk;
    }

    public void setAfk() {
        setAfk("null");
    }

    public void noLongerAfk() {
        setAfk(null);
    }

    public void setAfk(String afk) {
        this.afk = afk;

        String playerName = getName();

        if (afk != null) {
            if (afk.equals("null"))
                orbitMines.broadcast("Afk", Color.BLUE, "§7 " + playerName + "§7 is nu §6AFK§7.", "§7 " + playerName + "§7 is now §6AFK§7.");
            else
                orbitMines.broadcast("Afk", Color.BLUE, "§7 " + playerName + "§7 is nu §6AFK§7. (§7" + afk + "§7)", "§7 " + playerName + "§7 is now §6AFK§7. (§7" + afk + "§7)");
        } else {
            if (this.afk.equals("null"))
                orbitMines.broadcast("Afk", Color.BLUE, "§7 " + playerName + "§7 is niet meer §6AFK§7.", "§7 " + playerName + "§7 is no longer §6AFK§7.");
            else
                orbitMines.broadcast("Afk", Color.BLUE, "§7 " + playerName + "§7 is niet meer §6AFK§7. (§7" + this.afk + "§7)", "§7 " + playerName + "§7 is no longer §6AFK§7. (§7" + this.afk + "§7)");
        }
        this.afk = afk;
    }

    /*
        Scoreboard
     */

    public OMScoreboard getScoreboard() {
        return scoreboard;
    }

    public void resetScoreboard() {
        scoreboard.set(null);
    }

    public void clearScoreboard() {
        scoreboard.clear();
    }

    public void setScoreboard(ScoreboardSet set) {
        scoreboard.set(set);
    }

    public void updateScoreboard() {
        scoreboard.update();
    }

    /*
        GUI
     */

    public GUI getLastInventory() {
        return lastInventory;
    }

    public void setLastInventory(GUI lastInventory) {
        this.lastInventory = lastInventory;
    }

    /*
        Interactive Kit
     */

    public KitInteractive getLastInteractiveKit() {
        return lastInteractiveKit;
    }

    public void setLastInteractiveKit(KitInteractive lastInteractiveKit) {
        this.lastInteractiveKit = lastInteractiveKit;
    }

    /*
        ItemHover
     */

    public ItemHover getCurrentHover() {
        return currentHover;
    }

    public void setCurrentHover(ItemHover currentHover) {
        this.currentHover = currentHover;
    }

    /*
        Teleportable
     */

    public Teleportable getTeleportingTo() {
        return teleportingTo;
    }

    public void setTeleportingTo(Teleportable teleportingTo) {
        this.teleportingTo = teleportingTo;
    }

    public Timer getTeleportingTimer() {
        return teleportingTimer;
    }

    public void setTeleportingTimer(Timer teleportingTimer) {
        this.teleportingTimer = teleportingTimer;
    }

    /*
        Cooldowns
     */

    public boolean onCooldown(Cooldown cooldown) {
        return cooldown.onCooldown(getCooldown(cooldown));
    }

    public long getCooldown(Cooldown cooldown) {
        return cooldowns.getOrDefault(cooldown, -1L);
    }

    public void resetCooldown(Cooldown cooldown) {
        cooldowns.put(cooldown, System.currentTimeMillis());
    }

    public void removeCooldown(Cooldown cooldown) {
        cooldowns.remove(cooldown);
    }

    /*
        Data
     */

    public void updateSettings(Settings settings, SettingsType settingsType) {
        SettingsData data = (SettingsData) getData(Data.Type.SETTINGS);
        data.setSettings(settings, settingsType);

        /* Update Bungeecord */
        orbitMines.getServerHandler().getMessageHandler().dataTransfer(PluginMessage.UPDATE_SETTINGS, getUUID().toString());
    }

    public Data getData(Data.Type type) {
        if (data.containsKey(type))
            return data.get(type);

        Data data;
        switch (type) {

            case VOTES:
                data = new VoteData(getUUID());
                break;
            case FRIENDS:
                data = new FriendsData(getUUID());
                break;
            case SETTINGS:
                data = new SettingsData(getUUID());
                break;
            case PLAY_TIME:
                data = new PlayTimeData(getUUID());
                break;
            case LOOT:
                data = new LootData(getUUID());
                break;
            case PERIOD_LOOT:
                data = new PeriodLootData(getUUID());
                break;

            case SURVIVAL:
                data = new SurvivalData(getUUID());
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


        Freezer


     */

    public void freeze(Freezer freezer) {
        freeze(freezer, null);
    }

    public void freeze(Freezer freezer, Location location) {
        this.freezer = freezer;
        this.freezer.freeze(this, location);
    }

    public void clearFreeze() {
        if (!isFrozen())
            return;

        this.freezer.clearFreeze(this);
        this.freezer = null;
    }

    public boolean isFrozen() {
        return freezer != null;
    }

    public Freezer getFreezer() {
        return freezer;
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

        player.spigot().sendMessage(textComponent);
    }

    public UUID getUUID() {
        return player.getUniqueId();
    }

    public Location getLocation() {
        return player.getLocation();
    }

    public Vector getDirection() {
        return player.getLocation().getDirection();
    }

    public Vector getVelocity() {
        return player.getVelocity();
    }

    public Block getBlock() {
        return player.getLocation().getBlock();
    }

    public World getWorld() {
        return player.getWorld();
    }

    public GameMode getGameMode() {
        return player.getGameMode();
    }

    public PlayerInventory getInventory() {
        return player.getInventory();
    }

    public ItemStack getItemInMainHand() {
        return player.getInventory().getItemInMainHand();
    }

    public ItemStack getItemInOffHand() {
        return player.getInventory().getItemInOffHand();
    }

    /* Clear inventory & Armor Contents */
    public void clearFullInventory() {
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
    }

    public void updateInventory() {
        new BukkitRunnable() {
            public void run() {
                player.updateInventory();
            }
        }.runTaskLater(orbitMines, 1);
    }

    public void clearExperience() {
        player.setLevel(0);
        player.setExp(0f);
    }
    
    public void clearPotionEffects() {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }
    
    public void clearPotionEffect(PotionEffect effect) {
        player.removePotionEffect(effect.getType());
    }
    
    public void addPotionEffect(PotionBuilder builder) {
        player.addPotionEffect(builder.build());
    }

    public boolean isMoving(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();

        return Math.abs(from.getX() - to.getX()) > 0.05 || Math.abs(from.getY() - to.getY()) > 0.05 || Math.abs(from.getZ() - to.getZ()) > 0.05;
    }

    public void playSound(Sound sound) {
        playSound(sound, 1f);
    }

    public void playSound(Sound sound, float volume) {
        playSound(sound, volume, 1f);
    }

    public void playSound(Sound sound, float volume, float pitch) {
        playSound(getLocation(), sound, volume, pitch);
    }

    public void playSound(Location loc, Sound sound, float volume, float pitch) {
        player.playSound(loc, sound, volume, pitch);
    }

    /* Sets Header&Footer in Tablist */
    public void setTabList(String header, String footer) {
        orbitMines.getNms().tabList().send(Collections.singletonList(player), header, footer);
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


        OMPlayer Getters


     */

    public static OMPlayer getPlayer(Player player) {
        for (OMPlayer omp : players) {
            if (omp.getPlayer() == player)
                return omp;
        }
        return null;
    }

    public static OMPlayer getPlayer(String name) {
        for (OMPlayer omp : players) {
            if (omp.getName(true).equalsIgnoreCase(name))
                return omp;
        }
        return null;
    }

    public static OMPlayer getPlayer(UUID uuid) {
        for (OMPlayer omp : players) {
            if (omp.getUUID().toString().equals(uuid.toString()))
                return omp;
        }
        return null;
    }

    public static List<OMPlayer> getPlayers() {
        return players;
    }

}
