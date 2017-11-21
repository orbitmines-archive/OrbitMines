package com.orbitmines.spigot.api.events;

import com.madblock.spigot.api.handlers.ItemInteraction;
import com.madblock.spigot.api.handlers.OMPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class InteractEvent implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        OMPlayer omp = OMPlayer.getPlayer(player);

        ItemStack item = event.getItem();
        if (item == null)
            return;

        for (ItemInteraction interaction : ItemInteraction.getItemInteractions()) {
            if (!interaction.equals(item))
                continue;

            event.setCancelled(true);

            interaction.onInteract(mbp, event, item);

            switch (event.getAction()) {

                case LEFT_CLICK_AIR:
                case LEFT_CLICK_BLOCK:
                    interaction.onLeftClick(mbp, event, item);
                    break;
                case RIGHT_CLICK_AIR:
                case RIGHT_CLICK_BLOCK:
                    interaction.onRightClick(mbp, event, item);
                    break;
            }
            break;
        }
    }
}
