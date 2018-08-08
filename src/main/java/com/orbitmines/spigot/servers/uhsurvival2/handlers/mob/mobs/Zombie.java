package com.orbitmines.spigot.servers.uhsurvival2.handlers.mob.mobs;

import com.orbitmines.spigot.servers.uhsurvival2.handlers.mob.Attacker;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.mob.Mob;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.mob.MobType;
import org.bukkit.entity.EntityType;

public class Zombie extends MobType {

    //TODO: HOOK UP THE COMBAT SYSTEM, MAKE SOME CHANGES TO THE DEFEND AND ATTTACK METHODS OF THE UHPLAYER!!!

    public Zombie() {
        super(EntityType.ZOMBIE);
        this.setItemExp(10);
        this.setArmorExp(15);
    }

    @Override
    public boolean attack(Attacker attacker, Mob mob) {
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
