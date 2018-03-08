package com.orbitmines.spigot.servers.uhsurvival.events;

import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by Robin on 3/8/2018.
 */
public class MapEvents implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        UHPlayer uhPlayer = UHPlayer.getUHPlayer(event.getPlayer());
        if(uhPlayer != null){
            if(uhPlayer.isCloseToEdge()){

            }
        }
    }



}
