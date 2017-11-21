package com.orbitmines.spigot.api.nms.npc.skeleton.custom;

import com.orbitmines.spigot.api.nms.npc.NpcNms_1_11_R1;
import net.minecraft.server.v1_11_R1.EntitySkeleton;
import net.minecraft.server.v1_11_R1.GenericAttributes;
import net.minecraft.server.v1_11_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_11_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;

import java.util.LinkedHashSet;

/**
 * Created by Fadi on 27-9-2016.
 */
public class EntitySkeleton_1_11_R1 extends EntitySkeleton {

    private boolean moving;

    public EntitySkeleton_1_11_R1(World world) {
        super(world);
    }

    public EntitySkeleton_1_11_R1(Location location, boolean moving, boolean noAttack) {
        super(((CraftWorld) location.getWorld()).getHandle());

        this.moving = moving;

        LinkedHashSet targetB = (LinkedHashSet) NpcNms_1_11_R1.getPrivateField("b", PathfinderGoalSelector.class, this.targetSelector);
        targetB.clear();

        if (noAttack)
            return;

        LinkedHashSet goalB = (LinkedHashSet) NpcNms_1_11_R1.getPrivateField("b", PathfinderGoalSelector.class, this.goalSelector);
        goalB.clear();
        LinkedHashSet goalC = (LinkedHashSet) NpcNms_1_11_R1.getPrivateField("c", PathfinderGoalSelector.class, this.goalSelector);
        goalC.clear();
        LinkedHashSet targetC = (LinkedHashSet) NpcNms_1_11_R1.getPrivateField("c", PathfinderGoalSelector.class, this.targetSelector);
        targetC.clear();
    }

    @Override
    public void f(double x, double y, double z) {
    }

    @Override
    public void A_() {
        super.A_();

        if (moving)
            return;

        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.0D);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(Double.MAX_VALUE);
        this.getAttributeInstance(GenericAttributes.c).setValue(Double.MAX_VALUE);
    }
}
