package com.orbitmines.bungeecord.commands.moderator;

import com.orbitmines.api.Color;
import com.orbitmines.api.Server;
import com.orbitmines.api.StaffRank;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.cmd.StaffCommand;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class CommandMaintenance extends StaffCommand {

    private String[] alias = { "/maintenance" };

    private OrbitMinesBungee bungee;

    public CommandMaintenance(OrbitMinesBungee bungee) {
        super(StaffRank.MODERATOR);

        this.bungee = bungee;
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(BungeePlayer omp) {
        return "<server>";
    }

    @Override
    public void onDispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        if (a.length < 2) {
            getHelpMessage(omp).send(omp);
            return;
        }

        Server server = bungee.getServer(a[1]);

        if (server == null) {
            omp.sendMessage("Maintenance", Color.RED, "§7Ongeldige server.", "§7Invalid server.");
            return;
        }

        if (server.getStatus() == Server.Status.MAINTENANCE) {
            server.setStatus(Server.Status.OFFLINE);
            omp.sendMessage("Maintenance", Color.LIME, server.getDisplayName() + "§7 is nu uit " + Server.Status.MAINTENANCE.getDisplayName() + "§7.", server.getDisplayName() + "§7 is no longer in " + Server.Status.MAINTENANCE.getDisplayName() + "§7.");
            return;
        }

        server.setStatus(Server.Status.MAINTENANCE);
        for (BungeePlayer player : BungeePlayer.getPlayers(server)) {
            if (player.isEligible(StaffRank.MODERATOR))
                continue;

            ServerInfo fallback = player.getFallBackServer();

            if (fallback == null) {
                omp.getPlayer().disconnect(omp.lang("§8§lOrbit§7§lMines\n§7Kan geen server vinden om mee te verbinden.\n\n§7(" + server.getDisplayName() + "§r§7 is in " + Server.Status.MAINTENANCE.getDisplayName() + "§r§7)", "§8§lOrbit§7§lMines\n§7Cannot find a server to fall back to.\n\n§7(" + server.getDisplayName() + "§r§7 is in " + Server.Status.MAINTENANCE.getDisplayName() + "§r§7)"));
                continue;
            }

            player.connect(bungee.getServer(fallback), true);
        }
        omp.sendMessage("Maintenance", Color.LIME, server.getDisplayName() + "§7 is nu in " + Server.Status.MAINTENANCE.getDisplayName() + "§7.", server.getDisplayName() + "§7 is now in " + Server.Status.MAINTENANCE.getDisplayName() + "§7.");
    }
}
