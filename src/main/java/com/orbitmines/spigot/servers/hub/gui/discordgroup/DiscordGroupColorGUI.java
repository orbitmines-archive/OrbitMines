package com.orbitmines.spigot.servers.hub.gui.discordgroup;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.handlers.DiscordGroup;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class DiscordGroupColorGUI extends GUI {

    public DiscordGroupColorGUI() {
        newInventory(54, "§0§lPrivate Discord Servers");
    }

    public abstract void openOverview();

    @Override
    protected boolean onOpen(OMPlayer omp) {
        DiscordBot discord = OrbitMines.getInstance().getServerHandler().getDiscord();

        DiscordGroup group = DiscordGroup.getFromDatabase(discord, omp.getUUID());

        Color[] colors = Color.values();
        int i = 0;

        for (int row = 1; row < 4; row++) {
            for (int slot = 1; slot < 8; slot++) {
                if (slot == 4 || row == 2 && (slot == 2 || slot == 6))
                    continue;

                if (i >= 16)
                    break;

                Color color = colors[i];

                ItemBuilder item = new ItemBuilder(DiscordGroupGUI.getMaterial(color), 1, color.getChatColor() + "§l" + color.getName());

                add(row, slot, new ItemInstance(item.build()) {
                    @Override
                    public void onClick(InventoryClickEvent event, OMPlayer omp) {
                        group.setColor(color);
                        openOverview();
                    }
                });

                i++;
            }
        }

        add(4, 4, new ItemInstance(new PlayerSkullBuilder(() -> "Blue Left Arrow", 1, omp.lang("§9« Terug naar Overzicht", "§9« Back to Overview")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWFlNzg0NTFiZjI2Y2Y0OWZkNWY1NGNkOGYyYjM3Y2QyNWM5MmU1Y2E3NjI5OGIzNjM0Y2I1NDFlOWFkODkifX19").build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                openOverview();
            }
        });

        return true;
    }
}
