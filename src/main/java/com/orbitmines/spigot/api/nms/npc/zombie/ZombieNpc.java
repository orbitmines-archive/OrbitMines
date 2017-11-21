package com.orbitmines.spigot.api.nms.npc.zombie;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface ZombieNpc {

    int Id = 54;

    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack);

}
