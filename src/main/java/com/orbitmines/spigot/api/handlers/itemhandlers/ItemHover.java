package com.orbitmines.spigot.api.handlers.itemhandlers;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.runnables.PlayerRunnable;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public abstract class ItemHover {

    private static List<ItemHover> itemHovers = new ArrayList<>();

    private final ItemBuilder itemBuilder;
    private final boolean offHandAllowed;

    private PlayerRunnable runnable;

    protected Set<OMPlayer> entered;

    public ItemHover(ItemBuilder itemBuilder, boolean offHandAllowed) {
        this.itemBuilder = itemBuilder;
        this.offHandAllowed = offHandAllowed;
        this.entered = new HashSet<>();

        itemHovers.add(this);

        runnable = new PlayerRunnable(SpigotRunnable.TimeUnit.TICK, 1) {
            @Override
            public void run(OMPlayer omp) {
                ItemStack mainHand = omp.getItemInMainHand();
                ItemStack offHand = omp.getItemInOffHand();

                /* Player has hover, but not in main hand, and not in his off hand, leave that hover */
                if (omp.getCurrentHover() != null && (!omp.getCurrentHover().equals(mainHand) && (!offHandAllowed || !omp.getCurrentHover().equals(offHand))))
                    omp.getCurrentHover().leave(omp);

                /* Player has currently no hover in main hand, and offhand equals this hover */
                if (offHandAllowed && omp.getCurrentHover() == null && itemBuilder.equals(offHand))
                    enter(omp, offHand, 41);
            }
        };
    }

//    public ItemBuilder getItemBuilder() {
//        return itemBuilder;
//    }

    public boolean isOffHandAllowed() {
        return offHandAllowed;
    }

    protected abstract void onEnter(OMPlayer omp, ItemStack item, int slot);

    protected abstract void onLeave(OMPlayer omp);

    public void enter(OMPlayer omp, ItemStack item, int slot) {
        if (omp.getCurrentHover() != null)
            omp.getCurrentHover().leave(omp);

        omp.setCurrentHover(this);
        onEnter(omp, item, slot);

        entered.add(omp);
    }

    public void leave(OMPlayer omp) {
        if (!entered.contains(omp))
            return;

        onLeave(omp);

        omp.setCurrentHover(null);
    }

    public void unregister() {
        itemHovers.remove(this);

        if (runnable != null)
            runnable.cancel();
    }

    public boolean equals(ItemStack itemStack) {
        return itemBuilder.equals(itemStack);
    }

    public boolean hasEntered(OMPlayer omp) {
        return entered.contains(omp);
    }

    public static List<ItemHover> getItemHovers() {
        return itemHovers;
    }
}
