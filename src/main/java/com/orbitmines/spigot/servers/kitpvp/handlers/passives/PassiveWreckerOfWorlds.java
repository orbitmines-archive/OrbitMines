package com.orbitmines.spigot.servers.kitpvp.handlers.passives;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.spigot.servers.kitpvp.handlers.Passive;

public class PassiveWreckerOfWorlds implements Passive.Handler {

    /* soldier doesnt get any dmg from the lightning he produces with his sword he still gets lightning dmg from all other sources. */
    //Lvl 1: 1/3 chance for 1.0 dmg
    //LvL 2: 2/3 chance for 1.5 dmg

    @Override
    public void trigger(KitPvPPlayer player, KitPvPPlayer triggeredBy, int level) {

    }
}
