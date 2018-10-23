package com.orbitmines.spigot.api.nms.bednpc;

import com.orbitmines.spigot.api.handlers.npc.BedNpc;
import org.bukkit.Location;

/*
* OrbitMines - @author Fadi Shawki - 26-6-2017
*/
public interface BedNpcNms {

    int spawn(BedNpc bedNpc, boolean withArmor, boolean withItemsInHand);

    void destroy(BedNpc bedNpc);

    Location getFixedLocation(BedNpc bedNpc);

}
