package com.orbitmines.spigot.servers.kitpvp.events;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.nms.itemstack.ItemStackNms;
import com.orbitmines.spigot.servers.kitpvp.KitPvP;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.spigot.servers.kitpvp.handlers.passives.Passive;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class DamageEvent implements Listener {

    private final ItemStackNms nms;

    public DamageEvent(KitPvP kitPvP) {
        this.nms = kitPvP.getOrbitMines().getNms().customItem();
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player entity = (Player) event.getEntity();
            KitPvPPlayer omp = KitPvPPlayer.getPlayer(entity);

            if (omp.getSelectedKit() == null || omp.isSpectator()) {
                event.setCancelled(true);
                return;
            }

            Map<Passive, Integer> totalPassive = new HashMap<>();

            for (ItemStack item : omp.getInventory().getArmorContents()) {
                if (item == null)
                    continue;

                Map<Passive, Integer> passives = Passive.from(nms, item, Passive.Interaction.ON_HIT);

                /* No Passives on this item */
                if (passives == null)
                    continue;

                for (Passive passive : passives.keySet()) {
                    int level = passives.get(passive);

                    if (!totalPassive.containsKey(passive)) {
                        totalPassive.put(passive, level);
                    } else {
                        int totalLevel = totalPassive.get(passive);

                        if (passive.isStackable())
                            totalPassive.put(passive, totalLevel + level);
                        else if (level > totalLevel)
                            totalPassive.put(passive, level);
                    }
                }
            }

            for (Passive passive : totalPassive.keySet()) {
                passive.getHandler().trigger(event, totalPassive.get(passive));
            }
        }
    }
}
