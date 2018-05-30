package com.orbitmines.spigot.api.nms.itemstack;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

/**
 * Created by Fadi on 30-4-2016.
 */
public class ItemStackNms_1_11_R1 implements ItemStackNms {

    public ItemStack setUnbreakable(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack setEggId(ItemStack item, EntityType entityType) {
        SpawnEggMeta meta = (SpawnEggMeta) item.getItemMeta();
        meta.setSpawnedType(entityType);
        item.setItemMeta(meta);

        return item;
    }
}
