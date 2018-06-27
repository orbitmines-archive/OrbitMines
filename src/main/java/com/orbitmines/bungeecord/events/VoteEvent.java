package com.orbitmines.bungeecord.events;

import com.orbitmines.api.*;
import com.orbitmines.api.utils.uuid.UUIDUtils;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.utils.ConsoleUtils;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.utils.SkinLibrary;
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

        if (uuid == null) {/* Player doesn't exist / hasn't been on the server yet */
            ConsoleUtils.warn("Vote received for unknown player: '" + vote.getUsername() + "'");

            discord.getChannel(bungee.getToken(), DiscordBot.ChannelType.votes).sendMessage(discord.getEmote(bungee.getToken(), DiscordBot.CustomEmote.barrier).getAsMention() + " Received vote for unknown player '**" + vote.getUsername() + "**'.").queue();
            return;
        }
//        TODO
//        if (vote.getUsername().equalsIgnoreCase("O_o_Fadi_o_O"))
//            return;

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

        BungeePlayer omp = BungeePlayer.getPlayer(uuid);

        discord.getChannel(bungee.getToken(), DiscordBot.ChannelType.votes).sendMessage(discord.getEmote(bungee.getToken(), DiscordBot.CustomEmote.prismarine_shard).getAsMention() + " " + SkinLibrary.getEmote(discord.getGuild(bungee.getToken()), uuid).getAsMention() + getDiscordRankPrefix(uuid) + " **" + (omp != null ? omp.getName(true) : CachedPlayer.getPlayer(uuid).getPlayerName()) + "** has voted and received **250 Prisms**.").queue();

        if (omp == null)
            /* Offline */
            return;

        /* Online so we force a stats update, send a message & handle the vote */
        bungee.getMessageHandler().dataTransfer(PluginMessage.CHECK_VOTE_CACHE, omp.getPlayer(), uuid.toString());
    }

    private String getDiscordRankPrefix(UUID uuid) {
        CachedPlayer player = CachedPlayer.getPlayer(uuid);

        StaffRank staffRank = player.getStaffRank();
        VipRank vipRank = player.getVipRank();

        if (staffRank == StaffRank.NONE)
            return vipRank != VipRank.NONE ? " " + discord.getEmote(bungee.getToken(), vipRank).getAsMention() + "**" + player.getRankName() + "**" : "";

        return " **" + player.getRankName() + "**";
    }
}
