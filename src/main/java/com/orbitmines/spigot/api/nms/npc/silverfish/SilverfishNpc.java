package com.orbitmines.spigot.api.nms.npc.silverfish;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface SilverfishNpc {

    int Id = 60;

    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack);

}
