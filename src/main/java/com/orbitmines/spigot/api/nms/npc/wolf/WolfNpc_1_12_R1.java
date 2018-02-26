package com.orbitmines.spigot.api.nms.npc.wolf;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.nms.npc.NpcNms_1_12_R1;
import com.orbitmines.spigot.api.nms.npc.wolf.custom.EntityWolf_1_12_R1;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftWolf;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public class WolfNpc_1_12_R1 implements WolfNpc {

    public WolfNpc_1_12_R1() {
        OrbitMines.getInstance().getNms().npc().addCustomEntity(EntityWolf_1_12_R1.class, "CustomWolf", Id);
    }

    @Override
    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityWolf_1_12_R1 e = new EntityWolf_1_12_R1(nmsWorld, moving, noAttack);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);

        if (displayName != null) {
            e.setCustomName(displayName);
            e.setCustomNameVisible(true);
        }

        ((CraftWolf) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        if (!moving) {
            e.setNoGravity(true);
            NpcNms_1_12_R1.setNoAI(e.getBukkitEntity());
        }

        return e.getBukkitEntity();
    }
}
