package com.orbitmines.spigot.api.nms.npc.cavespider;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.nms.npc.NpcNms_1_9_R1;
import com.orbitmines.spigot.api.nms.npc.cavespider.custom.EntityCaveSpider_1_9_R1;
import net.minecraft.server.v1_9_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftCaveSpider;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public class CaveSpiderNpc_1_9_R1 implements CaveSpiderNpc {

    public CaveSpiderNpc_1_9_R1() {
        OrbitMines.getInstance().getNms().npc().addCustomEntity(EntityCaveSpider_1_9_R1.class, "CustomCaveSpider", Id);
    }

    @Override
    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityCaveSpider_1_9_R1 e = new EntityCaveSpider_1_9_R1(nmsWorld, moving, noAttack);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);

        if (displayName != null) {
            e.setCustomName(displayName);
            e.setCustomNameVisible(true);
        }

        ((CraftCaveSpider) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        if (!moving)
            NpcNms_1_9_R1.setNoAI(e.getBukkitEntity());

        return e.getBukkitEntity();
    }
}
