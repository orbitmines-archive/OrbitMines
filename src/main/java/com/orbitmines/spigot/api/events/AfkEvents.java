package com.orbitmines.spigot.api.events;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class AfkEvents implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
        OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());

        if (omp.isAfk())
            omp.noLongerAfk();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMove(PlayerMoveEvent event) {
        OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());

        if (omp.isAfk())
            omp.noLongerAfk();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());

        if (omp.isAfk())
            omp.noLongerAfk();
    }
}
