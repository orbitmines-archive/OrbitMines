package com.orbitmines.spigot.servers.kitpvp.handlers.passives;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.handlers.chat.ActionBar;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class PassiveLightningProtection implements Passive.Handler<EntityDamageEvent> {

    /* 12.5% per level chance to evade lightning */

    @Override
    public void trigger(EntityDamageEvent event, int level) {
        if (event.getCause() != EntityDamageEvent.DamageCause.LIGHTNING)
            return;

        /* There's a chance of evading the lightning hitting, otherwise move on */
        if (Math.random() >= getChance(level))
            return;

        event.setCancelled(true);

        Player player = (Player) event.getEntity();
        KitPvPPlayer omp = KitPvPPlayer.getPlayer(player);

        new ActionBar(omp, () -> omp.lang("§e§lBliksem Ontweken", "§e§lEvaded Lightning"), 30).send();
    }

    public double getChance(int level) {
        return 0.125D * level;
    }
}
