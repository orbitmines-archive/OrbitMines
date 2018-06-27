package com.orbitmines.spigot.servers.survival.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Server;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.gui.RegionGUI;
import com.orbitmines.spigot.servers.survival.handlers.region.Region;

public class CommandRegion extends Command {

    private String[] alias = { "/region", "/regions", "/rg" };

    private final Survival survival;

    public CommandRegion(Survival survival) {
        super(Server.SURVIVAL);

        this.survival = survival;
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return "(" + omp.lang("nummer", "number") + ")|random";
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        if (a.length == 1) {
            //TODO open closest region as 0,0?
            new RegionGUI(survival).open(omp);
        } else if (a.length == 2) {
            if (a[1].equalsIgnoreCase("random")) {
                Region region = Region.randomTeleportable();
                if (omp.getWorld() == survival.getOrbitMines().getLobby().getWorld())
                    omp.getPlayer().teleport(region.getLocation());
                else
                    region.teleport(omp);
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
                if (omp.getWorld() == survival.getOrbitMines().getLobby().getWorld())
                    omp.getPlayer().teleport(region.getLocation());
                else
                    region.teleport(omp);
                return;
            }

            omp.sendMessage("Region", Color.RED, "§7Kan §aRegion " + id + "§7 niet vinden.", "§7Can't find §aRegion " + id + "§7.");
        } else {
            getHelpMessage(omp).send(omp);
        }
    }
}
