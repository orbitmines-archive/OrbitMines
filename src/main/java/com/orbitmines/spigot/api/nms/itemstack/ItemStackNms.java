package com.orbitmines.spigot.api.nms.itemstack;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface ItemStackNms {

    ItemStack setUnbreakable(ItemStack item);

    ItemStack setEggId(ItemStack item, EntityType entityType);

    ItemStack setCustomSkullTexture(ItemStack item, String name, String value);
    
}
