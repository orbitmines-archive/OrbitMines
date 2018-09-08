package com.orbitmines.spigot.servers.uhsurvival.event;

import com.orbitmines.spigot.servers.uhsurvival.handlers.item.tool.Tool;
import com.orbitmines.spigot.servers.uhsurvival.handlers.item.tool.enchantments.Enchantments;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

public class EnchantmentEvent implements Listener {

    @EventHandler
    public void onEnchantment(EnchantItemEvent event){
        Tool tool = Tool.getTool(event.getItem());
        if(tool != null){
            Enchantments.enchant(tool, event.getExpLevelCost());
        }
    }
}
