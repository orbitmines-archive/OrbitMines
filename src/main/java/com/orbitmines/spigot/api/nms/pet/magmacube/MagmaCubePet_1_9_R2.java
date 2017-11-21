package com.orbitmines.spigot.api.nms.pet.magmacube;

import com.orbitmines.spigot.api.Mob;
import com.orbitmines.spigot.api.OrbitMinesApi;
import com.orbitmines.spigot.api.nms.npc.NpcNms_1_9_R2;
import com.orbitmines.spigot.api.nms.pet.Pet_1_9_R2;
import net.minecraft.server.v1_9_R2.EntityHuman;
import net.minecraft.server.v1_9_R2.EntityMagmaCube;
import net.minecraft.server.v1_9_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_9_R2.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftMagmaCube;
import org.bukkit.entity.Entity;

import java.util.LinkedHashSet;

/**
 * Created by Fadi on 30-4-2016.
 */
public class MagmaCubePet_1_9_R2 implements MagmaCubePet {

    public MagmaCubePet_1_9_R2() {
        OrbitMinesApi.getApi().getNms().npc().addCustomEntity(CustomNPC.class, "CustomMagmaCube", Mob.MAGMA_CUBE.getEggId());
    }

    @Override
    public Entity spawn(Location location, String displayName) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        CustomNPC e = new CustomNPC(nmsWorld);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);
        e.setCustomName(displayName);
        e.setCustomNameVisible(true);
        ((CraftMagmaCube) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        return e.getBukkitEntity();
    }

    public class CustomNPC extends EntityMagmaCube {

        private Pet_1_9_R2 pI;

        public CustomNPC(World world) {
            super(world);

            LinkedHashSet goalB = (LinkedHashSet) NpcNms_1_9_R2.getPrivateField("b", PathfinderGoalSelector.class, this.goalSelector);
            goalB.clear();
            LinkedHashSet targetB = (LinkedHashSet) NpcNms_1_9_R2.getPrivateField("b", PathfinderGoalSelector.class, this.targetSelector);
            targetB.clear();

            this.pI = new Pet_1_9_R2() {
                @Override
                protected void setYawPitchPet(float yaw, float pitch) {
                    setYawPitch(yaw, pitch);
                }
            };
        }

        @Override
        public void g(float sideMot, float forMot) {
            if (this.passengers.size() == 0 || !(this.passengers.get(0) instanceof EntityHuman)) {
                super.g(sideMot, forMot);
                this.P = 0.5F;
                return;
            }

            float[] f = pI.handleMovement(this, sideMot, forMot, 0.35F);
            super.g(f[0], f[1]);

            pI.handleJump(this);
        }
    }
}
