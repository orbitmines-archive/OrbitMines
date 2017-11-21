package com.orbitmines.spigot.api.nms.npc.irongolem.custom;

import com.orbitmines.spigot.api.nms.npc.NpcNms_1_8_R2;
import net.minecraft.server.v1_8_R2.EntityIronGolem;
import net.minecraft.server.v1_8_R2.GenericAttributes;
import net.minecraft.server.v1_8_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R2.World;

import java.util.List;

/**
 * Created by Fadi on 30-4-2016.
 */
public class EntityIronGolem_1_8_R2 extends EntityIronGolem {

    private boolean moving;

    public EntityIronGolem_1_8_R2(World world) {
        super(world);
    }

    public EntityIronGolem_1_8_R2(World world, boolean moving, boolean noAttack) {
        super(world);

        this.moving = moving;

        List targetB = (List) NpcNms_1_8_R2.getPrivateField("b", PathfinderGoalSelector.class, this.targetSelector);
        targetB.clear();

        if (noAttack)
            return;

        List goalB = (List) NpcNms_1_8_R2.getPrivateField("b", PathfinderGoalSelector.class, this.goalSelector);
        goalB.clear();
        List goalC = (List) NpcNms_1_8_R2.getPrivateField("c", PathfinderGoalSelector.class, this.goalSelector);
        goalC.clear();
        List targetC = (List) NpcNms_1_8_R2.getPrivateField("c", PathfinderGoalSelector.class, this.targetSelector);
        targetC.clear();
    }

    @Override
    public void g(double x, double y, double z) {
    }

    @Override
    public void m() {
        super.m();

        if (moving)
            return;

        this.getAttributeInstance(GenericAttributes.d).setValue(0.0D);
        this.getAttributeInstance(GenericAttributes.b).setValue(Double.MAX_VALUE);
        this.getAttributeInstance(GenericAttributes.c).setValue(Double.MAX_VALUE);
    }
}
