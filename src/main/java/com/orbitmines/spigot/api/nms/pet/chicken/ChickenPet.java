package com.orbitmines.spigot.api.nms.pet.chicken;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface ChickenPet {

    Entity spawn(Location location, String displayName);

}
