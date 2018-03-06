package com.orbitmines.spigot.servers.uhsurvival.events;

import com.orbitmines.spigot.servers.uhsurvival.handlers.map.Map;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

/**
 * Created by Robin on 3/6/2018.
 */
public class MobEvent implements Listener {

    @EventHandler
    public void spawnEvent(EntitySpawnEvent event){
        Map map = World.getWorldByEnvironment(event.getLocation().getWorld().getEnvironment()).getMap();
        if(map != null){
            map.getMobs().spawn(event.getEntity());
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void deathEvent(EntityDeathEvent event){
        Map map = World.getWorldByEnvironment(event.getEntity().getWorld().getEnvironment()).getMap();
        if(map != null){
            map.getMobs().death(event.getEntity());
        }
    }
}
