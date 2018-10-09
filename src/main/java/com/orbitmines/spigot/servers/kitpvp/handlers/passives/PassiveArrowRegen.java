package com.orbitmines.spigot.servers.kitpvp.handlers.passives;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.utils.PlayerUtils;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders.KitItemBuilder;
import org.bukkit.Material;

public class PassiveArrowRegen implements Passive.Handler<DummyEvent> {

    /* Every Level means: x seconds to gain 1 arrow */

    @Override
    public void trigger(DummyEvent event, int level) {
        if ((System.currentTimeMillis() / 1000) % level != 0)
            return;

        KitPvPPlayer omp = event.getPlayer();

        if (omp.getSelectedKit() == null)
            return;

        /* Only allow up to 64 arrows in the inventory */
        if (PlayerUtils.getAmount(omp.getPlayer(), Material.ARROW) >= 64)
            return;

        omp.getInventory().addItem(new KitItemBuilder(omp.getSelectedKit(), Material.ARROW).build());
    }
}
