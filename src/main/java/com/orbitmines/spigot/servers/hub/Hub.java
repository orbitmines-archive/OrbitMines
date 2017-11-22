package com.orbitmines.spigot.servers.hub;

import com.orbitmines.api.Server;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.OrbitMinesServer;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.PluginMessageHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class Hub extends OrbitMinesServer {

    public Hub(OrbitMines orbitMines, PluginMessageHandler messageHandler) {
        super(orbitMines, Server.HUB, messageHandler);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public OMPlayer newPlayerInstance(Player player) {
        return null;
    }

    @Override
    public boolean teleportToSpawn(Player player) {
        return false;
    }

    @Override
    public Location getSpawnLocation(Player player) {
        return null;
    }

    @Override
    public void registerEvents() {

    }

    @Override
    public void registerCommands() {

    }

    @Override
    public void registerRunnables() {

    }
}
