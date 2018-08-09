package com.orbitmines.discordbot.utils;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.discordbot.DiscordBot;
import net.dv8tion.jda.core.entities.Guild;

public class DiscordBungeeUtils {

    public static String getDisplay(DiscordBot discord, BotToken token, BungeePlayer omp) {
        Guild guild = discord.getGuild(token);

        String prefix;

        if (omp.getStaffRank() == StaffRank.NONE || omp.getStaffRank() == StaffRank.ADMIN)
            prefix = omp.getVipRank() != VipRank.NONE ? " " + discord.getEmote(token, omp.getVipRank()).getAsMention() + "**" + omp.getRankName() + "**" : "";
        else
            prefix = " **" + omp.getRankName() + "**";

        return SkinLibrary.getEmote(guild, omp.getUUID()).getAsMention() + prefix + " **" + omp.getName(true) + "**";
    }
}
