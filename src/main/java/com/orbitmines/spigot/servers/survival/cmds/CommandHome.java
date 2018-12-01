package com.orbitmines.spigot.servers.survival.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.teleportable.Home;

public class CommandHome extends Command {

    public CommandHome() {
        super(CommandLibrary.SURVIVAL_HOME);
    }

    @Override
    public void dispatch(OMPlayer player, String[] a) {
        SurvivalPlayer omp = (SurvivalPlayer) player;

        if (omp.getHomes().size() == 0) {
            omp.sendMessage("Home", Color.RED, "§7Je hebt nog geen " + Home.COLOR.getChatColor() + "home§7 neergezet!", "§7You haven't set a " + Home.COLOR.getChatColor() + "home§7 yet!");
        } else if (a.length == 1) {
            omp.getHomes().get(0).teleport(omp);
        } else if (a.length == 2) {
            Home home = omp.getHome(a[1]);

            if (home != null) {
                home.teleport(omp);
                return;
            }
            omp.sendMessage("Home", Color.RED, "§7Je hebt geen " + Home.COLOR.getChatColor() + "home§7 genaamd '" + Home.COLOR.getChatColor() + a[1] + "§7'!", "§7You don't have a " + Home.COLOR.getChatColor() + "home§7 named '" + Home.COLOR.getChatColor() + a[1] + "§7'!");
        } else {
            getHelpMessage(omp).send(omp);
        }
    }
}