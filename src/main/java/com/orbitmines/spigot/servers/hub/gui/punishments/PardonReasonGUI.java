package com.orbitmines.spigot.servers.hub.gui.punishments;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.punishment.Punishment;
import com.orbitmines.spigot.api.handlers.OMPlayer;

public class PardonReasonGUI extends ReasonGUI {

    private final Punishment active;

    public PardonReasonGUI(CachedPlayer player, Punishment active) {
        super(player);

        this.active = active;
    }

    @Override
    public void openReasonGUI(OMPlayer omp, String reason) {
        new ConfirmPardonGUI(player, active, reason).open(omp);
    }
}
