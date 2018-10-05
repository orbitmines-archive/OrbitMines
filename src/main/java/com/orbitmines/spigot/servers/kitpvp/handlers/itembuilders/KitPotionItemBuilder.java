package com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders;

import com.orbitmines.spigot.api.handlers.itembuilders.PotionBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PotionItemBuilder;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPKit;
import com.orbitmines.spigot.servers.kitpvp.handlers.Passive;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class KitPotionItemBuilder extends PotionItemBuilder implements KitItem {

    private final KitPvPKit.Level kit;
    private final Map<Passive, Integer> passives;

    public KitPotionItemBuilder(KitPvPKit.Level kit, PotionBuilder potionBuilder) {
        this(kit, Type.NORMAL, potionBuilder);
    }

    public KitPotionItemBuilder(KitPvPKit.Level kit, Type type, PotionBuilder potionBuilder) {
        this(kit, type, potionBuilder, false);
    }

    public KitPotionItemBuilder(KitPvPKit.Level kit, Type type, PotionBuilder potionBuilder, boolean effectHidden) {
        this(kit, type, potionBuilder, effectHidden, 1);
    }

    public KitPotionItemBuilder(KitPvPKit.Level kit, Type type, PotionBuilder potionBuilder, boolean effectHidden, int amount) {
        this(kit, type, potionBuilder, effectHidden, amount, null);
    }

    public KitPotionItemBuilder(KitPvPKit.Level kit, Type type, PotionBuilder potionBuilder, boolean effectHidden, int amount, String displayName) {
        this(kit, type, potionBuilder, effectHidden, amount, displayName, (List<String>) null);
    }

    public KitPotionItemBuilder(KitPvPKit.Level kit, Type type, PotionBuilder potionBuilder, boolean effectHidden, int amount, String displayName, String... lore) {
        this(kit, type, potionBuilder, effectHidden, amount, displayName, Arrays.asList(lore));
    }

    public KitPotionItemBuilder(KitPvPKit.Level kit, Type type, PotionBuilder potionBuilder, boolean effectHidden, int amount, String displayName, List<String> lore) {
        super(type, potionBuilder, effectHidden, amount, displayName, lore);

        this.kit = kit;
        this.passives = new HashMap<>();
    }

    @Override
    public KitPotionItemBuilder addPassive(Passive passive, Integer level) {
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
