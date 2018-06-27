package com.orbitmines.spigot.servers.survival.gui.warp;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.teleportable.Warp;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;

public class WarpIconEditorGUI extends GUI {

    private final Survival survival;
    private final Warp warp;

    public WarpIconEditorGUI(Survival survival, Warp warp) {
        this.survival = survival;
        this.warp = warp;

        newInventory(54, "§0§l" + warp.getName());
    }

    @Override
    protected boolean onOpen(OMPlayer player) {
        SurvivalPlayer omp = (SurvivalPlayer) player;

        Warp.Icon[] icons = Warp.Icon.values();
        for (int i = 0; i < icons.length; i++) {
            Warp.Icon icon = icons[i];

            ItemBuilder item = icon.getItemBuilder();

            if (warp.getIcon() == icon)
                item.glow();

            add(i, new ItemInstance(item.build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    warp.setIcon(icon);
                    omp.playSound(Sound.UI_BUTTON_CLICK);
                    reopen(omp);
                }
            });
        }

        add(5, 4, new ItemInstance(new PlayerSkullBuilder(() -> "Cyan Arrow Left", 1, omp.lang(Warp.COLOR.getChatColor() + "« Terug naar Jouw Warp", Warp.COLOR.getChatColor() + "« Back to Your Warp")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjc2OGVkYzI4ODUzYzQyNDRkYmM2ZWViNjNiZDQ5ZWQ1NjhjYTIyYTg1MmEwYTU3OGIyZjJmOWZhYmU3MCJ9fX0=").build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new WarpEditorGUI(survival, warp).open(omp);
            }
        });

        return true;
    }
}
