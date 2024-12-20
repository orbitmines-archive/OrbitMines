package com.orbitmines.spigot.servers.survival.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.gui.claim.ClaimListGUI;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;

public class CommandClaims extends Command {

    private final Survival survival;

    public CommandClaims(Survival survival) {
        super(CommandLibrary.SURVIVAL_CLAIMS);

        this.survival = survival;
    }

    @Override
    public void dispatch(OMPlayer player, String[] a) {
        SurvivalPlayer omp = (SurvivalPlayer) player;

        if (a.length != 1 && omp.isEligible(StaffRank.MODERATOR)) {
            CachedPlayer cachedPlayer = CachedPlayer.getPlayer(a[1]);

            if (cachedPlayer == null) {
                omp.sendMessage("Claim", "§7Kan die speler niet vinden.", "§7That player cannot be found.");
                return;
            }

            new ClaimListGUI(survival, cachedPlayer).open(omp);
        } else {
            new ClaimListGUI(survival, CachedPlayer.getPlayer(omp.getUUID())).open(omp);
        }
    }
}
