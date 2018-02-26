package com.orbitmines.spigot.api.nms.npc.blaze;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.nms.npc.NpcNms_1_8_R1;
import com.orbitmines.spigot.api.nms.npc.blaze.custom.EntityBlaze_1_8_R1;
import net.minecraft.server.v1_8_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftBlaze;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public class BlazeNpc_1_8_R1 implements BlazeNpc {

    public BlazeNpc_1_8_R1() {
        OrbitMines.getInstance().getNms().npc().addCustomEntity(EntityBlaze_1_8_R1.class, "CustomBlaze", Id);
    }

    @Override
    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityBlaze_1_8_R1 e = new EntityBlaze_1_8_R1(nmsWorld, moving, noAttack);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);

        if (displayName != null) {
            e.setCustomName(displayName);
            e.setCustomNameVisible(true);
        }

        ((CraftBlaze) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        if (!moving)
            NpcNms_1_8_R1.setNoAI(e.getBukkitEntity());

        return e.getBukkitEntity();
    }
}
