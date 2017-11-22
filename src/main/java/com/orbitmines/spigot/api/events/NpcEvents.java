package com.orbitmines.spigot.api.events;

import com.orbitmines.api.Cooldown;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.npc.*;
import com.orbitmines.spigot.api.utils.PlayerUtils;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
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

    @EventHandler
    public void onInteractEntityEvent(PlayerInteractEntityEvent event) {
        NPC npc = NPC.getNpc(event.getRightClicked());
        if (npc == null)
            return;

        event.setCancelled(true);

        OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());
        if (!omp.onCooldown(INTERACT_COOLDOWN)) {
            npc.click(omp);

            omp.resetCooldown(INTERACT_COOLDOWN);
        }
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        NPC npc = NPC.getNpc(event.getEntity());
        if (npc != null)
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemPickUpEvent(PlayerPickupItemEvent event) {

        floatingItem:
        {
            FloatingItem floatingItem = FloatingItem.getFloatingItem(event.getItem());

            if (floatingItem == null)
                break floatingItem;

            if (!floatingItem.canPickUp()) {
                event.setCancelled(true);
                return;
            }

            event.setCancelled(false);

            floatingItem.pickUp(event, OMPlayer.getPlayer(event.getPlayer()));
            return;
        }

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

    @EventHandler
    public void onInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof ArmorStand))
            return;

        ArmorStand armorStand = (ArmorStand) event.getRightClicked();

        Player player = event.getPlayer();

        FloatingItem floatingItem = FloatingItem.getFloatingItem(armorStand);

        if (floatingItem != null) {
            event.setCancelled(true);

            if (floatingItem.canClick())
                floatingItem.click(event, OMPlayer.getPlayer(player));

            PlayerUtils.updateInventory(player);
            return;
        }

        NpcArmorStand npcArmorStand = NpcArmorStand.getNpcArmorStand(armorStand);

        if (npcArmorStand != null) {
            event.setCancelled(true);

            if (npcArmorStand.canClick())
                npcArmorStand.click(event, OMPlayer.getPlayer(player));

            PlayerUtils.updateInventory(player);
            return;
        }


        Hologram hologram = Hologram.getHologram(armorStand);

        if (hologram != null) {
            event.setCancelled(true);

            PlayerUtils.updateInventory(player);
        }
    }

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        String worldName = event.getWorld().getName();

        for (NPC npc : new ArrayList<>(NPC.getNpcs())) {
            if (npc.getLocation().getWorld().getName().equals(worldName))
                npc.delete();
        }
        for (NpcArmorStand npcArmorStand : new ArrayList<>(NpcArmorStand.getNpcArmorStands())) {
            if (npcArmorStand.getLocation().getWorld().getName().equals(worldName))
                npcArmorStand.delete();
        }
        for (Hologram hologram : new ArrayList<>(Hologram.getHolograms())) {
            if (hologram.getLocation().getWorld().getName().equals(worldName))
                hologram.delete();
        }
        for (FloatingItem floatingItem : new ArrayList<>(FloatingItem.getFloatingItems())) {
            if (floatingItem.getLocation().getWorld().getName().equals(worldName))
                floatingItem.delete();
        }
    }
}
