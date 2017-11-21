package com.orbitmines.spigot.api.nms.npc.vindicator;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface VindicatorNpc {

    int Id = 36;

    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack);

}
