package com.orbitmines.spigot.api.nms.npc.skeletonhorse;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface SkeletonHorseNpc {

    int Id = 28;

    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack);

}
