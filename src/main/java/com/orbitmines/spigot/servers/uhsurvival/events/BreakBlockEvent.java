package com.orbitmines.spigot.servers.uhsurvival.events;

import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.Map;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Created by Robin on 3/6/2018.
 */
public class BreakBlockEvent implements Listener {

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent event){
        Map map = World.getWorldByEnvironment(event.getBlock().getWorld().getEnvironment()).getMap();
        if(map != null){
            UHPlayer p = UHPlayer.getUHPlayer(event.getPlayer());
            if(p != null){
                event.setCancelled(map.getBlocks().breakBlock(p, event.getBlock().getLocation()));
            }
        }
    }
}
