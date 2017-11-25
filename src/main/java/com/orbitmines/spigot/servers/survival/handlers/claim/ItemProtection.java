package com.orbitmines.spigot.servers.survival.handlers.claim;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class ItemProtection {

    private static List<ItemProtection> items = new ArrayList<>();

    private final Location location;
    private final UUID owner;
    private final long expirationTimestamp;
    private final ItemStack itemStack;

    public ItemProtection(Location location, UUID owner, long expirationTimestamp, ItemStack itemStack) {
        this.location = location;
        this.owner = owner;
        this.expirationTimestamp = expirationTimestamp;
        this.itemStack = itemStack;

        items.add(this);
    }

    public Location getLocation() {
        return location;
    }

    public UUID getOwner() {
        return owner;
    }

    public long getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public static List<ItemProtection> getItems() {
        return items;
    }
}
