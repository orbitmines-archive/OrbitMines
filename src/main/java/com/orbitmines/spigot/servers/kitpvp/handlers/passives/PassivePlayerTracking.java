package com.orbitmines.spigot.servers.kitpvp.handlers.passives;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.utils.WorldUtils;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class PassivePlayerTracking implements Passive.Handler<DummyEvent> {

    @Override
    public void trigger(DummyEvent event, int level) {
        KitPvPPlayer omp = event.getPlayer();
        Player player = omp.getPlayer();

        OMPlayer closest = WorldUtils.getClosestPlayer(omp, GameMode.SURVIVAL, true);

        if (closest == null)
            return;

        player.setCompassTarget(closest.getLocation());
    }
}
