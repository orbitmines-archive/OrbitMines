package com.orbitmines.bungeecord.commands.moderator;

import com.orbitmines.api.Color;
import com.orbitmines.api.Server;
import com.orbitmines.api.StaffRank;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.cmd.StaffCommand;
import net.md_5.bungee.api.event.ChatEvent;

import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class CommandSend extends StaffCommand {

    private String[] alias = { "/send" };

    private OrbitMinesBungee bungee;

    public CommandSend(OrbitMinesBungee bungee) {
        super(StaffRank.MODERATOR);

        this.bungee = bungee;
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(BungeePlayer omp) {
        return "<player>|<server>|all <server>";
    }

    @Override
    public void onDispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        if (a.length != 3) {
            getHelpMessage(omp).send(omp);
            return;
        }

        Server target = bungee.getServer(a[2]);

        if (target == null) {
            omp.sendMessage("Server", Color.RED, "§7Ongeldige target server.", "§7Invalid target server.");
            return;
        }

        BungeePlayer omp2 = BungeePlayer.getPlayer(a[1]);

        if (omp2 != null) {
            omp2.connect(target, false);

            omp2.sendMessage("Server", Color.LIME, omp2.getName() + " §7naar " + target.getDisplayName() + "§7 gestuurd.", "Sent " + omp2.getName() + " §7to " + target.getDisplayName() + "§7.");
            return;
        }

        if (a[1].equals("all")) {
            for (BungeePlayer player : BungeePlayer.getPlayers()) {
                player.connect(target, false);
            }

            int size = BungeePlayer.getPlayers().size();

            omp.sendMessage("Server", Color.LIME, "Alle spelers naar " + target.getDisplayName() + "§7 gestuurd. (" + size + " " + (size == 1 ? "speler" : "spelers") + ")", "Sent all players to " + target.getDisplayName() + "§7. (" + size + " " + (size == 1 ? "player" : "players") + ")");
            return;
        }

        Server server = bungee.getServer(a[1]);

        if (server != null) {
            List<BungeePlayer> players = BungeePlayer.getPlayers(server);

            for (BungeePlayer player : players) {
                player.connect(target, false);
            }

            int size = players.size();

            omp.sendMessage("Server", Color.LIME, server.getDisplayName() + " §7naar " + target.getDisplayName() + "§7 gestuurd. (" + size + " " + (size == 1 ? "speler" : "spelers") + ")", "Sent " + server.getDisplayName() + " §7to " + target.getDisplayName() + "§7. (" + size + " " + (size == 1 ? "player" : "players") + ")");
            return;
        }

        getHelpMessage(omp).send(omp);
    }
}
