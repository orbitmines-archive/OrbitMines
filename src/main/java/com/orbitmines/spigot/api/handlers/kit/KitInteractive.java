package com.orbitmines.spigot.api.handlers.kit;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.utils.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class KitInteractive extends Kit {

    private static boolean enabled = false;

    private List<InteractAction> interactions;

    public KitInteractive(String name) {
        super(name);

        interactions = new ArrayList<>();

        if (!enabled) {
            enabled = true;

            OrbitMines orbitMines = OrbitMines.getInstance();
            orbitMines.getServer().getPluginManager().registerEvents(new InteractiveKitEvent(), orbitMines);
        }
    }

    public void setItem(int index, InteractAction action) {
        setItem(index, action.getItemBuilder());

        interactions.add(action);
    }

    @Override
    public void setItems(OMPlayer omp) {
        super.setItems(omp);

        for (InteractAction action : interactions) {
            action.onReceive(omp);
        }

        registerLast(omp);
    }

    @Override
    public void addItems(OMPlayer omp) {
        super.addItems(omp);

        for (InteractAction action : interactions) {
            action.onReceive(omp);
        }

        registerLast(omp);
    }

    @Override
    public void replaceItems(OMPlayer omp) {
        super.replaceItems(omp);

        for (InteractAction action : interactions) {
            action.onReceive(omp);
        }

        registerLast(omp);
    }

    public List<InteractAction> getInteractions() {
        return interactions;
    }

    private void registerLast(OMPlayer omp) {
        omp.setLastInteractiveKit(this);
    }

    public static abstract class InteractAction {

        private ItemBuilder itemBuilder;

        public InteractAction(ItemBuilder itemBuilder) {
            this.itemBuilder = itemBuilder;
        }

        public abstract void onInteract(PlayerInteractEvent event, OMPlayer omp);

        /* Override to use */
        public void onReceive(OMPlayer omp) {

        }

        /* Override to change */
        public boolean equals(ItemStack item) {
            ItemStack itemStack = itemBuilder.build();
            return item.getType() == itemStack.getType() && item.getDurability() == itemStack.getDurability() && item.getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName());
        }

        public ItemBuilder getItemBuilder() {
            return itemBuilder;
        }
    }

    public class InteractiveKitEvent implements Listener {

        @EventHandler
        public void onInteract(PlayerInteractEvent event) {
            OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());
            if (omp == null)
                return;

            ItemStack itemStack = event.getItem();

            KitInteractive last = omp.getLastInteractiveKit();
            if (last == null || ItemUtils.isNull(itemStack))
                return;

            for (KitInteractive.InteractAction action : last.getInteractions()) {
                if (action.equals(itemStack)) {
                    action.onInteract(event, omp);
                    break;
                }
            }
        }
    }
}
