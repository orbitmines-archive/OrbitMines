package com.orbitmines.spigot.api.events;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class PlayerChatEvent implements Listener {

    private final OrbitMines orbitMines;

    public PlayerChatEvent() {
        this.orbitMines = OrbitMines.getInstance();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());

        orbitMines.getServerHandler().format(event, omp);
    }
}
