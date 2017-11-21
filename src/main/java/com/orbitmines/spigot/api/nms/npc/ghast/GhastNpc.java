package com.orbitmines.spigot.api.nms.npc.ghast;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface GhastNpc {

    int Id = 56;

    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack);

}
