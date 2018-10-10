package com.orbitmines.spigot.servers.kitpvp.events;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SpectatorEvents implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player))
            return;

        KitPvPPlayer omp = KitPvPPlayer.getPlayer((Player) event.getWhoClicked());

        if (!omp.isSpectator())
            return;

        event.setCancelled(true);
    }
}
