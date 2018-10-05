package com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPKit;
import com.orbitmines.spigot.servers.kitpvp.handlers.Passive;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class KitItemBuilder extends ItemBuilder implements KitItem {

    private final KitPvPKit.Level kit;
    private final Map<Passive, Integer> passives;

    public KitItemBuilder(KitPvPKit.Level kit, Material material) {
        this(kit, material, 1);
    }

    public KitItemBuilder(KitPvPKit.Level kit, Material material, int amount) {
        this(kit, material, amount, null);
    }

    public KitItemBuilder(KitPvPKit.Level kit, Material material, int amount, String displayName) {
        this(kit, material, amount, displayName, (List<String>) null);
    }

    public KitItemBuilder(KitPvPKit.Level kit, Material material, int amount, String displayName, String... lore) {
        this(kit, material, amount, displayName, new ArrayList<>(Arrays.asList(lore)));
    }

    public KitItemBuilder(KitPvPKit.Level kit, KitItemBuilder itemBuilder) {
        super(itemBuilder);

        this.kit = kit;
        this.passives = new HashMap<>();

        unbreakable(true);
    }

    public KitItemBuilder(KitPvPKit.Level kit, Material material, int amount, String displayName, List<String> lore) {
        super(material, amount, displayName, lore);

        this.kit = kit;
        this.passives = new HashMap<>();

        unbreakable(true);
    }

    @Override
    public KitItemBuilder addPassive(Passive passive, Integer level) {
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
