package com.orbitmines.spigot.api.events;

import com.orbitmines.spigot.OrbitMines;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class SpawnLocationEvent implements Listener {

    private final OrbitMines orbitMines;

    public SpawnLocationEvent() {
        this.orbitMines = OrbitMines.getInstance();
    }

    @EventHandler
    public void onSpawn(PlayerSpawnLocationEvent event) {
        if (orbitMines.getServerHandler().teleportToSpawn(event.getPlayer()))
            event.setSpawnLocation(orbitMines.getServerHandler().getSpawnLocation(event.getPlayer()));
    }
}
