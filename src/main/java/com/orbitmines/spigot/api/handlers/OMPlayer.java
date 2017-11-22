package com.orbitmines.spigot.api.handlers;

import com.orbitmines.api.*;
import com.orbitmines.api.database.*;
import com.orbitmines.api.database.Set;
import com.orbitmines.api.database.tables.TablePlayers;
import com.orbitmines.api.Cooldown;
import com.orbitmines.api.IP;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.Freezer;
import com.orbitmines.spigot.api.handlers.chat.Title;
import com.orbitmines.spigot.api.handlers.data.VoteData;
import com.orbitmines.spigot.api.handlers.itembuilders.PotionBuilder;
import com.orbitmines.spigot.api.handlers.kit.KitInteractive;
import com.orbitmines.spigot.api.handlers.leaderboard.LeaderBoard;
import com.orbitmines.spigot.api.handlers.leaderboard.PlayerLeaderBoard;
import com.orbitmines.spigot.api.handlers.npc.PlayerFreezer;
import com.orbitmines.spigot.api.handlers.scoreboard.OMScoreboard;
import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardSet;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.TimeUnit;

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
    protected boolean silent;
    protected int points;
    protected int tokens;
    protected Date monthlyBonus;

    protected String afk;

    protected OMScoreboard scoreboard;
    protected GUI lastInventory;
    protected KitInteractive lastInteractiveKit;

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
        this.silent = false;
        this.points = 0;
        this.tokens = 0;

        this.scoreboard = new OMScoreboard(this);

        this.cooldowns = new HashMap<>();
        this.data = new HashMap<>();
    }

    /* Called after the player logs in */
    protected abstract void onLogin();

    /* Called before the player logs out */
    protected abstract void onLogout();

    /* Called before a players votes, or when the players logs in and receives previous votes, this handles just the rewards */
    public abstract void onVote(int votes);

    /* Called when a player receives knock back, example: gadgets */
    public abstract boolean canReceiveVelocity();

    /*


        Login / Logout


     */

    public void login() {
        players.add(this);

        /* Update Players */
        //set server players

        if (!Database.get().contains(Table.PLAYERS, TablePlayers.UUID, new Where(TablePlayers.UUID, getUUID().toString()))) {
            Database.get().insert(Table.PLAYERS, Table.PLAYERS.values());
        } else {
            Map<Column, String> values = Database.get().getValues(Table.PLAYERS, new Column[]{
                    TablePlayers.STAFFRANK,
                    TablePlayers.VIPRANK,
                    TablePlayers.LANGUAGE,
                    TablePlayers.SILENT,
                    TablePlayers.POINTS,
                    TablePlayers.TOKENS,
                    TablePlayers.MONTHLY_BONUS
            }, new Where(TablePlayers.UUID, getUUID().toString()));

            staffRank = StaffRank.valueOf(values.get(TablePlayers.STAFFRANK));
            vipRank = VipRank.valueOf(values.get(TablePlayers.VIPRANK));
            language = Language.valueOf(values.get(TablePlayers.LANGUAGE));
            silent = "1".equals(values.get(TablePlayers.SILENT));
            points = Integer.parseInt(values.get(TablePlayers.POINTS));
            tokens = Integer.parseInt(values.get(TablePlayers.TOKENS));

            String monthlyBonus = values.get(TablePlayers.MONTHLY_BONUS);
            if (!monthlyBonus.equals("null"))
                this.monthlyBonus = DateUtils.parse(DateUtils.FORMAT, monthlyBonus);
        }

        /* Set Op */
        player.setOp(false);

        /* Default TabList */
        defaultTabList();

        /* Spawn Leaderboards */
        for (LeaderBoard leaderBoard : LeaderBoard.getLeaderBoards()) {
            if (leaderBoard instanceof PlayerLeaderBoard)
                ((PlayerLeaderBoard) leaderBoard).onLogin(this);
        }

        checkCachedVotes();

        /* Initiate server login */
        onLogin();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline())
                    return;

                /* Update current server */
                IP ip = IP.getIp(player.getUniqueId());
                ip.updateCurrentServer();

                ip.setCurrentServer(orbitMines.getServerHandler().getServer());
            }
        }.runTaskLater(orbitMines, 10);
    }

    public void logout() {
        /* Initiate server logout */
        onLogout();

        //set players -1

        /* Destroy Leaderboards */
        for (LeaderBoard leaderBoard : LeaderBoard.getLeaderBoards()) {
            if (leaderBoard instanceof PlayerLeaderBoard)
                ((PlayerLeaderBoard) leaderBoard).onLogout(this);
        }

        /* Remove PlayerFreezer */
        PlayerFreezer freezer = PlayerFreezer.getFreezer(player);
        if (freezer != null)
            freezer.delete();

        players.remove(this);
    }

    public void defaultTabList() {
        orbitMines.getNms().tabList().send(player, "§6§lOrbitMines§4§lNetwork\n" + orbitMines.getServerHandler().getServer().getDisplayName(), "§7Website: §6www.orbitmines.com §8| §7Twitter: §b@OrbitMines §8| §7" + lang("Winkel", "Shop") + ": §3shop.orbitmines.com");
    }

    /*


        Vote


     */

    public void vote(int votes) {

        onVote(votes);
    }

    public void checkCachedVotes() {
        VoteData data = (VoteData) getData(Data.Type.VOTES);
        Server server = orbitMines.getServerHandler().getServer();

        if (!data.getCachedVotes().containsKey(server))
            return;

        int votes = data.getCachedVotes().get(server);
        data.getCachedVotes().remove(server);

        vote(votes);
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
        Title t = new Title("", lang("§7Je bent nu een " + staffRank.getDisplayName() + "§7!", "§7You are now " + staffRank.getDisplayName() + " " + (staffRank == StaffRank.OWNER ? "an" : "a") + "§7!"), 20, 80, 20);
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
        Title t = new Title("", lang("§7Je bent nu een " + vipRank.getDisplayName() + "§7!", "§7You are now " + vipRank.getDisplayName() + " " + (vipRank == VipRank.EMERALD || vipRank == VipRank.IRON ? "an" : "a") + "§7!"), 20, 80, 20);
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
        Points
     */

    public int getPoints() {
        return points;
    }

    public void addPoints(int amount) {
        points += amount;

        updatePoints();
    }

    public void removePoints(int amount) {
        points -= amount;

        updatePoints();
    }

    private void updatePoints() {
        Database.get().update(Table.PLAYERS, new Set(TablePlayers.POINTS, this.points), new Where(TablePlayers.UUID, getUUID().toString()));
    }

    /*
        Tokens
     */

    public int getTokens() {
        return tokens;
    }

    public void addTokens(int amount) {
        tokens += amount;

        updateTokens();
    }

    public void removeTokens(int amount) {
        tokens -= amount;

        updateTokens();
    }

    private void updateTokens() {
        Database.get().update(Table.PLAYERS, new Set(TablePlayers.TOKENS, this.tokens), new Where(TablePlayers.UUID, getUUID().toString()));
    }

    /*
        Monthly Bonus
     */

    public Date getMonthlyBonus() {
        return monthlyBonus;
    }

    public void registerMonthlyBonus() {
        this.monthlyBonus = DateUtils.now();

        Database.get().update(TablePlayers.PLAYERS, new Set(TablePlayers.MONTHLY_BONUS, DateUtils.FORMAT.format(this.monthlyBonus)), new Where(TablePlayers.UUID, getUUID().toString()));
    }

    public boolean canReceiveMonthlyBonus() {
        return monthlyBonus == null || System.currentTimeMillis() - monthlyBonus.getTime() >= TimeUnit.DAYS.toMillis(30);
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
                orbitMines.broadcast("Afk", Color.ORANGE, "§7 " + playerName + "§7 is nu §6AFK§7.", "§7 " + playerName + "§7 is now §6AFK§7.");
            else
                orbitMines.broadcast("Afk", Color.ORANGE, "§7 " + playerName + "§7 is nu §6AFK§7. (§7" + afk + "§7)", "§7 " + playerName + "§7 is now §6AFK§7. (§7" + afk + "§7)");
        } else {
            if (this.afk.equals("null"))
                orbitMines.broadcast("Afk", Color.ORANGE, "§7 " + playerName + "§7 is niet meer §6AFK§7.", "§7 " + playerName + "§7 is no longer §6AFK§7.");
            else
                orbitMines.broadcast("Afk", Color.ORANGE, "§7 " + playerName + "§7 is niet meer §6AFK§7. (§7" + this.afk + "§7)", "§7 " + playerName + "§7 is no longer §6AFK§7. (§7" + this.afk + "§7)");
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

    public Data getData(Data.Type type) {
        if (data.containsKey(type))
            return data.get(type);

        Data data;
        switch (type) {

            case VOTES:
                data = new VoteData(getUUID());
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

    public ItemStack getCursor() {
        return player.getItemOnCursor();
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

    public boolean isMoving() {
        Vector v = getVelocity();
        return Math.abs(v.getX()) > 0.05 || Math.abs(v.getY()) > 0.05 || Math.abs(v.getZ()) > 0.05;
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
        orbitMines.getNms().tabList().send(player, header, footer);
    }

    /*


        OMPlayer Getters


     */

    public static OMPlayer getPlayer(Player player) {
        for (OMPlayer omp : players) {
            if (omp.getPlayer() == player)
                return omp;
        }
        throw new IllegalStateException();
    }

    public static OMPlayer getPlayer(String name) {
        for (OMPlayer omp : players) {
            if (omp.getName(true).equalsIgnoreCase(name))
                return omp;
        }
        throw new IllegalStateException();
    }

    public static OMPlayer getPlayer(UUID uuid) {
        for (OMPlayer omp : players) {
            if (omp.getUUID().toString().equals(uuid.toString()))
                return omp;
        }
        throw new IllegalStateException();
    }

    public static List<OMPlayer> getPlayers() {
        return players;
    }

}
