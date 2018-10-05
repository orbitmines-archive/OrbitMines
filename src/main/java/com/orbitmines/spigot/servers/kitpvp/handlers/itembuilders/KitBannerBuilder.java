package com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders;

import com.orbitmines.spigot.api.handlers.itembuilders.BannerBuilder;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPKit;
import com.orbitmines.spigot.servers.kitpvp.handlers.Passive;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class KitBannerBuilder extends BannerBuilder implements KitItem {

    private final KitPvPKit.Level kit;
    private final Map<Passive, Integer> passives;

    public KitBannerBuilder(KitPvPKit.Level kit, DyeColor baseColor) {
        this(kit, baseColor, new ArrayList<>());
    }

    public KitBannerBuilder(KitPvPKit.Level kit, DyeColor baseColor, Pattern... patterns) {
        this(kit, baseColor, new ArrayList<>(Arrays.asList(patterns)));
    }

    public KitBannerBuilder(KitPvPKit.Level kit, DyeColor baseColor, ArrayList<Pattern> patterns) {
        this(kit, baseColor, patterns, 1);
    }

    public KitBannerBuilder(KitPvPKit.Level kit, DyeColor baseColor, ArrayList<Pattern> patterns, int amount) {
        this(kit, baseColor, patterns, amount, null);
    }

    public KitBannerBuilder(KitPvPKit.Level kit, DyeColor baseColor, ArrayList<Pattern> patterns, int amount, String displayName) {
        this(kit, baseColor, patterns, amount, displayName, (List<String>) null);
    }

    public KitBannerBuilder(KitPvPKit.Level kit, DyeColor baseColor, ArrayList<Pattern> patterns, int amount, String displayName, String... lore) {
        this(kit, baseColor, patterns, amount, displayName, new ArrayList<>(Arrays.asList(lore)));
    }

    public KitBannerBuilder(KitPvPKit.Level kit, DyeColor baseColor, ArrayList<Pattern> patterns, int amount, String displayName, List<String> lore) {
        super(baseColor, patterns, amount, displayName, lore);

        this.kit = kit;
        this.passives = new HashMap<>();
    }

    @Override
    public KitBannerBuilder addPassive(Passive passive, Integer level) {
        this.passives.put(passive, level);
        return this;
    }

    @Override
    public ItemStack build() {
        return super.build();
    }

    @Override
    protected ItemStack modify(ItemStack itemStack) {
        return super.modify(itemStack);
    }
}
