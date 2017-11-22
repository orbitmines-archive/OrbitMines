package com.orbitmines.spigot.api.events;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class JoinQuitEvents implements Listener {

    private final OrbitMines orbitMines;

    public JoinQuitEvents() {
        orbitMines = OrbitMines.getInstance();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        OMPlayer omp = orbitMines.getServerHandler().newPlayerInstance(event.getPlayer());
        omp.login();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());
        omp.logout();
    }
}
