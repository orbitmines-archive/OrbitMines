package com.orbitmines.spigot.servers.uhsurvival.handlers.mob;

import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival.handlers.item.tool.Tool;
import com.orbitmines.spigot.servers.uhsurvival.handlers.item.tool.ToolInventory;
import com.orbitmines.spigot.servers.uhsurvival.handlers.item.tool.enchantments.Enchantments;
import com.orbitmines.spigot.servers.uhsurvival.utils.Enchantment;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;

public class Mob implements Attacker {

    private Entity entity;
    private ToolInventory inventory;

    private MobType type;

    private int powerLevel;

    Mob(MobType type, Entity entity){
        this.type = type;
        this.entity = entity;
        if(entity instanceof LivingEntity) this.inventory = new ToolInventory(((LivingEntity) entity).getEquipment());
    }

    @Override
    public boolean attack(Attacker defender) {
        /* determining if type has been initialized and running default procedure  */
        boolean cancelled = defender.defend(this);
        if (!cancelled && type != null) {
            cancelled = type.attack(defender, this);
        }
        if (hasInventory()) {
            Tool mainHand = inventory.getMainHand();
            if (mainHand != null && mainHand.isEnchanted()) {
                for (Enchantment enchantment : mainHand.getEnchantments().keySet()) {
                    if (!cancelled && enchantment.getOutput() instanceof Enchantments.AttackEnchantment) {
                        if (MathUtils.randomize(0, 100, (int) enchantment.getChance())) {
                            cancelled = ((Enchantments.AttackEnchantment) enchantment.getOutput()).output(this, defender, mainHand.getLevel(enchantment));
                        }
                    }
                }
            }
        }
        return cancelled;
    }

    @Override
    public boolean defend(Attacker attacker) {
        HashMap<Enchantment, Integer> enchantments = new HashMap<>();
        if(inventory != null){
            for(Tool tool : inventory.getArmor()){
                if(tool != null){
                    for(Enchantment enchantment : tool.getEnchantments().keySet()){
                        if(enchantments.containsKey(enchantment)){
                            enchantments.put(enchantment, tool.getLevel(enchantment) + enchantments.get(enchantment));
                        } else {
                            enchantments.put(enchantment, tool.getLevel(enchantment));
                        }
                    }
                }
            }
        }
        boolean cancelled = false;
        if(type != null){
            cancelled = type.defend(attacker, this);
        }
        for(Enchantment enchantment : enchantments.keySet()){
            if(!cancelled && enchantment.getOutput() instanceof Enchantments.AttackEnchantment) {
                if (MathUtils.randomize(0, 100, (int) enchantment.getChance())) {
                    cancelled = ((Enchantments.AttackEnchantment) enchantment.getOutput()).output(attacker, this, enchantments.get(enchantment));
                }
            }
        }
        return cancelled;
    }

    @Override
    public ToolInventory getToolInventory() {
        return inventory;
    }

    @Override
    public Location getLocation() {
        return entity.getLocation();
    }

    public MobType getType() {
        return type;
    }

    @Override
    public boolean hasInventory() {
        return inventory != null;
    }

    public boolean hasMobType(){
        return type != null;
    }

    @Override
    public boolean equals(Object entity) {
        return (entity instanceof Entity) && entity == this.entity;
    }
}
