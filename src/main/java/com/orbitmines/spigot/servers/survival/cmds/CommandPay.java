package com.orbitmines.spigot.servers.survival.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Server;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;

public class CommandPay extends Command {

    private String[] alias = { "/pay" };

    public CommandPay() {
        super(Server.SURVIVAL);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return "<player> <amount>";
    }

    @Override
    public void dispatch(OMPlayer player, String[] a) {
        SurvivalPlayer omp = (SurvivalPlayer) player;

        if (a.length < 3) {
            getHelpMessage(omp).send(omp);
            return;
        }

        SurvivalPlayer omp2 = SurvivalPlayer.getPlayer(a[1]);

        if (omp2 == null) {
            omp.sendMessage("Pay", Color.RED, "§6" + a[1] + "§7 is niet online!", "§6" + a[1] + "§7 is not online!");
            return;
        }

        if (omp == omp2) {
            omp.sendMessage("Pay", Color.RED, "§7Je kan niet jezelf betalen :(", "§7You can't pay yourself :(");
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(a[2]);
        } catch(NumberFormatException ex) {
            getHelpMessage(omp).send(omp);
            return;
        }

        if (amount <= 0) {
            omp.sendMessage("Pay", Color.RED, "Dat is een ongeldige hoeveelheid.", "That's an invalid amount.");
            return;
        }

        if (omp.getEarthMoney() < amount) {
            omp.sendMessage("Pay", Color.RED, "Je hebt niet genoeg §2§lCredits§7.", "You don't have enough §2§lCredits§7.");
            return;
        }

        omp.removeEarthMoney(amount);
        omp2.addEarthMoney(amount);

        String display = "§2§l" + NumberUtils.locale(amount) + " " + (amount == 1 ? "Credits" : "Credits");

        omp.sendMessage("Pay", Color.LIME, "Je hebt " + display + " §7betaald aan " + omp2.getName() + "§7.", "You have payed " + omp2.getName() + " " + display + "§7.");
        omp2.sendMessage("Pay", Color.LIME, "Je hebt " + display + " §7ontvangen van " + omp.getName() + "§7.", "You have received " + display + " §7from " + omp.getName() + "§7.");
    }
}
