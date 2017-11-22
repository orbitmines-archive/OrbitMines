package com.orbitmines.bungeecord.events;

import com.orbitmines.api.PluginMessage;
import com.orbitmines.api.ServerList;
import com.orbitmines.api.utils.uuid.UUIDUtils;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.spigot.api.handlers.data.VoteData;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class VoteEvent implements Listener {

    private final OrbitMinesBungee bungee;

    public VoteEvent() {
        bungee = OrbitMinesBungee.getBungee();
    }

    @EventHandler
    public void onVote(VotifierEvent event) {
        Vote vote = event.getVote();
        UUID uuid = UUIDUtils.getUUIDFromDatabase(vote.getUsername());

        if (uuid == null) {/* Player doesn't exist / hasn't been on the server yet */
            .println("Vote received for unknown player: '" + vote.getUsername() + "'");
            return;
        }

        ServerList serverList = ServerList.fromDomain(vote.getServiceName());

        if (serverList == null) {
            .println("Unknown server list: " + vote.getServiceName() + ".");
            return;
        }

        /* We don't save players stats in the Bungeecord server */
        VoteData data = new VoteData(uuid);
        data.load();

        data.addVote(serverList, Long.parseLong(vote.getTimeStamp()));

        if (player == null)
            return;/* Offline */

        /* Online so we force a stats update, send a message & handle the vote */
        plugin.getSpigotUtils().dataTransfer(PluginMessage.CHECK_VOTE_CACHE, omp.getPlayer(), uuid.toString());
    }
}
