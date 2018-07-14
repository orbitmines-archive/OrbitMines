package com.orbitmines.spigot.api.options.chestshops;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import org.bukkit.block.Chest;

public class ChestShopViewerGUI extends GUI {

    private final Chest chest;

    public ChestShopViewerGUI(Chest chest) {
        this.chest = chest;

        newInventory(chest.getInventory().getSize(), "§0§lChest Shop Viewer");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        inventory.setContents(chest.getInventory().getContents());
        return true;
    }
}
