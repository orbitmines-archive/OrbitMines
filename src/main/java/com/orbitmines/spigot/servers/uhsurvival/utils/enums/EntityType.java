package com.orbitmines.spigot.servers.uhsurvival.utils.enums;

import com.orbitmines.spigot.api.Mob;

/**
 * Created by Robin on 2/28/2018.
 */
public enum EntityType {

    ;

    private Mob mob;

    private boolean canHoldItems;
    private boolean canWearArmor;

    EntityType(Mob mob, boolean canHoldItems, boolean canWearArmor){
        this.mob = mob;
        this.canHoldItems = canHoldItems;
        this.canWearArmor = canWearArmor;
    }

    public Mob getMob() {
        return mob;
    }

    public boolean canHoldItems(){
        return canHoldItems;
    }

    public boolean canWearArmor(){
        return canWearArmor;
    }

    public static EntityType getEntityTypeByType(org.bukkit.entity.EntityType type){
        for(EntityType t : EntityType.values()){
            if(t.getMob().getType() == type){
                return t;
            }
        }
        return null;
    }
}
