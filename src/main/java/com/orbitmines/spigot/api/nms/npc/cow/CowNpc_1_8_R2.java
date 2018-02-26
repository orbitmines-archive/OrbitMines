package com.orbitmines.spigot.api.nms.npc.cow;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.nms.npc.NpcNms_1_8_R2;
import com.orbitmines.spigot.api.nms.npc.cow.custom.EntityCow_1_8_R2;
import net.minecraft.server.v1_8_R2.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftCow;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public class CowNpc_1_8_R2 implements CowNpc {

    public CowNpc_1_8_R2() {
        OrbitMines.getInstance().getNms().npc().addCustomEntity(EntityCow_1_8_R2.class, "CustomCow", Id);
    }

    @Override
    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityCow_1_8_R2 e = new EntityCow_1_8_R2(nmsWorld, moving, noAttack);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);

        if (displayName != null) {
            e.setCustomName(displayName);
            e.setCustomNameVisible(true);
        }

        ((CraftCow) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        if (!moving)
            NpcNms_1_8_R2.setNoAI(e.getBukkitEntity());

        return e.getBukkitEntity();
    }
}
