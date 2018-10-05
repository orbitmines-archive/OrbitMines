package com.orbitmines.spigot.api.cmds.moderator;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.StaffCommand;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Collections;

public class CommandInvSee extends StaffCommand {

    public CommandInvSee() {
        super(CommandLibrary.INVSEE);
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        if (a.length == 2) {
            Player p2 = Bukkit.getPlayer(a[1]);

            if (p2 != null) {
                if (omp.getPlayer() == p2) {
                    omp.sendMessage("InvSee", Color.RED, "Je kan je eigen inventory niet openen, gebruik 'e' :thinking:.", "You cannot open your own inventory, use 'e' :thinking:.");
                    return;
                }

                InventoryViewer view = new InventoryViewer(p2, omp.isEligible(StaffRank.ADMIN));
                view.open(omp);

                new SpigotRunnable(SpigotRunnable.TimeUnit.TICK, 1) {
                    @Override
                    public void run() {
                        if (!p2.isOnline() || !omp.getPlayer().isOnline() || omp.getPlayer().getOpenInventory().getTopInventory() == null || omp.getPlayer().getOpenInventory().getTopInventory().getName() == null || !omp.getPlayer().getOpenInventory().getTopInventory().getName().equals(view.getInventory().getName())) {
                            cancel();

                            if (omp.getPlayer().isOnline())
                                omp.getPlayer().closeInventory();

                            return;
                        }

                        view.update();
                    }
                };
            } else {
                omp.sendMessage("InvSee", Color.RED, "§7Player §6" + a[1] + " §7is niet online!", "§7Player §6" + a[1] + " §7isn't online!");
            }
        } else {
            getHelpMessage(omp).send(omp);
        }
    }

    private class InventoryViewer extends GUI {

        private final int[] empty = { 40, 42, 43, 44 };
        private final int helmet = 36;
        private final int chestplate = 37;
        private final int leggings = 38;
        private final int boots = 39;
        private final int offHand = 41;

        private final OrbitMines orbitMines;
        private Player viewing;
        private boolean interactable;

        public InventoryViewer(Player viewing, boolean interactable) {
            this.orbitMines = OrbitMines.getInstance();
            this.viewing = viewing;
            this.interactable = interactable;

            newInventory(45, "§0§l" + viewing.getName() + "'s Inventory");
        }

        @Override
        protected boolean onOpen(OMPlayer omp) {
            update();

            return true;
        }

        public void update() {
            PlayerInventory inv = viewing.getInventory();

            for(int i = 0; i < 27; i++){
                add(i, new EmptyItemInstance(inv.getItem(i + 9)));
            }
            for(int i = 27; i < 36; i++){
                add(i, new EmptyItemInstance(inv.getItem(i - 27)));
            }
            add(helmet, new EmptyItemInstance(inv.getHelmet()));
            add(chestplate, new EmptyItemInstance(inv.getChestplate()));
            add(leggings, new EmptyItemInstance(inv.getLeggings()));
            add(boots, new EmptyItemInstance(inv.getBoots()));

            add(offHand, new EmptyItemInstance(inv.getItemInOffHand()));

            for (int slot : empty) {
                add(slot, new EmptyItemInstance(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, " ").build()));
            }
        }

        @Override
        protected void onClick(InventoryClickEvent event, OMPlayer omp) {
            if (!interactable)
                return;

            for (int slot : empty) {
                if (event.getSlot() == slot) {
                    event.setCancelled(true);
                    return;
                }
            }

            event.setCancelled(false);

            updateSlots(Collections.singletonList(event.getSlot()));
        }

        @Override
        protected void onDrag(InventoryDragEvent event, OMPlayer omp) {
            if (!interactable)
                return;

            for (int slot : empty) {
                if (event.getInventorySlots().contains(slot)) {
                    event.setCancelled(true);
                    return;
                }
            }

            event.setCancelled(false);

            updateSlots(event.getInventorySlots());
        }

        private void updateSlots(Collection<Integer> slots) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (int slot : slots) {
                        updateSlot(slot);
                    }
                }
            }.runTaskLater(orbitMines, 0);
        }

        private void updateSlot(int slot) {
            PlayerInventory playerInventory = viewing.getInventory();
            ItemStack item = inventory.getItem(slot);

            if (slot < 27) {
                /* Inventory */
                playerInventory.setItem(slot + 9, item);
            } else if (slot < 36) {
                /* Hot bar */
                playerInventory.setItem(slot - 27, item);
            } else if (slot == helmet) {
                playerInventory.setHelmet(item);
            } else if (slot == chestplate) {
                playerInventory.setChestplate(item);
            } else if (slot == leggings) {
                playerInventory.setLeggings(item);
            } else if (slot == boots) {
                playerInventory.setBoots(item);
            } else if (slot == offHand) {
                playerInventory.setItemInOffHand(item);
            }
        }
    }
}
