package com.orbitmines.spigot.api.events;

import com.orbitmines.api.Cooldown;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.npc.FloatingItem;
import com.orbitmines.spigot.api.handlers.npc.Npc;
import com.orbitmines.spigot.api.handlers.npc.todo.CustomItem;
import com.orbitmines.spigot.api.utils.PlayerUtils;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import java.util.ArrayList;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class NpcEvents implements Listener {

    private OrbitMines orbitMines;

    private final Cooldown INTERACT_COOLDOWN = new Cooldown(1000);

    public NpcEvents() {
        orbitMines = OrbitMines.getInstance();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteractEntityEvent(PlayerInteractEntityEvent event) {
        Npc npc = Npc.getNpc(event.getRightClicked());
        if (npc == null)
            return;

        event.setCancelled(true);

        OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());
        if (npc.isClickable() && !omp.onCooldown(INTERACT_COOLDOWN)) {
            npc.getInteractAction().onInteract(event, omp);

            omp.resetCooldown(INTERACT_COOLDOWN);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageEvent(EntityDamageEvent event) {
        Npc npc = Npc.getNpc(event.getEntity());
        if (npc != null)
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemPickUpEvent(PlayerPickupItemEvent event) {
        CustomItem customItem = CustomItem.getCustomItem(event.getItem());

        if (customItem == null)
            return;

        if (!customItem.canPickUp()) {
            event.setCancelled(true);
            return;
        }

        event.setCancelled(false);

        customItem.pickUp(event, OMPlayer.getPlayer(event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof ArmorStand))
            return;

        ArmorStand armorStand = (ArmorStand) event.getRightClicked();

        Player player = event.getPlayer();

        Npc npc = Npc.getNpc(armorStand);

        if (npc != null) {
            event.setCancelled(true);

            if (npc.isClickable())
                npc.getInteractAction().onInteract(event, OMPlayer.getPlayer(player));

            PlayerUtils.updateInventory(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onWorldUnload(WorldUnloadEvent event) {
        String worldName = event.getWorld().getName();

        for (Npc npc : new ArrayList<>(Npc.getNpcs())) {
            if (npc.getSpawnLocation().getWorld().getName().equals(worldName))
                npc.destroy();
        }
    }

    @EventHandler
    public void onItemDespawnEvent(ItemDespawnEvent event) {
        FloatingItem npc = FloatingItem.getFloatingItem(event.getEntity());

        if (npc != null)
            event.setCancelled(true);
    }
}
