package com.orbitmines.spigot.servers.uhsurvival2.event;

import com.orbitmines.spigot.servers.uhsurvival2.UHSurvival;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.UHPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlockEvent implements Listener {

    private UHSurvival uhSurvival;

    public BreakBlockEvent(UHSurvival uhSurvival) {
        this.uhSurvival = uhSurvival;
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        UHPlayer player = UHPlayer.getUHPlayer(event.getPlayer());
        if (player != null) {
            event.setCancelled(uhSurvival.getBM().breakBlock(player, event.getBlock()));
        }
    }
}
