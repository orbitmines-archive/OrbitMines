package com.orbitmines.spigot.servers.uhsurvival2.handlers.mob.mobs;

import com.orbitmines.spigot.servers.uhsurvival2.handlers.mob.Attacker;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.mob.Mob;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.mob.MobType;
import org.bukkit.entity.EntityType;

public class Arrow extends MobType {

    public Arrow() {
        super(EntityType.ARROW);
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
