package com.orbitmines.spigot.servers.kitpvp.handlers.kits;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.LeatherArmorBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PotionBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PotionItemBuilder;
import com.orbitmines.spigot.api.handlers.kit.Kit;
import com.orbitmines.spigot.servers.kitpvp.HealthRegen;
import com.orbitmines.spigot.servers.kitpvp.KitClass;
import com.orbitmines.spigot.servers.kitpvp.KitPvP;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPKit;
import com.orbitmines.spigot.servers.kitpvp.handlers.actives.Active;
import com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders.KitItemBuilder;
import com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders.KitLeatherArmorBuilder;
import com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders.KitPotionItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffectType;

public class KitBunny extends KitPvPKit {

    public static final long ID = 8L;

    private static final org.bukkit.Color LEATHER_COLOR = org.bukkit.Color.WHITE;

    public KitBunny(KitPvP kitPvP) {
        super(
                kitPvP,

                ID, "Bunny",

                Color.YELLOW,
                new ItemBuilder(Material.LEATHER_BOOTS).addFlag(ItemFlag.HIDE_ATTRIBUTES),

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

            kit.setItem(0, new KitItemBuilder(this, Material.STONE_SWORD));
            kit.setItem(1, new KitPotionItemBuilder(this, PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.POISON, 45 * 20, 0)));

            kit.setHelmet(new KitItemBuilder(this, Material.IRON_HELMET));
            kit.setChestplate(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.CHESTPLATE, LEATHER_COLOR));
            kit.setLeggings(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.LEGGINGS, LEATHER_COLOR));
            kit.setBoots(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.BOOTS, LEATHER_COLOR));

            kit.addPotionEffect(new PotionBuilder(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, true, false, true).build());
            kit.addPotionEffect(new PotionBuilder(PotionEffectType.JUMP, Integer.MAX_VALUE, 3, true, false, true).build());

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

    protected class Level2 extends Level {

        @Override
        public int getPrice() {
            return 30000;
        }

        @Override
        protected Kit registerKit() {
            Kit kit = new Kit(name + "_2");

            kit.setItem(0, new KitItemBuilder(this, Material.STONE_SWORD));
            kit.setItem(1, new KitPotionItemBuilder(this, PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.POISON, 45 * 20, 0)));
            kit.setItem(2, new KitItemBuilder(this, Material.SUGAR).addActive(Active.SUGAR_RUSH, 1));

            kit.setHelmet(new KitItemBuilder(this, Material.IRON_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setChestplate(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.CHESTPLATE, LEATHER_COLOR));
            kit.setLeggings(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.LEGGINGS, LEATHER_COLOR));
            kit.setBoots(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.BOOTS, LEATHER_COLOR));

            kit.addPotionEffect(new PotionBuilder(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, true, false, true).build());
            kit.addPotionEffect(new PotionBuilder(PotionEffectType.JUMP, Integer.MAX_VALUE, 3, true, false, true).build());

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

    protected class Level3 extends Level {

        @Override
        public int getPrice() {
            return 70000;
        }

        @Override
        protected Kit registerKit() {
            Kit kit = new Kit(name + "_3");

            kit.setItem(0, new KitItemBuilder(this, Material.STONE_SWORD));
            kit.setItem(1, new KitPotionItemBuilder(this, PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.POISON, 21 * 20, 1)));
            kit.setItem(2, new KitItemBuilder(this, Material.SUGAR).addActive(Active.SUGAR_RUSH, 1));
            kit.setItem(3, new KitItemBuilder(this, Material.CARROT, 1, "§6§lKnockback Carrot").addEnchantment(Enchantment.KNOCKBACK, 3).addEnchantment(Enchantment.DAMAGE_ALL, 2));

            kit.setHelmet(new KitItemBuilder(this, Material.IRON_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setChestplate(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.CHESTPLATE, LEATHER_COLOR).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setLeggings(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.LEGGINGS, LEATHER_COLOR).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            kit.setBoots(new KitLeatherArmorBuilder(this, LeatherArmorBuilder.Type.BOOTS, LEATHER_COLOR).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1));

            kit.addPotionEffect(new PotionBuilder(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, true, false, true).build());
            kit.addPotionEffect(new PotionBuilder(PotionEffectType.JUMP, Integer.MAX_VALUE, 3, true, false, true).build());

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
