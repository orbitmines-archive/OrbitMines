package com.orbitmines.spigot.api.nms.npc.squid;

import com.orbitmines.spigot.api.OrbitMinesApi;
import com.orbitmines.spigot.api.nms.npc.NpcNms_1_10_R1;
import com.orbitmines.spigot.api.nms.npc.squid.custom.EntitySquid_1_10_R1;
import net.minecraft.server.v1_10_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftSquid;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public class SquidNpc_1_10_R1 implements SquidNpc {

    public SquidNpc_1_10_R1() {
        OrbitMinesApi.getApi().getNms().npc().addCustomEntity(EntitySquid_1_10_R1.class, "CustomSquid", Id);
    }

    @Override
    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntitySquid_1_10_R1 e = new EntitySquid_1_10_R1(nmsWorld, moving, noAttack);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);
        e.setCustomName(displayName);
        e.setCustomNameVisible(true);
        ((CraftSquid) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        if (!moving) {
            e.setNoGravity(true);
            NpcNms_1_10_R1.setNoAI(e.getBukkitEntity());
        }

        return e.getBukkitEntity();
    }
}
