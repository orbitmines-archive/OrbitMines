package com.orbitmines.spigot.api.handlers.itembuilders;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class ItemBuilder {

    protected Material material;
    protected int amount;
    protected short damage;
    protected String displayName;
    protected String localizedName;
    protected List<String> lore;

    protected Map<Enchantment, Integer> enchantments;

    protected boolean glow;
    protected boolean unbreakable;

    protected Set<ItemFlag> itemFlags;

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(Material material, int amount) {
        this(material, amount, null);
    }

    public ItemBuilder(Material material, int amount, String displayName) {
        this(material, amount, displayName, (List<String>) null);
    }

    public ItemBuilder(Material material, int amount, String displayName, String... lore) {
        this(material, amount, displayName, new ArrayList<>(Arrays.asList(lore)));
    }

    public ItemBuilder(ItemBuilder itemBuilder) {
        this.material = itemBuilder.material;
        this.amount = itemBuilder.amount;
        this.damage = itemBuilder.damage;
        this.displayName = itemBuilder.displayName;
        this.lore = itemBuilder.lore == null ? new ArrayList<>() : new ArrayList<>(itemBuilder.lore);
        this.enchantments = new HashMap<>(itemBuilder.enchantments);
        this.glow = itemBuilder.glow;
        this.unbreakable = itemBuilder.unbreakable;
        this.itemFlags = itemBuilder.itemFlags;
    }

    public ItemBuilder(Material material, int amount, String displayName, List<String> lore) {
        this.material = material;
        this.amount = amount;
        this.damage = (short) 0;
        this.displayName = displayName;
        this.lore = lore == null ? new ArrayList<>() : lore;
        this.enchantments = new HashMap<>();
        this.glow = false;
        this.itemFlags = new HashSet<>();
    }

    public Material getMaterial() {
        return material;
    }

    public ItemBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public short getDamage() {
        return damage;
    }

    public ItemBuilder setDamage(short damage) {
        this.damage = damage;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ItemBuilder setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    public ItemBuilder setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
        return this;
    }

    public List<String> getLore() {
        return lore;
    }

    public ItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder addLore(String lore) {
        List<String> list = new ArrayList<>(this.lore);
        list.add(lore);
        this.lore = list;
        return this;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        enchantments.put(enchantment, level);
        return this;
    }

    public ItemBuilder setEnchantments(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public ItemBuilder glow() {
        glow = true;
        return this;
    }

    public ItemBuilder unbreakable() {
        return unbreakable(true);
    }

    public ItemBuilder unbreakable(boolean hide) {
        unbreakable = true;
        if (hide)
            addFlag(ItemFlag.HIDE_UNBREAKABLE);
        return this;
    }

    public ItemBuilder addFlag(ItemFlag itemFlag) {
        itemFlags.add(itemFlag);

        return this;
    }

    public Set<ItemFlag> getItemFlags() {
        return itemFlags;
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(material, amount, damage);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLocalizedName(localizedName);
        meta.setLore((lore == null || lore.size() == 0) ? null : new ArrayList<>(lore));
        for (ItemFlag itemFlag : itemFlags) {
            meta.addItemFlags(itemFlag);
        }
        itemStack.setItemMeta(meta);

        return modify(itemStack);
    }

    protected ItemStack modify(ItemStack itemStack) {
        if (enchantments.size() != 0) {
            if (material == Material.ENCHANTED_BOOK) {
                /* Enchanted Book */
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
                for (Enchantment enchantment : this.enchantments.keySet()) {
                    meta.addStoredEnchant(enchantment, this.enchantments.get(enchantment), true);
                }
                itemStack.setItemMeta(meta);
            } else {
                itemStack.addUnsafeEnchantments(new HashMap<>(this.enchantments));
            }
        } else if (glow) {
            itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);

            ItemMeta meta = itemStack.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemStack.setItemMeta(meta);
        }

        if (unbreakable) {
            ItemMeta meta = itemStack.getItemMeta();
            meta.setUnbreakable(true);
            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }

    public boolean equals(ItemStack itemStack) {
        if (itemStack == null)
            return material == null;

        ItemMeta meta = itemStack.getItemMeta();
        return itemStack.getType() == material && (damage == -1 || itemStack.getDurability() == damage) && (displayName == null || meta != null && meta.getDisplayName() != null && meta.getDisplayName().equals(displayName)) && (lore == null || lore.size() == 0 || meta != null && meta.getLore() != null && meta.getLore().equals(lore));
    }

    public ItemBuilder clone() {
        return new ItemBuilder(this);
    }
}
