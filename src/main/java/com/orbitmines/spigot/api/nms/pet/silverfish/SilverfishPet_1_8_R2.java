package com.orbitmines.spigot.api.nms.pet.silverfish;

import com.orbitmines.spigot.api.Mob;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.nms.npc.NpcNms_1_8_R2;
import com.orbitmines.spigot.api.nms.pet.Pet_1_8_R2;
import net.minecraft.server.v1_8_R2.EntityHuman;
import net.minecraft.server.v1_8_R2.EntitySilverfish;
import net.minecraft.server.v1_8_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R2.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftSilverfish;
import org.bukkit.entity.Entity;

import java.util.List;

/**
 * Created by Fadi on 30-4-2016.
 */
public class SilverfishPet_1_8_R2 implements SilverfishPet {

    public SilverfishPet_1_8_R2() {
        OrbitMines.getInstance().getNms().npc().addCustomEntity(CustomNPC.class, "CustomSilverfish", Mob.SILVERFISH.getEggId());
    }

    @Override
    public Entity spawn(Location location, String displayName) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        CustomNPC e = new CustomNPC(nmsWorld);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);
        e.setCustomName(displayName);
        e.setCustomNameVisible(true);
        ((CraftSilverfish) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        return e.getBukkitEntity();
    }

    public class CustomNPC extends EntitySilverfish {

        private Pet_1_8_R2 pI;

        public CustomNPC(World world) {
            super(world);

            List goalB = (List) NpcNms_1_8_R2.getPrivateField("b", PathfinderGoalSelector.class, this.goalSelector);
            goalB.clear();
            List targetB = (List) NpcNms_1_8_R2.getPrivateField("b", PathfinderGoalSelector.class, this.targetSelector);
            targetB.clear();

            this.pI = new Pet_1_8_R2() {
                @Override
                protected void setYawPitchPet(float yaw, float pitch) {
                    setYawPitch(yaw, pitch);
                }
            };
        }

        @Override
        public void g(float sideMot, float forMot) {
            if (this.passenger == null || !(this.passenger instanceof EntityHuman)) {
                super.g(sideMot, forMot);
                this.S = 0.5F;
                return;
            }

            float[] f = pI.handleMovement(this, sideMot, forMot, 0.35F);
            super.g(f[0], f[1]);

            pI.handleJump(this);
        }
    }
}
