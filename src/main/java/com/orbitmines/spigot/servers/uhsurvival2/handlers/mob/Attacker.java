package com.orbitmines.spigot.servers.uhsurvival2.handlers.mob;

import com.orbitmines.spigot.servers.uhsurvival2.handlers.tool.ToolInventory;

public interface Attacker {

    boolean attack(Attacker attacker);

    boolean defend(Attacker attacker);

    ToolInventory getToolInventory();

    boolean hasInventory();

//    void use();
//      TODO:

}
