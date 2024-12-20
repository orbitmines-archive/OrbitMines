package com.orbitmines.bungeecord.events;

import com.orbitmines.api.PluginMessage;
import com.orbitmines.api.ServerList;
import com.orbitmines.api.utils.uuid.UUIDUtils;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.utils.ConsoleUtils;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.utils.DiscordUtils;
import com.orbitmines.spigot.api.handlers.data.VoteData;
import com.vexsoftware.votifier.bungee.events.VotifierEvent;
import com.vexsoftware.votifier.model.Vote;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class VoteEvent implements Listener {

    private final OrbitMinesBungee bungee;
    private final DiscordBot discord;

    public VoteEvent(OrbitMinesBungee bungee) {
        this.bungee = bungee;
        this.discord = bungee.getDiscord();
    }

    @EventHandler
    public void onVote(VotifierEvent event) {
        Vote vote = event.getVote();
        UUID uuid = UUIDUtils.getUUIDFromDatabase(vote.getUsername());

        if (vote.getUsername().equalsIgnoreCase("O_o_Fadi_o_O"))
            return;

        if (uuid == null) {/* Player doesn't exist / hasn't been on the server yet */
            ConsoleUtils.warn("Vote received for unknown player: '" + vote.getUsername() + "'");

            discord.getChannel(bungee.getToken(), DiscordBot.ChannelType.votes).sendMessage(discord.getEmote(bungee.getToken(), DiscordBot.CustomEmote.barrier).getAsMention() + " Received vote for unknown player '**" + vote.getUsername() + "**'.").queue();
            return;
        }

        ServerList serverList = ServerList.fromDomain(vote.getServiceName());

        if (serverList == null) {
            ConsoleUtils.warn("Unknown server list: " + vote.getServiceName() + ".");

            discord.getChannel(bungee.getToken(), DiscordBot.ChannelType.votes).sendMessage(discord.getEmote(bungee.getToken(), DiscordBot.CustomEmote.barrier).getAsMention() + " Received vote for unknown server list '**" + vote.getServiceName() + "**'.").queue();
            return;
        }

        /* We don't save players stats in the Bungeecord server */
        VoteData data = new VoteData(uuid);
        data.load();

        data.addVote(serverList, Long.parseLong(vote.getTimeStamp()));

        /* Discord message */
        discord.getChannel(bungee.getToken(), DiscordBot.ChannelType.votes).sendMessage(discord.getEmote(bungee.getToken(), DiscordBot.CustomEmote.prismarine_shard).getAsMention() + " " + DiscordUtils.getDisplay(discord, bungee.getToken(), uuid) + " has voted and received **250 Prisms**.").queue();

        BungeePlayer omp = BungeePlayer.getPlayer(uuid);

        if (omp == null)
            /* Offline */
            return;

        /* Online so we force a stats update, send a message & handle the vote */
        bungee.getMessageHandler().dataTransfer(PluginMessage.CHECK_VOTE_CACHE, omp.getPlayer(), uuid.toString());
    }
}
