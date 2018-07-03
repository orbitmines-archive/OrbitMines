package com.orbitmines.spigot.api.events;

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.utils.BotToken;
import com.orbitmines.discordbot.utils.SkinLibrary;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.cmds.CommandHelp;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class CommandPreprocessEvent implements Listener {

    private final OrbitMines orbitMines;

    public CommandPreprocessEvent(OrbitMines orbitMines) {
        this.orbitMines = orbitMines;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();
        OMPlayer omp = OMPlayer.getPlayer(p);

        String[] a = event.getMessage().split(" ");
        Command command = Command.getCommand(a[0]);

        event.setCancelled(true);

        if (command == null) {
            omp.sendMessage(Message.UNKNOWN_COMMAND);
            return;
        }

        command.dispatch(omp, a);

        if (command instanceof CommandHelp)/* Message already sent through Bungeecord */
            return;

        DiscordBot discord = orbitMines.getServerHandler().getDiscord();
        BotToken token = orbitMines.getServerHandler().getToken();

        discord.getChannel(token, DiscordBot.ChannelType.command_log).sendMessage(SkinLibrary.getEmote(discord.getGuild(token), omp.getUUID()).getAsMention() + getDiscordRankPrefix(omp.getUUID()) + " **" + omp.getName(true) + "** executed command **" + event.getMessage() + "**.").queue();
    }

    private String getDiscordRankPrefix(UUID uuid) {
        CachedPlayer player = CachedPlayer.getPlayer(uuid);

        if (player == null)
            return "";

        StaffRank staffRank = player.getStaffRank();
        VipRank vipRank = player.getVipRank();

        if (staffRank == StaffRank.NONE)
            return vipRank != VipRank.NONE ? " " + orbitMines.getServerHandler().getDiscord().getEmote(orbitMines.getServerHandler().getToken(), vipRank).getAsMention() + "**" + player.getRankName() + "**" : "";

        return " **" + player.getRankName() + "**";
    }
}
