package com.orbitmines.spigot.servers.uhsurvival.handlers.mob.mobtypes;

import com.orbitmines.spigot.servers.uhsurvival.handlers.mob.DefaultMobType;
import org.bukkit.entity.EntityType;

public class Zombie extends DefaultMobType {

    public Zombie() {
        super(EntityType.ZOMBIE);
        this.setItemExp(10);
        this.setArmorExp(15);
    }
}
