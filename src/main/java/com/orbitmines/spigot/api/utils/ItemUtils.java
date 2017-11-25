package com.orbitmines.spigot.api.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class ItemUtils {

    public static int FLAG_ENCHANTMENTS = 1;
    public static int FLAG_ATTRIBUTES_MODIFIERS = 2;
    public static int FLAG_UNBREAKABLE = 4;
    public static int FLAG_CAN_DESTROY = 8;
    public static int FLAG_CAN_PLACE_ON = 16;
    public static int FLAG_POTIONS = 32;

    public static boolean isNull(ItemStack item) {
        return isNull(item, false);
    }

    public static boolean isNull(ItemStack item, boolean checkLore){
        return item == null || item.getItemMeta() == null || item.getItemMeta().getDisplayName() == null && (!checkLore || item.getItemMeta().getLore() != null);
    }

    private static final List<Material> FARM_MATERIALS = Arrays.asList(
            Material.PUMPKIN_STEM,
            Material.CROPS,
            Material.MELON_STEM,
            Material.CARROT,
            Material.POTATO,
            Material.NETHER_WARTS,
            Material.BEETROOT_BLOCK
    );

    public static boolean isFarmMaterial(Material material) {
        return FARM_MATERIALS.contains(material);
    }

    public static final Set<PotionEffectType> POSITIVE_EFFECTS = new HashSet<>(Arrays.asList(
                    PotionEffectType.ABSORPTION,
                    PotionEffectType.DAMAGE_RESISTANCE,
                    PotionEffectType.FAST_DIGGING,
                    PotionEffectType.FIRE_RESISTANCE,
                    PotionEffectType.HEAL,
                    PotionEffectType.HEALTH_BOOST,
                    PotionEffectType.INCREASE_DAMAGE,
                    PotionEffectType.INVISIBILITY,
                    PotionEffectType.JUMP,
                    PotionEffectType.NIGHT_VISION,
                    PotionEffectType.REGENERATION,
                    PotionEffectType.SATURATION,
                    PotionEffectType.SPEED,
                    PotionEffectType.WATER_BREATHING
            ));
}
