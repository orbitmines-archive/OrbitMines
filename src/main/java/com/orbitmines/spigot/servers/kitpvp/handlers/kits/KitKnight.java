package com.orbitmines.spigot.servers.kitpvp.handlers.kits;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PotionBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PotionItemBuilder;
import com.orbitmines.spigot.api.handlers.kit.Kit;
import com.orbitmines.spigot.servers.kitpvp.HealthRegen;
import com.orbitmines.spigot.servers.kitpvp.KitClass;
import com.orbitmines.spigot.servers.kitpvp.KitPvP;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPKit;
import com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders.KitItemBuilder;
import com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders.KitPotionItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffectType;

public class KitKnight extends KitPvPKit {

    public KitKnight(KitPvP kitPvP) {
        super(
                kitPvP,

                0L, "Knight",

                Color.GREEN,
                new ItemBuilder(Material.IRON_SWORD).addFlag(ItemFlag.HIDE_ATTRIBUTES),

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

            kit.setItem(0, new KitItemBuilder(this, Material.IRON_SWORD));
            kit.setItem(1, new KitPotionItemBuilder(this, PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.HEAL, 0)));

            kit.setHelmet(new KitItemBuilder(this, Material.CHAINMAIL_HELMET));
            kit.setChestplate(new KitItemBuilder(this, Material.CHAINMAIL_CHESTPLATE));
            kit.setLeggings(new KitItemBuilder(this, Material.CHAINMAIL_LEGGINGS));
            kit.setBoots(new KitItemBuilder(this, Material.CHAINMAIL_BOOTS));

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 20D;
        }

        @Override
        public double getKnockbackResistance() {
            return 0.0D;
        }

        @Override
        public HealthRegen getHealthRegen() {
            return HealthRegen.HIGHEST;
        }
    }

    protected class Level2 extends Level {

        @Override
        public int getPrice() {
            return 20000;
        }

        @Override
        protected Kit registerKit() {
            Kit kit = new Kit(name + "_2");

            kit.setItem(0, new KitItemBuilder(this, Material.IRON_SWORD));
            kit.setItem(1, new KitPotionItemBuilder(this, PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.HEAL, 1)));

            kit.setHelmet(new KitItemBuilder(this, Material.CHAINMAIL_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setChestplate(new KitItemBuilder(this, Material.CHAINMAIL_CHESTPLATE));
            kit.setLeggings(new KitItemBuilder(this, Material.CHAINMAIL_LEGGINGS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setBoots(new KitItemBuilder(this, Material.CHAINMAIL_BOOTS));

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 20D;
        }

        @Override
        public double getKnockbackResistance() {
            return 0.0D;
        }

        @Override
        public HealthRegen getHealthRegen() {
            return HealthRegen.HIGHEST;
        }
    }

    protected class Level3 extends Level {

        @Override
        public int getPrice() {
            return 50000;
        }

        @Override
        protected Kit registerKit() {
            Kit kit = new Kit(name + "_3");

            kit.setItem(0, new KitItemBuilder(this, Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1));
            kit.setItem(1, new KitPotionItemBuilder(this, PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.HEAL, 1), false, 2));

            kit.setHelmet(new KitItemBuilder(this, Material.CHAINMAIL_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setChestplate(new KitItemBuilder(this, Material.CHAINMAIL_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setLeggings(new KitItemBuilder(this, Material.CHAINMAIL_LEGGINGS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setBoots(new KitItemBuilder(this, Material.CHAINMAIL_BOOTS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 20D;
        }

        @Override
        public double getKnockbackResistance() {
            return 0.0D;
        }

        @Override
        public HealthRegen getHealthRegen() {
            return HealthRegen.HIGHEST;
        }
    }
}
