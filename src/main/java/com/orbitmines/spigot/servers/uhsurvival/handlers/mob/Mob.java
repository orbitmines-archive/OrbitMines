package com.orbitmines.spigot.servers.uhsurvival.handlers.mob;

import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.tool.ToolInventory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

/**
 * Created by Robin on 2/28/2018.
 */
public class Mob {

    private MobType type;

    private Entity entity;

    private ToolInventory inv;

    private UHPlayer target;
    private UHPlayer killer;

    public Mob(MobType type, Entity entity){
        this.type = type;
        this.entity = entity;
        if(type.getType().canHoldItems() || type.getType().canWearArmor()) {
            this.inv = new ToolInventory(((LivingEntity)entity).getEquipment());
        }
        this.target = null;
    }

    /* STANDARD METHODS */
    public void death(){
        this.type.death(this);
    }

    public void spawn(){
        this.type.spawn(this);
    }

    /* GETTERS */
    public UHPlayer getTarget() {
        return target;
    }

    public ToolInventory getInventory() {
        return inv;
    }

    public Entity getEntity() {
        return entity;
    }

    public UHPlayer getKiller() {
        return killer;
    }

    /* SETTERS */
    public void setKiller(UHPlayer killer) {
        this.killer = killer;
    }
}
