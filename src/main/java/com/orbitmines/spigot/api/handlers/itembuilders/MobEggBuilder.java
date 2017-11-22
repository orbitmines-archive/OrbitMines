package com.orbitmines.spigot.api.handlers.itembuilders;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.Mob;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class MobEggBuilder extends ItemBuilder {

    private Mob mob;

    public MobEggBuilder(Mob mob, int amount) {
        super(Material.MONSTER_EGG, amount);

        this.mob = mob;
    }

    public MobEggBuilder(Mob mob, int amount, String displayName) {
        super(Material.MONSTER_EGG, amount, 0, displayName);

        this.mob = mob;
    }

    public MobEggBuilder(Mob mob, int amount, String displayName, String... lore) {
        super(Material.MONSTER_EGG, amount, 0, displayName, lore);

        this.mob = mob;
    }

    public MobEggBuilder(Mob mob, MobEggBuilder itemBuilder) {
        super(itemBuilder);

        this.mob = mob;
    }

    public MobEggBuilder(Mob mob, int amount, String displayName, List<String> lore) {
        super(Material.MONSTER_EGG, amount, 0, displayName, lore);

        this.mob = mob;
    }

    public Mob getMob() {
        return mob;
    }

    public MobEggBuilder setMob(Mob mob) {
        this.mob = mob;

        return this;
    }

    @Override
    protected ItemStack modify(ItemStack itemStack) {
        return OrbitMines.getInstance().getNms().customItem().setEggId(super.modify(itemStack), mob);
    }
}
