package com.orbitmines.spigot.servers.kitpvp.events;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class RegainHealthEvent implements Listener {

    @EventHandler
    public void onRegain(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player) || event.getRegainReason() != EntityRegainHealthEvent.RegainReason.SATIATED || event.getRegainReason() != EntityRegainHealthEvent.RegainReason.REGEN)
            return;

        Player p = (Player) event.getEntity();
        KitPvPPlayer omp = KitPvPPlayer.getPlayer(p);

        if (omp.getSelectedKit() == null)
            return;

        /* Increase health regen */
        double amount = event.getAmount() * 1.75;
        amount *= omp.getSelectedKit().getAttributes().getHealthRegen().getMultiplier();

        event.setAmount(amount);

        //TODO RESDET DAMAGE
    }
}
