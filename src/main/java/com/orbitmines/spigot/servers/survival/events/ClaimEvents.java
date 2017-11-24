package com.orbitmines.spigot.servers.survival.events;

import com.orbitmines.spigot.servers.survival.Survival;
import org.bukkit.event.Listener;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class ClaimEvents implements Listener {

    private Survival survival;

    public ClaimEvents(Survival survival) {
        this.survival = survival;
    }

//    @EventHandler
//    public void onInteract(PlayerInteractEvent event) {
//        Player p = event.getPlayer();
//        ItemStack item = event.getItem();
//
//        if (p.getWorld().getName().equals(survival.getWorld().getName()) || item == null || item.getType() != Material.STONE_HOE)
//            return;
//
//        event.setCancelled(true);
//        SurvivalPlayer.getPlayer(p).updateInventory();
//    }


}
