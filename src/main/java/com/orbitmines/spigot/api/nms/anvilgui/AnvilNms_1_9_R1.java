package com.orbitmines.spigot.api.nms.anvilgui;

import com.orbitmines.spigot.OrbitMines;
import net.minecraft.server.v1_9_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

/**
 * Created by chasechocolate.
 */
public class AnvilNms_1_9_R1 implements AnvilNms {

    private class AnvilContainer extends ContainerAnvil {
        public AnvilContainer(EntityHuman entity) {
            super(entity.inventory, entity.world, BlockPosition.ZERO, entity);
        }

        @Override
        public boolean a(EntityHuman entityhuman) {
            return true;
        }
    }

    private Player player;

    private AnvilClickEventHandler handler;

    private HashMap<AnvilSlot, ItemStack> items = new HashMap<>();

    private Inventory inv;

    private Listener listener;

    public AnvilNms_1_9_R1(Player player, AnvilClickEventHandler handler, AnvilCloseEvent closeEvent) {
        this.player = player;
        this.handler = handler;

        this.listener = new Listener() {
            @EventHandler
            public void onInventoryClick(InventoryClickEvent event) {
                if (event.getWhoClicked() instanceof Player) {
                    if (event.getInventory().equals(inv)) {
                        event.setCancelled(true);

                        ItemStack item = event.getCurrentItem();
                        int slot = event.getRawSlot();
                        String name = "";

                        if (item != null) {
                            if (item.hasItemMeta()) {
                                ItemMeta meta = item.getItemMeta();

                                if (meta.hasDisplayName()) {
                                    name = meta.getDisplayName();
                                }
                            }
                        }

                        AnvilClickEvent clickEvent = new AnvilClickEvent(AnvilSlot.bySlot(slot), name);

                        handler.onAnvilClick(clickEvent);

                        if (clickEvent.getWillClose())
                            event.getWhoClicked().closeInventory();

                        if (clickEvent.getWillDestroy())
                            destroy();
                    }
                }
            }

            @EventHandler
            public void onInventoryClose(InventoryCloseEvent event) {
                if (event.getPlayer() instanceof Player) {
                    Inventory inv = event.getInventory();

                    if (inv.equals(AnvilNms_1_9_R1.this.inv)) {
                        inv.clear();
                        destroy();

                        if (closeEvent != null)
                            closeEvent.onClose();
                    }
                }
            }

            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent event) {
                if (event.getPlayer().equals(getPlayer())) {
                    destroy();

                    if (closeEvent != null)
                        closeEvent.onClose();
                }
            }
        };

        Bukkit.getPluginManager().registerEvents(listener, OrbitMines.getInstance());
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HashMap<AnvilSlot, ItemStack> getItems() {
        return items;
    }

    public ItemStack getSlot(Player p, int slot) {
        return p.getOpenInventory().getTopInventory().getItem(2);
    }

    public void setSlot(AnvilSlot slot, ItemStack item) {
        items.put(slot, item);
    }

    @Override
    public void open() {
        EntityPlayer p = ((CraftPlayer) player).getHandle();

        AnvilContainer container = new AnvilContainer(p);

        //Set the items to the items from the inventory given
        inv = container.getBukkitView().getTopInventory();

        for (AnvilSlot slot : items.keySet()) {
            inv.setItem(slot.getSlot(), items.get(slot));
        }

        //Counter stuff that the game uses to keep track of inventories
        int c = p.nextContainerCounter();

        //Send the packet
        p.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, "minecraft:anvil", new ChatMessage("Repairing", new Object[]{}), 0));

        //Set their active container to the container
        p.activeContainer = container;

        //Set their active container window id to that counter stuff
        p.activeContainer.windowId = c;

        //Add the slot listener
        p.activeContainer.addSlotListener(p);
    }

    public void destroy() {
        player = null;
        handler = null;
        items = null;

        HandlerList.unregisterAll(listener);

        listener = null;
    }
}
