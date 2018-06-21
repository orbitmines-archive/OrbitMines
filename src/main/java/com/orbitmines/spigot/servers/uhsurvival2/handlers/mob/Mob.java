package com.orbitmines.spigot.servers.uhsurvival2.handlers.mob;

import com.orbitmines.spigot.servers.uhsurvival2.handlers.tool.ToolInventory;
import org.bukkit.entity.LivingEntity;

public abstract class Mob implements Attacker {

    private ToolInventory inventory;
    private LivingEntity entity;

    public Mob(LivingEntity entity){
        this.entity = entity;
        this.inventory = new ToolInventory(entity.getEquipment());
    }



    public LivingEntity getEntity() {
        return entity;
    }

    @Override
    public ToolInventory getToolInventory() {
        return inventory;
    }

    @Override
    public boolean hasInventory() {
        return inventory != null;
    }
}
