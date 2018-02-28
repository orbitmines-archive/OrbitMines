package com.orbitmines.spigot.servers.uhsurvival.handlers.mob;

import com.orbitmines.spigot.servers.uhsurvival.events.UHEvents.MobAttackEvent;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.Map;
import org.bukkit.entity.Entity;

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

    public void spawn(Entity entity){
        MobType type = getMobType(entity);
        if(type != null) {
            Mob mob = new Mob(type, entity);
            this.mobs.put(entity.getUniqueId(), mob);
            this.map.getMapSection(entity.getLocation()).addMob(mob);
            mob.spawn();
        }
    }

    public void attack(MobAttackEvent event){
        MobType type = getMobType(event.getMob().getEntity());
        if(type != null){
            type.attack(event);
        }
    }

    public void death(Entity entity){
       Mob mob = map.getMapSection(entity.getLocation()).removeMob(entity);
       if(mob != null){
           this.mobs.remove(entity.getUniqueId(), mob);
           mob.death();
       }
    }

    public HashMap<UUID, Mob> getMobs() {
        return mobs;
    }

    public MobType getMobType(Entity entity){
        for(MobType type : types){
            if(type.getType().getMob().getType() == entity.getType()){
                return type;
            }
        }
        return null;
    }
}
