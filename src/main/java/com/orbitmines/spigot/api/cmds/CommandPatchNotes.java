package com.orbitmines.spigot.api.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Server;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.PatchNotes;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;

public class CommandPatchNotes extends Command {

    private String[] alias = { "/patchnotes" };

    private final OrbitMines orbitMines;

    public CommandPatchNotes(OrbitMines orbitMines) {
        super(null);

        this.orbitMines = orbitMines;
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return "(server) (version)";
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        if (a.length == 1) {
            orbitMines.getPatchNotes().getLatest(orbitMines.getServerHandler().getServer()).open(omp);
            return;
        }

        Server server;
        try {
            server = Server.valueOf(a[1].toUpperCase());
        } catch(IllegalArgumentException ex) {
            omp.sendMessage("PatchNotes", Color.RED, "Die server bestaat niet!", "That server doesn't exist.");
            return;
        }

        if (a.length == 2) {
            orbitMines.getPatchNotes().getLatest(server).open(omp);
            return;
        }

        String version = a[2];

        PatchNotes.Instance instance = orbitMines.getPatchNotes().get(server, version);

        if (instance == null) {
            omp.sendMessage("PatchNotes", Color.RED, "Onbekende versie.", "Unknown version.");
            return;
        }

        instance.open(omp);
    }
}
