package com.orbitmines.spigot.runnables;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import com.orbitmines.spigot.gui.ServerSelectorGUI;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class ServerSelectorRunnable extends SpigotRunnable {

    private OrbitMines orbitMines;

    public ServerSelectorRunnable() {
        super(TimeUnit.SECOND, 2);

        orbitMines = OrbitMines.getInstance();
    }

    @Override
    public void run() {
        for (ServerSelectorGUI gui : orbitMines.getServerSelectors().values()) {
            gui.update();
        }
    }
}
