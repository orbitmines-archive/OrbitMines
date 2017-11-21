package com.orbitmines.spigot.api.nms.npc.bat;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface BatNpc {

    int Id = 65;

    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack);

}
