package com.orbitmines.spigot;

import com.orbitmines.api.*;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.TablePlayers;
import com.orbitmines.api.database.tables.TableServers;
import com.orbitmines.api.database.tables.TableVotes;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.discordbot.utils.ColorUtils;
import com.orbitmines.spigot.api._2fa._2FA;
import com.orbitmines.spigot.api.cmds.*;
import com.orbitmines.spigot.api.cmds.moderator.*;
import com.orbitmines.spigot.api.cmds.vip.CommandAfk;
import com.orbitmines.spigot.api.cmds.vip.CommandNick;
import com.orbitmines.spigot.api.datapoints.DataPointHandler;
import com.orbitmines.spigot.api.events.*;
import com.orbitmines.api.CachedPlayer;
import com.orbitmines.spigot.api.handlers.ConfigHandler;
import com.orbitmines.spigot.api.handlers.NewsHologram;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.OrbitMinesMap;
import com.orbitmines.spigot.api.handlers.leaderboard.LeaderBoard;
import com.orbitmines.spigot.api.handlers.leaderboard.hologram.DefaultHologramLeaderBoard;
import com.orbitmines.spigot.api.handlers.npc.Hologram;
import com.orbitmines.spigot.api.handlers.worlds.WorldLoader;
import com.orbitmines.spigot.api.nms.Nms;
import com.orbitmines.spigot.api.utils.ReflectionUtils;
import com.orbitmines.spigot.gui.ServerSelectorGUI;
import com.orbitmines.spigot.leaderboards.hologram.LeaderBoardDonations;
import com.orbitmines.spigot.runnables.LeaderBoardRunnable;
import com.orbitmines.spigot.runnables.NPCRunnable;
import com.orbitmines.spigot.runnables.ScoreboardRunnable;
import com.orbitmines.spigot.runnables.ServerSelectorRunnable;
import net.dv8tion.jda.core.EmbedBuilder;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class OrbitMines extends JavaPlugin {

    public static final String DOMAIN = "orbitmines.com";

    private static OrbitMines instance;

    private _2FA _2fa;

    private Nms nms;

    private PatchNotes patchNotes;

    private String resourceFolder;

    private OrbitMinesServer serverHandler;
    private OrbitMinesMap lobby;

    private ConfigHandler configHandler;
    private WorldLoader worldLoader;

    private Map<Language, ServerSelectorGUI> serverSelectors;

    private final ScrollerList<String> scoreboardAnimation = new ScrollerList<>(
            "§8§lOrbit§7§lMines",
            "§7§lO§8§lrbit§7§lMines",
            "§7§lOr§8§lbit§7§lMines",
            "§7§lOrb§8§lit§7§lMines",
            "§7§lOrbi§8§lt§7§lMines",
            "§7§lOrbit§7§lMines",
            "§7§lOrbit§8§lM§7§lines",
            "§7§lOrbit§8§lMi§7§lnes",
            "§7§lOrbit§8§lMin§7§les",
            "§7§lOrbit§8§lMine§7§ls",
            "§7§lOrbit§8§lMines",
            
            "§8§lO§7§lrbit§8§lMines",
            "§8§lOr§7§lbit§8§lMines",
            "§8§lOrb§7§lit§8§lMines",
            "§8§lOrbi§7§lt§8§lMines",
            "§8§lOrbit§8§lMines",
            "§8§lOrbit§7§lM§8§lines",
            "§8§lOrbit§7§lMi§8§lnes",
            "§8§lOrbit§7§lMin§8§les",
            "§8§lOrbit§7§lMine§8§ls"
    );

    static {
        new LeaderBoard.Instantiator("SOLARS") {
            @Override
            public LeaderBoard instantiate(Location location, String[] data) {
                return new DefaultHologramLeaderBoard(location, 0, () -> "§7§lTop Solars", 10, Table.PLAYERS, TablePlayers.UUID, TablePlayers.SOLARS);
            }
        };
        new LeaderBoard.Instantiator("PRISMS") {
            @Override
            public LeaderBoard instantiate(Location location, String[] data) {
                return new DefaultHologramLeaderBoard(location, 0, () -> "§7§lTop Prisms", 10, Table.PLAYERS, TablePlayers.UUID, TablePlayers.PRISMS);
            }
        };

        new LeaderBoard.Instantiator("TOP_VOTERS") {
            @Override
            public LeaderBoard instantiate(Location location, String[] data) {
                return new DefaultHologramLeaderBoard(location, 0, () -> "§7§lTop Voters of " + DateUtils.getMonth() + " " + DateUtils.getYear(), 5, Table.VOTES, TableVotes.UUID, TableVotes.VOTES) {

                    @Override
                    public String getValue(CachedPlayer player, int count) {
                        return "§9§l" + count + " " + (count == 1 ? "Vote" : "Votes");
                    }
                };
            }
        };

        new LeaderBoard.Instantiator("TOTAL_VOTES") {
            @Override
            public LeaderBoard instantiate(Location location, String[] data) {
                return new DefaultHologramLeaderBoard(location, 0, () -> "§7§lTop Total Votes", 10, Table.VOTES, TableVotes.UUID, TableVotes.TOTAL_VOTES);
            }
        };

        new LeaderBoard.Instantiator("DONATIONS") {
            @Override
            public LeaderBoard instantiate(Location location, String[] data) {
                return new LeaderBoardDonations(location, Hologram.Y_OFFSET_PER_LINE * 2 /* This is to match the height of Top Total Votes */, () -> "§7§lRecent Donations", 3);
            }
        };
    }

    @Override
    public void onEnable() {
        instance = this;

        serverSelectors = new HashMap<>();

        /* Setup Config Files */
        configHandler = new ConfigHandler(this);
        configHandler.setup("settings", "resources");

        /* Setup 2FA */
        _2fa = new _2FA();

        /* Setup Nms */
        new Nms();

        /* Setup Database */
        FileConfiguration settings = configHandler.get("settings");

        Database database = new Database(settings.getString("host"), settings.getInt("port"), settings.getString("database"), settings.getString("user"), settings.getString("password"));
        database.openConnection();
        database.setupTables();

        /* Setup Server */
        String ip = getServer().getIp();
        int port = getServer().getPort();
        Server server;

        if (Database.get().contains(Table.SERVERS, new Column[] { TableServers.IP, TableServers.PORT }, new Where(TableServers.IP, ip), new Where(TableServers.PORT, port))) {
            try {
                server = Server.valueOf(Database.get().getString(Table.SERVERS, TableServers.SERVER, new Where(TableServers.IP, ip), new Where(TableServers.PORT, port)));
            } catch(IllegalArgumentException ex) {
                getLogger().warning("Invalid Server in the database, shutting down...");
                getServer().shutdown();
                return;
            }
        } else {
            getLogger().warning("Server IP and Port are not found in the database, shutting down...");
            getServer().shutdown();
            return;
        }

        ReflectionUtils.setMaxCapacity(server.getMaxPlayers());

        /* Setup World Loader & Lobby */
        resourceFolder = configHandler.get("resources").getString("path");
        worldLoader = new WorldLoader(resourceFolder, server.cleanUpPlayerData());

        lobby = OrbitMinesMap.getLobby(server);

        World lobbyWorld = worldLoader.fromZip(lobby.getWorldName(), true, lobby.getWorldGenerator());
        lobbyWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        lobbyWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        lobbyWorld.setGameRule(GameRule.DO_FIRE_TICK, false);
        lobbyWorld.setTime(18000);

        lobby.setWorld(lobbyWorld);

        /* Register */
        registerCommands();

        registerEvents(
                new AfkEvents(),
                new ClickEvent(),
                new CommandPreprocessEvent(this),
                new FreezeEvent(),
                new ItemHandlerEvents(),
                new JoinQuitEvents(),
                new NpcEvents(),
                new PlayerChatEvent(),
                new PlayerHeadOnMobEvent(),
                new SignLogEvent(this),
                new SpawnLocationEvent(),
                new TeleportingMoveEvent(),
                new WorldAdvancementsFix_1_12()
        );

        registerRunnables();

        /* Prepare Server */
        serverHandler = OrbitMinesServer.getServer(this, server);

        /* Setup Lobby DataPoints */
        lobby.setHandler(DataPointHandler.getHandler(serverHandler.getServer(), OrbitMinesMap.Type.LOBBY));
        lobby.setupDataPoints();

        /* Setup Server Selector */
        for (Language language : Language.values()) {
            serverSelectors.put(language, new ServerSelectorGUI(language));
        }

        /* Discord Startup Message */
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Starting " + server.getName() + "...");
        builder.setColor(ColorUtils.from(Color.LIME));
        serverHandler.getDiscordChannel().sendMessage(builder.build()).queue();

        /* Setup Server */
        serverHandler.onEnable();

        /* Set Server Online */
        server.setPlayers(0);
        server.setStatus(Server.Status.ONLINE);

        /* Discord Message Listener Message */
        serverHandler.getDiscord().getJDA(serverHandler.getToken()).addEventListener(new DiscordMessageListener(this));

        /* News Holograms */
        NewsHologram.setup();

        /* Setup Patch Notes */
        patchNotes = new PatchNotes(this);
        patchNotes.build();
    }

    @Override
    public void onDisable() {
        if (worldLoader != null)
            worldLoader.cleanUp();

        if (serverHandler != null) {
            serverHandler.getServer().setStatus(Server.Status.OFFLINE);
            serverHandler.getServer().setPlayers(0);

            /* Discord Shutdown Message */
            EmbedBuilder builder = new EmbedBuilder();
            builder.setAuthor("Shutting down " + serverHandler.getServer().getName() + "...");
            builder.setColor(ColorUtils.from(Color.RED));
            serverHandler.getDiscordChannel().sendMessage(builder.build()).queue();

            serverHandler.onDisable();
        }
    }

    public static OrbitMines getInstance() {
        return instance;
    }

    public _2FA get2FA() {
        return _2fa;
    }

    public Nms getNms() {
        return nms;
    }

    public void setNms(Nms nms) {
        this.nms = nms;
    }

    public PatchNotes getPatchNotes() {
        return patchNotes;
    }

    public String getResourceFolder() {
        return resourceFolder;
    }

    public OrbitMinesServer getServerHandler() {
        return serverHandler;
    }

    public OrbitMinesMap getLobby() {
        return lobby;
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public WorldLoader getWorldLoader() {
        return worldLoader;
    }

    public ScrollerList<String> getScoreboardAnimation() {
        return scoreboardAnimation;
    }

    public Map<Language, ServerSelectorGUI> getServerSelectors() {
        return serverSelectors;
    }

    private void registerCommands() {
        new CommandRules(this);
        new CommandPatchNotes(this);

        new CommandServers(this);

        new CommandTopVoters();

        new CommandLoot();
        new CommandFriends();
        new CommandDiscordServer();
        new CommandStats();
        new CommandSettings();

        new CommandPrisms();
        new CommandSolars();

        new CommandAfk();
        new CommandNick();

        new CommandOpMode();
        new CommandPunish();
        new CommandHologram();
        new CommandInvSee();
        new CommandTeleport();
        new CommandFly();
    }

    private void registerEvents(Listener... listeners) {
        PluginManager pluginManager = getServer().getPluginManager();
        for (Listener l : listeners) {
            pluginManager.registerEvents(l, this);
        }
    }

    private void registerRunnables() {
        new LeaderBoardRunnable();
        new NPCRunnable();
        new ScoreboardRunnable();
        new ServerSelectorRunnable();
    }

    public void broadcast(String... messages) {
        broadcast((StaffRank) null, new Message(messages));
    }

    public void broadcast(String prefix, Color prefixColor, String... messages) {
        broadcast((StaffRank) null, new Message(prefix, prefixColor, messages));
    }

    public void broadcast(Message message) {
        broadcast((StaffRank) null, message);
    }

    public void broadcast(StaffRank staffRank, String... messages) {
        broadcast(staffRank, new Message(messages));
    }

    public void broadcast(StaffRank staffRank, String prefix, Color prefixColor, String... messages) {
        broadcast(staffRank, new Message(prefix, prefixColor, messages));
    }

    public void broadcast(StaffRank staffRank, Message message) {
        for (OMPlayer omp : OMPlayer.getPlayers()) {
            if (staffRank == null || omp.isEligible(staffRank))
                omp.sendMessage(message);
        }
    }

    public void broadcast(OMPlayer exclude, String... messages) {
        broadcast(exclude, null, new Message(messages));
    }

    public void broadcast(OMPlayer exclude, String prefix, Color prefixColor, String... messages) {
        broadcast(exclude, null, new Message(prefix, prefixColor, messages));
    }

    public void broadcast(OMPlayer exclude, Message message) {
        broadcast(exclude, null, message);
    }

    public void broadcast(OMPlayer exclude, StaffRank staffRank, String... messages) {
        broadcast(exclude, staffRank, new Message(messages));
    }

    public void broadcast(OMPlayer exclude, StaffRank staffRank, String prefix, Color prefixColor, String... messages) {
        broadcast(exclude, staffRank, new Message(prefix, prefixColor, messages));
    }

    public void broadcast(OMPlayer exclude, StaffRank staffRank, Message message) {
        for (OMPlayer omp : OMPlayer.getPlayers()) {
            if (omp != exclude && (staffRank == null || omp.isEligible(staffRank)))
                omp.sendMessage(message);
        }
    }
}
