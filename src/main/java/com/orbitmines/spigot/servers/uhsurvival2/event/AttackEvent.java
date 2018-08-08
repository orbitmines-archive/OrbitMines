package com.orbitmines.spigot.servers.uhsurvival2.event;

import com.orbitmines.spigot.servers.uhsurvival2.UHSurvival;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.mob.Attacker;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.mob.Mob;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class AttackEvent implements Listener {

    private UHSurvival uhSurvival;

    public AttackEvent(UHSurvival uhSurvival){
        this.uhSurvival = uhSurvival;
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event){
        /* initialize variables */
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();

        Attacker attacker = null;
        Attacker defender = null;

        /* determining attacker */
        if(damager instanceof Player){
            attacker = UHPlayer.getUHPlayer(damager.getUniqueId());
        } else if(entity instanceof LivingEntity || entity instanceof Projectile){
            Mob mob = uhSurvival.getMap(damager.getLocation().getWorld()).getMob(damager);
            if(mob != null && mob.hasMobType()){
                if(mob.getType().getDamage() > 0){
                    event.setDamage(mob.getType().getDamage());
                }
            }
            attacker = mob;
        }

        /* determining defender */
        if(entity instanceof Player){
            defender = UHPlayer.getUHPlayer(entity.getUniqueId());
        } else if(entity instanceof LivingEntity || entity instanceof Projectile){
            defender = uhSurvival.getMap(entity.getWorld()).getMob(entity);
        }
        if(attacker != null && defender != null) {

            /* finalize attack */
            event.setCancelled(attacker.attack(defender));
        }
    }

}
