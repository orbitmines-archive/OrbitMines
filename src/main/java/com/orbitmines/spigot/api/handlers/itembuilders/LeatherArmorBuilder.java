package com.orbitmines.spigot.api.handlers.itembuilders;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class LeatherArmorBuilder extends ItemBuilder {

    protected Type type;
    protected Color color;

    public LeatherArmorBuilder(Type type) {
        this(type, null, 1);
    }

    public LeatherArmorBuilder(Type type, Color color) {
        this(type, color, 1);
    }

    public LeatherArmorBuilder(Type type, Color color, int amount) {
        this(type, color, amount, null);
    }

    public LeatherArmorBuilder(Type type, Color color, int amount, String displayName) {
        this(type, color, amount, displayName, (List<String>) null);
    }

    public LeatherArmorBuilder(Type type, Color color, int amount, String displayName, String... lore) {
        this(type, color, amount, displayName, new ArrayList<>(Arrays.asList(lore)));
    }

    public LeatherArmorBuilder(Type type, Color color, int amount, String displayName, List<String> lore) {
        super(type.material, amount, displayName, lore);

        this.type = type;
        this.color = color;
    }


    public LeatherArmorBuilder(LeatherArmorBuilder builder) {
        super(builder.material, builder.amount, builder.displayName, new ArrayList<>(builder.lore));

        this.type = builder.type;
        this.color = builder.color;
    }

    public Color getColor() {
        return color;
    }

    public LeatherArmorBuilder setColor(Color color) {
        this.color = color;

        return this;
    }

    public LeatherArmorBuilder setType(Type type) {
        this.material = type.material;

        return this;
    }

    @Override
    public ItemStack build() {
        ItemStack itemStack = new ItemStack(material, amount, damage);
        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore((lore == null || lore.size() == 0) ? null : new ArrayList<>(lore));

        if (color != null)
            meta.setColor(color);

        for (ItemFlag itemFlag : itemFlags) {
            meta.addItemFlags(itemFlag);
        }
        itemStack.setItemMeta(meta);

        return modify(itemStack);
    }

    public enum Type {

        HELMET(Material.LEATHER_HELMET),
        CHESTPLATE(Material.LEATHER_CHESTPLATE),
        LEGGINGS(Material.LEATHER_LEGGINGS),
        BOOTS(Material.LEATHER_BOOTS);

        private final Material material;

        Type(Material material) {
            this.material = material;
        }

        public Material material() {
            return material;
        }
    }
}
