package com.orbitmines.spigot.api.cmds.moderator;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.Color;
import com.orbitmines.api.StaffRank;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.StaffCommand;
import com.orbitmines.spigot.servers.hub.gui.punishments.PunishGUI;

public class CommandPunish extends StaffCommand {

    private String[] alias = { "/mod", "/ban", "/unban", "/warn", "/mute" };

    public CommandPunish() {
        super(StaffRank.MODERATOR);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return "<player>";
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        if (a.length != 2) {
            getHelpMessage(omp).send(omp);
            return;
        }

        CachedPlayer player = CachedPlayer.getPlayer(a[1]);

        if (player == null) {
            omp.sendMessage("Mod", Color.RED, "§7Kan die speler niet in ons database vinden.", "§7Can't find that player in our database.");
            return;
        }

        new PunishGUI(player).open(omp);
    }
}
