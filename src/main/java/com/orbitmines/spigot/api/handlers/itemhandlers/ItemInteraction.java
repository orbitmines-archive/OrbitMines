package com.orbitmines.spigot.api.handlers.itemhandlers;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public abstract class ItemInteraction {

    private static List<ItemInteraction> itemInteractions = new ArrayList<>();

    private final ItemBuilder itemBuilder;

    public ItemInteraction(ItemBuilder itemBuilder) {
        this.itemBuilder = itemBuilder;

        itemInteractions.add(this);
    }

    /* Cancelled by default, use event#setCancelled in order to allow if need be */
    public abstract void onInteract(OMPlayer omp, PlayerInteractEvent event, ItemStack itemStack);

    /* @Override to use */
    public void onLeftClick(OMPlayer omp, PlayerInteractEvent event, ItemStack itemStack) {}
    /* @Override to use */
    public void onRightClick(OMPlayer omp, PlayerInteractEvent event, ItemStack itemStack) {}

    public ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    public void unregister() {
        itemInteractions.remove(this);
    }

    public static List<ItemInteraction> getItemInteractions() {
        return itemInteractions;
    }
}
