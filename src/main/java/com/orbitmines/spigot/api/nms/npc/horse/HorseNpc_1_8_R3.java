package com.orbitmines.spigot.api.nms.npc.horse;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.nms.npc.NpcNms_1_8_R3;
import com.orbitmines.spigot.api.nms.npc.horse.custom.EntityHorse_1_8_R3;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHorse;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public class HorseNpc_1_8_R3 implements HorseNpc {

    public HorseNpc_1_8_R3() {
        OrbitMines.getInstance().getNms().npc().addCustomEntity(EntityHorse_1_8_R3.class, "CustomHorse", Id);
    }

    @Override
    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityHorse_1_8_R3 e = new EntityHorse_1_8_R3(nmsWorld, moving, noAttack);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);

        if (displayName != null) {
            e.setCustomName(displayName);
            e.setCustomNameVisible(true);
        }

        ((CraftHorse) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        if (!moving)
            NpcNms_1_8_R3.setNoAI(e.getBukkitEntity());

        return e.getBukkitEntity();
    }
}
