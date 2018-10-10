package com.orbitmines.spigot.servers.kitpvp.handlers.passives;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.OrbitMines;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class PassiveSuckerPunch implements Passive.Handler<EntityDamageByEntityEvent> {

    /* Chance of applying knockback enchantment 1: 50%, 2: 55%, 3: 60%*/

    @Override
    public void trigger(EntityDamageByEntityEvent event, int level) {
        if (!(event.getDamager() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity))
            return;

        LivingEntity damager = (LivingEntity) event.getDamager();
        EntityEquipment equipment = damager.getEquipment();
        ItemStack item = equipment.getItemInMainHand();

        /* No knockback on item */
        if (item == null || item.getEnchantments().containsKey(Enchantment.KNOCKBACK))
            return;

        /* There's a chance of the knockback applying */
        if (Math.random() >= getChance(level)) {
            int enchLevel = item.getEnchantmentLevel(Enchantment.KNOCKBACK);
            item.getEnchantments().remove(Enchantment.KNOCKBACK);
            equipment.setItemInMainHand(item);

            new BukkitRunnable() {
                @Override
                public void run() {
                    item.addEnchantment(Enchantment.KNOCKBACK, enchLevel);
                    equipment.setItemInMainHand(item);
                }
            }.runTaskLater(OrbitMines.getInstance(), 1);
        }

        /* Knockback gets applied */
    }

    public double getChance(int level) {
        return 0.50D + (0.05D * (level -1));
    }
}
