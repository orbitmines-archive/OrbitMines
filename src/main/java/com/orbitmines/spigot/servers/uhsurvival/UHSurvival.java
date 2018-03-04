package com.orbitmines.spigot.servers.uhsurvival;

import com.google.common.io.ByteArrayDataInput;
import com.orbitmines.api.PluginMessage;
import com.orbitmines.api.Server;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.OrbitMinesServer;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.PluginMessageHandler;
import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.profile.food.FoodManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Playwarrior - 2017
*/
public class UHSurvival extends OrbitMinesServer {

    private FoodManager foodManager;

    public UHSurvival(OrbitMines orbitMines) {
        super(orbitMines, Server.UHSURVIVAL, new PluginMessageHandler() {
            @Override
            public void onReceive(ByteArrayDataInput in, PluginMessage message) {

            }
        });
    }

    @Override
    public void onEnable() {
        this.foodManager = new FoodManager(this);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public OMPlayer newPlayerInstance(Player player) {
        return new UHPlayer(this, player);
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

    @Override
    public void setupNpc(String npcName, Location location) {

    }

    public FoodManager getFoodManager() {
        return foodManager;
    }
}
