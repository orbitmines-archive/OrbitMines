package com.orbitmines.spigot.runnables;

import com.orbitmines.spigot.api.handlers.npc.FloatingItem;
import com.orbitmines.spigot.api.handlers.npc.Hologram;
import com.orbitmines.spigot.api.handlers.npc.NpcArmorStand;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class NPCRunnable extends SpigotRunnable {

    public NPCRunnable() {
        super(SpigotRunnable.TimeUnit.SECOND, 1);
    }

    @Override
    public void run() {
        /* Destroy hidden items */
        for (FloatingItem floatingItem : FloatingItem.getFloatingItems()) {
            for (FloatingItem.ItemInstance itemInstance : floatingItem.getItemInstances()) {
                if (itemInstance.hideOnJoin())
                    itemInstance.createForWatchers();
            }
        }
        /* Destroy hidden ArmorStands */
        for (NpcArmorStand npcArmorStand : NpcArmorStand.getNpcArmorStands()) {
            if (npcArmorStand.hideOnJoin())
                npcArmorStand.createForWatchers();
        }
        /* Destroy hidden Hologram */
        for (Hologram hologram : Hologram.getHolograms()) {
            if (hologram.hideOnJoin())
                hologram.createForWatchers();
        }
    }
}
