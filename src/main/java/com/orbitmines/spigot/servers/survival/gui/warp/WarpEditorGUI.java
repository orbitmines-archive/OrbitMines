package com.orbitmines.spigot.servers.survival.gui.warp;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import com.orbitmines.spigot.api.nms.anvilgui.AnvilNms;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.teleportable.Warp;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class WarpEditorGUI extends GUI {

    private final OrbitMines orbitMines;
    private final Warp warp;

    public WarpEditorGUI(Warp warp) {
        this.orbitMines = OrbitMines.getInstance();
        this.warp = warp;

        newInventory(27, "§0§l" + warp.getName());
    }

    @Override
    protected boolean onOpen(OMPlayer player) {
        SurvivalPlayer omp = (SurvivalPlayer) player;

        add(1, 1, new ItemInstance(new PlayerSkullBuilder(() -> "Cyan Arrow Left", 1, omp.lang(Warp.COLOR.getChatColor() + "« Terug naar Jouw Warps", Warp.COLOR.getChatColor() + "« Back to Your Warps")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjc2OGVkYzI4ODUzYzQyNDRkYmM2ZWViNjNiZDQ5ZWQ1NjhjYTIyYTg1MmEwYTU3OGIyZjJmOWZhYmU3MCJ9fX0=").build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new WarpSlotsGUI().open(omp);
            }
        });

        add(1, 4, new ItemInstance(new ItemBuilder(Material.BOOK_AND_QUILL, 1, 0, omp.lang("§7§lHernoem Warp", "§7§lRename Warp")).build()) {
            @Override
            public void onClick(InventoryClickEvent e, OMPlayer omp) {
                AnvilNms anvil = orbitMines.getNms().anvilGui(omp.getPlayer(), (event) -> {
                    if (event.getSlot() != AnvilNms.AnvilSlot.OUTPUT) {
                        event.setWillClose(false);
                        event.setWillDestroy(false);
                        return;
                    }

                    String warpName = event.getName();
                    Warp warp = Warp.getWarp(warpName);

                    if (warp == null) {
                        event.setWillClose(true);
                        event.setWillDestroy(true);

                        WarpEditorGUI.this.warp.setName(warpName);
                        omp.playSound(Sound.ENTITY_ARROW_HIT_PLAYER);
                    } else {
                        event.setWillClose(false);
                        event.setWillDestroy(false);
                        omp.sendMessage("Warp", Color.RED, "§7Er bestaat al een Warp met die naam.", "§7There already is a Warp with that name.");
                    }
                }, new AnvilNms.AnvilCloseEvent() {
                    @Override
                    public void onClose() {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                reopen(omp);
                            }
                        }.runTaskLater(orbitMines, 1);
                    }
                });

                anvil.getItems().put(AnvilNms.AnvilSlot.INPUT_LEFT, warp.getIcon().getItemBuilder().setDisplayName(warp.getName()).build());

                anvil.open();
            }
        });

        {
            ItemBuilder item = new ItemBuilder(warp.getLocation() == null ? Material.EMPTY_MAP : Material.MAP, 1, 0, omp.lang("§7§lZet Warp Locatie", "§7§lSet Warp Location"));

            if (warp.getLocation() == null)
                item.glow();

            add(1, 5, new ItemInstance(item.build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    omp.getPlayer().closeInventory();

                    warp.setLocation(omp.getPlayer().getLocation());
                    omp.sendMessage("Warp", Color.LIME, "§7De warp locatie van '" + Warp.COLOR.getChatColor() + warp.getName() + "§7' is veranderd.", "§7Changed the warp location of '" + Warp.COLOR.getChatColor() + warp.getName() + "§7'.");
                    omp.playSound(Sound.ENTITY_ARROW_HIT_PLAYER);
                }
            });
        }

        add(1, 6, new ItemInstance(warp.getIcon().getItemBuilder().setDisplayName(omp.lang("§7§lVerander Icoon", "§7§lChange Icon")).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                //TODO OPEN ICON INV
            }
        });

        add(1, 7, new ItemInstance(new ItemBuilder(warp.isEnabled() ? Material.EYE_OF_ENDER : Material.ENDER_PEARL, 1, 0, warp.isEnabled() ? omp.lang("§7§lTeleporteren §a§lINGESCHAKELD", "§7§lTeleporting §a§lENABLED") : omp.lang("§7§lTeleporteren §c§lUITGESCHAKELD", "§7§lTeleporting §c§lDISABLED")).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                if (warp.getLocation() == null)
                    return;

                warp.setEnabled(!warp.isEnabled());

                omp.playSound(Sound.UI_BUTTON_CLICK);
                reopen(omp);
            }
        });

        return true;
    }
}
