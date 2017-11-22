package com.orbitmines.spigot.api.events;

import com.orbitmines.api.Message;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class CommandPreprocessEvent implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();
        OMPlayer omp = OMPlayer.getPlayer(p);

        String[] a = event.getMessage().split(" ");
        Command command = Command.getCommand(a[0]);

        event.setCancelled(true);

        if (command == null) {
            omp.sendMessage(Message.UNKNOWN_COMMAND);
            return;
        }

        command.dispatch(omp, a);
    }
}
