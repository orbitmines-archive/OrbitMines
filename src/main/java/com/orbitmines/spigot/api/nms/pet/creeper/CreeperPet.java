package com.orbitmines.spigot.api.nms.pet.creeper;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface CreeperPet {

    public Entity spawn(Location location, String displayName);

}
