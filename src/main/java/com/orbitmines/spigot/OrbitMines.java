package com.orbitmines.spigot;

import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.api.ScrollerList;
import com.orbitmines.api.Server;
import com.orbitmines.api.database.Database;
import com.orbitmines.spigot.api._2fa._2FA;
import com.orbitmines.spigot.api.events.*;
import com.orbitmines.spigot.api.handlers.ConfigHandler;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.OrbitMinesMap;
import com.orbitmines.spigot.api.handlers.worlds.WorldLoader;
import com.orbitmines.spigot.api.nms.Nms;
import com.orbitmines.spigot.api.utils.ReflectionUtils;
import com.orbitmines.spigot.runnables.LeaderBoardRunnable;
import com.orbitmines.spigot.runnables.NPCRunnable;
import com.orbitmines.spigot.runnables.ScoreboardRunnable;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class OrbitMines extends JavaPlugin {

    public static final String DOMAIN = "orbitmines.com";

    private static OrbitMines instance;

    private _2FA _2fa;

    private Nms nms;

    private String resourceFolder;

    private OrbitMinesServer serverHandler;
    private OrbitMinesMap lobby;

    private ConfigHandler configHandler;
    private WorldLoader worldLoader;

    private final ScrollerList<String> scoreboardAnimation = new ScrollerList<>(
            "§6§lTITLE"
    );

    @Override
    public void onEnable() {
        instance = this;


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
        Server server;

        try {
            server = Server.valueOf(configHandler.get("settings").getString("server"));
        } catch(IllegalArgumentException ex) {
            getLogger().warning("Invalid Server in settings.yml, shutting down...");
            getServer().shutdown();
            return;
        }

        server.setStatus(Server.Status.ONLINE);
        server.setPlayers(0);

        ReflectionUtils.setMaxCapacity(server.getMaxPlayers());

        /* Setup World Loader & Lobby */
        resourceFolder = configHandler.get("resources").getString("path");
        worldLoader = new WorldLoader(resourceFolder, true);

        lobby = OrbitMinesMap.getLobby(server);

        World lobbyWorld = worldLoader.fromZip(lobby.getWorldName(), true, lobby.getWorldGenerator());
        lobbyWorld.setGameRuleValue("doDaylightCycle", "false");
        lobbyWorld.setGameRuleValue("doMobSpawning", "false");
        lobbyWorld.setGameRuleValue("doFireTick", "false");
        lobbyWorld.setTime(6000);

        lobby.setWorld(lobbyWorld);
//        lobby.setHandler(DataPointHandler.getHandler(gameServer.getGameMode(), OrbitMinesMap.Type.LOBBY));
//        lobby.setupDataPoints();

        /* Register */
        registerCommands();
        registerEvents(
            new ClickEvent(),
                new CommandPreprocessEvent(),
                new FreezeEvent(),
                new InteractEvent(),
                new NpcEvents(),
                new VoidDamageEvent()
        );
        registerRunnables();

        /* Setup Server */
        serverHandler = OrbitMinesServer.getServer(this, server);
        serverHandler.onEnable();
    }

    @Override
    public void onDisable() {
        if (worldLoader != null)
            worldLoader.cleanUp();

        if (serverHandler != null) {
            serverHandler.getServer().setStatus(Server.Status.OFFLINE);
            serverHandler.getServer().setPlayers(0);

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

    private void registerCommands() {

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
    }

    public void broadcast(String... messages) {
        broadcast(new Message(messages));
    }

    public void broadcast(String prefix, Color prefixColor, String... messages) {
        broadcast(new Message(prefix, prefixColor, messages));
    }

    public void broadcast(Message message) {
        for (OMPlayer omp : OMPlayer.getPlayers()) {
            omp.sendMessage(message);
        }
    }
}
