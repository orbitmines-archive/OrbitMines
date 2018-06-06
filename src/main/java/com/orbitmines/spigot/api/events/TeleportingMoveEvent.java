package com.orbitmines.spigot.api.events;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Message;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.Teleportable;
import com.orbitmines.spigot.api.handlers.chat.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class TeleportingMoveEvent implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        OMPlayer omp = OMPlayer.getPlayer(p);

        if (omp.getTeleportingTo() == null)
            return;

        if (!omp.isMoving())
            return;

        Teleportable teleportable = omp.getTeleportingTo();

        new Title(new Message(""), new Message(teleportable.getColor().getChatColor() + "§l" + teleportable.getName() + " §7§lTeleportatie §c§lGeannuleerd§7.", "§c§lCancelled " + teleportable.getColor().getChatColor() + "§l" + teleportable.getName() + " §7§lTeleportation."), 0, 40, 20).send(omp);

        omp.setTeleportingTo(null);
        omp.getTeleportingTimer().cancel();
        omp.setTeleportingTimer(null);
    }
}
