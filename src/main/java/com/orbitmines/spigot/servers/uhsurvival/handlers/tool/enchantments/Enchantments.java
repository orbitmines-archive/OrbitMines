package com.orbitmines.spigot.servers.uhsurvival.handlers.tool.enchantments;

import com.orbitmines.spigot.servers.uhsurvival.UHSurvival;
import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.tool.Tool;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.Enchantment;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;

/**
 * Created by Robin on 2/27/2018.
 */
class Enchantments {

    private UHSurvival uhSurvival;

    Enchantments(UHSurvival uhSurvival, EnchantmentManager e){
        this.uhSurvival = uhSurvival;
        e.registerOutput(new Strike());
        e.registerOutput(new LightCrit());
        e.registerOutput(new DarkCrit());
        e.registerOutput(new LifeSteal());
        e.registerOutput(new Exploding());
        e.registerOutput(new Stun());
        e.registerOutput(new Ender());
        e.registerOutput(new ExplosiveDeath());
        e.registerOutput(new Reflect());
        e.registerOutput(new EnderLeggings());
    }

    private class Strike extends EnchantmentManager.EnchantmentOutput {

        Strike() {
            super(Enchantment.STRIKE, false, 100);
        }

        @Override
        public void output(Event event, int level) {
            if(event instanceof EntityDamageByEntityEvent){
                Tool tool = null;
                Entity entity = ((EntityDamageByEntityEvent) event).getDamager();
                /*if(UHPlayer.getPlayer(entity.getUniqueId()) != null){
                    tool = UHPlayer.getUHPlayer(entity.getUniqueId()).getUHInventory().getMainHand();
                } else if(uhSurvival.getMobManager().getMob(entity.getLocation(), entity.getUniqueId()) != null){
                    tool = uhSurvival.getMobManager().getMob(entity.getLocation(), entity.getUniqueId()).getInventory().getMainHand();
                }
                if(tool != null) {
                    float newAttackSpeed = (float) (tool.getAttackSpeed() - (0.25 * level));
                    if (tool.getAttackSpeed() != newAttackSpeed) {
                        tool.setAttackSpeed(newAttackSpeed);
                    }
                }*/
            }
        }
    }

    private class LightCrit extends EnchantmentManager.EnchantmentOutput {

        public LightCrit() {
            super(Enchantment.LIGHT_CRIT, false, 100);
        }

        @Override
        public void output(Event event, int level) {
            if(event instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
                World w = e.getDamager().getWorld();
                if(!e.isCancelled() && (w.getTime() >= 7000 && w.getTime() <= 18000)){
                    e.setDamage(e.getDamage() + (0.75 * level));
                }
            }
        }
    }

    private class DarkCrit extends EnchantmentManager.EnchantmentOutput {

        public DarkCrit() {
            super(Enchantment.DARK_CRIT, false, 100);
        }

        @Override
        public void output(Event event, int level) {
            if(event instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
                World w = e.getDamager().getWorld();
                if(!e.isCancelled() && (w.getTime() <= 7000 && w.getTime() >= 18000)){
                    e.setDamage(e.getDamage() + (0.75 * level));
                }
            }
        }
    }

    private class LifeSteal extends EnchantmentManager.EnchantmentOutput {

        public LifeSteal() {
            super(Enchantment.LIFE_STEAL, false, 33);
        }

        @Override
        public void output(Event event, int level) {
            if(event instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
                if (!e.isCancelled()) {
                    Entity damager = e.getDamager();
                    if (damager instanceof LivingEntity) {
                        LivingEntity d = (LivingEntity) damager;
                        if (d.getHealth() < 20) {
                            d.setHealth(d.getHealth() + (0.25 * level));
                        }
                    }
                }
            }
        }
    }

    private class Exploding extends EnchantmentManager.EnchantmentOutput {

        public Exploding() {
            super(Enchantment.EXPLODING, true, 50);
        }

        @Override
        public void output(Event event, int level) {
            if(event instanceof EntityDamageByEntityEvent){
                EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
                if(!e.isCancelled()){
                    Entity entity = e.getEntity();
                    if(entity instanceof LivingEntity){
                        entity.getWorld().createExplosion(entity.getLocation(), (0.25F * level), true);
                    }
                }
            }
        }
    }

    private class Stun extends EnchantmentManager.EnchantmentOutput {

        public Stun() {
            super(Enchantment.STUN, true, 50);
        }

        @Override
        public void output(Event event, int level) {
            if(event instanceof EntityDamageByEntityEvent){
                EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
                if(!e.isCancelled()){
                    Entity en = e.getEntity();
                    if(en instanceof Player){
                        UHPlayer p = UHPlayer.getUHPlayer(en.getUniqueId());
                        p.getFreezer().freeze(p, p.getLocation());
                        new BukkitRunnable(){

                            public void run(){
                                p.clearFreeze();
                            }

                        }.runTaskLater(uhSurvival.getOrbitMines(), 100L);
                    }
                }
            }
        }
    }

    private class Ender extends EnchantmentManager.EnchantmentOutput {

        public Ender() {
            super(Enchantment.ENDER, true, 25);
        }

        @Override
        public void output(Event event, int level) {
            if(event instanceof EntityDamageByEntityEvent){
                EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
                if(!e.isCancelled()){
                    Arrow a = (Arrow) e.getDamager();
                    if(a.getShooter() instanceof Entity){
                        ((Entity) a.getShooter()).teleport(e.getEntity().getLocation());
                    }
                }
            }
        }
    }

    private class ExplosiveDeath extends EnchantmentManager.EnchantmentOutput {

        public ExplosiveDeath() {
            super(Enchantment.EXPLOSIVE_DEATH, false, 33);
        }

        @Override
        public void output(Event event, int level) {
            if(event instanceof EntityDamageByEntityEvent){
                EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
                if(!e.isCancelled()){
                    if(e.getEntity() instanceof LivingEntity){
                        LivingEntity en = (LivingEntity) e.getEntity();
                        if(en.isDead()){
                            if(e.getDamager() instanceof Arrow){
                                Arrow a = (Arrow) e.getDamager();
                                if(a.getShooter() instanceof LivingEntity) {
                                    LivingEntity shooter = (LivingEntity) a.getShooter();
                                    shooter.getWorld().createExplosion(shooter.getLocation(), 0.05F * level);
                                } else {
                                    e.getEntity().getWorld().createExplosion(e.getDamager().getLocation(), 0.05F * level);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private class Reflect extends EnchantmentManager.EnchantmentOutput {

        public Reflect() {
            super(Enchantment.REFLECT, false, 25);
        }

        @Override
        public void output(Event event, int level) {
            if(event instanceof EntityDamageByEntityEvent){
                EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
                if(!e.isCancelled()){
                    if(e.getDamager() instanceof LivingEntity && e.getEntity() instanceof LivingEntity){
                        LivingEntity damager = (LivingEntity) e.getDamager();
                        e.setCancelled(true);
                        damager.damage(((EntityDamageByEntityEvent) event).getDamage(EntityDamageEvent.DamageModifier.ARMOR));
                    }
                }
            }
        }
    }

    private class EnderLeggings extends EnchantmentManager.EnchantmentOutput {

        public EnderLeggings() {
            super(Enchantment.ENDER_LEGGINGS, false, 100);
        }

        @Override
        public void output(Event event, int level) {
            if(event instanceof PlayerInteractEvent){
                PlayerInteractEvent e = (PlayerInteractEvent) event;
                if(!e.isCancelled()){
                    Player p = e.getPlayer();
                    HashSet<Material> m = null;
                    Block b = p.getTargetBlock(m, 30 * level);
                    if(b.getType() != Material.AIR){
                        Location newLoc = b.getLocation().add(0,1,0);
                        p.teleport(newLoc);
                        p.setHealth(p.getHealth() - 0.5 * level);
                    }

                }
            }
        }
    }

}
