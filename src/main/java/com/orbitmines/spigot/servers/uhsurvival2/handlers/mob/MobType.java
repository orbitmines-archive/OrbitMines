package com.orbitmines.spigot.servers.uhsurvival2.handlers.mob;

import com.orbitmines.spigot.servers.uhsurvival2.handlers.map.dungeon.loottable.LootTable;
import org.bukkit.entity.EntityType;

public abstract class MobType {

    private EntityType type;

    private double itemExp;
    private double armorExp;

    private double damage;

    private LootTable lootTable;

    public MobType(EntityType type){
        this.type = type;
        this.itemExp = 0;
        this.armorExp = 0;
        this.damage = 0;
        this.lootTable = null;
    }

    /* ABSTRACT METHODS */
    public abstract boolean attack(Attacker defender, Mob mob);

    public abstract boolean defend(Attacker attacker, Mob mob);

    public abstract void die(Mob mob);

    public abstract void spawn(Mob mob);

    /* GETTERS */
    public EntityType getType() {
        return type;
    }

    public double getItemExp() {
        return itemExp;
    }

    public double getArmorExp() {
        return armorExp;
    }

    public double getDamage(){
        return damage;
    }

    public LootTable getLootTable() {
        return lootTable;
    }

    /* BOOLEAN */
    public boolean hasLootTable(){
        return lootTable != null;
    }

    /* SETTERS */
    public void setType(EntityType type) {
        this.type = type;
    }

    public void setItemExp(double itemExp) {
        this.itemExp = itemExp;
    }

    public void setArmorExp(double armorExp) {
        this.armorExp = armorExp;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public void setLootTable(LootTable lootTable) {
        this.lootTable = lootTable;
    }
}
