package com.orbitmines.spigot.api.nms.npc.chicken;

import com.orbitmines.spigot.api.nms.npc.NpcNms;
import com.orbitmines.spigot.api.utils.ReflectionUtils;
import net.minecraft.server.v1_9_R2.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftChicken;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Created by Fadi on 30-4-2016.
 */
public class ChickenNpc_v1_9_R2 implements ChickenNpc {

    private final NpcNms npcNms;

    public ChickenNpc_v1_9_R2(NpcNms npcNms) {
        this.npcNms = npcNms;
        npcNms.addCustomEntity(CustomChickenNpc.class, "CustomChickenNpc", EntityType.CHICKEN.getTypeId());
        npcNms.addCustomEntity(CustomChickenRideable.class, "CustomChickenRideable", EntityType.CHICKEN.getTypeId());
    }

    public Entity spawn(Location location, Collection<Option> options) {
        World world = ((CraftWorld) location.getWorld()).getHandle();
        CustomChickenNpc entity = new CustomChickenNpc(world, options);

        entity.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        world.addEntity(entity);

        CraftEntity craftEntity = entity.getBukkitEntity();
        ((CraftChicken) craftEntity).setRemoveWhenFarAway(false);

        if (options.contains(Option.DISABLE_MOVEMENT))
            npcNms.setNoAI(craftEntity);

        return craftEntity;
    }

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
        public void g(double x, double y, double z) {
            if (!options.contains(Option.DISABLE_COLLISION))
                super.g(x, y, z);
        }

        @Override
        public void m() {
            super.m();

            if (!options.contains(Option.DISABLE_MOVEMENT))
                return;

            this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.0D);
            this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(Double.MAX_VALUE);
            this.getAttributeInstance(GenericAttributes.c).setValue(Double.MAX_VALUE);
        }

        @Override
        protected SoundEffect G() {
            return options.contains(Option.DISABLE_SOUNDS) ? null : super.G();
        }

        @Override
        protected SoundEffect bS() {
            return options.contains(Option.DISABLE_SOUNDS) ? null : super.bS();
        }

        @Override
        protected SoundEffect bT() {
            return options.contains(Option.DISABLE_SOUNDS) ? null : super.bT();
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
        public void g(float sideMot, float forMot) {
            if (this.passengers.size() == 0 || !(this.passengers.get(0) instanceof EntityHuman)) {
                super.g(sideMot, forMot);
                this.P = walkHeight;
                return;
            }

            CraftEntity entity = getBukkitEntity();

            float[] f = npcNms.handleMovement(entity, speed, backMultiplier, sideMultiplier, walkHeight);
            super.g(f[0], f[1]);

            npcNms.handleJump(entity, jumpHeight);
        }
    }
}
