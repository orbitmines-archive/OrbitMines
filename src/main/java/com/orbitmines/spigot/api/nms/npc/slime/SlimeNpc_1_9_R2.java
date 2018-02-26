package com.orbitmines.spigot.api.nms.npc.slime;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.nms.npc.NpcNms_1_9_R2;
import com.orbitmines.spigot.api.nms.npc.slime.custom.EntitySlime_1_9_R2;
import net.minecraft.server.v1_9_R2.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftSlime;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public class SlimeNpc_1_9_R2 implements SlimeNpc {

    public SlimeNpc_1_9_R2() {
        OrbitMines.getInstance().getNms().npc().addCustomEntity(EntitySlime_1_9_R2.class, "CustomSlime", Id);
    }

    @Override
    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntitySlime_1_9_R2 e = new EntitySlime_1_9_R2(nmsWorld, moving, noAttack);
        e.setSize(2);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);

        if (displayName != null) {
            e.setCustomName(displayName);
            e.setCustomNameVisible(true);
        }

        ((CraftSlime) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        if (!moving)
            NpcNms_1_9_R2.setNoAI(e.getBukkitEntity());

        return e.getBukkitEntity();
    }
}
