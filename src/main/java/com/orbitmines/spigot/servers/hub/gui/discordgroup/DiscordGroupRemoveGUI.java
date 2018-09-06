package com.orbitmines.spigot.servers.hub.gui.discordgroup;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.PluginMessage;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.handlers.DiscordSquad;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.data.DiscordGroupData;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public abstract class DiscordGroupRemoveGUI extends GUI {

    private final OrbitMines orbitMines;

    public DiscordGroupRemoveGUI() {
        this.orbitMines = OrbitMines.getInstance();

        newInventory(54, "§0§lAre you sure?");
    }

    protected abstract void onCancel();

    @Override
    protected boolean onOpen(OMPlayer omp) {
        DiscordBot discord = orbitMines.getServerHandler().getDiscord();
        DiscordGroupData data = (DiscordGroupData) omp.getData(Data.Type.DISCORD_GROUPS);
        DiscordSquad group = DiscordSquad.getFromDatabase(discord, omp.getUUID());

        add(2, 4, new EmptyItemInstance(new ItemBuilder(Material.BARRIER, 1, "§c§l" + omp.lang("Verwijder Squad", "Delete Squad")).build()));
        {
            ItemBuilder item = new PlayerSkullBuilder(() -> omp.getName(true), 1, group.getDisplayName());

            DiscordGroupGUI.setOnlineLore(item, group);

            add(3, 4, new EmptyItemInstance(item.build()));
        }
        ItemInstance confirm = new ItemInstance(new ItemBuilder(Material.LIME_STAINED_GLASS_PANE, 1, omp.lang("§a§lBevestigen", "§a§lConfirm")).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                omp.getPlayer().closeInventory();

                for (UUID invite : data.getInvites()) {
                    data.clearInvite(CachedPlayer.getPlayer(invite), false);
                }

                orbitMines.getServerHandler().getMessageHandler().dataTransfer(PluginMessage.DISCORD_GROUP_ACTION, omp.getUUID().toString(), "DESTROY");
            }
        };
        ItemInstance cancel = new ItemInstance(new ItemBuilder(Material.RED_STAINED_GLASS_PANE, 1, omp.lang("§c§lAnnuleer", "§c§lCancel")).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                onCancel();
            }
        };

        for (int i = 0; i < 9; i++) {
            if (i > 2 && i < 6)
                continue;

            for (int j = 1; j < 5; j++) {
                add(j, i, i <= 2 ? confirm : cancel);
            }
        }

        return true;
    }
}
