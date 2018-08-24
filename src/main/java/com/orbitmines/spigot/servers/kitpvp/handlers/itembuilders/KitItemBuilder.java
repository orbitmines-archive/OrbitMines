package com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class KitItemBuilder extends ItemBuilder {

    public KitItemBuilder(Material material) {
        super(material);
    }

    public KitItemBuilder(Material material, int amount) {
        super(material, amount);
    }

    public KitItemBuilder(Material material, int amount, String displayName) {
        super(material, amount, displayName);
    }

    public KitItemBuilder(Material material, int amount, String displayName, String... lore) {
        super(material, amount, displayName, lore);
    }

    public KitItemBuilder(ItemBuilder itemBuilder) {
        super(itemBuilder);
    }

    public KitItemBuilder(Material material, int amount, String displayName, List<String> lore) {
        super(material, amount, displayName, lore);
    }

    @Override
    public ItemStack build() {
        return super.build();
    }

    @Override
    protected ItemStack modify(ItemStack itemStack) {
        return super.modify(itemStack);
    }
}
