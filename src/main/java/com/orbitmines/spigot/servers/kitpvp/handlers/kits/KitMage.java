package com.orbitmines.spigot.servers.kitpvp.handlers.kits;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.spigot.api.handlers.itembuilders.PotionBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PotionItemBuilder;
import com.orbitmines.spigot.api.handlers.kit.Kit;
import com.orbitmines.spigot.servers.kitpvp.HealthRegen;
import com.orbitmines.spigot.servers.kitpvp.KitClass;
import com.orbitmines.spigot.servers.kitpvp.KitPvP;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPKit;
import com.orbitmines.spigot.servers.kitpvp.handlers.passives.Passive;
import com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders.KitItemBuilder;
import com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders.KitPotionItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffectType;

public class KitMage extends KitPvPKit {

    public KitMage(KitPvP kitPvP) {
        super(
                kitPvP,

                3L, "Mage",

                Color.YELLOW,
                new PotionItemBuilder(PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.HARM, 0)).addFlag(ItemFlag.HIDE_POTION_EFFECTS),

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

    protected class Level1 extends Level {

        @Override
        public int getPrice() {
            return 10000;
        }

        @Override
        protected Kit registerKit() {
            Kit kit = new Kit(name + "_1");

            kit.setItem(0, new KitItemBuilder(this, Material.STONE_SWORD).addPassive(Passive.POTION_BREWER, 1));
            kit.setItem(1, new KitPotionItemBuilder(this, PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.FIRE_RESISTANCE, 30 * 20, 0)));
            kit.setItem(2, new KitPotionItemBuilder(this, PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.WEAKNESS, 15 * 20, 0)));
            kit.setItem(3, new KitPotionItemBuilder(this, PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.HARM, 0)));
            kit.setItem(4, new KitPotionItemBuilder(this, PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.SPEED, 60 * 20, 0)));
            kit.setItem(5, new KitPotionItemBuilder(this, PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.REGENERATION, 20 * 20, 0)));

            kit.setHelmet(new KitItemBuilder(this, Material.GOLDEN_HELMET));
            kit.setChestplate(new KitItemBuilder(this, Material.GOLDEN_CHESTPLATE));
            kit.setLeggings(new KitItemBuilder(this, Material.GOLDEN_LEGGINGS));
            kit.setBoots(new KitItemBuilder(this, Material.GOLDEN_BOOTS));

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 22D;
        }

        @Override
        public double getKnockbackResistance() {
            return 0.0D;
        }

        @Override
        public HealthRegen getHealthRegen() {
            return HealthRegen.HIGH;
        }
    }

    protected class Level2 extends Level {

        @Override
        public int getPrice() {
            return 25000;
        }

        @Override
        protected Kit registerKit() {
            Kit kit = new Kit(name + "_2");

            kit.setItem(0, new KitItemBuilder(this, Material.STONE_SWORD).addPassive(Passive.POTION_BREWER, 1));
            kit.setItem(1, new KitPotionItemBuilder(this, PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.FIRE_RESISTANCE, 35 * 20, 0)));
            kit.setItem(2, new KitPotionItemBuilder(this, PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.WEAKNESS, 17 * 20, 0)));
            kit.setItem(3, new KitPotionItemBuilder(this, PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.HARM, 1)));
            kit.setItem(4, new KitPotionItemBuilder(this, PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.SPEED, 60 * 20, 0)));
            kit.setItem(5, new KitPotionItemBuilder(this, PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.REGENERATION, 30 * 20, 0)));
            kit.setItem(6, new KitPotionItemBuilder(this, PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.SPEED, 10 * 20, 1)));

            kit.setHelmet(new KitItemBuilder(this, Material.GOLDEN_HELMET));
            kit.setChestplate(new KitItemBuilder(this, Material.GOLDEN_CHESTPLATE).addPassive(Passive.LIGHTNING_PROTECTION, 1));
            kit.setLeggings(new KitItemBuilder(this, Material.GOLDEN_LEGGINGS));
            kit.setBoots(new KitItemBuilder(this, Material.GOLDEN_BOOTS).addPassive(Passive.LIGHTNING_PROTECTION, 1));

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 22D;
        }

        @Override
        public double getKnockbackResistance() {
            return 0.0D;
        }

        @Override
        public HealthRegen getHealthRegen() {
            return HealthRegen.HIGH;
        }
    }

    protected class Level3 extends Level {

        @Override
        public int getPrice() {
            return 60000;
        }

        @Override
        protected Kit registerKit() {
            Kit kit = new Kit(name + "_3");

            kit.setItem(0, new KitItemBuilder(this, Material.STONE_SWORD).addPassive(Passive.POTION_BREWER, 1));
            kit.setItem(1, new KitPotionItemBuilder(this, PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.FIRE_RESISTANCE, 40 * 20, 0)));
            kit.setItem(2, new KitPotionItemBuilder(this, PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.WEAKNESS, 17 * 20, 0), false, 2));
            kit.setItem(3, new KitPotionItemBuilder(this, PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.HARM, 1), false, 2));
            kit.setItem(4, new KitPotionItemBuilder(this, PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.SPEED, 60 * 20, 0)));
            kit.setItem(5, new KitPotionItemBuilder(this, PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.REGENERATION, 32 * 20, 0)));
            kit.setItem(6, new KitPotionItemBuilder(this, PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.SPEED, 15 * 20, 1)));

            kit.setHelmet(new KitItemBuilder(this, Material.GOLDEN_HELMET).addPassive(Passive.LIGHTNING_PROTECTION, 1));
            kit.setChestplate(new KitItemBuilder(this, Material.GOLDEN_CHESTPLATE).addPassive(Passive.LIGHTNING_PROTECTION, 1));
            kit.setLeggings(new KitItemBuilder(this, Material.GOLDEN_LEGGINGS).addPassive(Passive.LIGHTNING_PROTECTION, 1));
            kit.setBoots(new KitItemBuilder(this, Material.GOLDEN_BOOTS).addPassive(Passive.LIGHTNING_PROTECTION, 1));

            return kit;
        }

        @Override
        public double getMaxHealth() {
            return 22D;
        }

        @Override
        public double getKnockbackResistance() {
            return 0.0D;
        }

        @Override
        public HealthRegen getHealthRegen() {
            return HealthRegen.HIGH;
        }
    }
}
