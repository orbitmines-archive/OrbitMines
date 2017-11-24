package com.orbitmines.spigot.api.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

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
}
