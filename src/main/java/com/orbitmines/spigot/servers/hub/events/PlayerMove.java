package com.orbitmines.spigot.servers.hub.events;


import com.orbitmines.spigot.servers.hub.handlers.HubPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    public PlayerMove() {

    }

    @EventHandler (priority = EventPriority.LOW)
    public void onPlayerMove (PlayerMoveEvent e) {
        HubPlayer p = HubPlayer.getPlayer(e.getPlayer());
        p.updateCosmetics();
    }
}
