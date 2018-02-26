package com.orbitmines.spigot.api.nms.npc.skeleton;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.nms.npc.NpcNms_1_11_R1;
import com.orbitmines.spigot.api.nms.npc.skeleton.custom.EntitySkeleton_1_11_R1;
import net.minecraft.server.v1_11_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftSkeleton;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public class SkeletonNpc_1_11_R1 implements SkeletonNpc {

    public SkeletonNpc_1_11_R1() {
        OrbitMines.getInstance().getNms().npc().addCustomEntity(EntitySkeleton_1_11_R1.class, "CustomSkeleton", Id);
    }

    @Override
    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntitySkeleton_1_11_R1 e = new EntitySkeleton_1_11_R1(location, moving, noAttack);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);

        if (displayName != null) {
            e.setCustomName(displayName);
            e.setCustomNameVisible(true);
        }

        ((CraftSkeleton) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        if (!moving) {
            e.setNoGravity(true);
            NpcNms_1_11_R1.setNoAI(e.getBukkitEntity());
        }

        return e.getBukkitEntity();
    }
}
