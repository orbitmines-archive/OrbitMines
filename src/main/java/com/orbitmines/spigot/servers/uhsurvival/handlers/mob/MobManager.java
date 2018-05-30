package com.orbitmines.spigot.servers.uhsurvival.handlers.mob;

import com.orbitmines.spigot.servers.uhsurvival.UHSurvival;
import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.Map;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.mapsection.MapSection;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Robin on 2/28/2018.
 */
public class MobManager {

    private Map map;

    private List<MobType> types;
    private HashMap<UUID, Mob> mobs;

    public MobManager(Map map){
        this.map = map;
        this.types = new ArrayList<>();
        this.mobs = new HashMap<>();
    }

    /* MOB METHODS */
    public void spawn(Entity entity){
        MobType type = getMobType(entity);
        if(type != null) {
            Mob mob = new Mob(type, entity, getLevel(entity.getLocation()));
            this.mobs.put(entity.getUniqueId(), mob);
            this.map.getMapSection(entity.getLocation()).addMob(mob);
            mob.spawn();
        }
    }

    public void attack(UHSurvival uhSurvival, Entity entity, Event event){
        Mob mob = map.getMapSection(entity.getLocation()).getMob(entity);
        if(mob != null) {
            if (event instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) event;
                Entity e = ev.getEntity();
                if (e instanceof Player) {
                    UHPlayer.getUHPlayer(e.getUniqueId()).protect(entity, event);
                } else {
                    Mob m = mobs.get(e.getUniqueId());
                    if (m != null) {
                        m.protect(uhSurvival, event);
                    }
                }
                mob.getType().attack(event);
                if (!ev.isCancelled()) {
                    mob.getType().attack(event);
                }
            }
            if (mob.getType().getType().canHoldItems() && event instanceof EntityShootBowEvent) {
                mob.shoot(uhSurvival, ((EntityShootBowEvent)event));
            }
        }
    }

    public void death(Entity entity){
       Mob mob = map.getMapSection(entity.getLocation()).removeMob(entity);
       if(mob != null){
           this.mobs.remove(entity.getUniqueId(), mob);
           mob.death();
       }
    }

    public void acquireTarget(UHPlayer player){
        if(!player.isCloseToEdge()) {
            for (Mob mob : player.getSection().getMobs()) {
                if(mob.isInRange(player.getLocation())) {
                    if (mob.hasTarget()) {
                        if(mob.getTarget().getLocation().distance(mob.getEntity().getLocation()) > player.getLocation().distance(mob.getEntity().getLocation())){
                            mob.setTarget(player);
                        }
                    } else {
                        mob.setTarget(player);
                    }
                }
            }
        }
    }

    /* GETTERS */
    public HashMap<UUID, Mob> getMobs() {
        return mobs;
    }

    private MobType getMobType(Entity entity){
        for(MobType type : types){
            if(type.getType().getMob().getType() == entity.getType()){
                return type;
            }
        }
        return null;
    }

    private int getLevel(Location location){
        MapSection mapSection = map.getMapSection(location);
        //TODO: ADD LEVEL SYSTEM WITH SECTIONS!
        return 0;
    }
}
