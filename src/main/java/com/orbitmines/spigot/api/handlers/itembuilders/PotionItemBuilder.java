package com.orbitmines.spigot.api.handlers.itembuilders;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class PotionItemBuilder extends ItemBuilder {

    private ArrayList<PotionBuilder> potionBuilders;

    public PotionItemBuilder(PotionBuilder potionBuilder) {
        this(false, potionBuilder);
    }

    public PotionItemBuilder(boolean splash, PotionBuilder potionBuilder) {
        this(splash, potionBuilder, 1);
    }

    public PotionItemBuilder(boolean splash, PotionBuilder potionBuilder, int amount) {
        this(splash, potionBuilder, amount, null);
    }

    public PotionItemBuilder(boolean splash, PotionBuilder potionBuilder, int amount, String displayName) {
        this(splash, potionBuilder, amount, displayName, (List<String>) null);
    }

    public PotionItemBuilder(boolean splash, PotionBuilder potionBuilder, int amount, String displayName, String... lore) {
        this(splash, potionBuilder, amount, displayName, new ArrayList<>(Arrays.asList(lore)));
    }

    public PotionItemBuilder(boolean splash, PotionBuilder potionBuilder, int amount, String displayName, List<String> lore) {
        super(Material.POTION, amount, 0, displayName, lore);

        if (splash) {
            durability = (short) (durability | 16384);
        }

        this.potionBuilders = new ArrayList<>();
        this.potionBuilders.add(potionBuilder);
    }

    public ArrayList<PotionBuilder> getPotionBuilders() {
        return potionBuilders;
    }

    public void setPotionBuilders(ArrayList<PotionBuilder> potionBuilders) {
        this.potionBuilders = potionBuilders;
    }

    @Override
    public ItemStack build() {
        ItemStack itemStack = new ItemStack(material, amount, durability);
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();

        meta.setDisplayName(displayName);
        meta.setLore((lore == null || lore.size() == 0) ? null : new ArrayList<>(lore));

        for (int i = 0; i < potionBuilders.size(); i++) {
            if (i == 0)
                meta.setMainEffect(potionBuilders.get(i).getType());

            meta.addCustomEffect(potionBuilders.get(i).build(), true);
        }

        for (ItemFlag itemFlag : itemFlags) {
            meta.addItemFlags(itemFlag);
        }

        itemStack.setItemMeta(meta);

        return modify(itemStack);
    }

    //TODO
    public enum Type {

        /* Does not work properly for 1.8, create a fix soon */
        NORMAL,
        SPLASH;
        //LINGERING;

    }
}
