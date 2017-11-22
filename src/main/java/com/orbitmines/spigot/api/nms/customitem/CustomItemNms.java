package com.orbitmines.spigot.api.nms.customitem;

import com.orbitmines.spigot.api.Mob;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface CustomItemNms {

    ItemStack hideFlags(ItemStack item, int... hideFlags);

    ItemStack setUnbreakable(ItemStack item);

    ItemStack setEggId(ItemStack item, Mob mob);

}
