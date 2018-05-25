package com.orbitmines.spigot.api.handlers.itembuilders;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class WrittenBookBuilder {

    protected int amount;
    protected String displayName;
    protected List<String> lore;

    protected Map<Enchantment, Integer> enchantments;

    protected boolean glow;

    private String author;
    private List<String> pages;

    public WrittenBookBuilder(int amount, String displayName) {
        this(amount, displayName, null);
    }

    public WrittenBookBuilder(int amount, String displayName, String author, String... pages) {
        this(amount, displayName, null, author, pages);
    }

    public WrittenBookBuilder(int amount, String displayName, List<String> lore) {
        this(amount, displayName, lore, null);
    }

    public WrittenBookBuilder(WrittenBookBuilder itemBuilder) {
        this.amount = itemBuilder.amount;
        this.displayName = itemBuilder.displayName;
        this.lore = itemBuilder.lore == null ? new ArrayList<>() : new ArrayList<>(itemBuilder.lore);
        this.enchantments = new HashMap<>(itemBuilder.enchantments);
        this.glow = itemBuilder.glow;
        this.pages = new ArrayList<>(itemBuilder.pages);
    }

    public WrittenBookBuilder(int amount, String displayName, List<String> lore, String author, String... pages) {
        this.amount = amount;
        this.displayName = displayName;
        this.lore = lore == null ? new ArrayList<>() : lore;
        this.enchantments = new HashMap<>();
        this.glow = false;

        this.author = author;
        this.pages = new ArrayList<>(Arrays.asList(pages));
    }

    public int getAmount() {
        return amount;
    }

    public WrittenBookBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public WrittenBookBuilder setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public List<String> getLore() {
        return lore;
    }

    public WrittenBookBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public WrittenBookBuilder addLore(String lore) {
        List<String> list = new ArrayList<>(this.lore);
        list.add(lore);
        this.lore = list;
        return this;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public WrittenBookBuilder addEnchantment(Enchantment enchantment, int level) {
        enchantments.put(enchantment, level);
        return this;
    }

    public WrittenBookBuilder setEnchantments(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public WrittenBookBuilder glow() {
        glow = true;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public WrittenBookBuilder setAuthor(String author) {
        this.author = author;
        return this;
    }

    public List<String> getPages() {
        return pages;
    }

    public WrittenBookBuilder addPage(String page) {
        pages.add(page);
        return this;
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(Material.WRITTEN_BOOK, amount);
        BookMeta meta = (BookMeta) itemStack.getItemMeta();
        meta.setDisplayName(displayName);

        for (int i = 1; i < pages.size() + 1; i++) {
            meta.addPage(i + "");
            meta.setPage(i, pages.get(i - 1));
        }

        if (author != null)
            meta.setAuthor(author);

        meta.setLore((lore == null || lore.size() == 0) ? null : new ArrayList<>(lore));
        itemStack.setItemMeta(meta);

        return modify(itemStack);
    }

    protected ItemStack modify(ItemStack itemStack) {
        if (enchantments.size() != 0) {
            itemStack.addUnsafeEnchantments(new HashMap<>(enchantments));
        } else if (glow) {
            itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);

            ItemMeta meta = itemStack.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }

    public WrittenBookBuilder clone() {
        return new WrittenBookBuilder(this);
    }
}
