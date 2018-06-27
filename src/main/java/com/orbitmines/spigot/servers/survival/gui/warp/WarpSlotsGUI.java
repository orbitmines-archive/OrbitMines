package com.orbitmines.spigot.servers.survival.gui.warp;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.api.CachedPlayer;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.teleportable.Warp;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class WarpSlotsGUI extends GUI {

    private Survival survival;

    public WarpSlotsGUI(Survival survival) {
        this.survival = survival;
    }

    @Override
    protected boolean onOpen(OMPlayer player) {
        SurvivalPlayer omp = (SurvivalPlayer) player;
        newInventory(27, "§0§l" + omp.lang("Jouw Warps", "Your Warps"));

        add(1, 2, new ItemInstance(new PlayerSkullBuilder(() -> "Cyan Arrow Left", 1, omp.lang(Warp.COLOR.getChatColor() + "« Terug naar Warps", Warp.COLOR.getChatColor() + "« Back to Warps")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjc2OGVkYzI4ODUzYzQyNDRkYmM2ZWViNjNiZDQ5ZWQ1NjhjYTIyYTg1MmEwYTU3OGIyZjJmOWZhYmU3MCJ9fX0=").build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new WarpGUI(survival).open(omp);
            }
        });

        List<Warp> warps = Warp.getWarpsFor(omp.getUUID());

        Warp.Type[] types = Warp.Type.values();
        for (int i = 0; i < types.length; i++) {
            Warp.Type type = types[i];

            /* Get Player's warp for this slot */
            Warp warp = getFromType(warps, type);

            ItemInstance instance;

            if (warp == null) {
                if (type.hasUnlocked(omp)) {
                    instance = new ItemInstance(new ItemBuilder(Material.BOOK_AND_QUILL, i + 1, 0,
                            omp.lang(type.getSlotName()) + " §7§lSlot",
                            " " + omp.lang(type.getDescription()),
                            "",
                            omp.lang("§aKlik hier om je warp te maken.", "§aClick here to create your warp.")
                    ).glow().build()) {
                        @Override
                        public void onClick(InventoryClickEvent event, OMPlayer omp) {
                            /* Double check, if this is somehow fired twice */
                            if (getFromType(Warp.getWarpsFor(omp.getUUID()), type) != null)
                                return;

                            Warp warp = Warp.createWarp(omp.getUUID(), omp.getName(true) + "s Warp", false, type, Warp.Icon.random(), null);
                            new WarpEditorGUI(survival, warp).open(omp);
                        }
                    };
                } else {
                    instance = new EmptyItemInstance(new ItemBuilder(Material.BOOK, i + 1, 0, omp.lang(type.getSlotName()) + " §7§lSlot", " " + omp.lang(type.getDescription())).build());
                }
            } else {
                ItemBuilder item = warp.getIcon().getItemBuilder().setAmount(i + 1);

                item.setDisplayName("§7§lWarp " + Warp.COLOR.getChatColor() + "§l" + warp.getName());

                CachedPlayer owner = warp.getOwner();
                item.addLore(" §7" + omp.lang("Eigenaar", "Owner") + ": " + owner.getRankPrefixColor().getChatColor() + owner.getPlayerName());
                item.addLore(" §7XZ: " + (warp.getLocation() == null ? omp.lang("§c§lNIET INGESTELD", "§c§lNOT SET") : Warp.COLOR.getChatColor() + "§l" + NumberUtils.locale(warp.getLocation().getBlockX()) + "§7 / " + Warp.COLOR.getChatColor() + "§l" + NumberUtils.locale(warp.getLocation().getBlockZ())));

                item.addLore("");
                item.addLore(omp.lang("§aKlik hier om te veranderen.", "§aClick here to edit."));

                instance = new ItemInstance(item.build()) {
                    @Override
                    public void onClick(InventoryClickEvent event, OMPlayer omp) {
                        new WarpEditorGUI(survival, warp).open(omp);
                    }
                };
            }

            add(1, 4 + i, instance);
        }

        return true;
    }

    private Warp getFromType(List<Warp> warps, Warp.Type type) {
        for (Warp warp : warps) {
            if (warp.getType() == type)
                return warp;
        }
        return null;
    }
}
