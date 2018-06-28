package com.orbitmines.spigot.servers.hub.gui.punishments;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.punishment.offences.Offence;
import com.orbitmines.api.punishment.offences.Severity;
import com.orbitmines.spigot.api.handlers.OMPlayer;

public class PunishReasonGUI extends ReasonGUI {

    public PunishReasonGUI(CachedPlayer player, Offence offence, Severity severity) {
        super(player, offence, severity);
    }

    @Override
    public void openReasonGUI(OMPlayer omp, String reason) {
        new ConfirmPunishGUI(player, offence, severity, reason).open(omp);
    }
}
