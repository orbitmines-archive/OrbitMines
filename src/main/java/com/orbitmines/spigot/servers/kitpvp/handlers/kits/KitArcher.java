package com.orbitmines.spigot.servers.kitpvp.handlers.kits;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.LeatherArmorBuilder;
import com.orbitmines.spigot.api.handlers.kit.Kit;
import com.orbitmines.spigot.servers.kitpvp.HealthRegen;
import com.orbitmines.spigot.servers.kitpvp.KitClass;
import com.orbitmines.spigot.servers.kitpvp.KitPvP;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPKit;
import com.orbitmines.spigot.servers.kitpvp.handlers.passives.Passive;
import com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders.KitItemBuilder;
import com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders.KitLeatherArmorBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class KitArcher extends KitPvPKit {

    public static final long ID = 1L;

    private static final org.bukkit.Color LEATHER_COLOR = org.bukkit.Color.fromRGB(51, 204, 255);

    public KitArcher(KitPvP kitPvP) {
        super(
                kitPvP,

                ID, "Archer",

                Color.GREEN,
                new ItemBuilder(Material.BOW),

                KitClass.RANGED
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

            kit.setItem(0, new KitItemBuilder(this, Material.WOODEN_SWORD));
            kit.setItem(1, new KitItemBuilder(this, Material.BOW).addPassive(Passive.ARROW_REGEN, 14));
            kit.setItem(2, new KitItemBuilder(this, Material.ARROW, 40));

            kit.setHelmet(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.HELMET, LEATHER_COLOR));
            kit.setChestplate(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.CHESTPLATE, LEATHER_COLOR));
            kit.setLeggings(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.LEGGINGS, LEATHER_COLOR));
            kit.setBoots(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.BOOTS, LEATHER_COLOR).addEnchantment(Enchantment.PROTECTION_FALL, 1));

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 16D;
        }

        @Override
        public double getKnockbackResistance() {
            return 0.0D;
        }

        @Override
        public HealthRegen getHealthRegen() {
            return HealthRegen.LOW;
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

            kit.setItem(0, new KitItemBuilder(this, Material.STONE_SWORD));
            kit.setItem(1, new KitItemBuilder(this, Material.BOW).addPassive(Passive.ARROW_REGEN, 12).addPassive(Passive.BOW_LIGHTNING, 1));
            kit.setItem(2, new KitItemBuilder(this, Material.ARROW, 46));

            kit.setHelmet(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.HELMET, LEATHER_COLOR).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setChestplate(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.CHESTPLATE, LEATHER_COLOR));
            kit.setLeggings(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.LEGGINGS, LEATHER_COLOR));
            kit.setBoots(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.BOOTS, LEATHER_COLOR).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.PROTECTION_FALL, 1));

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 16D;
        }

        @Override
        public double getKnockbackResistance() {
            return 0.0D;
        }

        @Override
        public HealthRegen getHealthRegen() {
            return HealthRegen.LOW;
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

            kit.setItem(0, new KitItemBuilder(this, Material.STONE_SWORD));
            kit.setItem(1, new KitItemBuilder(this, Material.BOW).addPassive(Passive.ARROW_REGEN, 10).addPassive(Passive.BOW_LIGHTNING, 2).addEnchantment(Enchantment.ARROW_DAMAGE, 1));
            kit.setItem(2, new KitItemBuilder(this, Material.ARROW, 54));

            kit.setHelmet(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.HELMET, LEATHER_COLOR).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setChestplate(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.CHESTPLATE, LEATHER_COLOR).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setLeggings(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.LEGGINGS, LEATHER_COLOR).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setBoots(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.BOOTS, LEATHER_COLOR).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.PROTECTION_FALL, 2));

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 16D;
        }

        @Override
        public double getKnockbackResistance() {
            return 0.0D;
        }

        @Override
        public HealthRegen getHealthRegen() {
            return HealthRegen.LOW;
        }
    }
}
