package com.orbitmines.spigot.api.handlers.itembuilders;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class BannerBuilder extends ItemBuilder {

    private DyeColor baseColor;
    private ArrayList<Pattern> patterns;

    public BannerBuilder(DyeColor baseColor) {
        this(baseColor, new ArrayList<>());
    }

    public BannerBuilder(DyeColor baseColor, Pattern... patterns) {
        this(baseColor, new ArrayList<>(Arrays.asList(patterns)));
    }

    public BannerBuilder(DyeColor baseColor, ArrayList<Pattern> patterns) {
        this(baseColor, patterns, 1);
    }

    public BannerBuilder(DyeColor baseColor, ArrayList<Pattern> patterns, int amount) {
        this(baseColor, patterns, amount, null);
    }

    public BannerBuilder(DyeColor baseColor, ArrayList<Pattern> patterns, int amount, String displayName) {
        this(baseColor, patterns, amount, displayName, (List<String>) null);
    }

    public BannerBuilder(DyeColor baseColor, ArrayList<Pattern> patterns, int amount, String displayName, String... lore) {
        this(baseColor, patterns, amount, displayName, new ArrayList<>(Arrays.asList(lore)));
    }

    public BannerBuilder(DyeColor baseColor, ArrayList<Pattern> patterns, int amount, String displayName, List<String> lore) {
        super(Material.BANNER, amount, 0, displayName, lore);

        this.baseColor = baseColor;
        this.patterns = patterns;
    }

    public DyeColor getBaseColor() {
        return baseColor;
    }

    public void setBaseColor(DyeColor baseColor) {
        this.baseColor = baseColor;
    }

    public ArrayList<Pattern> getPatterns() {
        return patterns;
    }

    public void addPattern(DyeColor dyeColor, PatternType patternType) {
        patterns.add(new Pattern(dyeColor, patternType));
    }

    public void removePattern(DyeColor dyeColor, PatternType patternType) {
        for (Pattern pattern : new ArrayList<>(patterns)) {
            if (pattern.getColor() == dyeColor && pattern.getPattern() == patternType)
                patterns.remove(pattern);
        }
    }

    @Override
    public ItemStack build() {
        ItemStack itemStack = new ItemStack(material, amount, durability);
        BannerMeta meta = (BannerMeta) itemStack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore((lore == null || lore.size() == 0) ? null : new ArrayList<>(lore));
        meta.setBaseColor(baseColor);
        meta.setPatterns(new ArrayList<>(patterns));
        for (ItemFlag itemFlag : itemFlags) {
            meta.addItemFlags(itemFlag);
        }
        itemStack.setItemMeta(meta);

        return modify(itemStack);
    }
}
