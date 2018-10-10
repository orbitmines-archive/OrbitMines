package com.orbitmines.spigot.servers.survival.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.gui.RegionGUI;
import com.orbitmines.spigot.servers.survival.handlers.region.Region;

public class CommandRegion extends Command {

    private final Survival survival;

    public CommandRegion(Survival survival) {
        super(CommandLibrary.SURVIVAL_REGION);

        this.survival = survival;
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        if (a.length == 1) {
            //TODO open closest region as 0,0?
            new RegionGUI(survival).open(omp);
        } else if (a.length == 2) {
            if (a[1].equalsIgnoreCase("random")) {
                Region region = Region.randomTeleportable(omp.isFirstLogin()); /* We don't want new players to be spawning in regions under water, after that it's fine. */
                if (omp.getWorld() == survival.getOrbitMines().getLobby().getWorld()) {
                    omp.getPlayer().teleport(region.getLocation());
                    omp.sendMessage("Teleporter", Color.LIME, "§7" + omp.lang("Geteleporteerd naar", "Teleported to") + " " + region.getColor().getChatColor() + region.getName() + "§7.");
                } else {
                    region.teleport(omp);
                }
                return;
            }
            int id;

            try {
                id = Integer.parseInt(a[1]);
            } catch(NumberFormatException ex) {
                omp.sendMessage("Region", Color.RED, "§7Dat is geen geldig §aRegion§7 nummer.", "§7That's not a valid §aRegion§7 number.");
                return;
            }

            if (id > 0 && id <= Region.TELEPORTABLE) {
                Region region = Region.getRegion(id - 1);
                if (omp.getWorld() == survival.getOrbitMines().getLobby().getWorld()) {
                    omp.getPlayer().teleport(region.getLocation());
                    omp.sendMessage("Teleporter", Color.LIME, "§7" + omp.lang("Geteleporteerd naar", "Teleported to") + " " + region.getColor().getChatColor() + region.getName() + "§7.");
                } else {
                    region.teleport(omp);
                }
                return;
            }

            omp.sendMessage("Region", Color.RED, "§7Kan §aRegion " + id + "§7 niet vinden.", "§7Can't find §aRegion " + id + "§7.");
        } else {
            getHelpMessage(omp).send(omp);
        }
    }
}
