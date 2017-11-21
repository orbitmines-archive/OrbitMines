package com.orbitmines.spigot.api.events;

import com.madblock.spigot.api.Freezer;
import com.madblock.spigot.api.handlers.OMPlayer;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class FreezeEvent implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());
        if (!mbp.isFrozen())
            return;

        switch (mbp.getFreezer()) {

            case MOVE:
            case MOVE_AND_JUMP:
                Location to = event.getTo();
                Location newTo = event.getFrom();

                newTo.setYaw(to.getYaw());
                newTo.setPitch(to.getPitch());

                if (mbp.getFreezer() != Freezer.MOVE_AND_JUMP)
                    newTo.setY(to.getY());

                event.setTo(newTo);
                break;
            case MOVE_AND_LOOK_AROUND:
                event.setTo(event.getFrom());
                break;
        }
    }
}
