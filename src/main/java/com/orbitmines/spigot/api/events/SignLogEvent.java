package com.orbitmines.spigot.api.events;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.Color;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.utils.BotToken;
import com.orbitmines.discordbot.utils.ColorUtils;
import com.orbitmines.discordbot.utils.SkinLibrary;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.utils.LocationUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.UUID;

public class SignLogEvent implements Listener {

    private final OrbitMines orbitMines;

    public SignLogEvent(OrbitMines orbitMines) {
        this.orbitMines = orbitMines;
    }

    @EventHandler
    public void onSignPlace(SignChangeEvent event) {
        Player player = event.getPlayer();
        OMPlayer omp = OMPlayer.getPlayer(player);

        DiscordBot discord = orbitMines.getServerHandler().getDiscord();
        BotToken token = orbitMines.getServerHandler().getToken();

        TextChannel channel = discord.getChannel(token, DiscordBot.ChannelType.sign_log);

        channel.sendMessage(SkinLibrary.getEmote(discord.getGuild(token), omp.getUUID()).getAsMention() + getDiscordRankPrefix(omp.getUUID()) + " **" + omp.getName(true) + "** has placed a sign.").queue();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("SIGN PLACEMENT");
        builder.setDescription(LocationUtils.friendlyString(event.getBlock().getLocation()) + " (world: " + event.getBlock().getWorld().getName() + ")");
        builder.setColor(ColorUtils.from(Color.ORANGE));

        builder.addField("Player", omp.getName(true), true);
        builder.addField("Server", orbitMines.getServerHandler().getServer().getName(), true);
        builder.addField("Date", DateUtils.FORMAT.format(DateUtils.now()), true);
        builder.addField("1.", event.getLine(0), false);
        builder.addField("2.", event.getLine(1), false);
        builder.addField("3.", event.getLine(2), false);
        builder.addField("4.", event.getLine(3), false);

        builder.setThumbnail(DiscordBot.Images.SIGN.getUrl());

        channel.sendMessage(builder.build()).queue();

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
