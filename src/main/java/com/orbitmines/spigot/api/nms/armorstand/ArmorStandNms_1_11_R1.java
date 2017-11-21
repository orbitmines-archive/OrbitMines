package com.orbitmines.spigot.api.nms.armorstand;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

/**
 * Created by Fadi on 30-4-2016.
 */
public class ArmorStandNms_1_11_R1 implements ArmorStandNms {

    @Override
    public ArmorStand spawn(Location location, boolean visible) {
        ArmorStand as = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        ((CraftArmorStand) as).getHandle().setInvisible(!visible);
        return as;
    }
}
