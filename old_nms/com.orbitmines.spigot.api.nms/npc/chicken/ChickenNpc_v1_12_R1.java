package com.orbitmines.spigot.api.nms.npc.chicken;

import com.orbitmines.spigot.api.nms.npc.NpcNms;
import com.orbitmines.spigot.api.utils.ReflectionUtils;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftChicken;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Created by Fadi on 30-4-2016.
 */
public class ChickenNpc_v1_12_R1 implements ChickenNpc {

    private final NpcNms npcNms;

    public ChickenNpc_v1_12_R1(NpcNms npcNms) {
        this.npcNms = npcNms;
        npcNms.addCustomEntity(CustomChickenNpc.class, "CustomChickenNpc", EntityType.CHICKEN.getTypeId());
        npcNms.addCustomEntity(CustomChickenRideable.class, "CustomChickenRideable", EntityType.CHICKEN.getTypeId());
    }

    @Override
    public Entity spawn(Location location, Collection<Option> options) {
        World world = ((CraftWorld) location.getWorld()).getHandle();
        CustomChickenNpc entity = new CustomChickenNpc(world, options);

        entity.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        world.addEntity(entity);

        CraftEntity craftEntity = entity.getBukkitEntity();
        ((CraftChicken) craftEntity).setRemoveWhenFarAway(false);

        if (options.contains(Option.DISABLE_MOVEMENT))
            npcNms.setNoAI(craftEntity);

        if (options.contains(Option.DISABLE_GRAVITY))
            entity.setNoGravity(true);

        return craftEntity;
    }

    @Override
    public Entity spawnRideable(Location location, float speed, float backMultiplier, float sideMultiplier, float walkHeight, float jumpHeight) {
        World world = ((CraftWorld) location.getWorld()).getHandle();
        CustomChickenRideable entity = new CustomChickenRideable(world, speed, backMultiplier, sideMultiplier, walkHeight, jumpHeight);

        entity.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        world.addEntity(entity);

        CraftEntity craftEntity = entity.getBukkitEntity();
        ((CraftChicken) craftEntity).setRemoveWhenFarAway(false);

        npcNms.setNoAI(craftEntity);

        return craftEntity;
    }

    private class CustomChickenNpc extends EntityChicken {

        private final Collection<Option> options;

        CustomChickenNpc(World world, Collection<Option> options) {
            super(world);

            this.options = options;

            ((LinkedHashSet) ReflectionUtils.getDeclaredObject("b", PathfinderGoalSelector.class, this.targetSelector)).clear();

            if (!options.contains(Option.DISABLE_ATTACK))
                return;

            ((LinkedHashSet) ReflectionUtils.getDeclaredObject("b", PathfinderGoalSelector.class, this.goalSelector)).clear();
            ((LinkedHashSet) ReflectionUtils.getDeclaredObject("c", PathfinderGoalSelector.class, this.goalSelector)).clear();
            ((LinkedHashSet) ReflectionUtils.getDeclaredObject("c", PathfinderGoalSelector.class, this.targetSelector)).clear();
        }

        @Override
        public void f(double x, double y, double z) {
            if (!options.contains(Option.DISABLE_COLLISION))
                super.f(x, y, z);
        }

        @Override
        public void B_() {
            super.B_();

            if (!options.contains(Option.DISABLE_MOVEMENT))
                return;

            this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.0D);
            this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(Double.MAX_VALUE);
            this.getAttributeInstance(GenericAttributes.c).setValue(Double.MAX_VALUE);
        }

        @Override
        protected SoundEffect F() {
            return options.contains(Option.DISABLE_SOUNDS) ? null : super.F();
        }

        @Override
        protected SoundEffect d(DamageSource damageSource) {
            return options.contains(Option.DISABLE_SOUNDS) ? null : super.d(damageSource);
        }

        @Override
        protected SoundEffect cf() {
            return options.contains(Option.DISABLE_SOUNDS) ? null : super.cf();
        }
    }

    private class CustomChickenRideable extends EntityChicken {

        private final float speed;
        private final float backMultiplier;
        private final float sideMultiplier;
        private final float walkHeight;
        private final float jumpHeight;

        CustomChickenRideable(World world, float speed, float backMultiplier, float sideMultiplier, float walkHeight, float jumpHeight) {
            super(world);

            this.speed = speed;
            this.backMultiplier = backMultiplier;
            this.sideMultiplier = sideMultiplier;
            this.walkHeight = walkHeight;
            this.jumpHeight = jumpHeight;

            ((LinkedHashSet) ReflectionUtils.getDeclaredObject("b", PathfinderGoalSelector.class, this.goalSelector)).clear();
            ((LinkedHashSet) ReflectionUtils.getDeclaredObject("b", PathfinderGoalSelector.class, this.targetSelector)).clear();
        }

        @Override
        public void a(float sideMot, float highMot, float forMot) {
            if (this.passengers.size() == 0 || !(this.passengers.get(0) instanceof EntityHuman)) {
                super.a(sideMot, highMot, forMot);
                this.P = walkHeight;
                return;
            }

            CraftEntity entity = getBukkitEntity();

            float[] f = npcNms.handleMovement(entity, speed, backMultiplier, sideMultiplier, walkHeight);
            super.a(f[0], f[1], f[2]);

            npcNms.handleJump(entity, jumpHeight);
        }
    }
}
