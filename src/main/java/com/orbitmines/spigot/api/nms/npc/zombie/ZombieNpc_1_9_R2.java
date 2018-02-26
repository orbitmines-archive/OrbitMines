package com.orbitmines.spigot.api.nms.npc.zombie;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.nms.npc.NpcNms_1_9_R2;
import com.orbitmines.spigot.api.nms.npc.zombie.custom.EntityZombie_1_9_R2;
import net.minecraft.server.v1_9_R2.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftZombie;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public class ZombieNpc_1_9_R2 implements ZombieNpc {

    public ZombieNpc_1_9_R2() {
        OrbitMines.getInstance().getNms().npc().addCustomEntity(EntityZombie_1_9_R2.class, "CustomZombie", Id);
    }

    @Override
    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityZombie_1_9_R2 e = new EntityZombie_1_9_R2(nmsWorld, moving, noAttack);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);

        if (displayName != null) {
            e.setCustomName(displayName);
            e.setCustomNameVisible(true);
        }

        ((CraftZombie) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        if (!moving)
            NpcNms_1_9_R2.setNoAI(e.getBukkitEntity());

        return e.getBukkitEntity();
    }
}
