package com.orbitmines.spigot.api.nms.npc.skeletonhorse;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.nms.npc.NpcNms_1_11_R1;
import com.orbitmines.spigot.api.nms.npc.skeletonhorse.custom.EntitySkeletonHorse_1_11_R1;
import net.minecraft.server.v1_11_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftSkeletonHorse;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public class SkeletonHorseNpc_1_11_R1 implements SkeletonHorseNpc {

    public SkeletonHorseNpc_1_11_R1() {
        OrbitMines.getInstance().getNms().npc().addCustomEntity(EntitySkeletonHorse_1_11_R1.class, "CustomSkeletonHorse", Id);
    }

    @Override
    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntitySkeletonHorse_1_11_R1 e = new EntitySkeletonHorse_1_11_R1(nmsWorld, moving, noAttack);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);

        if (displayName != null) {
            e.setCustomName(displayName);
            e.setCustomNameVisible(true);
        }

        ((CraftSkeletonHorse) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        if (!moving) {
            e.setNoGravity(true);
            NpcNms_1_11_R1.setNoAI(e.getBukkitEntity());
        }

        return e.getBukkitEntity();
    }
}
