package com.orbitmines.spigot;

import com.orbitmines.api.Server;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.PluginMessageHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public abstract class OrbitMinesServer {

    protected final OrbitMines orbitMines;
    protected final Server server;
    private final PluginMessageHandler messageHandler;

    public OrbitMinesServer(OrbitMines orbitMines, Server server, PluginMessageHandler messageHandler) {
        this.orbitMines = orbitMines;
        this.server = server;
        this.messageHandler = messageHandler;

        registerEvents();
        registerCommands();
        registerRunnables();
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract OMPlayer newPlayerInstance(Player player);

    /* OMPlayer is not yet initiated here */
    public abstract boolean teleportToSpawn(Player player);

    /* OMPlayer is not yet initiated here */
    public abstract Location getSpawnLocation(Player player);

    public abstract void registerEvents();

    public abstract void registerCommands();

    public abstract void registerRunnables();

    public void format(AsyncPlayerChatEvent event, OMPlayer omp) {

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
                return null;
            case SURVIVAL:
                return null;
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
}
