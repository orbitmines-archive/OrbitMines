package com.orbitmines.spigot.servers.uhsurvival2.event;

import com.orbitmines.spigot.servers.uhsurvival2.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.map.dungeon.DungeonManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;

public class PlayerInteractEvent implements Listener {

    @EventHandler
    public void onInteract(org.bukkit.event.player.PlayerInteractEvent event) {
        UHPlayer player = UHPlayer.getUHPlayer(event.getPlayer());
        if (player != null) {
            player.getInventory().setItem(8, DungeonManager.getDungeonSelector().getSelector());
            if (DungeonManager.getDungeonSelector().canSelect(player.getStaffRank(), event.getItem())) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    DungeonManager.getDungeonSelector().rightClick(player, event.getClickedBlock().getLocation());
                    player.sendMessage("First Corner Selected!");
                } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    DungeonManager.getDungeonSelector().leftClick(player, event.getClickedBlock().getLocation());
                    player.sendMessage("Second Corner Selected!");
                }
                event.setCancelled(true);
            }
        }
    }
}
