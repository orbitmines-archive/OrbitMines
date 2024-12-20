package com.orbitmines.spigot.api.nms.itemstack;

import com.orbitmines.spigot.api.handlers.itembuilders.WrittenBookBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface ItemStackNms {

    ItemStack setCustomSkullTexture(ItemStack item, String name, String value);

    void openBook(Player player, WrittenBookBuilder builder);

    ItemStack setMetaData(ItemStack item, String tagName, String key, String value);

    String getMetaData(ItemStack item, String tagName, String key);

    Map<String, String> getMetaData(ItemStack item, String tagName);

    ItemStack setAttackDamage(ItemStack item, int damage);

}
