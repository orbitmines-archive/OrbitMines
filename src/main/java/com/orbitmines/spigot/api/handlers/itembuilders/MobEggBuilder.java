package com.orbitmines.spigot.api.handlers.itembuilders;

import com.orbitmines.spigot.api.Mob;

import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class MobEggBuilder extends ItemBuilder {

    public MobEggBuilder(Mob mob, int amount) {
        super(mob.getSpawnEggMaterial(), amount);
    }

    public MobEggBuilder(Mob mob, int amount, String displayName) {
        super(mob.getSpawnEggMaterial(), amount, displayName);
    }

    public MobEggBuilder(Mob mob, int amount, String displayName, String... lore) {
        super(mob.getSpawnEggMaterial(), amount, displayName, lore);
    }

    public MobEggBuilder(Mob mob, MobEggBuilder itemBuilder) {
        super(itemBuilder);
        itemBuilder.material = mob.getSpawnEggMaterial();
    }

    public MobEggBuilder(Mob mob, int amount, String displayName, List<String> lore) {
        super(mob.getSpawnEggMaterial(), amount, displayName, lore);
    }

    public MobEggBuilder setMob(Mob mob) {
        this.material = mob.getSpawnEggMaterial();

        return this;
    }
}
