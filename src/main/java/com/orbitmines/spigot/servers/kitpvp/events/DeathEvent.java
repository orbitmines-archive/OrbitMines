package com.orbitmines.spigot.servers.kitpvp.events;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.nms.entity.EntityNms;
import com.orbitmines.spigot.servers.kitpvp.KitPvP;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class DeathEvent implements Listener {

    private KitPvP kitPvP;

    public DeathEvent(KitPvP kitPvP) {
        this.kitPvP = kitPvP;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        /* Clear Drops */
        event.setDroppedExp(0);
        event.getDrops().clear();

        Player player = event.getEntity();
        KitPvPPlayer omp = KitPvPPlayer.getPlayer(player);

        /* Clear Inventory & Potion Effects */
        omp.clearFullInventory();
        omp.clearPotionEffects();

        /* Set Health */
        kitPvP.getOrbitMines().getNms().entity().setAttribute(player, EntityNms.Attribute.MAX_HEALTH, 20D);
        player.setHealth(20D);

        Player playerKiller = player.getKiller();
        KitPvPPlayer ompKiller = playerKiller != null ? KitPvPPlayer.getPlayer(playerKiller) : null;

        /* Handle Death */
        omp.processDeath(ompKiller);
        /* Handle Kill */
        if (ompKiller != null)
            ompKiller.processKill(omp);

        /* Teleport to Spawn */
        player.teleport(kitPvP.getSpawnLocation(player));

        new BukkitRunnable() {
            @Override
            public void run() {
                /* Clear Velocity */
                player.setVelocity(new Vector(0, 0, 0));
                /* Clear Fire ticks */
                player.setFireTicks(0);
                /* Give Lobby Kit */
                kitPvP.getLobbyKit(omp).setItems(omp);
                player.getInventory().setHeldItemSlot(4);
            }
        }.runTaskLater(kitPvP.getOrbitMines(), 1);
    }
}
