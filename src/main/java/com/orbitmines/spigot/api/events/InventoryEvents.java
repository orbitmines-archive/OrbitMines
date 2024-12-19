package com.orbitmines.spigot.api.events;

import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class InventoryEvents implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDrag(InventoryDragEvent event) {
        OMPlayer omp = OMPlayer.getPlayer((Player) event.getWhoClicked());

        if (!omp.isLoggedIn()) {
            event.setCancelled(true);
            return;
        }

        GUI lastInventory = omp.getLastInventory();

        if (lastInventory == null)
            return;

        lastInventory.processDragEvent(event, omp);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClose(InventoryCloseEvent event) {
        OMPlayer omp = OMPlayer.getPlayer((Player) event.getPlayer());

        if (!omp.isLoggedIn())
            return;

        GUI lastInventory = omp.getLastInventory();

        if (lastInventory == null)
            return;

        lastInventory.processCloseEvent(event, omp);
    }
}
