package com.orbitmines.spigot.servers.uhsurvival2.handlers.mob;

import com.orbitmines.spigot.servers.uhsurvival2.handlers.item.tool.enchantments.Enchantments;
import com.orbitmines.spigot.servers.uhsurvival2.utils.Enchantment;
import com.orbitmines.spigot.servers.uhsurvival2.utils.ToolType;
import org.bukkit.entity.Entity;

import java.util.HashMap;

public class Arrow extends Mob {

    private HashMap<Enchantment, Integer> enchantments;
    private Attacker shooter;

    Arrow(MobType type, Entity entity, Attacker attacker) {
        super(type, entity);
        this.shooter = attacker;
        if(attacker.getToolInventory().getMainHand().getType() == ToolType.BOW){
            enchantments = attacker.getToolInventory().getMainHand().getEnchantments();
        } else {
            enchantments = attacker.getToolInventory().getOffHand().getEnchantments();
        }
    }

    @Override
    public boolean attack(Attacker defender) {
        boolean cancelled = defender.defend(shooter);
        for (Enchantment enchantment : enchantments.keySet()) {
            if(!cancelled && enchantment.getOutput() instanceof Enchantments.AttackEnchantment) {
                //TODO: FIX IT~DOUBLE!
                cancelled = ((Enchantments.AttackEnchantment) enchantment.getOutput()).output(shooter, defender, enchantments.get(enchantment));
            }
        }
        return cancelled;
    }

    @Override
    public boolean defend(Attacker attacker) {
        return true;
    }
}
