package com.orbitmines.spigot.servers.survival.gui;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.region.Region;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Collections;

public class RegionGUI extends GUI {

    private int x;
    private int y;

    public RegionGUI() {
        this(0, 0);
    }

    public RegionGUI(int x, int y) {
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
                    add(row, slot, new ItemInstance(new PlayerSkullBuilder(navigator::getTexture, 1, omp.lang("§a§lMeer Regions", "§a§lMore Regions")).build()) {
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
                            //TODO Open
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

        UP(0, 1, 0, 4, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2Y0NmFiYWQ5MjRiMjIzNzJiYzk2NmE2ZDUxN2QyZjFiOGI1N2ZkZDI2MmI0ZTA0ZjQ4MzUyZTY4M2ZmZjkyIn19fQ=="),
        DOWN(0, -1, 4, 4, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmU5YWU3YTRiZTY1ZmNiYWVlNjUxODEzODlhMmY3ZDQ3ZTJlMzI2ZGI1OWVhM2ViNzg5YTkyYzg1ZWE0NiJ9fX0="),
        LEFT(1, 0, 2, 0, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY5NzFkZDg4MWRiYWY0ZmQ2YmNhYTkzNjE0NDkzYzYxMmY4Njk2NDFlZDU5ZDFjOTM2M2EzNjY2YTVmYTYifX19"),
        RIGHT(-1, 0, 2, 8, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjMyY2E2NjA1NmI3Mjg2M2U5OGY3ZjMyYmQ3ZDk0YzdhMGQ3OTZhZjY5MWM5YWMzYTkxMzYzMzEzNTIyODhmOSJ9fX0="),
        UP_LEFT(1, 1, 0, 0, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzhjZjQ0NGJjMGUwZGRjNmE2YTlkNzU5NTE3NDZlMDk1NWJkNjc2YzRhNjc3MWMxYTVlZDkwZTc4MjdmIn19fQ=="),
        UP_RIGHT(-1, 1, 0, 8, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjc2ZGNiMjc0ZTJlMjMxNDlmNDcyNzgxNjA1YjdjNmY4Mzk5MzFhNGYxZDJlZGJkMWZmNTQ2M2FiN2M0MTI0NiJ9fX0="),
        DOWN_LEFT(1, -1, 4, 0, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTY5NjFhZDFmNWM3NmU5NzM1OGM0NDRmZTBlODNhMzk1NjRlNmI0ODEwOTE3MDk4NGE4NGVjYTVkY2NkNDI0In19fQ=="),
        DOWN_RIGHT(-1, -1, 4, 8, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWExMDNhY2MyMWVlMGExZjg3YjViZThmM2VjN2JmNDc3NDVlYzk5MjA4MWJlOWI0MTU0OGVlM2UzNjgyIn19fQ==");

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
