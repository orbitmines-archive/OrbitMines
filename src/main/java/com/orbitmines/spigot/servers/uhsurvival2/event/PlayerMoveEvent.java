package com.orbitmines.spigot.servers.uhsurvival2.event;

import com.orbitmines.spigot.servers.uhsurvival2.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.map.MapSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerMoveEvent implements Listener {

    @EventHandler
    public void onMove(org.bukkit.event.player.PlayerMoveEvent event) {
        UHPlayer player = UHPlayer.getUHPlayer(event.getPlayer());
        if (player != null) {
            MapSection mapSection = player.getMapLocation();
            if (mapSection.isTransfering(event.getFrom())) {
                if (!mapSection.isLocation(event.getTo())) {
                    player.updateMapLocation();
                }
            }
        }
    }
}
