package com.orbitmines.spigot.servers.kitpvp.events;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.nms.itemstack.ItemStackNms;
import com.orbitmines.spigot.servers.kitpvp.KitPvP;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.spigot.servers.kitpvp.handlers.passives.Passive;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class DamageByEntityEvent implements Listener {

    private final ItemStackNms nms;

    public DamageByEntityEvent(KitPvP kitPvP) {
        this.nms = kitPvP.getOrbitMines().getNms().customItem();
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            KitPvPPlayer ompD = KitPvPPlayer.getPlayer(damager);

            if (ompD.getSelectedKit() == null || ompD.isSpectator()) {
                event.setCancelled(true);
                return;
            }

            ItemStack item = damager.getInventory().getItemInMainHand();

            if (item == null)
                return;

            Map<Passive, Integer> passives = Passive.from(nms, item, Passive.Interaction.HIT_OTHER);

            /* No Passives on this item */
            if (passives == null)
                return;

            for (Passive passive : passives.keySet()) {
                passive.getHandler().trigger(event, passives.get(passive));
            }
        } else if (event.getDamager() instanceof Firework) {
            event.setCancelled(true);
            /* Firework used by kits */
        }
    }

}
