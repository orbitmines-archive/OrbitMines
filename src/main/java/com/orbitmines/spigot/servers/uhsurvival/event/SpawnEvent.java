package com.orbitmines.spigot.servers.uhsurvival.event;

import com.orbitmines.spigot.servers.uhsurvival.UHSurvival;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.Map;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.world.ChunkLoadEvent;

public class SpawnEvent implements Listener {

    private UHSurvival uhSurvival;

    public SpawnEvent(UHSurvival uhSurvival){
        this.uhSurvival = uhSurvival;
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event){
        Map map = uhSurvival.getMap(event.getLocation().getWorld());
        if(map != null){
            if(map.getMM() != null){
                map.getMM().spawn(event.getEntity(), true);
            }
        }
    }

    @EventHandler
    public void onDead(EntityDeathEvent event){
        Map map = uhSurvival.getMap(event.getEntity().getWorld());
        if(map != null){
            map.die(event.getEntity());
        }
    }

    @EventHandler
    public void onLoad(ChunkLoadEvent event){
        Map map = uhSurvival.getMap(event.getWorld());
        if(map != null){
            if(map.getMM() != null){
                for(Entity entity : event.getChunk().getEntities()){
                    if(!(entity instanceof Player) && entity instanceof LivingEntity) {
                        map.getMM().spawn(entity, false);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event){
        if(event.getProjectile() instanceof Arrow){
            Map map = uhSurvival.getMap(event.getProjectile().getWorld());
            if(map != null){
                if(map.getMM() != null){
                    map.getMM().spawn(event.getProjectile(), true);
                }
            }
        }
    }


}
