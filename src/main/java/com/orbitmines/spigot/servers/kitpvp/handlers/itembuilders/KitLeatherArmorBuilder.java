package com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders;

import com.orbitmines.spigot.api.handlers.itembuilders.LeatherArmorBuilder;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPKit;
import com.orbitmines.spigot.servers.kitpvp.handlers.Passive;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class KitLeatherArmorBuilder extends LeatherArmorBuilder implements KitItem {

    private final KitPvPKit.Level kit;
    private final Map<Passive, Integer> passives;

    public KitLeatherArmorBuilder(KitPvPKit.Level kit, Type type) {
        this(kit, type, Color.WHITE, 1);
    }

    public KitLeatherArmorBuilder(KitPvPKit.Level kit, Type type, Color color) {
        this(kit, type, color, 1);
    }

    public KitLeatherArmorBuilder(KitPvPKit.Level kit, Type type, Color color, int amount) {
        this(kit, type, color, amount, null);
    }

    public KitLeatherArmorBuilder(KitPvPKit.Level kit, Type type, Color color, int amount, String displayName) {
        this(kit, type, color, amount, displayName, (List<String>) null);
    }

    public KitLeatherArmorBuilder(KitPvPKit.Level kit, Type type, Color color, int amount, String displayName, String... lore) {
        this(kit, type, color, amount, displayName, new ArrayList<>(Arrays.asList(lore)));
    }

    public KitLeatherArmorBuilder(KitPvPKit.Level kit, Type type, Color color, int amount, String displayName, List<String> lore) {
        super(type, color, amount, displayName, lore);

        this.kit = kit;
        this.passives = new HashMap<>();

        unbreakable(true);
    }

    @Override
    public KitLeatherArmorBuilder addPassive(Passive passive, Integer level) {
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
