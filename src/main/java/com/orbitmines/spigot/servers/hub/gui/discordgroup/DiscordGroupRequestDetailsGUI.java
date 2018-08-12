package com.orbitmines.spigot.servers.hub.gui.discordgroup;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.data.DiscordGroupData;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class DiscordGroupRequestDetailsGUI extends GUI implements DiscordGroupGUIInstance {

    private CachedPlayer owner;
    private String name;

    public DiscordGroupRequestDetailsGUI(CachedPlayer owner, String name) {
        this.owner = owner;
        this.name = name;

        newInventory(27, "§0§lPrivate Discord Servers");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        DiscordGroupData data = (DiscordGroupData) omp.getData(Data.Type.DISCORD_GROUPS);

        add(1, 1, new ItemInstance(new ItemBuilder(Material.BOOK, 1, omp.lang("§a§lAccepteer Verzoek §7(Om " + name + " §7te joinen)", "§a§lAccept Request §7(To join " + name + "§7)")).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                omp.getPlayer().closeInventory();

                data.onAccept(owner);
                data.setSelected(owner.getUUID());
            }
        });

        add(1, 4, new ItemInstance(new PlayerSkullBuilder(() -> "Discord Skull", 1, omp.lang("§9« Terug naar Overzicht", "§9« Back to Overview")).setTexture(DiscordBot.SKULL_TEXTURE).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new DiscordGroupGUI().open(omp);
            }
        });

        add(1, 7, new ItemInstance(new ItemBuilder(Material.BARRIER, 1, omp.lang("§c§lWeiger Verzoek §7(Om " + name + " §7te joinen)", "§c§lDeny Request §7(To join " + name + "§7)")).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                data.onDeny(owner);

                new DiscordGroupGUI().open(omp);
            }
        });

        return true;
    }
}
