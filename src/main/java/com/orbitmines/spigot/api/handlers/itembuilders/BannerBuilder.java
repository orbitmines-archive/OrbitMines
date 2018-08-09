package com.orbitmines.spigot.api.handlers.itembuilders;

import com.orbitmines.api.Language;
import com.orbitmines.spigot.api.utils.ColorUtils;
import org.bukkit.DyeColor;
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
        super(ColorUtils.getBannerMaterial(baseColor), amount, displayName, lore);

        this.patterns = patterns;
    }

    public void setBaseColor(DyeColor baseColor) {
        this.material = ColorUtils.getBannerMaterial(baseColor);
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
        ItemStack itemStack = new ItemStack(material, amount, damage);
        BannerMeta meta = (BannerMeta) itemStack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore((lore == null || lore.size() == 0) ? null : new ArrayList<>(lore));
        meta.setPatterns(new ArrayList<>(patterns));
        for (ItemFlag itemFlag : itemFlags) {
            meta.addItemFlags(itemFlag);
        }
        itemStack.setItemMeta(meta);

        return modify(itemStack);
    }

    public static BannerBuilder getBuilder(Language language) {
        BannerBuilder builder;
        switch (language) {

            case DUTCH:
                builder = new BannerBuilder(DyeColor.WHITE);
                builder.addPattern(DyeColor.BLUE, PatternType.STRIPE_BOTTOM);
                builder.addPattern(DyeColor.RED, PatternType.STRIPE_TOP);
                return builder;
            case ENGLISH:
                builder = new BannerBuilder(DyeColor.BLUE);
                builder.addPattern(DyeColor.WHITE, PatternType.STRIPE_DOWNLEFT);
                builder.addPattern(DyeColor.WHITE, PatternType.STRIPE_DOWNRIGHT);
                builder.addPattern(DyeColor.RED, PatternType.CROSS);
                builder.addPattern(DyeColor.WHITE, PatternType.STRIPE_CENTER);
                builder.addPattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE);
                builder.addPattern(DyeColor.RED, PatternType.STRAIGHT_CROSS);
                return builder;
        }
        throw new NullPointerException();
    }
}
