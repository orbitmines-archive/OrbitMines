package com.orbitmines.spigot.servers.uhsurvival2.handlers.mob;

import com.orbitmines.spigot.servers.uhsurvival2.handlers.tool.ToolInventory;

public interface Attacker {

    void attack();

    void defend();

    ToolInventory getToolInventory();

    boolean hasInventory();

//    void use();
//      TODO:

}
