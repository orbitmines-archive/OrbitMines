package com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders;

import com.orbitmines.spigot.api.Mob;
import com.orbitmines.spigot.api.handlers.itembuilders.MobEggBuilder;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPKit;
import com.orbitmines.spigot.servers.kitpvp.handlers.Passive;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class KitMobEggBuilder extends MobEggBuilder implements KitItem {

    private final KitPvPKit.Level kit;
    private final Map<Passive, Integer> passives;

    public KitMobEggBuilder(KitPvPKit.Level kit, Mob mob, int amount) {
        super(mob, amount);

        this.kit = kit;
        this.passives = new HashMap<>();
    }

    public KitMobEggBuilder(KitPvPKit.Level kit, Mob mob, int amount, String displayName) {
        super(mob, amount, displayName);

        this.kit = kit;
        this.passives = new HashMap<>();
    }

    public KitMobEggBuilder(KitPvPKit.Level kit, Mob mob, int amount, String displayName, String... lore) {
        super(mob, amount, displayName, lore);

        this.kit = kit;
        this.passives = new HashMap<>();
    }

    public KitMobEggBuilder(KitPvPKit.Level kit, Mob mob, KitMobEggBuilder itemBuilder) {
        super(mob, itemBuilder);

        this.kit = kit;
        this.passives = new HashMap<>();
    }

    public KitMobEggBuilder(KitPvPKit.Level kit, Mob mob, int amount, String displayName, List<String> lore) {
        super(mob, amount, displayName, lore);

        this.kit = kit;
        this.passives = new HashMap<>();
    }

    @Override
    public KitMobEggBuilder addPassive(Passive passive, Integer level) {
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
