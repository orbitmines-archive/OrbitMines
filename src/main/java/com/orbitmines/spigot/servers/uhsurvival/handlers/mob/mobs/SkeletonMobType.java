package com.orbitmines.spigot.servers.uhsurvival.handlers.mob.mobs;

import com.orbitmines.spigot.servers.uhsurvival.events.UHEvents.MobAttackEvent;
import com.orbitmines.spigot.servers.uhsurvival.handlers.mob.Mob;
import com.orbitmines.spigot.servers.uhsurvival.handlers.mob.MobType;
import com.orbitmines.spigot.servers.uhsurvival.handlers.tool.ToolInventory;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.EntityType;

/**
 * Created by Robin on 3/1/2018.
 */
public class SkeletonMobType extends MobType {

    public SkeletonMobType() {
        super("Default_Skeleton", EntityType.SKELETON, null);
    }

    @Override
    public void attack(MobAttackEvent event) {

    }

    @Override
    public void death(Mob mob) {

    }

    @Override
    public void spawn(Mob mob) {
        ToolInventory inventory = mob.getInventory();
        if(inventory != null){

        }
    }
}
