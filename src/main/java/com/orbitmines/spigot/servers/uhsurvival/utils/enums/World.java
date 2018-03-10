package com.orbitmines.spigot.servers.uhsurvival.utils.enums;

import com.orbitmines.spigot.servers.uhsurvival.handlers.map.Map;
import org.bukkit.Bukkit;
import org.bukkit.World.Environment;

/**
 * Created by Robin on 2/27/2018.
 */
public enum World {

    WORLD(new Map(Bukkit.getWorlds().get(0))),
    NETHER(new Map(Bukkit.getWorlds().get(1))),
    END(new Map(Bukkit.getWorlds().get(2))),
    LOBBY(null);

    private Map map;

    World(Map map){
        this.map = map;
    }

    public Map getMap() {
        return map;
    }

    public boolean isLobby(){
        return this == LOBBY;
    }

    public static World getWorldByEnvironment(Environment environment){
        for(World world : World.values()){
            if(world.getMap().getWorld().getEnvironment() == environment){
                return world;
            }
        }
        return null;
    }
}
