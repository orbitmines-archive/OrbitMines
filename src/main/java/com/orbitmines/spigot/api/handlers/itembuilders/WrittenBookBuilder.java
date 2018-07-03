package com.orbitmines.spigot.api.handlers.itembuilders;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.*;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class WrittenBookBuilder extends ItemBuilder {

    private String author;
    private List<TextComponent> pages;

    public WrittenBookBuilder(int amount, String displayName) {
        this(amount, displayName, null);
    }

    public WrittenBookBuilder(int amount, String displayName, String author, TextComponent... pages) {
        this(amount, displayName, null, author, pages);
    }

    public WrittenBookBuilder(int amount, String displayName, List<String> lore) {
        this(amount, displayName, lore, null);
    }

    public WrittenBookBuilder(WrittenBookBuilder itemBuilder) {
        super(itemBuilder);

        this.author = itemBuilder.author;
        this.pages = new ArrayList<>(itemBuilder.pages);
    }

    public WrittenBookBuilder(int amount, String displayName, List<String> lore, String author, TextComponent... pages) {
        super(Material.WRITTEN_BOOK, amount, 0, displayName, lore);

        this.author = author;
        this.pages = new ArrayList<>(Arrays.asList(pages));
    }

    @Override
    public ItemBuilder setMaterial(Material material) {
        throw new IllegalStateException();
    }

    public String getAuthor() {
        return author;
    }

    public WrittenBookBuilder setAuthor(String author) {
        this.author = author;
        return this;
    }

    public List<TextComponent> getPages() {
        return pages;
    }

    public WrittenBookBuilder addPage(String page) {
        pages.add(new TextComponent(page));
        return this;
    }

    public WrittenBookBuilder addPage(TextComponent page) {
        pages.add(page);
        return this;
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(Material.WRITTEN_BOOK, amount);
        BookMeta meta = (BookMeta) itemStack.getItemMeta();
        meta.setDisplayName(displayName);

        for (int i = 1; i < pages.size() + 1; i++) {
            meta.addPage(i + "");
            meta.spigot().setPage(i, pages.get(i - 1));
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
