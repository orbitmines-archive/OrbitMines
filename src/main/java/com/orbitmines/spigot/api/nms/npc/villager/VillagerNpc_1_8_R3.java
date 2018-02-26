package com.orbitmines.spigot.api.nms.npc.villager;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.nms.npc.NpcNms_1_8_R3;
import com.orbitmines.spigot.api.nms.npc.villager.custom.EntityVillager_1_8_R3;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftVillager;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public class VillagerNpc_1_8_R3 implements VillagerNpc {

    public VillagerNpc_1_8_R3() {
        OrbitMines.getInstance().getNms().npc().addCustomEntity(EntityVillager_1_8_R3.class, "CustomVillager", Id);
    }

    @Override
    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityVillager_1_8_R3 e = new EntityVillager_1_8_R3(nmsWorld, moving, noAttack);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);

        if (displayName != null) {
            e.setCustomName(displayName);
            e.setCustomNameVisible(true);
        }

        ((CraftVillager) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        if (!moving)
            NpcNms_1_8_R3.setNoAI(e.getBukkitEntity());

        return e.getBukkitEntity();
    }
}
