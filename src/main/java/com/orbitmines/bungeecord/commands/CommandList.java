package com.orbitmines.bungeecord.commands;

import com.orbitmines.api.Server;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.cmd.Command;
import net.md_5.bungee.api.event.ChatEvent;

import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class CommandList extends Command {

    public CommandList() {
        super(CommandLibrary.LIST);
    }

    @Override
    public void dispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        omp.sendMessage("");

        int size = BungeePlayer.getPlayers().size();

        omp.sendMessage(" §8§lOrbit§7§lMines " + "§6§l " + size + " " + omp.lang(size == 1 ? "Speler" : "Spelers", size == 1 ? "Player" : "Players") + " Online");

        for (Server server : Server.values()) {
            /* Not Setup */
            if (server.getIp() == null)
                continue;

            List<BungeePlayer> players = BungeePlayer.getPlayers(server);

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < players.size(); i++) {
                if (i != 0)
                    stringBuilder.append("§7, ");

                stringBuilder.append(players.get(i).getName());
            }

            omp.sendMessage("  " + server.getDisplayName() + "§7(" + players.size() + "): " + stringBuilder.toString());
        }
    }
}
