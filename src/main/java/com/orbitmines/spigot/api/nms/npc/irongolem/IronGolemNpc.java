package com.orbitmines.spigot.api.nms.npc.irongolem;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface IronGolemNpc {

    int Id = 99;

    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack);

}
