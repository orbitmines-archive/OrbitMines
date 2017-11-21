package com.orbitmines.spigot.api.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class VoidDamageEvent implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID && event.getEntity() instanceof LivingEntity)
            event.setDamage(((LivingEntity) event.getEntity()).getHealth());
    }
}
