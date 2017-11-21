package com.orbitmines.spigot.api.handlers.kit;

import com.madblock.spigot.MadBlock;
import com.madblock.spigot.api.handlers.OMPlayer;
import com.madblock.spigot.api.utils.ItemUtils;
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

            MadBlock madBlock = MadBlock.getInstance();
            madBlock.getServer().getPluginManager().registerEvents(new InteractiveKitEvent(), madBlock);
        }
    }

    public void setItem(int index, InteractAction action) {
        setItem(index, action.getItemStack());

        interactions.add(action);
    }

    @Override
    public void setItems(OMPlayer omp) {
        super.setItems(mbp);

        for (InteractAction action : interactions) {
            action.onReceive(mbp);
        }

        registerLast(mbp);
    }

    @Override
    public void addItems(OMPlayer omp) {
        super.addItems(mbp);

        for (InteractAction action : interactions) {
            action.onReceive(mbp);
        }

        registerLast(mbp);
    }

    @Override
    public void replaceItems(OMPlayer omp) {
        super.replaceItems(mbp);

        for (InteractAction action : interactions) {
            action.onReceive(mbp);
        }

        registerLast(mbp);
    }

    public List<InteractAction> getInteractions() {
        return interactions;
    }

    private void registerLast(OMPlayer omp) {
        mbp.setLastInteractiveKit(this);
    }

    public static abstract class InteractAction {

        private ItemStack itemStack;

        public InteractAction(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        public abstract void onInteract(PlayerInteractEvent event, OMPlayer omp);

        /* Override to use */
        public void onReceive(OMPlayer omp) {

        }

        /* Override to change */
        public boolean equals(ItemStack item) {
            return item.getType() == itemStack.getType() && item.getDurability() == itemStack.getDurability() && item.getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName());
        }

        public ItemStack getItemStack() {
            return itemStack;
        }
    }

    public class InteractiveKitEvent implements Listener {

        @EventHandler
        public void onInteract(PlayerInteractEvent event) {
            OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());
            if (mbp == null)
                return;

            ItemStack itemStack = event.getItem();

            KitInteractive last = mbp.getLastInteractiveKit();
            if (last == null || ItemUtils.isNull(itemStack))
                return;

            for (KitInteractive.InteractAction action : last.getInteractions()) {
                if (action.equals(itemStack)) {
                    action.onInteract(event, mbp);
                    break;
                }
            }
        }
    }
}
