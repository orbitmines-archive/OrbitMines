package com.orbitmines.spigot.servers.kitpvp.handlers.passives;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.servers.kitpvp.KitPvP;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DummyEvent extends Event {

    private final KitPvP kitPvP;
    private final KitPvPPlayer player;

    public DummyEvent(KitPvP kitPvP, KitPvPPlayer player) {
        this.kitPvP = kitPvP;
        this.player = player;
    }

    public KitPvP getKitPvP() {
        return kitPvP;
    }

    public KitPvPPlayer getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
            return null;
        }
}
