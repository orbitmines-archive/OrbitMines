package com.orbitmines.spigot.servers.uhsurvival.handlers.mob;

import com.orbitmines.spigot.servers.uhsurvival.handlers.item.tool.ToolInventory;
import org.bukkit.Location;

public interface Attacker {

    boolean attack(Attacker attacker);

    boolean defend(Attacker attacker);

    boolean hasInventory();

    ToolInventory getToolInventory();

    Location getLocation();

//    void use();
//      TODO:

}