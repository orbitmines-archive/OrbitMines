package com.orbitmines.spigot.api.handlers.itemhandlers;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.chat.ActionBar;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;

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

    public abstract String getMessage(OMPlayer omp);

    public void onEnter(OMPlayer omp) {
        ActionBar actionBar = actionBars.computeIfAbsent(omp, key -> new ActionBar(omp, () -> getMessage(omp), Long.MAX_VALUE));
        actionBar.send();
    }

    public void onLeave(OMPlayer omp) {
        if (actionBars.containsKey(omp))
            actionBars.get(omp).forceStop();
    }
}
