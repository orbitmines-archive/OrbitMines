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

public class DiscordGroupDetailsGUI extends GUI {

    private CachedPlayer owner;
    private String name;

    public DiscordGroupDetailsGUI(CachedPlayer owner, String name) {
        this.owner = owner;
        this.name = name;

        newInventory(27, "§0§lPrivate Discord Servers");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        DiscordGroupData data = (DiscordGroupData) omp.getData(Data.Type.DISCORD_GROUPS);

        boolean isOwner = owner.getUUID().toString().equals(omp.getUUID().toString());

        boolean isSelected = data.getSelected() != null && data.getSelected().toString().equals(owner.getUUID().toString());

        if (!isSelected)
            add(1, 1, new ItemInstance(new ItemBuilder(Material.EMERALD, 1, omp.lang("§c§lZet als Geselecteerd", "§c§lSet as Selected")).build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    data.setSelected(owner.getUUID());

                    new DiscordGroupGUI().open(omp);
                }
            });
        else
            add(1, 1, new EmptyItemInstance(new ItemBuilder(Material.EMERALD, 1, omp.lang("§a§lZet als Geselecteerd", "§a§lSet as Selected")).glow().build()));

        add(1, 4, new ItemInstance(new PlayerSkullBuilder(() -> "Discord Skull", 1, omp.lang("§9« Terug naar Private Discord Servers", "§9« Back to Private Discord Servers")).setTexture(DiscordBot.SKULL_TEXTURE).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new DiscordGroupGUI().open(omp);
            }
        });

        add(1, 7, new ItemInstance(new ItemBuilder(Material.BARRIER, 1, omp.lang("§c§lWeiger Verzoek §7(Om " + name + " §7te joinen)", "§c§lDeny Request §7(To join " + name + "§7)")).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                omp.getPlayer().closeInventory();

                data.getDataFor(owner.getUUID()).onMemberRemoval(CachedPlayer.getPlayer(omp.getUUID()), false);
            }
        });

        return true;
    }
}
