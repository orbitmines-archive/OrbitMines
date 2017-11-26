package com.orbitmines.spigot.api.handlers.itemhandlers;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.runnables.PlayerRunnable;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public abstract class ItemHover {

    private static List<ItemHover> itemHovers = new ArrayList<>();

    private final ItemBuilder itemBuilder;
    private final boolean offHandAllowed;

    private PlayerRunnable offHandRunnable;

    public ItemHover(ItemBuilder itemBuilder, boolean offHandAllowed) {
        this.itemBuilder = itemBuilder;
        this.offHandAllowed = offHandAllowed;

        itemHovers.add(this);

        if (!offHandAllowed)
            return;

        offHandRunnable = new PlayerRunnable(SpigotRunnable.TimeUnit.TICK, 1) {
            @Override
            public void run(OMPlayer omp) {
                ItemStack mainHand = omp.getItemInMainHand();
                ItemStack offHand = omp.getItemInMainHand();

                /* Player has hover, but not in main hand, and not in his off hand, leave that hover */
                if (omp.getCurrentHover() != null && !omp.getCurrentHover().getItemBuilder().equals(mainHand) && !omp.getCurrentHover().getItemBuilder().equals(offHand))
                    omp.getCurrentHover().leave(omp);

                /* Player has currently no hover in main hand, and offhand equals this hover */
                if (omp.getCurrentHover() == null && itemBuilder.equals(offHand))
                    enter(omp);
            }
        };
    }

    public ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    public boolean isOffHandAllowed() {
        return offHandAllowed;
    }

    protected abstract void onEnter(OMPlayer omp);

    protected abstract void onLeave(OMPlayer omp);

    public void enter(OMPlayer omp) {
        if (omp.getCurrentHover() != null)
            omp.getCurrentHover().leave(omp);

        omp.setCurrentHover(this);
        onEnter(omp);
    }

    public void leave(OMPlayer omp) {
        onLeave(omp);

        omp.setCurrentHover(null);
    }

    public void unregister() {
        itemHovers.remove(this);

        if (offHandRunnable != null)
            offHandRunnable.cancel();
    }

    public static List<ItemHover> getItemHovers() {
        return itemHovers;
    }
}
