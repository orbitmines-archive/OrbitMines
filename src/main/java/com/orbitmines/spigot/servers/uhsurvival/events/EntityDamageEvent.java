package com.orbitmines.spigot.servers.uhsurvival.events;

import com.orbitmines.spigot.servers.uhsurvival.UHSurvival;
import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.Map;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

/**
 * Created by Robin on 3/6/2018.
 */
public class EntityDamageEvent implements Listener {

    private UHSurvival uhSurvival;

    public EntityDamageEvent(UHSurvival uhSurvival){
        this.uhSurvival = uhSurvival;
    }

    @EventHandler
    public void onDamageEvent(EntityDamageByEntityEvent event){
        Map map = World.getWorldByEnvironment(event.getDamager().getWorld().getEnvironment()).getMap();
        if(map != null){
            if(event.getDamager() instanceof Player){
                UHPlayer uhPlayer = UHPlayer.getUHPlayer(event.getDamager().getUniqueId());
                uhPlayer.attack(event.getEntity(), event);
            } else if(event.getDamager() instanceof Projectile){
                Projectile p = (Projectile) event.getDamager();
                if(p.getShooter() instanceof Entity && !(p.getShooter() instanceof Player)){
                    map.getMobs().attack(uhSurvival, (Entity) p.getShooter(), event);
                }
            } else {
                map.getMobs().attack(uhSurvival, event.getDamager(), event);
            }
        }
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event){
        Map map = World.getWorldByEnvironment(event.getEntity().getWorld().getEnvironment()).getMap();
        if(map != null){
            if(event.getProjectile() instanceof Projectile) {
                Projectile p = (Projectile) event.getProjectile();
                if(p.getShooter() instanceof Entity && !(p.getShooter() instanceof Player)) {
                    map.getMobs().attack(uhSurvival, ((Entity) p.getShooter()), event);
                } else if(p.getShooter() instanceof Player){
                    UHPlayer player = UHPlayer.getUHPlayer(p.getUniqueId());
                    if(player != null){
                        player.shoot(event);
                    }
                }
            }
        }
    }
}