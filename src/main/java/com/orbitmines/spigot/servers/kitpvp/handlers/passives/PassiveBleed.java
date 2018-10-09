package com.orbitmines.spigot.servers.kitpvp.handlers.passives;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.handlers.timer.Timer;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;

public class PassiveBleed implements Passive.Handler<EntityDamageByEntityEvent> {

    private final Map<LivingEntity, Timer> bleedTimers = new HashMap<>();

    private final Map<Block, Timer> blockTimers = new HashMap<>();

    @Override
    public void trigger(EntityDamageByEntityEvent event, int level) {
        if (!(event.getDamager() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity))
            return;

        /* There's a chance of the lightning hitting, otherwise move on */
        if (Math.random() >= getChance(level))
            return;

        LivingEntity damager = (LivingEntity) event.getDamager();
        LivingEntity entity = (LivingEntity) event.getEntity();
        double damage = getDamage(level);
        int seconds = getSeconds(level);
        double damagePerTick = damage / (seconds * 2); /* 2 Damage ticks per second */

        if (bleedTimers.containsKey(entity))
            bleedTimers.get(entity).cancel();

        bleedTimers.put(entity, new Timer(new SpigotRunnable.Time(SpigotRunnable.TimeUnit.SECOND, seconds), new SpigotRunnable.Time(SpigotRunnable.TimeUnit.TICK, 10)) {
            @Override
            public void onFinish() {
                bleedTimers.remove(entity);
            }

            @Override
            public void onInterval() {
                if (entity.isDead() || entity instanceof Player && (KitPvPPlayer.getPlayer((Player) entity) == null || KitPvPPlayer.getPlayer((Player) entity).getSelectedKit() == null)) {
                    cancel();
                    bleedTimers.remove(entity);
                    return;
                }

                entity.damage(damagePerTick);
                entity.getWorld().playEffect(entity.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);

                /* Redstone effect on ground */
                Block block = entity.getLocation().getBlock();

                if (block.getType() != Material.AIR || block.getRelative(BlockFace.DOWN).getBlockData() instanceof Waterlogged /* Is waterlogged if not full block */)
                    return;

                if (blockTimers.containsKey(block))
                    blockTimers.get(block).cancel();
                else
                    block.setType(Material.REDSTONE_WIRE);

                blockTimers.put(block, new Timer(new SpigotRunnable.Time(SpigotRunnable.TimeUnit.SECOND, seconds)) {
                    @Override
                    public void onFinish() {
                        block.setType(Material.AIR);
                        blockTimers.remove(block);
                    }
                });
            }
        });
    }

    public double getDamage(int level) {
        return 4.0D;
    }

    public double getChance(int level) {
        switch (level) {
            case 1:
                return 0.125D;
            case 2:
                return 0.150D;
            case 3:
                return 0.250D;
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
    }

    public int getSeconds(int level) {
        return 4;
    }
}
