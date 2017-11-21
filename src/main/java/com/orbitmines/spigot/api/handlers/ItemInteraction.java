package com.orbitmines.spigot.api.handlers;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public abstract class ItemInteraction {

    private static List<ItemInteraction> itemInteractions = new ArrayList<>();

    private final Material material;
    private final short durability;
    private final String displayName;
    private final List<String> lore;

    public ItemInteraction(Material material) {
        this(material, -1);
    }

    public ItemInteraction(Material material, int durability) {
        this(material, durability, null);
    }

    public ItemInteraction(Material material, String displayName) {
        this(material, -1, displayName, (List<String>) null);
    }

    public ItemInteraction(Material material, String displayName, String... lore) {
        this(material, -1, displayName, new ArrayList<>(Arrays.asList(lore)));
    }

    public ItemInteraction(Material material, String displayName, List<String> lore) {
        this(material, -1, displayName, lore);
    }

    public ItemInteraction(Material material, int durability, String displayName) {
        this(material, durability, displayName, (List<String>) null);
    }

    public ItemInteraction(Material material, int durability, String displayName, String... lore) {
        this(material, durability, displayName, new ArrayList<>(Arrays.asList(lore)));
    }

    public ItemInteraction(Material material, int durability, String displayName, List<String> lore) {
        itemInteractions.add(this);

        this.material = material;
        this.durability = (short) durability;
        this.displayName = displayName;
        this.lore = lore;
    }

    /* Cancelled by default, use event#setCancelled in order to allow if need be */
    public abstract void onInteract(OMPlayer omp, PlayerInteractEvent event, ItemStack itemStack);

    /* @Override to use */
    public void onLeftClick(OMPlayer omp, PlayerInteractEvent event, ItemStack itemStack) {}
    /* @Override to use */
    public void onRightClick(OMPlayer omp, PlayerInteractEvent event, ItemStack itemStack) {}

    public boolean equals(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        return itemStack.getType() == material && (durability == -1 || itemStack.getDurability() == durability) && (displayName == null || meta != null && meta.getDisplayName() != null && meta.getDisplayName().equals(displayName)) && (lore == null || lore.size() == 0 || meta != null && meta.getLore() != null && meta.getLore().equals(lore));
    }

    public Material getMaterial() {
        return material;
    }

    public short getDurability() {
        return durability;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getLore() {
        return lore;
    }

    public void unregister() {
        itemInteractions.remove(this);
    }

    public static List<ItemInteraction> getItemInteractions() {
        return itemInteractions;
    }
}
