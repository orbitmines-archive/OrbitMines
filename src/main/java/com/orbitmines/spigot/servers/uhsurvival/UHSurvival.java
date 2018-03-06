package com.orbitmines.spigot.servers.uhsurvival;

import com.google.common.io.ByteArrayDataInput;
import com.orbitmines.api.PluginMessage;
import com.orbitmines.api.Server;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.OrbitMinesServer;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.PluginMessageHandler;
import com.orbitmines.spigot.servers.uhsurvival.events.BreakBlockEvent;
import com.orbitmines.spigot.servers.uhsurvival.events.EntityDamageEvent;
import com.orbitmines.spigot.servers.uhsurvival.events.LoadChunkEvent;
import com.orbitmines.spigot.servers.uhsurvival.events.MobEvent;
import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.Map;
import com.orbitmines.spigot.servers.uhsurvival.handlers.profile.food.FoodManager;
import com.orbitmines.spigot.servers.uhsurvival.handlers.tool.enchantments.EnchantmentManager;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.World;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Playwarrior - 2017
*/
public class UHSurvival extends OrbitMinesServer {

    private FoodManager foodManager;
    private EnchantmentManager enchantmentManager;

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
        this.enchantmentManager = new EnchantmentManager(this);
        for(World world : World.values()){
            Map map = world.getMap();
            if(map != null){
                map.getDungeons().deserialize();
            }
        }
    }

    @Override
    public void onDisable() {
        for(World world : World.values()){
            if(world.getMap() != null){
                Map map = world.getMap();
                map.getDungeons().serialize();
            }
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
    public void registerEvents() {
        registerEvents(new BreakBlockEvent(),
                        new EntityDamageEvent(this),
                        new LoadChunkEvent(),
                        new MobEvent());
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

    public EnchantmentManager getEnchantmentManager() {
        return enchantmentManager;
    }
}
