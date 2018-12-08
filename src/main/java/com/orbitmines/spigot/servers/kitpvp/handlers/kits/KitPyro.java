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

public class KitPyro extends KitPvPKit {

    public static final long ID = 7L;

    public KitPyro(KitPvP kitPvP) {
        super(
                kitPvP,

                ID, "Pyro",

                Color.YELLOW,
                new ItemBuilder(Material.GOLDEN_HELMET).addFlag(ItemFlag.HIDE_ATTRIBUTES),

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
            return 12500;
        }

        @Override
        protected Kit registerKit() {
            Kit kit = new Kit(name + "_1");

            kit.setItem(0, new KitItemBuilder(this, Material.STONE_SWORD).addEnchantment(Enchantment.FIRE_ASPECT, 1));

            kit.setHelmet(new KitItemBuilder(this, Material.GOLDEN_HELMET));
            kit.setChestplate(new KitItemBuilder(this, Material.GOLDEN_CHESTPLATE));
            kit.setLeggings(new KitItemBuilder(this, Material.GOLDEN_LEGGINGS));
            kit.setBoots(new KitItemBuilder(this, Material.GOLDEN_BOOTS));

            kit.addPotionEffect(new PotionBuilder(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, false, true).build());

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 19D;
        }

        @Override
        public double getKnockbackResistance() {
            return 0D;
        }

        @Override
        public HealthRegen getHealthRegen() {
            return HealthRegen.NORMAL;
        }
    }

    protected class Level2 extends Level {

        @Override
        public int getPrice() {
            return 30000;
        }

        @Override
        protected Kit registerKit() {
            Kit kit = new Kit(name + "_2");

            kit.setItem(0, new KitItemBuilder(this, Material.STONE_SWORD).addEnchantment(Enchantment.FIRE_ASPECT, 1));

            kit.setHelmet(new KitItemBuilder(this, Material.GOLDEN_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setChestplate(new KitItemBuilder(this, Material.GOLDEN_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setLeggings(new KitItemBuilder(this, Material.GOLDEN_LEGGINGS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setBoots(new KitItemBuilder(this, Material.GOLDEN_BOOTS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));

            kit.addPotionEffect(new PotionBuilder(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, false, true).build());

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 19D;
        }

        @Override
        public double getKnockbackResistance() {
            return 0D;
        }

        @Override
        public HealthRegen getHealthRegen() {
            return HealthRegen.NORMAL;
        }
    }

    protected class Level3 extends Level {

        @Override
        public int getPrice() {
            return 70000;
        }

        @Override
        protected Kit registerKit() {
            Kit kit = new Kit(name + "_3");

            kit.setItem(0, new KitItemBuilder(this, Material.IRON_SWORD).addEnchantment(Enchantment.FIRE_ASPECT, 1));
            kit.setItem(1, new KitItemBuilder(this, Material.BOW).addPassive(Passive.ARROW_REGEN, 20).addEnchantment(Enchantment.ARROW_FIRE, 1));
            kit.setItem(2, new KitItemBuilder(this, Material.ARROW, 5));

            kit.setHelmet(new KitItemBuilder(this, Material.GOLDEN_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setChestplate(new KitItemBuilder(this, Material.GOLDEN_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setLeggings(new KitItemBuilder(this, Material.GOLDEN_LEGGINGS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setBoots(new KitItemBuilder(this, Material.GOLDEN_BOOTS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));

            kit.addPotionEffect(new PotionBuilder(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, false, true).build());

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 19D;
        }

        @Override
        public double getKnockbackResistance() {
            return 0D;
        }

        @Override
        public HealthRegen getHealthRegen() {
            return HealthRegen.NORMAL;
        }
    }
}
