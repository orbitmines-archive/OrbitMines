package com.orbitmines.spigot.servers.kitpvp.handlers.passives;

import com.orbitmines.api.Color;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.Mob;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class PassiveSummoner implements Passive.Handler<EntityDamageByEntityEvent> {


    @Override
    public void trigger(EntityDamageByEntityEvent event, int level) {
        if (!(event.getDamager() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity))
            return;

        if (Math.random() >= getChanceSpider(level))
            return;

        LivingEntity damager = (LivingEntity) event.getDamager();
        LivingEntity entity = (LivingEntity) event.getEntity();

        KitPvPPlayer player = KitPvPPlayer.getPlayer((Player) damager);

        Mob mob = isCaveSpider(level) ? Mob.SPIDER : Mob.CAVE_SPIDER;

        Monster spider = (Monster) mob.spawn(entity.getLocation());

        if (isInvisible(level)) {
            spider.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
        }

        spider.setCustomName(Color.PURPLE.getChatColor() + player.getName(true) + "'s spider");
        spider.setCustomNameVisible(true);

        player.playSound(Sound.ENTITY_WITCH_AMBIENT);

        spider.setTarget(entity);

        new BukkitRunnable() {

            @Override
            public void run() {
                spider.remove();
            }
        }.runTaskLater(OrbitMines.getInstance(), 10 * 20);
    }

    public double getChanceSpider(int level) {
        switch (level) {
            case 1:
                return 0.125D;
            case 2:
                return 0.145D;
            case 3:
                return 0.15D;
            default:
                return 0D;
        }
    }

    public boolean isCaveSpider(int level) {
        switch (level) {
            case 2:
                return !(Math.random() >= 0.05D);
            case 3:
                return !(Math.random() >= 0.08D);
            default:
                return false;
        }
    }

    public boolean isInvisible(int level) {
        switch (level) {
            case 3:
                return !(Math.random() >= 0.02D);
            default:
                return false;
        }
    }
}
