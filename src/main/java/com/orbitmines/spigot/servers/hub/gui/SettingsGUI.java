package com.orbitmines.spigot.servers.hub.gui;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;

public class SettingsGUI extends GUI {

    private OrbitMines orbitMines;

    public SettingsGUI() {
        this.orbitMines = OrbitMines.getInstance();
        newInventory(54, "§0§lSettings");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {

        return true;
    }
}
