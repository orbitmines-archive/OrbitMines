package com.orbitmines.spigot.servers.uhsurvival.handlers.mob;

import org.bukkit.entity.Entity;

public class DefaultMob extends Mob {

    public DefaultMob(MobType type, Entity entity) {
        super(type, entity);
    }

    @Override
    public boolean attack(Attacker defender) {
        return super.attack(defender);
    }

    @Override
    public boolean defend(Attacker attacker) {
        return super.defend(attacker);
    }
}
