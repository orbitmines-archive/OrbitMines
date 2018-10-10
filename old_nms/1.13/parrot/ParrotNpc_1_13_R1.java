package com.orbitmines.spigot.api.nms.npc.parrot;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.nms.npc.NpcNms;
import com.orbitmines.spigot.api.utils.ReflectionUtils;
import net.minecraft.server.v1_13_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftParrot;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

public class ParrotNpc_1_13_R1 implements ParrotNpc {

    private final NpcNms npcNms;

    public ParrotNpc_1_13_R1(NpcNms npcNms) {
        this.npcNms = npcNms;
    }

    @Override
    public Entity spawn(Location location, Collection<Option> options) {
        World world = ((CraftWorld) location.getWorld()).getHandle();
        CustomParrotNpc entity = new CustomParrotNpc(world, options);

        entity.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        world.addEntity(entity);

        CraftEntity craftEntity = entity.getBukkitEntity();
        ((CraftParrot) craftEntity).setRemoveWhenFarAway(false);

        if (options.contains(Option.DISABLE_MOVEMENT))
            npcNms.setNoAI(craftEntity);

        if (options.contains(Option.DISABLE_GRAVITY))
            entity.setNoGravity(true);

        return craftEntity;
    }

    @Override
    public Entity spawnRideable(Location location, float speed, float backMultiplier, float sideMultiplier, float walkHeight, float jumpHeight) {
        World world = ((CraftWorld) location.getWorld()).getHandle();
        CustomParrotRideable entity = new CustomParrotRideable(world, npcNms, speed, backMultiplier, sideMultiplier, walkHeight, jumpHeight);

        entity.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        world.addEntity(entity);

        CraftEntity craftEntity = entity.getBukkitEntity();
        ((CraftParrot) craftEntity).setRemoveWhenFarAway(false);

        return craftEntity;
    }

    private static class CustomParrotNpc extends EntityParrot {

        private final Collection<Option> options;

        CustomParrotNpc(World world) {
            this(world, new ArrayList<>());
        }

        CustomParrotNpc(World world, Collection<Option> options) {
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
        public void tick() {
            super.tick();

            if (!options.contains(Option.DISABLE_MOVEMENT))
                return;

            this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.0D);
            this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(Double.MAX_VALUE);
            this.getAttributeInstance(GenericAttributes.c).setValue(Double.MAX_VALUE);
        }

        @Override
        public SoundEffect D() {
            return options.contains(Option.DISABLE_SOUNDS) ? null : super.D();
        }

        @Override
        protected SoundEffect d(DamageSource damageSource) {
            return options.contains(Option.DISABLE_SOUNDS) ? null : super.d(damageSource);
        }

        @Override
        protected SoundEffect cs() {
            return options.contains(Option.DISABLE_SOUNDS) ? null : super.cs();
        }
    }

    private static class CustomParrotRideable extends EntityParrot {

        private final NpcNms npcNms;

        private final float speed;
        private final float backMultiplier;
        private final float sideMultiplier;
        private final float walkHeight;
        private final float jumpHeight;

        CustomParrotRideable(World world) {
            this(world, null, 0F, 0F, 0F, 0F, 0F); //DEFAULTS
        }

        CustomParrotRideable(World world, NpcNms npcNms, float speed, float backMultiplier, float sideMultiplier, float walkHeight, float jumpHeight) {
            super(world);

            this.npcNms = npcNms;

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
                this.Q = walkHeight;
                return;
            }

            CraftEntity entity = getBukkitEntity();

            float[] f = npcNms.handleMovement(entity, speed, backMultiplier, sideMultiplier, walkHeight);
            super.a(f[0], f[1], f[2]);

            npcNms.handleJump(entity, jumpHeight);
        }
    }
}
