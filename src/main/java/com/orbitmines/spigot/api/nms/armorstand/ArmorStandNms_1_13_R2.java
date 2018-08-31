package com.orbitmines.spigot.api.nms.armorstand;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class ArmorStandNms_1_13_R2 implements ArmorStandNms {

    @Override
    public ArmorStand spawn(Location location, boolean visible) {
        ArmorStand as = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        ((CraftArmorStand) as).getHandle().setInvisible(!visible);
        return as;
    }
}
