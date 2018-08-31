package com.orbitmines.spigot.servers.survival.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Server;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.claim.Claim;
import com.orbitmines.spigot.servers.survival.handlers.teleportable.Home;

public class CommandSetHome extends Command {

    private String[] alias = { "/sethome", "/seth" };

    private Survival survival;

    public CommandSetHome(Survival survival) {
        super(Server.SURVIVAL);

        this.survival = survival;
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return "<" + omp.lang("naam", "name") + ">";
    }

    @Override
    public void dispatch(OMPlayer player, String[] a) {
        SurvivalPlayer omp = (SurvivalPlayer) player;

        int max = omp.getHomesAllowed();

        if (a.length != 2) {
            getHelpMessage(omp).send(omp);
        } else if (omp.getHomes().size() >= max && omp.getHome(a[1]) == null) {
            omp.sendMessage("Home", Color.RED, "§7Je hebt al de maximale hoeveelheid homes! (" + Home.COLOR.getChatColor() + max + " Homes§7)", "§7You already reached the maximum amount of homes! (" + Home.COLOR.getChatColor() + max + " Homes§7)");
        } else if (a[1].length() > Home.MAX_CHARACTERS){
            omp.sendMessage("Home", Color.RED, "§7Je mag maximaal maar " + Home.COLOR.getChatColor() + Home.MAX_CHARACTERS + " karakters§7 gebruiken.", "§7You're only allowed to use " + Home.COLOR.getChatColor() + Home.MAX_CHARACTERS + " characters§7.");
        } else {
            for (int i = 0; i < a[1].length(); i++) {
                char c = a[1].charAt(i);
                if (!Character.isAlphabetic(c) && !Character.isDigit(c) && c != '_') {
                    omp.sendMessage("Home", Color.RED, "§7Je " + Home.COLOR.getChatColor() + "home naam§7 kan alleen maar bestaan uit " + Home.COLOR.getChatColor() + "letters§7 en " + Home.COLOR.getChatColor() + "nummers§7.", "§7Your " + Home.COLOR.getChatColor() + "home name§7 can only contain " + Home.COLOR.getChatColor() + "alphabetic§7 and " + Home.COLOR.getChatColor() + "numeric§7 characters.");
                    return;
                }
            }

            Claim claim = survival.getClaimHandler().getClaimAt(omp.getLocation(), false, omp.getLastClaim());
            if (claim != null)
                omp.setLastClaim(claim);

            if (claim == null || claim.canAccess(omp, true)) {
                omp.setHome(a[1]);
            } else {
                omp.sendMessage("Home", Color.RED, "Je hebt §a§l" + omp.lang(Claim.Permission.ACCESS.getName()) + " §7nodig van " + claim.getOwnerName() + " §7om hier een home te zetten.", "You need §a§l" + omp.lang(Claim.Permission.ACCESS.getName()) + " §7from " + claim.getOwnerName() + " §7to set a home here.");
            }
        }
    }
}
