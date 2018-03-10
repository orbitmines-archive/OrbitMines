package com.orbitmines.spigot.servers.uhsurvival.events;

import com.orbitmines.spigot.servers.uhsurvival.UHSurvival;
import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Created by Robin on 3/6/2018.
 */
public class BreakBlockEvent implements Listener {

    private UHSurvival uhSurvival;

    public BreakBlockEvent(UHSurvival uhSurvival){
        this.uhSurvival = uhSurvival;
    }

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent event) {
        UHPlayer p = UHPlayer.getUHPlayer(event.getPlayer());
        if (p != null) {
            event.setCancelled(uhSurvival.getBlockManager().breakBlock(p, event.getBlock().getLocation()));
        }
    }
}
