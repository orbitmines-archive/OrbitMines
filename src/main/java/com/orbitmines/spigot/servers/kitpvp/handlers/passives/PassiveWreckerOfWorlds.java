package com.orbitmines.spigot.servers.kitpvp.handlers.passives;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.PluginManager;

import java.util.List;

public class PassiveWreckerOfWorlds implements Passive.Handler<EntityDamageByEntityEvent> {

    /* soldier doesnt get any dmg from the lightning he produces with his sword he still gets lightning dmg from all other sources. */
    //Lvl 1: 1/3 chance for 1.0 dmg
    //LvL 2: 2/3 chance for 1.5 dmg

    @Override
    public void trigger(EntityDamageByEntityEvent event, int level) {
        if (!(event.getDamager() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity))
            return;

        /* There's a chance of the lightning hitting, otherwise move on */
        if (Math.random() >= getChance(level))
            return;

        double damage = getDamage(level);

        Entity damager = event.getDamager();
        Entity entity = event.getEntity();

        /* Play effect */
        entity.getWorld().strikeLightningEffect(entity.getLocation());

        /* Damage nearby */
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        List<Entity> entities = entity.getNearbyEntities(0.5, 0.5, 0.5);
        entities.add(entity);

        for (Entity en : entities) {
            if (!(en instanceof LivingEntity) || entity == damager /* Damager doesn't get damaged by the lightning */)
                continue;

            EntityDamageEvent e = new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.LIGHTNING, damage);
            entity.setLastDamageCause(e);
            pluginManager.callEvent(e);
        }
    }

    public double getDamage(int level) {
        switch (level) {
            case 1:
                return 1.0D;
            case 2:
                return 1.5D;
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
    }

    public double getChance(int level) {
        switch (level) {
            case 1:
                return 0.3D;
            case 2:
                return 0.5D; //TODO: NERF! MAYBE COOLDOWN!
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
    }
}
