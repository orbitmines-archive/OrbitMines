package com.orbitmines.spigot.servers.survival.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Server;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class CommandAccept extends Command {

    private String[] alias = { "/accept" };

    public CommandAccept() {
        super(Server.SURVIVAL);
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
    public void dispatch(OMPlayer player, String[] a) {
        SurvivalPlayer omp = (SurvivalPlayer) player;

        if (a.length == 1) {
            getHelpMessage(omp).send(omp);
            return;
        }

        Player p2 = Bukkit.getPlayer(a[1]);
        if (p2 == null) {
            omp.sendMessage("Teleport", Color.RED, "§6" + a[1] + "§7 is niet meer online!", "§6" + a[1] + "§7 is no longer online!");
            omp.getTpHereRequests().remove(a[1]);
            omp.getTpRequests().remove(a[1]);
            return;
        }

        Player p = omp.getPlayer();
        SurvivalPlayer omp2 = SurvivalPlayer.getPlayer(p2);

        if (omp.hasTpRequestFrom(a[1])) {
            omp2.setBackLocation(p2.getLocation());
            p2.teleport(p);
            omp.sendMessage("Teleport", Color.LIME, omp2.getName() + " §7is naar jouw geteleporteerd.", omp2.getName() + " §7has been teleporter to you.");
            omp2.sendMessage("Teleport", Color.LIME, omp.getName() + " §7heeft je verzoek geaccepteerd.", omp.getName() + " §7accepted your request.");
            p2.playSound(p2.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1);

            omp.getTpRequests().remove(a[1]);
        } else if (omp.hasTpHereRequestFrom(a[1])) {
            omp.setBackLocation(p.getLocation());
            p.teleport(p2);
            omp.sendMessage("Teleport", Color.LIME, "§7Je bent geteleporteerd naar " + omp2.getName() + "§7.", "§7You have been teleported to " + omp2.getName() + "§7.");
            omp2.sendMessage("Teleport", Color.LIME, omp.getName() + " §7heeft je verzoek geaccepteerd.", omp.getName() + " §7accepted your request.");
            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1);

            omp.getTpHereRequests().remove(a[1]);
        } else {
            getHelpMessage(omp).send(omp);
        }
    }
}
