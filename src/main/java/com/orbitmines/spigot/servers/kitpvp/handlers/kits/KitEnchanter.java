package com.orbitmines.spigot.servers.kitpvp.handlers.kits;

import com.orbitmines.api.Color;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.LeatherArmorBuilder;
import com.orbitmines.spigot.api.handlers.kit.Kit;
import com.orbitmines.spigot.servers.kitpvp.HealthRegen;
import com.orbitmines.spigot.servers.kitpvp.KitClass;
import com.orbitmines.spigot.servers.kitpvp.KitPvP;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPKit;
import com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders.KitItemBuilder;
import com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders.KitLeatherArmorBuilder;
import com.orbitmines.spigot.servers.kitpvp.handlers.passives.Passive;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class KitEnchanter extends KitPvPKit {

    private static final org.bukkit.Color DEFAULT_COLOR = org.bukkit.Color.PURPLE;

    public KitEnchanter(KitPvP kitPvP) {
        super(
                kitPvP,

                9L, "Enchanter",

                Color.PURPLE, new ItemBuilder(Material.ENCHANTED_BOOK),

                KitClass.SPELLCASTER
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

    private class Level1 extends KitPvPKit.Level {

        @Override
        public int getPrice() {
            return 12500;
        }

        @Override
        protected Kit registerKit() {
            Kit kit = new Kit(name + "_1");

            kit.setItem(0, new KitItemBuilder(this, Material.ENCHANTED_BOOK).addPassive(Passive.ATTACK_DAMAGE, 4).addEnchantment(Enchantment.DAMAGE_ALL, 4));

            kit.setHelmet(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.HELMET, DEFAULT_COLOR).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setChestplate(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.CHESTPLATE, DEFAULT_COLOR).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setLeggings(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.LEGGINGS, DEFAULT_COLOR).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setBoots(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.BOOTS, DEFAULT_COLOR).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 21;
        }

        @Override
        public double getKnockbackResistance() {
            return 0;
        }

        @Override
        public HealthRegen getHealthRegen() {
            return HealthRegen.NORMAL;
        }
    }

    private class Level2 extends KitPvPKit.Level {

        @Override
        public int getPrice() {
            return 30000;
        }

        @Override
        protected Kit registerKit() {
            Kit kit = new Kit(name + "_2");

            kit.setItem(0, new KitItemBuilder(this, Material.ENCHANTED_BOOK).addPassive(Passive.ENCHANTING_TABLE, 1).addPassive(Passive.ATTACK_DAMAGE, 4).addEnchantment(Enchantment.DAMAGE_ALL, 4));

            kit.setHelmet(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.HELMET, DEFAULT_COLOR).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setChestplate(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.CHESTPLATE, DEFAULT_COLOR).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setLeggings(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.LEGGINGS, DEFAULT_COLOR).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setBoots(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.BOOTS, DEFAULT_COLOR).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 21;
        }

        @Override
        public double getKnockbackResistance() {
            return 0;
        }

        @Override
        public HealthRegen getHealthRegen() {
            return HealthRegen.NORMAL;
        }
    }

    private class Level3 extends KitPvPKit.Level {

        @Override
        public int getPrice() {
            return 70000;
        }

        @Override
        protected Kit registerKit() {
            Kit kit = new Kit(name + "_3");

            kit.setItem(0, new KitItemBuilder(this, Material.ENCHANTED_BOOK).addPassive(Passive.ENCHANTING_TABLE, 1).addPassive(Passive.ATTACK_DAMAGE, 5).addEnchantment(Enchantment.DAMAGE_ALL, 5));

            kit.setHelmet(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.HELMET, DEFAULT_COLOR).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2));
            kit.setChestplate(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.CHESTPLATE, DEFAULT_COLOR).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2));
            kit.setLeggings(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.LEGGINGS, DEFAULT_COLOR).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setBoots(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.BOOTS, DEFAULT_COLOR).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 21;
        }

        @Override
        public double getKnockbackResistance() {
            return 0;
        }

        @Override
        public HealthRegen getHealthRegen() {
            return HealthRegen.NORMAL;
        }
    }
}
