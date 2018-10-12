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
import com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders.KitItemBuilder;
import com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders.KitLeatherArmorBuilder;
import com.orbitmines.spigot.servers.kitpvp.handlers.passives.Passive;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

public class KitSoldier extends KitPvPKit {

    public KitSoldier(KitPvP kitPvP) {
        super(
                kitPvP,

                2L, "Soldier",

                Color.GREEN,
                new ItemBuilder(Material.IRON_LEGGINGS).addFlag(ItemFlag.HIDE_ATTRIBUTES),

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

            kit.setItem(0, new KitItemBuilder(this, Material.STONE_SWORD));
            kit.setItem(1, new KitItemBuilder(this, Material.BOW));
            kit.setItem(2, new KitItemBuilder(this, Material.ARROW, 10));

            kit.setHelmet(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.HELMET));
            kit.setChestplate(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.CHESTPLATE));
            kit.setLeggings(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.LEGGINGS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setBoots(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.BOOTS));

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 18D;
        }

        @Override
        public double getKnockbackResistance() {
            return 0.0D;
        }

        @Override
        public HealthRegen getHealthRegen() {
            return HealthRegen.NORMAL;
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

            kit.setItem(0, new KitItemBuilder(this, Material.IRON_AXE, 1, "§b§lJarnbjorn").addPassive(Passive.ATTACK_DAMAGE, 5).addPassive(Passive.WRECKER_OF_WORLDS, 1));
            kit.setItem(1, new KitItemBuilder(this, Material.BOW));
            kit.setItem(2, new KitItemBuilder(this, Material.ARROW, 10));

            kit.setHelmet(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.HELMET).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1));
            kit.setChestplate(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.CHESTPLATE));
            kit.setLeggings(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.LEGGINGS));
            kit.setBoots(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.BOOTS));

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 18D;
        }

        @Override
        public double getKnockbackResistance() {
            return 0.0D;
        }

        @Override
        public HealthRegen getHealthRegen() {
            return HealthRegen.NORMAL;
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

            kit.setItem(0, new KitItemBuilder(this, Material.IRON_AXE, 1, "§b§lJarnbjorn").addPassive(Passive.ATTACK_DAMAGE, 5).addPassive(Passive.WRECKER_OF_WORLDS, 2));
            kit.setItem(1, new KitItemBuilder(this, Material.BOW));
            kit.setItem(2, new KitItemBuilder(this, Material.ARROW, 10));

            kit.setHelmet(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.HELMET).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1));
            kit.setChestplate(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.CHESTPLATE));
            kit.setLeggings(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.LEGGINGS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setBoots(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.BOOTS));

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 18D;
        }

        @Override
        public double getKnockbackResistance() {
            return 0.0D;
        }

        @Override
        public HealthRegen getHealthRegen() {
            return HealthRegen.NORMAL;
        }
    }
}
