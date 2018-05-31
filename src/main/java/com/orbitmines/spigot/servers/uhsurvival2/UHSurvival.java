package com.orbitmines.spigot.servers.uhsurvival2;

import com.google.common.io.ByteArrayDataInput;
import com.orbitmines.api.PluginMessage;
import com.orbitmines.api.Server;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.OrbitMinesServer;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.PluginMessageHandler;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.map.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class UHSurvival extends OrbitMinesServer {

    private List<Map> maps;

    public UHSurvival(OrbitMines orbitMines) {
        super(orbitMines, Server.UHSURVIVAL, new PluginMessageHandler() {
            @Override
            public void onReceive(ByteArrayDataInput in, PluginMessage message) {

            }
        });
        this.maps = new ArrayList<>();
        maps.add(new Map(Bukkit.getWorlds().get(0), -10000, 10000, -10000, 10000));
    }

    @Override
    public void onEnable() {

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
    protected void registerEvents() {

    }

    @Override
    protected void registerCommands() {

    }

    @Override
    protected void registerRunnables() {

    }

    @Override
    public void setupNpc(String npcName, Location location) {

    }

    public Map getMap(World world){
        for(Map map : maps){
            if(map.getWorld().equals(world)){
                return map;
            }
        }
        return null;
    }
}
