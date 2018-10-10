package com.orbitmines.spigot.servers.uhsurvival.handlers.map.dungeon.loottable;

import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.utils.MathUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class LootItem extends ItemBuilder {

    private int maxAmount;
    private double chance;

    LootItem(Material m, byte data, int maxAmount, double chance){
        super(m, data);
        this.maxAmount = maxAmount;
        this.chance = chance;
    }

    /* BUILD METHODS */
    @Override
    public ItemStack build() {
        this.setAmount(MathUtils.randomInteger(maxAmount));
        return super.build();
    }

    /* GETTERS */
    public double getChance() {
        return chance;
    }

    /* OVERRIDEABLE METHODS */
    @Override
    public String toString() {
        return getMaterial().name() + "|" + maxAmount +"|" + chance;
    }
}
