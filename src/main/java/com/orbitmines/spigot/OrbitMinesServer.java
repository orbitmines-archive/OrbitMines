package com.orbitmines.spigot;

import com.orbitmines.api.Server;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.PluginMessageHandler;
import com.orbitmines.spigot.api.handlers.PreventionSet;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import com.orbitmines.spigot.servers.hub.Hub;
import com.orbitmines.spigot.servers.survival.Survival;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginManager;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public abstract class OrbitMinesServer {

    protected final OrbitMines orbitMines;
    protected final Server server;
    private final PluginMessageHandler messageHandler;

    protected final PreventionSet preventionSet;

    public OrbitMinesServer(OrbitMines orbitMines, Server server, PluginMessageHandler messageHandler) {
        this.orbitMines = orbitMines;
        this.server = server;
        this.messageHandler = messageHandler;

        this.preventionSet = new PreventionSet();

        registerEvents();
        registerCommands();
        registerRunnables();

        new SpigotRunnable(SpigotRunnable.TimeUnit.SECOND, 10) {
            @Override
            public void run() {
                if (server.getStatus() == Server.Status.OFFLINE)
                    server.setStatus(Server.Status.ONLINE);
            }
        };
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract OMPlayer newPlayerInstance(Player player);

    /* OMPlayer is not yet initiated here */
    public abstract boolean teleportToSpawn(Player player);

    /* OMPlayer is not yet initiated here */
    public abstract Location getSpawnLocation(Player player);

    protected abstract void registerEvents();

    protected abstract void registerCommands();

    protected abstract void registerRunnables();

    public void format(AsyncPlayerChatEvent event, OMPlayer omp) {
        event.setFormat(omp.getRankPrefix() + omp.getName() + "§7 » " + omp.getRankChatColor().getChatColor() + "%2$s");
    }

    protected void registerEvents(Listener... listeners) {
        PluginManager pluginManager = orbitMines.getServer().getPluginManager();
        for (Listener l : listeners) {
            pluginManager.registerEvents(l, orbitMines);
        }
    }

    public static OrbitMinesServer getServer(OrbitMines orbitMines, Server server) {
        switch (server) {

            case KITPVP:
                return null;
            case PRISON:
                return null;
            case CREATIVE:
                return null;
            case HUB:
                return new Hub(orbitMines);
            case SURVIVAL:
                return new Survival(orbitMines);
            case SKYBLOCK:
                return null;
            case FOG:
                return null;
            case MINIGAMES:
                return null;
            default:
                throw new IllegalStateException();
        }
    }

    public OrbitMines getOrbitMines() {
        return orbitMines;
    }

    public Server getServer() {
        return server;
    }

    public PluginMessageHandler getMessageHandler() {
        return messageHandler;
    }

    public PreventionSet getPreventionSet() {
        return preventionSet;
    }
}
