package com.orbitmines.spigot.api.events;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itemhandlers.ItemHover;
import com.orbitmines.spigot.api.handlers.itemhandlers.ItemInteraction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class ItemHandlerEvents implements Listener {

    /*
        ItemInteraction
     */

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        OMPlayer omp = OMPlayer.getPlayer(player);

        ItemStack item = event.getItem();
        if (item == null)
            return;

        for (ItemInteraction interaction : ItemInteraction.getItemInteractions()) {
            if (!interaction.getItemBuilder().equals(item))
                continue;

            event.setCancelled(true);

            interaction.onInteract(omp, event, item);

            switch (event.getAction()) {

                case LEFT_CLICK_AIR:
                case LEFT_CLICK_BLOCK:
                    interaction.onLeftClick(omp, event, item);
                    break;
                case RIGHT_CLICK_AIR:
                case RIGHT_CLICK_BLOCK:
                    interaction.onRightClick(omp, event, item);
                    break;
            }
            break;
        }
    }

    /*
        ItemHover
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onHover(PlayerItemHeldEvent event) {
        OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());

        /* Cancel previous hover */
        ItemStack previous = omp.getInventory().getItem(event.getPreviousSlot());
        if (previous != null) {
            for (ItemHover hover : ItemHover.getItemHovers()) {
                if (!hover.equals(previous))
                    continue;

                hover.leave(omp);
                break;
            }
        }

        /* Start next hover */
        ItemStack next = omp.getInventory().getItem(event.getNewSlot());
        if (next != null) {
            for (ItemHover hover : ItemHover.getItemHovers()) {
                if (!hover.equals(next))
                    continue;

                hover.enter(omp, next, event.getNewSlot());
                break;
            }
        }
    }
}
