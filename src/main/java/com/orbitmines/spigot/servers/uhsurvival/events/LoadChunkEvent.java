package com.orbitmines.spigot.servers.uhsurvival.events;

import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.Map;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

/**
 * Created by Robin on 3/6/2018.
 */
public class LoadChunkEvent implements Listener {

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event){
        if(event.isNewChunk()){
            Map map = World.getWorldByEnvironment(event.getWorld().getEnvironment()).getMap();
            if(map != null){
                map.getDungeons().buildRandomDungeon(event.getChunk(), 90 * MathUtils.randomInteger(3));
            }
        }
    }
}
