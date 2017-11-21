package com.orbitmines.spigot.api.events;

import com.madblock.spigot.api.handlers.GUI;
import com.madblock.spigot.api.handlers.OMPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class ClickEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        OMPlayer omp = OMPlayer.getPlayer((Player) event.getWhoClicked());

        if (!omp.isLoggedIn()) {
            event.setCancelled(true);
            return;
        }

        GUI lastInventory = omp.getLastInventory();

        if (lastInventory == null)
            return;

        lastInventory.processClickEvent(event, omp);
    }
}
