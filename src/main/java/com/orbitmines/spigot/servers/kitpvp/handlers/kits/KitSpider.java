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
import com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders.KitItemSkullBuilder;
import com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders.KitLeatherArmorBuilder;
import com.orbitmines.spigot.servers.kitpvp.handlers.passives.Passive;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class KitSpider extends KitPvPKit {

    public static final long ID = 10L;

    private static final org.bukkit.Color LEATHER_COLOR = org.bukkit.Color.BLACK;

    public KitSpider(KitPvP kitPvP) {
        super(
                kitPvP,

                ID, "Spider",

                Color.PURPLE,
                new ItemBuilder(Material.SPIDER_EYE),

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

    private class Level1 extends Level {


        @Override
        public int getPrice() {
            return 0;
        }

        @Override
        protected Kit registerKit() {
            Kit kit = new Kit(name + "_1");

            kit.setHelmet(new KitItemSkullBuilder(this, () -> "MHF_Spider", 1, getColor().getChatColor() + "§l" + name + "'s Head").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3));
            kit.setChestplate(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.CHESTPLATE, LEATHER_COLOR));
            kit.setLeggings(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.LEGGINGS, LEATHER_COLOR));
            kit.setBoots(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.BOOTS, LEATHER_COLOR).addPassive(Passive.SPIDER_CLIMB, 1));

            kit.setItem(0, new KitItemBuilder(this, Material.SPIDER_EYE).addPassive(Passive.SUMMONER, 1).addEnchantment(Enchantment.DAMAGE_ALL, 4));

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 18D;
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

    private class Level2 extends Level {


        @Override
        public int getPrice() {
            return 0;
        }

        @Override
        protected Kit registerKit() {
            Kit kit = new Kit(name + "_2");

            kit.setHelmet(new KitItemSkullBuilder(this, () -> "MHF_Spider", 1, getColor().getChatColor() + "§l" + name + "'s Head").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4));
            kit.setChestplate(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.CHESTPLATE, LEATHER_COLOR));
            kit.setLeggings(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.LEGGINGS, LEATHER_COLOR));
            kit.setBoots(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.BOOTS, LEATHER_COLOR).addPassive(Passive.SPIDER_CLIMB, 1));

            kit.setItem(0, new KitItemBuilder(this, Material.SPIDER_EYE).addPassive(Passive.SUMMONER, 2).addEnchantment(Enchantment.DAMAGE_ALL, 4));

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 18D;
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

    private class Level3 extends KitPvPKit.Level {


        @Override
        public int getPrice() {
            return 0;
        }

        @Override
        protected Kit registerKit() {
            Kit kit = new Kit(name + "_3");

            kit.setHelmet(new KitItemSkullBuilder(this, () -> "MHF_Spider", 1, getColor().getChatColor() + "§l" + name + "'s Head").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5));
            kit.setChestplate(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.CHESTPLATE, LEATHER_COLOR));
            kit.setLeggings(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.LEGGINGS, LEATHER_COLOR));
            kit.setBoots(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.BOOTS, LEATHER_COLOR).addPassive(Passive.SPIDER_CLIMB, 1));

            kit.setItem(0, new KitItemBuilder(this, Material.SPIDER_EYE).addPassive(Passive.SUMMONER, 3).addPassive(Passive.POISONOUS, 1).addEnchantment(Enchantment.DAMAGE_ALL, 4));

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 18D;
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
