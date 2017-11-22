package com.orbitmines.spigot.api.nms.armorstand;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface ArmorStandNms {

    ArmorStand spawn(Location location, boolean visible);

}
