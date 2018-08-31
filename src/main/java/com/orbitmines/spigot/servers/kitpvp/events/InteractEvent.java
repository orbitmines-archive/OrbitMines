package com.orbitmines.spigot.servers.kitpvp.events;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPMap;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractEvent implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        KitPvPPlayer omp = KitPvPPlayer.getPlayer(event.getPlayer());

        if (omp.isSpectator()) {
            event.setCancelled(true);
            return;
        }

        if (omp.getSelectedKit() == null)
            return;

        Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.SIGN && block.getType() != Material.WALL_SIGN)
            return;

        KitPvPMap map = KitPvPMap.getMap(block.getLocation());

        if (map == null)
            return;

        KitPvPMap previous = KitPvPMap.getVotedFor(omp.getUUID());
        if (previous != null)
            previous.getVotes().remove(omp.getUUID());

        map.getVotes().add(omp.getUUID());

        omp.playSound(Sound.ENTITY_ARROW_HIT_PLAYER);
    }
}
