package com.orbitmines.discordbot.utils;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.utils.uuid.UUIDUtils;
import com.orbitmines.discordbot.DiscordBot;
import net.dv8tion.jda.core.entities.*;

import java.util.UUID;

public class DiscordUtils {

    public static String getDisplay(DiscordBot discord, BotToken token, UUID uuid) {
        CachedPlayer player = CachedPlayer.getPlayer(uuid);

        String name;
        String prefix;

        if (player == null) {
            name = UUIDUtils.getName(uuid);
            if (name == null)
                name = "(Unknown Player, UUID: " + uuid.toString() + ")";

            prefix = "";
        } else {
            name = player.getPlayerName();

            StaffRank staffRank = player.getStaffRank();
            VipRank vipRank = player.getVipRank();

            if (staffRank == StaffRank.NONE || staffRank == StaffRank.ADMIN)
                prefix = vipRank != VipRank.NONE ? " " + discord.getEmote(token, vipRank).getAsMention() + "**" + player.getRankName() + "**" : "";
            else
                prefix = " **" + player.getRankName() + "**";
        }

        return (player != null ? SkinLibrary.getEmote(discord.getGuild(token), player.getUUID()).getAsMention() : "") + prefix + " **" + name + "**";
    }

    public static String filterToDiscord(DiscordBot discord, BotToken token, String message) {
        Guild guild = discord.getGuild(token);

        for (Role role : guild.getRoles()) {
            message = message.replaceAll("@" + role.getName(), role.getAsMention());
        }
        for (TextChannel textChannel : guild.getTextChannels()) {
            message = message.replaceAll("#" + textChannel.getName(), textChannel.getAsMention());
        }
        for (Member member : guild.getMembers()) {
            message = message.replace("@" + member.getEffectiveName() + "#" + member.getUser().getDiscriminator(), member.getAsMention()).replaceAll("@" + member.getEffectiveName(), member.getAsMention()).replaceAll("@" + member.getNickname(), member.getAsMention());
        }
        for (Emote emote : guild.getEmotes()) {
            message = message.replaceAll(":" + emote.getName() + ":", emote.getAsMention());
        }

        return message;
    }

    public static String filterFromDiscord(Message message) {
        String msg = message.getContentDisplay();

        for (Role role : message.getMentionedRoles()) {
            msg = msg.replaceAll(role.getAsMention(), "@" + role.getName());
        }
        for (TextChannel textChannel : message.getMentionedChannels()) {
            msg = msg.replaceAll(textChannel.getAsMention(), "#" + textChannel.getName());
        }
        for (Member mem : message.getMentionedMembers()) {
            msg = msg.replaceAll(mem.getAsMention(), "@" + mem.getEffectiveName());
        }
        for (Emote emote : message.getEmotes()) {
            msg = msg.replaceAll(emote.getAsMention(), emote.getName());
        }

        return msg;
    }
}
