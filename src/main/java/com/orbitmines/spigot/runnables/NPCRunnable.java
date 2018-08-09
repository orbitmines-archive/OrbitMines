package com.orbitmines.spigot.runnables;

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
//        /* Destroy hidden items */
//        for (DEPRECATED_FloatingItem floatingItem : DEPRECATED_FloatingItem.getFloatingItems()) {
//            for (DEPRECATED_FloatingItem.ItemInstance itemInstance : floatingItem.getItemInstances()) {
//                if (itemInstance.hideOnJoin())
//                    itemInstance.createForWatchers();
//            }
//        }
//        /* Destroy hidden ArmorStands */
//        for (ArmorStandNpc armorStandNpc : ArmorStandNpc.getArmorStandNpcs()) {
//            if (armorStandNpc.hideOnJoin())
//                armorStandNpc.createForWatchers();
//        }
//        /* Destroy hidden Hologram */
//        for (Hologram hologram : Hologram.getHolograms()) {
//            if (hologram.hideOnJoin())
//                hologram.createForWatchers();
//        }TODO

    }
}
