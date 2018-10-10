package com.orbitmines.spigot.servers.uhsurvival.handlers.mob.mobs;

import com.orbitmines.spigot.servers.uhsurvival.handlers.item.tool.enchantments.Enchantments;
import com.orbitmines.spigot.servers.uhsurvival.handlers.mob.Attacker;
import com.orbitmines.spigot.servers.uhsurvival.handlers.mob.DefaultMob;
import com.orbitmines.spigot.servers.uhsurvival.handlers.mob.MobType;
import com.orbitmines.spigot.servers.uhsurvival.utils.Enchantment;
import com.orbitmines.spigot.servers.uhsurvival.utils.ToolType;
import org.bukkit.entity.Entity;

import java.util.HashMap;

public class Arrow extends DefaultMob {

    private HashMap<Enchantment, Integer> enchantments;
    private Attacker shooter;

    public Arrow(MobType type, Entity entity, Attacker attacker) {
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
