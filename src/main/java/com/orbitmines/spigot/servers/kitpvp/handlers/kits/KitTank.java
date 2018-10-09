package com.orbitmines.spigot.servers.kitpvp.handlers.kits;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PotionBuilder;
import com.orbitmines.spigot.api.handlers.kit.Kit;
import com.orbitmines.spigot.servers.kitpvp.HealthRegen;
import com.orbitmines.spigot.servers.kitpvp.KitClass;
import com.orbitmines.spigot.servers.kitpvp.KitPvP;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPKit;
import com.orbitmines.spigot.servers.kitpvp.handlers.passives.Passive;
import com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders.KitItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffectType;

public class KitTank extends KitPvPKit {

    public KitTank(KitPvP kitPvP) {
        super(
                kitPvP,

                4L, "Tank",

                Color.YELLOW,
                new ItemBuilder(Material.IRON_CHESTPLATE).addFlag(ItemFlag.HIDE_ATTRIBUTES),

                KitClass.MELEE
        );
    }

    @Override
    protected Level[] registerLevels() {
        return new Level[]{
                new Level1(),
                new Level2(),
                new Level3()
        };
    }

    protected class Level1 extends Level {

        @Override
        public int getPrice() {
            return 0;
        }

        @Override
        protected Kit registerKit() {
            Kit kit = new Kit(name + "_1");

            kit.setItem(0, new KitItemBuilder(this, Material.WOODEN_SWORD).addPassive(Passive.SUCKER_PUNCH, 1).addEnchantment(Enchantment.KNOCKBACK, 1));

            kit.setHelmet(new KitItemBuilder(this, Material.IRON_HELMET));
            kit.setChestplate(new KitItemBuilder(this, Material.IRON_CHESTPLATE));
            kit.setLeggings(new KitItemBuilder(this, Material.IRON_LEGGINGS));
            kit.setBoots(new KitItemBuilder(this, Material.DIAMOND_BOOTS));

            kit.addPotionEffect(new PotionBuilder(PotionEffectType.SLOW, Integer.MAX_VALUE, 1, true, false, true).build());

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 25D;
        }

        @Override
        public double getKnockbackResistance() {
            return 0.25D;
        }

        @Override
        public HealthRegen getHealthRegen() {
            return HealthRegen.EXTREMELY_LOW;
        }
    }

    protected class Level2 extends Level {

        @Override
        public int getPrice() {
            return 0;
        }

        @Override
        protected Kit registerKit() {
            Kit kit = new Kit(name + "_2");

            kit.setItem(0, new KitItemBuilder(this, Material.WOODEN_SWORD).addPassive(Passive.SUCKER_PUNCH, 2).addEnchantment(Enchantment.KNOCKBACK, 1));

            kit.setHelmet(new KitItemBuilder(this, Material.IRON_HELMET));
            kit.setChestplate(new KitItemBuilder(this, Material.IRON_CHESTPLATE));
            kit.setLeggings(new KitItemBuilder(this, Material.IRON_LEGGINGS));
            kit.setBoots(new KitItemBuilder(this, Material.DIAMOND_BOOTS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2));

            kit.addPotionEffect(new PotionBuilder(PotionEffectType.SLOW, Integer.MAX_VALUE, 1, true, false, true).build());

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 25D;
        }

        @Override
        public double getKnockbackResistance() {
            return 0.25D;
        }

        @Override
        public HealthRegen getHealthRegen() {
            return HealthRegen.EXTREMELY_LOW;
        }
    }

    protected class Level3 extends Level {

        @Override
        public int getPrice() {
            return 0;
        }

        @Override
        protected Kit registerKit() {
            Kit kit = new Kit(name + "_3");

            kit.setItem(0, new KitItemBuilder(this, Material.STONE_SWORD).addPassive(Passive.SUCKER_PUNCH, 3).addEnchantment(Enchantment.KNOCKBACK, 2));

            kit.setHelmet(new KitItemBuilder(this, Material.IRON_HELMET));
            kit.setChestplate(new KitItemBuilder(this, Material.IRON_CHESTPLATE));
            kit.setLeggings(new KitItemBuilder(this, Material.IRON_LEGGINGS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2));
            kit.setBoots(new KitItemBuilder(this, Material.DIAMOND_BOOTS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2));

            kit.addPotionEffect(new PotionBuilder(PotionEffectType.SLOW, Integer.MAX_VALUE, 1, true, false, true).build());

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 25D;
        }

        @Override
        public double getKnockbackResistance() {
            return 0.25D;
        }

        @Override
        public HealthRegen getHealthRegen() {
            return HealthRegen.EXTREMELY_LOW;
        }
    }
}
