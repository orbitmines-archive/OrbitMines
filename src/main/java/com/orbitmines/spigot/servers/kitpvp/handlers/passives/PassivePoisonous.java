package com.orbitmines.spigot.servers.kitpvp.handlers.passives;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PassivePoisonous implements Passive.Handler<EntityDamageByEntityEvent> {

    @Override
    public void trigger(EntityDamageByEntityEvent event, int level) {
        if(!(event.getEntity() instanceof LivingEntity))
            return;

        if(Math.random() >= getChance(level))
            return;

        LivingEntity entity = (LivingEntity) event.getEntity();

        entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, getDuration(level) * 20,  level));
    }

    public static double getChance(int level){
        switch(level){
            case 1:
                return 0.05D;
            default:
                return 0D;
        }
    }

    public static int getDuration(int level){
        switch(level){
            case 1:
                return 4;
            default:
                return 0;
        }
    }
}
