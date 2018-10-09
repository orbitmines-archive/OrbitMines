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
import org.bukkit.potion.PotionEffectType;

public class KitDrunk extends KitPvPKit {

    public KitDrunk(KitPvP kitPvP) {
        super(
                kitPvP,

                5L, "Drunk",

                Color.YELLOW,
                new ItemBuilder(Material.GLASS_BOTTLE),

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

            kit.setItem(0, new KitItemBuilder(this, Material.GLASS_BOTTLE).addPassive(Passive.BLEED, 1).addPassive(Passive.ATTACK_DAMAGE, 5));
            kit.setItem(1, new KitItemBuilder(this, Material.BOW).addPassive(Passive.ARROW_REGEN, 15));
            kit.setItem(2, new KitItemBuilder(this, Material.ARROW, 10));

            kit.setHelmet(new KitItemBuilder(this, Material.CHAINMAIL_HELMET).addPassive(Passive.LAST_BREATH, 1));
            kit.setChestplate(new KitItemBuilder(this, Material.CHAINMAIL_CHESTPLATE));
            kit.setLeggings(new KitItemBuilder(this, Material.CHAINMAIL_LEGGINGS));
            kit.setBoots(new KitItemBuilder(this, Material.CHAINMAIL_BOOTS));

            kit.addPotionEffect(new PotionBuilder(PotionEffectType.CONFUSION, Integer.MAX_VALUE, 1, true, false, true).build());

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 21D;
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
            return 0;
        }

        @Override
        protected Kit registerKit() {
            Kit kit = new Kit(name + "_2");

            kit.setItem(0, new KitItemBuilder(this, Material.GLASS_BOTTLE).addPassive(Passive.BLEED, 2).addPassive(Passive.ATTACK_DAMAGE, 6));
            kit.setItem(1, new KitItemBuilder(this, Material.BOW).addPassive(Passive.ARROW_REGEN, 13));
            kit.setItem(2, new KitItemBuilder(this, Material.ARROW, 11));

            kit.setHelmet(new KitItemBuilder(this, Material.CHAINMAIL_HELMET).addPassive(Passive.LAST_BREATH, 1));
            kit.setChestplate(new KitItemBuilder(this, Material.CHAINMAIL_CHESTPLATE));
            kit.setLeggings(new KitItemBuilder(this, Material.CHAINMAIL_LEGGINGS));
            kit.setBoots(new KitItemBuilder(this, Material.CHAINMAIL_BOOTS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));

            kit.addPotionEffect(new PotionBuilder(PotionEffectType.CONFUSION, Integer.MAX_VALUE, 1, true, false, true).build());

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 21D;
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
            return 0;
        }

        @Override
        protected Kit registerKit() {
            Kit kit = new Kit(name + "_3");

            kit.setItem(0, new KitItemBuilder(this, Material.GLASS_BOTTLE).addPassive(Passive.BLEED, 3).addPassive(Passive.ATTACK_DAMAGE, 6));
            kit.setItem(1, new KitItemBuilder(this, Material.BOW).addPassive(Passive.ARROW_REGEN, 10));
            kit.setItem(2, new KitItemBuilder(this, Material.ARROW, 12));

            kit.setHelmet(new KitItemBuilder(this, Material.DIAMOND_HELMET).addPassive(Passive.LAST_BREATH, 1));
            kit.setChestplate(new KitItemBuilder(this, Material.CHAINMAIL_CHESTPLATE));
            kit.setLeggings(new KitItemBuilder(this, Material.CHAINMAIL_LEGGINGS));
            kit.setBoots(new KitItemBuilder(this, Material.DIAMOND_BOOTS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));

            kit.addPotionEffect(new PotionBuilder(PotionEffectType.CONFUSION, Integer.MAX_VALUE, 1, true, false, true).build());

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 21D;
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
