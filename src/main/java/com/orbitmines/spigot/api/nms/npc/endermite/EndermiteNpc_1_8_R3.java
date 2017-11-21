package com.orbitmines.spigot.api.nms.npc.endermite;

import com.orbitmines.spigot.api.OrbitMinesApi;
import com.orbitmines.spigot.api.nms.npc.NpcNms_1_8_R3;
import com.orbitmines.spigot.api.nms.npc.endermite.custom.EntityEndermite_1_8_R3;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEndermite;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public class EndermiteNpc_1_8_R3 implements EndermiteNpc {

    public EndermiteNpc_1_8_R3() {
        OrbitMinesApi.getApi().getNms().npc().addCustomEntity(EntityEndermite_1_8_R3.class, "CustomEndermite", Id);
    }

    @Override
    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityEndermite_1_8_R3 e = new EntityEndermite_1_8_R3(nmsWorld, moving, noAttack);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);
        e.setCustomName(displayName);
        e.setCustomNameVisible(true);
        ((CraftEndermite) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        if (!moving)
            NpcNms_1_8_R3.setNoAI(e.getBukkitEntity());

        return e.getBukkitEntity();
    }
}
