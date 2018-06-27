package com.orbitmines.spigot.servers.survival.gui;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.region.Region;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Collections;

public class RegionGUI extends GUI {

    private final Survival survival;
    private int x;
    private int y;

    public RegionGUI(Survival survival) {
        this(survival, 0, 0);
    }

    public RegionGUI(Survival survival, int x, int y) {
        this.survival = survival;
        this.x = x;
        this.y = y;

        newInventory(45, "§0§lRegion Teleporter");
    }

    @Override
    protected boolean onOpen(OMPlayer player) {
        SurvivalPlayer omp = (SurvivalPlayer) player;

        for (int slot = 0; slot < 9; slot++) {
            for (int row = 0; row < 5; row++) {
                int inventoryX = slot - 4 + x;
                int inventoryY = row - 2 + y;

                Region region = Region.getRegion(inventoryX, inventoryY);

                Navigator navigator = Navigator.from(row, slot);

                if (navigator != null && navigator.canExpand(inventoryX, inventoryY)) {
                    add(row, slot, new ItemInstance(new PlayerSkullBuilder(() -> "Arrow " + navigator.toString(), 1, omp.lang("§a§lMeer Regions", "§a§lMore Regions")).setTexture(navigator.texture).build()) {
                        @Override
                        public void onClick(InventoryClickEvent event, OMPlayer omp) {
                            RegionGUI.this.x -= navigator.getXOff();
                            RegionGUI.this.y -= navigator.getYOff();
                            reopen(omp);
                        }
                    });

                    continue;
                }

                if (region.getId() < Region.TELEPORTABLE) {
                    add(row, slot, new ItemInstance(region.getIcon().build()) {
                        @Override
                        public void onClick(InventoryClickEvent event, OMPlayer omp) {
                            omp.getPlayer().closeInventory();

                            if (omp.getWorld() == survival.getOrbitMines().getLobby().getWorld())
                                omp.getPlayer().teleport(region.getLocation());
                            else
                                region.teleport(omp);
                        }
                    });
                } else {
                    add(row, slot, new EmptyItemInstance(region.getIcon().setMaterial(Material.STAINED_GLASS_PANE).setDurability((short) 15).setDisplayName("§8§lRegion §a§l" + (region.getId() + 1)).setLore(Collections.singletonList(" §8§lUNKNOWN")).build()));
                }
            }
        }

        return true;
    }

    private enum Navigator {

        UP(0, 1, 0, 4, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWRhMDI3NDc3MTk3YzZmZDdhZDMzMDE0NTQ2ZGUzOTJiNGE1MWM2MzRlYTY4YzhiN2JjYzAxMzFjODNlM2YifX19"),
        DOWN(0, -1, 4, 4, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmY3NDE2Y2U5ZTgyNmU0ODk5YjI4NGJiMGFiOTQ4NDNhOGY3NTg2ZTUyYjcxZmMzMTI1ZTAyODZmOTI2YSJ9fX0="),
        LEFT(1, 0, 2, 0, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU1MGI3Zjc0ZTllZDc2MzNhYTI3NGVhMzBjYzNkMmU4N2FiYjM2ZDRkMWY0Y2E2MDhjZDQ0NTkwY2NlMGIifX19"),
        RIGHT(-1, 0, 2, 8, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTYzMzlmZjJlNTM0MmJhMThiZGM0OGE5OWNjYTY1ZDEyM2NlNzgxZDg3ODI3MmY5ZDk2NGVhZDNiOGFkMzcwIn19fQ=="),
        UP_LEFT(1, 1, 0, 0, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2MyNGVjYTIwOGY3ZGQ2ZWVlZGNiOGI4MWE0MmZkY2Q4NmMxMWEyZWIzNzdmZWJjZDZkNTQ1MGFkNmJiMCJ9fX0="),
        UP_RIGHT(-1, 1, 0, 8, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmY2Yjg1ZjYyNjQ0NGRiZDViZGRmN2E1MjFmZTUyNzQ4ZmU0MzU2NGUwM2ZiZDM1YjZiNWU3OTdkZTk0MmQifX19"),
        DOWN_LEFT(1, -1, 4, 0, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzM2NThmOWVkMjE0NWVmODMyM2VjOGRjMjY4ODE5N2M1ODk2NDUxMGYzZDI5OTM5MjM4Y2UxYjZlNDVhZjBmZiJ9fX0="),
        DOWN_RIGHT(-1, -1, 4, 8, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjVhM2U1Mjg1NjU3NGE3YzgzY2QxMThiMWExMzYwYTdhOGJlODVjOGE0YzZmODZlMDY4ZGM0ZjRiNTA2YjY2In19fQ==");

        private final int xOff;
        private final int yOff;

        private final int row;
        private final int slot;

        private final String texture;

        Navigator(int xOff, int yOff, int row, int slot, String texture) {
            this.xOff = xOff;
            this.yOff = yOff;
            this.row = row;
            this.slot = slot;
            this.texture = texture;
        }

        public int getXOff() {
            return xOff;
        }

        public int getYOff() {
            return yOff;
        }

        public int getRow() {
            return row;
        }

        public int getSlot() {
            return slot;
        }

        public String getTexture() {
            return texture;
        }

        public boolean canExpand(int inventoryX, int inventoryY) {
            return Region.getRegion(inventoryX - xOff, inventoryY - yOff) != null;
        }

        public static Navigator from(int row, int slot) {
            for (Navigator navigator : Navigator.values()) {
                if (navigator.row == row && navigator.slot == slot)
                    return navigator;
            }
            return null;
        }
    }
}
