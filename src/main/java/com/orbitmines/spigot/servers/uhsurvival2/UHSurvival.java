package com.orbitmines.spigot.servers.uhsurvival2;

import com.google.common.io.ByteArrayDataInput;
import com.orbitmines.api.PluginMessage;
import com.orbitmines.api.Server;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.OrbitMinesServer;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.PluginMessageHandler;
import com.orbitmines.spigot.servers.uhsurvival2.event.BreakBlockEvent;
import com.orbitmines.spigot.servers.uhsurvival2.event.PlayerInteractEvent;
import com.orbitmines.spigot.servers.uhsurvival2.event.PlayerMoveEvent;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.map.Map;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.map.block.BlockManager;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.map.dungeon.loottable.LootTableManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class UHSurvival extends OrbitMinesServer {

    private List<Map> maps;

    private BlockManager  blockManager;
    private static LootTableManager lootTableManager;

    public UHSurvival(OrbitMines orbitMines) {
        super(orbitMines, Server.UHSURVIVAL, new PluginMessageHandler() {
            @Override
            public void onReceive(ByteArrayDataInput in, PluginMessage message) {

            }
        });
        this.maps = new ArrayList<>();
        this.maps.add(new Map(Bukkit.getWorlds().get(0), -10000, 10000, -10000, 10000));
        this.blockManager = new BlockManager(this);
        lootTableManager = new LootTableManager(this);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        lootTableManager.serialize();
        for(Map map : maps){
            map.getDM().serialize();
        }
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
        registerEvents(new BreakBlockEvent(this),
                new PlayerMoveEvent(),
                new PlayerInteractEvent());

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

    public BlockManager getBM() {
        return blockManager;
    }

    public static LootTableManager getLM() {
        return lootTableManager;
    }
}
