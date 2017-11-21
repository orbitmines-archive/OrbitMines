package com.orbitmines.spigot.api.nms.npc.zombie;

import com.orbitmines.spigot.api.OrbitMinesApi;
import com.orbitmines.spigot.api.nms.npc.NpcNms_1_12_R1;
import com.orbitmines.spigot.api.nms.npc.zombie.custom.EntityZombie_1_12_R1;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftZombie;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public class ZombieNpc_1_12_R1 implements ZombieNpc {

    public ZombieNpc_1_12_R1() {
        OrbitMinesApi.getApi().getNms().npc().addCustomEntity(EntityZombie_1_12_R1.class, "CustomZombie", Id);
    }

    @Override
    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityZombie_1_12_R1 e = new EntityZombie_1_12_R1(nmsWorld, moving, noAttack);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);
        e.setCustomName(displayName);
        e.setCustomNameVisible(true);
        ((CraftZombie) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        if (!moving) {
            e.setNoGravity(true);
            NpcNms_1_12_R1.setNoAI(e.getBukkitEntity());
        }

        return e.getBukkitEntity();
    }
}
