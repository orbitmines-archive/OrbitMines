package com.orbitmines.spigot.api.nms.npc.ghast;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.nms.npc.NpcNms_1_8_R2;
import com.orbitmines.spigot.api.nms.npc.ghast.custom.EntityGhast_1_8_R2;
import net.minecraft.server.v1_8_R2.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftGhast;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public class GhastNpc_1_8_R2 implements GhastNpc {

    public GhastNpc_1_8_R2() {
        OrbitMines.getInstance().getNms().npc().addCustomEntity(EntityGhast_1_8_R2.class, "CustomGhast", Id);
    }

    @Override
    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityGhast_1_8_R2 e = new EntityGhast_1_8_R2(nmsWorld, moving, noAttack);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);

        if (displayName != null) {
            e.setCustomName(displayName);
            e.setCustomNameVisible(true);
        }

        ((CraftGhast) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        if (!moving)
            NpcNms_1_8_R2.setNoAI(e.getBukkitEntity());

        return e.getBukkitEntity();
    }
}
