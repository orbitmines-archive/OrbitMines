package com.orbitmines.spigot.api.handlers.itemhandlers;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.chat.ActionBar;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public abstract class ItemHoverActionBar extends ItemHover {

    private Map<OMPlayer, ActionBar> actionBars;

    public ItemHoverActionBar(ItemBuilder itemBuilder, boolean offHandAllowed) {
        super(itemBuilder, offHandAllowed);

        this.actionBars = new HashMap<>();
    }

    public abstract String getMessage(OMPlayer omp, ItemStack item);

    @Override
    public void onEnter(OMPlayer omp, ItemStack item, int slot) {
        ActionBar actionBar = actionBars.computeIfAbsent(omp, key -> new ActionBar(omp, () -> getMessage(omp, omp.getInventory().getItem(slot)), Long.MAX_VALUE));
        actionBar.send();
    }

    @Override
    public void onLeave(OMPlayer omp) {
        if (actionBars.containsKey(omp))
            actionBars.get(omp).forceStop();
    }
}
