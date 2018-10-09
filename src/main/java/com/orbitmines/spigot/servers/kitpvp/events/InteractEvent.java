package com.orbitmines.spigot.servers.kitpvp.events;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.spigot.api.nms.itemstack.ItemStackNms;
import com.orbitmines.spigot.servers.kitpvp.KitPvP;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPMap;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.spigot.servers.kitpvp.handlers.actives.Active;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class InteractEvent implements Listener {

    private final ItemStackNms nms;

    public InteractEvent(KitPvP kitPvP) {
        this.nms = kitPvP.getOrbitMines().getNms().customItem();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        KitPvPPlayer omp = KitPvPPlayer.getPlayer(event.getPlayer());

        if (omp.isSpectator()) {
            event.setCancelled(true);
            return;
        }

        if (omp.getSelectedKit() != null) {
            /* In arena */
            ItemStack item = event.getItem();

            if (item == null)
                return;

            int slot = omp.getInventory().first(item);

            Map<Active, Integer> actives = Active.from(nms, item);

            /* No Actives on this item */
            if (actives == null)
                return;

            /* Active found, so we cancel the event */
            event.setCancelled(true);

            for (Active active : actives.keySet()) {
                Active.Handler handler = active.getHandler();
                int level = actives.get(active);

                if (active.canUse(nms, item, level)) {
                    /* Reset Cooldown */
                    item = active.resetCooldown(nms, item, level);
                    omp.getInventory().setItem(slot, item);

                    /* Trigger active */
                    String name = active.getColor().getChatColor() + "§l" + active.getName();
                    omp.sendMessage("Active", Color.BLUE, "Je hebt " + name + "§7 gebruikt.", "You have used " + name + "§7.");
                    handler.trigger(event, omp, level);
                }
            }

            return;
        }

        /* In lobby */
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
