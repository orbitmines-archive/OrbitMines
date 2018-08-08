package com.orbitmines.spigot.servers.uhsurvival2.handlers.mob;

import com.orbitmines.spigot.servers.uhsurvival2.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.map.Map;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.mob.mobs.Zombie;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MobManager {

    private static HashMap<EntityType, MobType> mobTypes = new HashMap<>();

    private Map map;

    public MobManager(Map map) {
        this.map = map;
        mobTypes.put(EntityType.ZOMBIE, new Zombie());
        mobTypes.put(EntityType.ARROW, new com.orbitmines.spigot.servers.uhsurvival2.handlers.mob.mobs.Arrow());
        this.init();
    }

    public void spawn(Entity entity, boolean spawned) {
        MobType type = mobTypes.get(entity.getType());
        Mob mob = null;
        if(entity.getType() == EntityType.ARROW){
            org.bukkit.entity.Arrow arrow = (org.bukkit.entity.Arrow) entity;
            Attacker shooter;
            if(arrow.getShooter() instanceof Player){
                shooter = UHPlayer.getUHPlayer(((Player) arrow.getShooter()));
            } else {
                shooter = map.getMob((Entity) arrow.getShooter());
            }
            if(shooter != null) {
                mob = new Arrow(type, entity, shooter);
            }
        } else {
            mob = new Mob(type, entity);
            if (mob.hasMobType() && spawned) {
                mob.getType().spawn(mob);
            }
        }
        if(mob != null) {
            map.spawn(mob);
        }
    }

    public void onDeath(Mob mob) {
        if (mob != null && mob.hasMobType()) {
            mob.getType().die(mob);
        }
    }

    private void init() {
        int i = 0;
        for (Entity entity : map.getWorld().getEntities()) {
            if (!(entity instanceof Player) && entity instanceof LivingEntity) {
                Bukkit.getLogger().info("Init Mob #" + i + "[" + entity.getType().name() + "]");
                spawn(entity, false);
                i++;
            }
        }
    }
}
