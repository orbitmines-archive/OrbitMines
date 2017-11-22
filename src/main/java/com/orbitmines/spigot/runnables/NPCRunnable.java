package com.orbitmines.spigot.runnables;

import com.orbitmines.spigot.api.handlers.npc.*;
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

        /* Moving Npcs */
        for (NPC npc : NPC.getNpcs()) {
            if (!(npc instanceof NPCMoving))
                continue;

            NPCMoving npcMoving = (NPCMoving) npc;

            if (npcMoving.getMoveLocations().size() > 0) {
                if (npcMoving.getMovingTo() != null) {
                    if (npcMoving.isAtLocation(npcMoving.getMovingTo())) {
                        int index = npcMoving.getMovingToIndex();
                        npcMoving.setSecondsToStay(npcMoving.getSecondsToStay() - 1);

                        if (npcMoving.getSecondsToStay() == 0) {
                            npcMoving.setMovingTo(npcMoving.nextLocation());
                            npcMoving.setSecondsToStay(npcMoving.getSecondsToStay(npcMoving.getMovingTo()));

                            if (npcMoving.getSecondsToStay() == 0) {
                                npcMoving.setMovingTo(npcMoving.nextLocation());
                                npcMoving.setSecondsToStay(npcMoving.getSecondsToStay(npcMoving.getMovingTo()));
                            }
                        } else {
                            int seconds = npcMoving.getSecondsToStay();

                            npcMoving.arrive(index, seconds);
                        }
                    }
                } else {
                    npcMoving.setMovingTo(npcMoving.nextLocation());
                }
            }

            npcMoving.moveToLocation(npcMoving.getMovingTo());
        }
    }
}
