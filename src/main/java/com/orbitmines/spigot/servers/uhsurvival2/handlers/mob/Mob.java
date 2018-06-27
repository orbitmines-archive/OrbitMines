package com.orbitmines.spigot.servers.uhsurvival2.handlers.mob;

import com.orbitmines.spigot.servers.uhsurvival2.handlers.map.MapSection;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.tool.ToolInventory;
import org.bukkit.entity.LivingEntity;

public abstract class Mob implements Attacker {

    private MapSection location;

    private ToolInventory inventory;
    private LivingEntity entity;

    private Attacker target;

    public Mob(LivingEntity entity, MapSection location){
        this.entity = entity;
        this.location = location;
        this.inventory = new ToolInventory(entity.getEquipment());
        this.target = null;
    }

    @Override
    public boolean attack(Attacker attacker) {
        /* acquiring new target */
        if(attacker != target){
            this.target = attacker;
        }

        /* calling defend method */
        return attacker.defend(this);
    }

    @Override
    public boolean defend(Attacker attacker) {
        //TODO: USE ENCHANTMENTS & ADD BLOCKING CHANCE!
        return false;
    }


    /* GETTERS */
    public MapSection getLocation() {
        return location;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    @Override
    public ToolInventory getToolInventory() {
        return inventory;
    }

    public Attacker getTarget() {
        return target;
    }

    /* BOOLEANS */
    @Override
    public boolean hasInventory() {
        return inventory != null;
    }

    public boolean hasTarget(){
        return target != null;
    }
}
