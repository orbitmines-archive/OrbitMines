package com.orbitmines.spigot.api.events;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class VoidDamageEvent implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            event.setDamage(((LivingEntity) event.getEntity()).getHealth());

            Location respawnLocation = getRespawnLocation((Player) event.getEntity());
            if (respawnLocation != null)
                event.getEntity().teleport(respawnLocation);
        }
    }

    public Location getRespawnLocation(Player player) {
        return null;
    }
}
