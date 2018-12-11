package com.orbitmines.spigot.api.handlers;

import com.orbitmines.spigot.api.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public abstract class GUI {

    protected Inventory inventory;
    protected ItemInstance[] itemInstances;

    /* Called before inventory is opened, return false to cancel */
    protected abstract boolean onOpen(OMPlayer omp);

    /* @Override: Called after a player clicks an item */
    protected void onClick(InventoryClickEvent event, OMPlayer omp) {}

    /* @Override: Called after a player drags an item */
    protected void onDrag(InventoryDragEvent event, OMPlayer omp) {}

    /* @Override: Called after a player closes the inventory */
    protected void onClose(InventoryCloseEvent event, OMPlayer omp) {}

    public Inventory getInventory() {
        return inventory;
    }

    protected void newInventory(int size, String title) {
        this.inventory = Bukkit.createInventory(null, size, title);
        this.itemInstances = new ItemInstance[inventory.getSize()];
    }

    protected void add(int row, int slot, ItemInstance itemInstance) {
        add(row * 9 + slot, itemInstance);
    }

    protected void add(int slot, ItemInstance itemInstance) {
        inventory.setItem(slot, itemInstance.itemStack);
        itemInstances[slot] = itemInstance;
    }

    protected void clear(int row, int slot) {
        clear(row * 9 + slot);
    }

    protected void clear(int slot) {
        inventory.setItem(slot, null);
        itemInstances[slot] = null;
    }

    protected void clear() {
        inventory.clear();
        for (int i = 0; i < itemInstances.length; i++) {
            itemInstances[i] = null;
        }
    }

    public void open(OMPlayer omp) {
        if (!onOpen(omp))
            return;

        omp.getPlayer().openInventory(inventory);
        omp.setLastInventory(this);
        PlayerUtils.updateInventory(omp.getPlayer());
    }

    public void reopen(OMPlayer omp) {
        clear();
        open(omp);
        PlayerUtils.updateInventory(omp.getPlayer());
    }

    /* Event Cancelled on default, use Event#setCancelled(false) in ItemInstance#onClick in in order to undo */
    public void processClickEvent(InventoryClickEvent event, OMPlayer omp) {
        Inventory clicked = event.getClickedInventory();
        if (clicked == null || clicked.getTitle() == null || !clicked.getTitle().equals(inventory.getTitle()))
            return;

        event.setCancelled(true);

        ItemInstance itemInstance = itemInstances[event.getSlot()];

        if (itemInstance != null)
            itemInstance.onClick(event, omp);

        onClick(event, omp);
    }

    /* Event Cancelled on default, use Event#setCancelled(false) in ItemInstance#onClick in in order to undo */
    public void processDragEvent(InventoryDragEvent event, OMPlayer omp) {
        Inventory clicked = event.getInventory();
        if (clicked == null || clicked.getTitle() == null || !clicked.getTitle().equals(inventory.getTitle()))
            return;

        event.setCancelled(true);

//        ItemInstance itemInstance = itemInstances[event.gets()];
//
//        if (itemInstance != null)
//            itemInstance.onClick(event, omp);

        onDrag(event, omp);
    }

    public void processCloseEvent(InventoryCloseEvent event, OMPlayer omp) {
        Inventory clicked = event.getInventory();
        if (clicked == null || clicked.getTitle() == null || !clicked.getTitle().equals(inventory.getTitle()))
            return;

        onClose(event, omp);
    }

    public void clearInstances() {
        for (int i = 0; i < itemInstances.length; i++) {
            itemInstances[i] = null;
        }
    }

    public void updateForPlayers() {
        for (OMPlayer omp : OMPlayer.getPlayers()) {
            if (omp.getLastInventory() != this)
                return;

            if (hasOpened(omp))
                omp.getPlayer().openInventory(inventory);
        }
    }

    public boolean hasOpened(OMPlayer omp) {
        Inventory topInv = omp.getPlayer().getOpenInventory().getTopInventory();

        return topInv != null && topInv.getName().equals(inventory.getName());
    }

    public abstract class ItemInstance {

        protected ItemStack itemStack;

        public ItemInstance(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        public abstract void onClick(InventoryClickEvent event, OMPlayer omp);

        public ItemStack getItemStack() {
            return itemStack;
        }
    }

    public class EmptyItemInstance extends ItemInstance {

        public EmptyItemInstance(ItemStack itemStack) {
            super(itemStack);
        }

        @Override
        public void onClick(InventoryClickEvent event, OMPlayer omp) { }
    }
}
