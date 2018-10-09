package com.orbitmines.spigot.servers.kitpvp.handlers.actives;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Cooldown;
import com.orbitmines.api.utils.TimeUtils;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itemhandlers.ItemHoverActionBar;
import com.orbitmines.spigot.api.nms.itemstack.ItemStackNms;
import com.orbitmines.spigot.servers.kitpvp.KitPvP;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Map;

public class ItemHoverActiveHandler extends ItemHoverActionBar {

    private final int BAR_COUNT = 40;

    private final ItemStackNms nms;

    public ItemHoverActiveHandler(KitPvP kitPvP) {
        super(null, false);

        this.nms = kitPvP.getOrbitMines().getNms().customItem();
    }

    @Override
    public String getMessage(OMPlayer omp, ItemStack item) {
        Map<Active, Integer> actives = Active.from(nms, item);

        /* No Actives on this item */
        if (actives == null)
            return null;

        Active active = new ArrayList<>(actives.keySet()).get(0);
        int level = actives.get(active);
        Cooldown cooldown = active.getHandler().getCooldown(level);
        long lastUse = active.getLastUse(nms, item, level);
        String name = active.getColor().getChatColor() + "§l" + active.getName();

        if (!cooldown.onCooldown(lastUse))
            return name + " §7| §e§l" + omp.lang("Rechtermuisklik", "Right Click");

        long timeLeft = cooldown.getCooldown() - (System.currentTimeMillis() - lastUse);
        int red = (int) (((float) (timeLeft) / (float) cooldown.getCooldown()) * BAR_COUNT);

        StringBuilder actionBar = new StringBuilder(name + " §7| §e" + TimeUtils.secondDecimal(timeLeft, "%.1f") + " §7| ");

        for (int i = 0; i < BAR_COUNT - red; i++) {
            if (i == 0)
                actionBar.append("§a");

            actionBar.append("|");
        }
        for (int i = 0; i < red; i++) {
            if (i == 0)
                actionBar.append("§c");

            actionBar.append("|");
        }

        return actionBar.toString();
    }

    @Override
    public boolean equals(ItemStack itemStack) {
        return Active.from(nms, itemStack) != null;
    }
}
