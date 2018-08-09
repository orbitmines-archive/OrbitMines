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
    private boolean effectHidden;

    public PotionItemBuilder(PotionBuilder potionBuilder) {
        this(Type.NORMAL, potionBuilder);
    }

    public PotionItemBuilder(Type type, PotionBuilder potionBuilder) {
        this(type, potionBuilder, false);
    }

    public PotionItemBuilder(Type type, PotionBuilder potionBuilder, boolean effectHidden) {
        this(type, potionBuilder, effectHidden, 1);
    }

    public PotionItemBuilder(Type type, PotionBuilder potionBuilder, boolean effectHidden, int amount) {
        this(type, potionBuilder, effectHidden, amount, null);
    }

    public PotionItemBuilder(Type type, PotionBuilder potionBuilder, boolean effectHidden, int amount, String displayName) {
        this(type, potionBuilder, effectHidden, amount, displayName, (List<String>) null);
    }

    public PotionItemBuilder(Type type, PotionBuilder potionBuilder, boolean effectHidden, int amount, String displayName, String... lore) {
        this(type, potionBuilder, effectHidden, amount, displayName, Arrays.asList(lore));
    }

    public PotionItemBuilder(Type type, PotionBuilder potionBuilder, boolean effectHidden, int amount, String displayName, List<String> lore) {
        super(type.material, amount, displayName, lore);

        this.potionBuilders = new ArrayList<>();
        this.potionBuilders.add(potionBuilder);
        this.effectHidden = effectHidden;
    }

    public ArrayList<PotionBuilder> getPotionBuilders() {
        return potionBuilders;
    }

    public void setPotionBuilders(ArrayList<PotionBuilder> potionBuilders) {
        this.potionBuilders = potionBuilders;
    }

    public boolean isEffectHidden() {
        return effectHidden;
    }

    public void setEffectHidden(boolean effectHidden) {
        this.effectHidden = effectHidden;
    }

    @Override
    public ItemStack build() {
        ItemStack itemStack = new ItemStack(material, amount, damage);
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore((lore == null || lore.size() == 0) ? null : new ArrayList<>(lore));

        for (PotionBuilder potionBuilder : potionBuilders) {
            meta.addCustomEffect(potionBuilder.build(), true);
        }
        meta.setColor(potionBuilders.get(0).getColor());

        if (effectHidden)
            addFlag(ItemFlag.HIDE_POTION_EFFECTS);

        for (ItemFlag itemFlag : itemFlags) {
            meta.addItemFlags(itemFlag);
        }
        itemStack.setItemMeta(meta);

        return modify(itemStack);
    }


    public enum Type {

        NORMAL(Material.POTION),
        SPLASH(Material.SPLASH_POTION),
        LINGERING(Material.LINGERING_POTION);

        private final Material material;

        Type(Material material) {
            this.material = material;
        }

        public Material material() {
            return material;
        }
    }
}
