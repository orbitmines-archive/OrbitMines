package com.orbitmines.spigot.api.nms.itemstack;

import com.orbitmines.spigot.api.handlers.itembuilders.WrittenBookBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface ItemStackNms {

    ItemStack setCustomSkullTexture(ItemStack item, String name, String value);

    void openBook(Player player, WrittenBookBuilder builder);

    ItemStack setMetaData(ItemStack item, String key, String value);

    String getMetaData(ItemStack item, String key);

}
