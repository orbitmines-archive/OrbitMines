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
        for (ArmorStandNpc armorStandNpc : ArmorStandNpc.getArmorStandNpcs()) {
            if (armorStandNpc.hideOnJoin())
                armorStandNpc.createForWatchers();
        }
        /* Destroy hidden Hologram */
        for (Hologram hologram : Hologram.getHolograms()) {
            if (hologram.hideOnJoin())
                hologram.createForWatchers();
        }

        /* MovingNpc Npcs */
        for (NPC npc : NPC.getNpcs()) {
            if (!(npc instanceof MovingNpc))
                continue;

            MovingNpc movingNpc = (MovingNpc) npc;

            if (movingNpc.getMoveLocations().size() > 0) {
                if (movingNpc.getMovingTo() != null) {
                    if (movingNpc.isAtLocation(movingNpc.getMovingTo())) {
                        int index = movingNpc.getMovingToIndex();
                        movingNpc.setSecondsToStay(movingNpc.getSecondsToStay() - 1);

                        if (movingNpc.getSecondsToStay() == 0) {
                            movingNpc.setMovingTo(movingNpc.nextLocation());
                            movingNpc.setSecondsToStay(movingNpc.getSecondsToStay(movingNpc.getMovingTo()));

                            if (movingNpc.getSecondsToStay() == 0) {
                                movingNpc.setMovingTo(movingNpc.nextLocation());
                                movingNpc.setSecondsToStay(movingNpc.getSecondsToStay(movingNpc.getMovingTo()));
                            }
                        } else {
                            int seconds = movingNpc.getSecondsToStay();

                            movingNpc.arrive(index, seconds);
                        }
                    }
                } else {
                    movingNpc.setMovingTo(movingNpc.nextLocation());
                }
            }

            movingNpc.moveToLocation(movingNpc.getMovingTo());
        }
    }
}
