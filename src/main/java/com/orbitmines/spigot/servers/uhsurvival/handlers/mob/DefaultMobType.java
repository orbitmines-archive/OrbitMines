package com.orbitmines.spigot.servers.uhsurvival.handlers.mob;

import org.bukkit.entity.EntityType;

public class DefaultMobType extends MobType {

    public DefaultMobType(EntityType type) {
        super(type);
    }

    @Override
    public boolean attack(Attacker defender, Mob mob) {
        return false;
    }

    @Override
    public boolean defend(Attacker attacker, Mob mob) {
        return false;
    }

    @Override
    public void die(Mob mob) {

    }

    @Override
    public void spawn(Mob mob) {

    }
}
