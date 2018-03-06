package com.orbitmines.spigot.servers.uhsurvival.handlers.mob;

import com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.DungeonManager;
import com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.loottable.LootTable;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.EntityType;
import org.bukkit.event.Event;

/**
 * Created by Robin on 2/28/2018.
 */
public abstract class MobType {

    private String name;
    private EntityType type;

    private double attack;
    private double specialAttack;

    private int itemExp;
    private int armorExp;

    private LootTable lootTable;

    private int radius;

    public MobType(String name, EntityType type, String lootTable){
        this.name = name;
        this.type = type;
        this.lootTable = DungeonManager.getLootTableManager().getLootTable(lootTable);
        this.radius = 0;
        this.itemExp = 0;
        this.armorExp = 0;
        this.attack = 0;
        this.specialAttack = 0;
    }

    /* ABSTRACT METHODS */
    public abstract void attack(Event event);

    public abstract void death(Mob mob);

    public abstract void spawn(Mob mob);

    /* SETTERS */
    public void setItemExp(int itemExp) {
        this.itemExp = itemExp;
    }

    public void setArmorExp(int armorExp) {
        this.armorExp = armorExp;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setAttack(double attack) {
        this.attack = attack;
    }

    public void setSpecialAttack(double specialAttack) {
        this.specialAttack = specialAttack;
    }

    /* GETTERS */
    public EntityType getType() {
        return type;
    }

    public LootTable getLootTable() {
        return lootTable;
    }

    public double getAttack() {
        return attack;
    }

    public double getSpecialAttack() {
        return specialAttack;
    }

    public int getRadius() {
        return radius;
    }

    public int getItemExp() {
        return itemExp;
    }

    public int getArmorExp() {
        return armorExp;
    }

    public String getName() {
        return name;
    }
}
