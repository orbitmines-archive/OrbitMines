package com.orbitmines.spigot.servers.kitpvp.handlers.passives;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.PluginManager;

import java.util.List;

public class PassiveBowLightning implements Passive.Handler<ProjectileHitEvent> {

    @Override
    public void trigger(ProjectileHitEvent event, int level) {
        Entity entity = event.getHitEntity();

        /* Only triggers if the arrow actually hit an entity. */
        if (entity == null)
            return;

        /* There's a chance of the lightning hitting, otherwise move on */
        if (Math.random() >= getChance(level))
            return;

        double damage = getDamage(level);

        /* Play effect */
        entity.getWorld().strikeLightningEffect(entity.getLocation());

        /* Damage nearby */
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        List<Entity> entities = entity.getNearbyEntities(0.5, 0.5, 0.5);
        entities.add(entity);

        for (Entity en : entities) {
            if (!(en instanceof LivingEntity))
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
                return 1D / 3D;
            case 2:
                return 2D / 3D;
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
    }
}
