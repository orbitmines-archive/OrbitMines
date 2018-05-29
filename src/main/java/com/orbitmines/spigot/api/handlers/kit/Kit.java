package com.orbitmines.spigot.api.handlers.kit;

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.utils.PlayerUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class Kit {
    private static List<Kit> kits = new ArrayList<>();

    private String name;
    private ItemBuilder[] armorContents;
    private ItemBuilder[] contents;
    private ItemBuilder itemOffHand;

    private List<PotionEffect> potionEffects;

    private CachedPlayer lastUsedBy;

    public Kit(String name) {
        kits.add(this);

        this.name = name;
        this.armorContents = new ItemBuilder[4];
        this.contents = new ItemBuilder[36];
        this.potionEffects = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemBuilder getHelmet() {
        return this.armorContents[3];
    }

    public void setHelmet(ItemBuilder helmet) {
        this.armorContents[3] = helmet;
    }

    public ItemBuilder getChestplate() {
        return this.armorContents[2];
    }

    public void setChestplate(ItemBuilder chestplate) {
        this.armorContents[2] = chestplate;
    }

    public ItemBuilder getLeggings() {
        return this.armorContents[1];
    }

    public void setLeggings(ItemBuilder leggings) {
        this.armorContents[1] = leggings;
    }

    public ItemBuilder getBoots() {
        return this.armorContents[0];
    }

    public void setBoots(ItemBuilder boots) {
        this.armorContents[0] = boots;
    }

    public ItemBuilder[] getArmorContents() {
        return armorContents;
    }

    public void setArmorContents(ItemBuilder[] armorContents) {
        this.armorContents = armorContents;
    }

    public ItemBuilder[] getContents() {
        return contents;
    }

    public void setContents(ItemBuilder[] contents) {
        this.contents = contents;
    }

    public void setItem(int index, ItemBuilder content) {
        this.contents[index] = content;
    }

    public ItemBuilder getItem(int index) {
        return this.contents[index];
    }

    public void setItemOffHand(ItemBuilder itemOffHand) {
        this.itemOffHand = itemOffHand;
    }

    public ItemBuilder getItemOffHand() {
        return itemOffHand;
    }

    public ItemBuilder getFirstItem() {
        for (ItemBuilder item : this.contents) {
            if (item != null)
                return item;
        }
        return null;
    }

    public int contentItems() {
        int amount = 0;
        for (ItemBuilder item : getContents()) {
            if (item != null)
                amount++;
        }
        return amount;
    }

    public List<PotionEffect> getPotionEffects() {
        return potionEffects;
    }

    public void addPotionEffect(PotionEffect potionEffect) {
        this.potionEffects.add(potionEffect);
    }

    public CachedPlayer getLastUsedBy() {
        return lastUsedBy;
    }

    public void setItems(OMPlayer omp) {
        lastUsedBy = CachedPlayer.getPlayer(omp.getUUID());

        PlayerInventory inventory = omp.getPlayer().getInventory();

        ItemStack[] armorContents = new ItemStack[4];
        int index = 0;
        for (ItemBuilder item : getArmorContents()) {
            if (item != null)
                armorContents[index] = item.build();
            else
                armorContents[index] = inventory.getArmorContents()[index];

            index++;
        }
        inventory.setArmorContents(armorContents);

        index = 0;
        for (ItemBuilder item : getContents()) {
            if (item != null)
                inventory.setItem(index, item.build());

            index++;
        }

        if (itemOffHand != null)
            inventory.setItemInOffHand(itemOffHand.build());

        if (potionEffects.size() != 0) {
            for (PotionEffect potionEffect : potionEffects) {
                omp.getPlayer().addPotionEffect(potionEffect);
            }
        }

        PlayerUtils.updateInventory(omp.getPlayer());
    }

    public void replaceItems(OMPlayer omp) {
        lastUsedBy = CachedPlayer.getPlayer(omp.getUUID());

        PlayerInventory inventory = omp.getPlayer().getInventory();

        ItemStack[] armorContents = new ItemStack[4];
        int index = 0;
        for (ItemBuilder item : getArmorContents()) {
            if (item != null && setItem(inventory.getArmorContents(), index, item.build()))
                armorContents[index] = item.build();
            else
                armorContents[index] = inventory.getArmorContents()[index];

            index++;
        }
        inventory.setArmorContents(armorContents);

        index = 0;
        for (ItemBuilder item : getContents()) {
            if (item != null && setItem(inventory.getContents(), index, item.build()))
                inventory.setItem(index, item.build());

            index++;
        }

        if (itemOffHand != null && setItem(itemOffHand.build(), inventory.getItemInOffHand()))
            inventory.setItemInOffHand(itemOffHand.build());

        if (potionEffects.size() != 0) {
            for (PotionEffect potionEffect : potionEffects) {
                omp.getPlayer().addPotionEffect(potionEffect);
            }
        }

        PlayerUtils.updateInventory(omp.getPlayer());
    }

    private boolean setItem(ItemStack item, ItemStack item2) {
        return item2 == null || item.getType() != item2.getType() || item.getAmount() != item2.getAmount() || !item.getItemMeta().getDisplayName().equals(item2.getItemMeta().getDisplayName());

    }

    private boolean setItem(ItemStack[] contents, int index, ItemStack item) {
        ItemStack item2 = contents[index];

        return item2 == null || item.getType() != item2.getType() || item.getAmount() != item2.getAmount() || !item.getItemMeta().getDisplayName().equals(item2.getItemMeta().getDisplayName());

    }

    public void addItems(OMPlayer omp) {
        lastUsedBy = CachedPlayer.getPlayer(omp.getUUID());

        PlayerInventory inventory = omp.getPlayer().getInventory();

        for (ItemBuilder item : getContents()) {
            if (item != null)
                inventory.addItem(item.build());
        }
        int index = 0;
        for (ItemBuilder item : getArmorContents()) {
            ItemStack item2 = inventory.getArmorContents()[index];

            if (item2 != null)
                inventory.addItem(item2);

            if (index == 0)
                inventory.setBoots(item.build());
            else if (index == 1)
                inventory.setLeggings(item.build());
            else if (index == 2)
                inventory.setChestplate(item.build());
            else
                inventory.setHelmet(item.build());

            index++;
        }

        if (itemOffHand != null) {
            if (inventory.getItemInOffHand() == null)
                inventory.setItemInOffHand(itemOffHand.build());
            else
                inventory.addItem(itemOffHand.build());
        }

        if (potionEffects.size() != 0) {
            for (PotionEffect potionEffect : potionEffects) {
                omp.getPlayer().addPotionEffect(potionEffect);
            }
        }

        PlayerUtils.updateInventory(omp.getPlayer());
    }

    public static Kit getKit(String name) {
        for (Kit kit : kits) {
            if (kit.getName().equals(name)) {
                return kit;
            }
        }
        return null;
    }
}
