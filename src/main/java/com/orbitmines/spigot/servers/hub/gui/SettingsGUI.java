package com.orbitmines.spigot.servers.hub.gui;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.api.settings.Settings;
import com.orbitmines.api.settings.SettingsType;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.data.SettingsData;
import com.orbitmines.spigot.api.handlers.itembuilders.BannerBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.utils.ColorUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;

public class SettingsGUI extends GUI {

    private OrbitMines orbitMines;

    public SettingsGUI() {
        this.orbitMines = OrbitMines.getInstance();
        newInventory(36, "§0§lSettings");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        SettingsData data = (SettingsData) omp.getData(Data.Type.SETTINGS);

        int index = 0;
        for (Settings settings : Settings.values()) {
            add(1, 1 + index, new EmptyItemInstance(getItemBuilder(settings).setDisplayName("§7§l" + omp.lang(settings.getName())).build()));

            SettingsType settingsType = data.getSettings().get(settings);
            add(2, 1 + index, new ItemInstance(new ItemBuilder(ColorUtils.getStainedGlassPaneMaterial(settingsType.getColor()), 1, settingsType.getDisplayName(omp.getLanguage())).build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    omp.updateSettings(settings, settingsType.next());
                    omp.playSound(Sound.UI_BUTTON_CLICK);
                    reopen(omp);
                }
            });

            index++;
        }

        add(1, 6, new EmptyItemInstance(new ItemBuilder(Material.PAPER, 1, "§7§lScoreboard").build()));

        boolean hasScoreboard = omp.hasScoreboard();
        add(2, 6, new ItemInstance(new ItemBuilder(hasScoreboard ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE, 1, hasScoreboard ? omp.lang("§aAan", "§aEnabled") : omp.lang("§cUit", "§cDisabled")).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                omp.setHasScoreboard(!hasScoreboard);
                omp.playSound(Sound.UI_BUTTON_CLICK);
                reopen(omp);
            }
        });

        add(1, 7, new EmptyItemInstance(BannerBuilder.getBuilder(omp.getLanguage()).setDisplayName(omp.lang(new Message("§7§lTaal", "§7§lLanguage"))).addFlag(ItemFlag.HIDE_POTION_EFFECTS).build()));

        add(2, 7, new ItemInstance(new ItemBuilder(ColorUtils.getStainedGlassPaneMaterial(Color.BLUE), 1, omp.lang(new Message("§9Dutch §7/ §9Nederlands", "§9English"))).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                omp.setLanguage(omp.getLanguage().next());
                omp.playSound(Sound.UI_BUTTON_CLICK);
                reopen(omp);
            }
        });

        return true;
    }

    private ItemBuilder getItemBuilder(Settings settings) {
        switch (settings) {

            case PRIVATE_MESSAGES:
                return new ItemBuilder(Material.WRITABLE_BOOK);
            case PLAYER_VISIBILITY:
                return new ItemBuilder(Material.ENDER_EYE);
            case GADGETS:
                return new ItemBuilder(Material.COMPASS);
            case STATS:
                return new ItemBuilder(Material.EMERALD);
        }
        throw new NullPointerException();
    }
}
