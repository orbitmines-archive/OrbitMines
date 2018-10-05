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
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CommandBack extends Command {

    public CommandBack() {
        super(CommandLibrary.SURVIVAL_BACK);
    }

    @Override
    public void dispatch(OMPlayer player, String[] a) {
        SurvivalPlayer omp = (SurvivalPlayer) player;
        Player p = omp.getPlayer();

        if (omp.getBackLocation() != null) {
            if (omp.getBackCharges() > 0) {
                Location l = p.getLocation();
                p.teleport(omp.getBackLocation());

                omp.setBackLocation(l);
                omp.removeBackCharges(1);

                omp.sendMessage("Back", Color.LIME, "§7Geteleporteerd naar je vorige locatie. (" + NumberUtils.locale(omp.getBackCharges()) + " " + (omp.getBackCharges() == 1 ? "charge" : "charges") + " over)", "§7Teleported to your previous location. (" + NumberUtils.locale(omp.getBackCharges()) + " " + (omp.getBackCharges() == 1 ? "charge" : "charges") + " remaining)");
            } else {
                omp.sendMessage("Back", Color.RED, "Je hebt geen §6§lBack Charges§7 meer.", "You don't have any §6§lBack Charges§7.");
            }
        } else {
            omp.sendMessage("Back", Color.RED, "§7Je kan niet teleporteren naar je vorige locatie.", "§7You cannot teleport to your previous location.");
        }
    }
}
