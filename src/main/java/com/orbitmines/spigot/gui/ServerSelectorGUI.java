package com.orbitmines.spigot.gui;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Language;
import com.orbitmines.api.Message;
import com.orbitmines.api.Server;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.servers.survival.handlers.region.Region;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerSelectorGUI extends GUI {

    private OrbitMines orbitMines;
    private Language language;

    public ServerSelectorGUI(Language language) {
        this.orbitMines = OrbitMines.getInstance();
        this.language = language;

        newInventory(54, "§0§lServer Selector");

        EmptyItemInstance item = new EmptyItemInstance(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, "§f").build());
        for (int i = 0; i < inventory.getSize(); i++) {
            add(i, item);
        }

        update();
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        return true;
    }

    public void update() {
        add(1, 4, getItem(
                Server.HUB,
                new ItemBuilder(Material.LIGHT_GRAY_TERRACOTTA, 1),
                new String[] {
                        "§7§oMaanbasis",
                        "§7§oMoonbase"
                },
                new String[][] {
                        new String[] {
                                "§7Het midden van de OrbitMines",
                                "§7Galaxy bevindt zich hier."
                        },
                        new String[] {
                                "§7The center of the OrbitMines",
                                "§7Galaxy is located here."
                        }
                }));
        add(4, 4, getItem(
                Server.SURVIVAL,
                new ItemBuilder(Material.STONE_HOE, 1).addFlag(ItemFlag.HIDE_ATTRIBUTES),
                new String[] {
                        "§7§oKolonie op Planeet Aarde",
                        "§7§oColony on Planet Earth"
                },
                new String[][] {
                        new String[] {
                                "§7De meest recente OrbitMines kolonie",
                                "§7op Planeet Aarde. Waar overleven",
                                "§7van het grootste belang is.",
                                "",
                                "§7- " + Server.SURVIVAL.getColor().getChatColor() + "Regions",
                                "§7- " + Server.SURVIVAL.getColor().getChatColor() + "Land Claims",
                                "§7- " + Server.SURVIVAL.getColor().getChatColor() + NumberUtils.locale(Region.WORLD_BORDER) + "x" + NumberUtils.locale(Region.WORLD_BORDER) + " Wereld",
                                "§7- " + Server.SURVIVAL.getColor().getChatColor() + "Homes",
                                "§7- " + Server.SURVIVAL.getColor().getChatColor() + "Warps",
                                "§7- " + Server.SURVIVAL.getColor().getChatColor() + "Chest Shops"
                        },
                        new String[] {
                                "§7The most recent OrbitMines colony",
                                "§7sent to Planet Earth. Where your",
                                "§7survival is of the utmost importance.",
                                "",
                                "§7- " + Server.SURVIVAL.getColor().getChatColor() + "Regions",
                                "§7- " + Server.SURVIVAL.getColor().getChatColor() + "Land Claims",
                                "§7- " + Server.SURVIVAL.getColor().getChatColor() + NumberUtils.locale(Region.WORLD_BORDER) + "x" + NumberUtils.locale(Region.WORLD_BORDER) + " World",
                                "§7- " + Server.SURVIVAL.getColor().getChatColor() + "Homes",
                                "§7- " + Server.SURVIVAL.getColor().getChatColor() + "Warps",
                                "§7- " + Server.SURVIVAL.getColor().getChatColor() + "Chest Shops"
                        }
                }));

        EmptyItemInstance item = new EmptyItemInstance(new ItemBuilder(Material.RED_STAINED_GLASS_PANE, 1, new Message("§cOnbekende Galaxies", "§cUnknown Galaxies").lang(language)).build());
        add(3, 1, item);
        add(3, 3, item);
        add(3, 5, item);
        add(3, 7, item);
        add(4, 2, item);
        add(4, 6, item);
    }

    private ItemInstance getItem(Server server, ItemBuilder item, String[] underline, String[][] comments) {
        List<String> lore = new ArrayList<>();
        lore.add(underline[language.ordinal()]);
        lore.add("");

        Server.Status status = server.getStatus();
        lore.add(status != Server.Status.ONLINE ? status.getColor().getChatColor() + "§l" + status.getName() : server.getColor().getChatColor() + "§l" + server.getPlayers() + " §7§l/ " + server.getMaxPlayers());
        lore.add("");

        Collections.addAll(lore, comments[language.ordinal()]);

        lore.add("");

        if (server == orbitMines.getServerHandler().getServer()) {
            item.glow();
            lore.add(new Message("§cAl verbonden.", "§cAlready connected.").lang(language));
        } else {
            lore.add(new Message("§aKlik hier om te verbinden.", "§aClick here to connect.").lang(language));
        }

        return new ItemInstance(item
                .setDisplayName("§8§lOrbit§7§lMines " + server.getDisplayName())
                .setLore(lore)
                .build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                omp.connect(server, true);
            }
        };
    }
}
