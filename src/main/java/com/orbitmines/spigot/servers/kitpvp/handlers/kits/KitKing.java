package com.orbitmines.spigot.servers.kitpvp.handlers.kits;

import com.orbitmines.api.Color;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.kit.Kit;
import com.orbitmines.spigot.servers.kitpvp.HealthRegen;
import com.orbitmines.spigot.servers.kitpvp.KitClass;
import com.orbitmines.spigot.servers.kitpvp.KitPvP;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPKit;
import com.orbitmines.spigot.servers.kitpvp.handlers.actives.Active;
import com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders.KitItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

public class KitKing extends KitPvPKit {


    public KitKing(KitPvP kitPvP) {
        super(
                kitPvP,

                5L, "King",

                Color.YELLOW,
                new ItemBuilder(Material.DIAMOND_HELMET).addFlag(ItemFlag.HIDE_ATTRIBUTES),

                KitClass.MELEE);
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
            return 10000;
        }

        @Override
        protected Kit registerKit() {
            Kit kit = new Kit(name+ "_1");

            kit.setHelmet(new KitItemBuilder(this, Material.DIAMOND_HELMET));
            kit.setChestplate(new KitItemBuilder(this, Material.GOLDEN_CHESTPLATE));
            kit.setLeggings(new KitItemBuilder(this, Material.GOLDEN_LEGGINGS));
            kit.setBoots(new KitItemBuilder(this, Material.GOLDEN_BOOTS));

            kit.setItem(0, new KitItemBuilder(this, Material.STONE_SWORD));

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 20D;
        }

        @Override
        public double getKnockbackResistance() {
            return 0D;
        }

        @Override
        public HealthRegen getHealthRegen() {
            return HealthRegen.MEDIUM;
        }
    }

    private class Level2 extends Level {

        @Override
        public int getPrice() {
            return 25000;
        }

        @Override
        protected Kit registerKit() {
            Kit kit = new Kit(name+ "_2");

            kit.setHelmet(new KitItemBuilder(this, Material.DIAMOND_HELMET));
            kit.setChestplate(new KitItemBuilder(this, Material.GOLDEN_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setLeggings(new KitItemBuilder(this, Material.GOLDEN_LEGGINGS));
            kit.setBoots(new KitItemBuilder(this, Material.GOLDEN_BOOTS));

            kit.setItem(0, new KitItemBuilder(this, Material.STONE_SWORD).addActive(Active.HEAL, 1));

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 20D;
        }

        @Override
        public double getKnockbackResistance() {
            return 0D;
        }

        @Override
        public HealthRegen getHealthRegen() {
            return HealthRegen.MEDIUM;
        }
    }
    private class Level3 extends Level {

        @Override
        public int getPrice() {
            return 60000;
        }

        @Override
        protected Kit registerKit() {
            Kit kit = new Kit(name+ "_3");

            kit.setHelmet(new KitItemBuilder(this, Material.DIAMOND_HELMET));
            kit.setChestplate(new KitItemBuilder(this, Material.GOLDEN_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2));
            kit.setLeggings(new KitItemBuilder(this, Material.GOLDEN_LEGGINGS));
            kit.setBoots(new KitItemBuilder(this, Material.GOLDEN_BOOTS));

            kit.setItem(0, new KitItemBuilder(this, Material.STONE_SWORD).addActive(Active.HEAL, 2));

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 20D;
        }

        @Override
        public double getKnockbackResistance() {
            return 0D;
        }

        @Override
        public HealthRegen getHealthRegen() {
            return HealthRegen.MEDIUM;
        }
    }
}
