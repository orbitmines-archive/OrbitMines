package com.orbitmines.spigot.api.nms.npc.ocelot;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface OcelotNpc {

    int Id = 98;

    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack);

}
