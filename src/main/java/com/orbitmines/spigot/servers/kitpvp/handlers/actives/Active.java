package com.orbitmines.spigot.servers.kitpvp.handlers.actives;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Cooldown;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.spigot.api.handlers.itembuilders.PotionBuilder;
import com.orbitmines.spigot.api.nms.itemstack.ItemStackNms;
import com.orbitmines.spigot.api.utils.ItemUtils;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public enum Active {

    SUGAR_RUSH("Sugar Rush", Color.WHITE, new ActiveSugarRush()) {
        @Override
        public String[] getDescription(int level) {
            ActiveSugarRush active = (ActiveSugarRush) getHandler();
            PotionBuilder builder = active.getBuilder(level);

            return new String[] {
                    "  §7§oReceive §e§o" + ItemUtils.getName(builder.getType()) + " " + NumberUtils.toRoman(builder.getAmplifier() + 1),
                    "  §7§ofor §9§o" + (builder.getDuration() / 20) + " seconds§7§o on a",
                    "  §b§o" + ((int) (active.getCooldown(level).getCooldown() / 1000)) + " second cooldown§7."
            };
        }
    },
    HEAL("Healing", Color.FUCHSIA, new ActiveHealing()) {
        @Override
        public String[] getDescription(int level) {
            ActiveHealing active = (ActiveHealing) getHandler();
            PotionBuilder builder = active.getBuilder(level);

            return new String[]{
                    "  §7§oReceive §e§o" + ItemUtils.getName(builder.getType()) + " " + NumberUtils.toRoman(builder.getAmplifier() + 1),
                    "  §7§ofor §9§o" + (builder.getDuration() / 20) + " seconds§7§o on a",
                    "  §b§o" + ((int) (active.getCooldown(level).getCooldown() / 1000)) + " second cooldown§7."
            };
        }
    };

    private final String name;
    private final Color color;
    private final Handler handler;

    Active(String name, Color color, Handler handler) {
        this.name = name;
        this.color = color;
        this.handler = handler;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName(int level) {
        return color.getChatColor() + name + " " + NumberUtils.toRoman(level) + " §d[ACTIVE]";
    }

    public String[] getDescription(int level) {
        return new String[] {};
    }

    public Color getColor() {
        return color;
    }

    public Handler getHandler() {
        return handler;
    }

    public static Map<Active, Integer> from(ItemStackNms nms, ItemStack itemStack) {
        Map<String, String> metaData = nms.getMetaData(itemStack, "active");

        if (metaData == null)
            return null;

        Map<Active, Integer> actives = new HashMap<>();

        for (String string : metaData.keySet()) {
            Active passive = Active.valueOf(string);

            actives.put(passive, Integer.parseInt(metaData.get(string)));
        }

        if (actives.size() == 0)
            return null;

        return actives;
    }

    public ItemStack apply(ItemStackNms nms, ItemStack itemStack, int level) {
        return nms.setMetaData(itemStack, "active", toString(), level + "");
    }

    public boolean canUse(ItemStackNms nms, ItemStack itemStack, int level) {
        Cooldown cooldown = handler.getCooldown(level);

        /* Check if cooldown is actually on cooldown; can use if not on cooldown */
        return !cooldown.onCooldown(getLastUse(nms, itemStack, level));
    }

    public long getLastUse(ItemStackNms nms, ItemStack itemStack, int level) {
        String metaData = nms.getMetaData(itemStack, "activeCooldown", toString() + level);

        /* Not used before */
        if (metaData == null)
            return -1;

        return Long.parseLong(metaData);
    }

    public ItemStack resetCooldown(ItemStackNms nms, ItemStack itemStack, int level) {
        return nms.setMetaData(itemStack, "activeCooldown", toString() + level, System.currentTimeMillis() + "");
    }

    public interface Handler {

        void trigger(PlayerInteractEvent event, KitPvPPlayer omp, int level);

        Cooldown getCooldown(int level);

    }
}
