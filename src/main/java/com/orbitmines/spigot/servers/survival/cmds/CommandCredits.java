package com.orbitmines.spigot.servers.survival.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;

public class CommandCredits extends Command {

    public CommandCredits() {
        super(CommandLibrary.SURVIVAL_CREDITS);
    }

    @Override
    public void dispatch(OMPlayer player, String[] a) {
        SurvivalPlayer omp = (SurvivalPlayer) player;
        omp.sendMessage("Credits", Color.BLUE, "§7" + omp.lang("Je hebt", "You've got") + " §2§l" + NumberUtils.locale(omp.getEarthMoney()) + " " + (omp.getEarthMoney() == 1 ? "Credit" : "Credits") + "§7.");
    }
}
