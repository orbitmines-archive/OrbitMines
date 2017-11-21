package com.orbitmines.spigot.api.nms.npc.snowman;

import com.orbitmines.spigot.api.OrbitMinesApi;
import com.orbitmines.spigot.api.nms.npc.NpcNms_1_8_R1;
import com.orbitmines.spigot.api.nms.npc.snowman.custom.EntitySnowman_1_8_R1;
import net.minecraft.server.v1_8_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftSnowman;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public class SnowmanNpc_1_8_R1 implements SnowmanNpc {

    public SnowmanNpc_1_8_R1() {
        OrbitMinesApi.getApi().getNms().npc().addCustomEntity(EntitySnowman_1_8_R1.class, "CustomSnowman", Id);
    }

    @Override
    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntitySnowman_1_8_R1 e = new EntitySnowman_1_8_R1(nmsWorld, moving, noAttack);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);
        e.setCustomName(displayName);
        e.setCustomNameVisible(true);
        ((CraftSnowman) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        if (!moving)
            NpcNms_1_8_R1.setNoAI(e.getBukkitEntity());

        return e.getBukkitEntity();
    }
}
