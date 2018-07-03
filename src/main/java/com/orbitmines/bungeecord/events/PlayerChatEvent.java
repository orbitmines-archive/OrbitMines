package com.orbitmines.bungeecord.events;

import com.orbitmines.api.*;
import com.orbitmines.api.Message;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.cmd.Command;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.utils.SkinLibrary;
import net.dv8tion.jda.core.entities.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class PlayerChatEvent implements Listener {

    private OrbitMinesBungee bungee;

    public PlayerChatEvent() {
        bungee = OrbitMinesBungee.getBungee();
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        String[] a = event.getMessage().split(" ");

        if (!(event.getSender() instanceof ProxiedPlayer))
            return;

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        BungeePlayer omp = BungeePlayer.getPlayer(player);

        if (!a[0].startsWith("/")) {
            /* Message is not a command */

            if (a[0].startsWith("@") && omp.isEligible(StaffRank.MODERATOR)) {
                /* Staff Message */
                event.setCancelled(true);

                if (!omp.isLoggedIn()) {
                    omp.sendMessage(Message.ENTER_2FA);
                    return;
                }

                if (event.getMessage().length() != 1) {
                    bungee.broadcast(StaffRank.MODERATOR, "Staff", Color.AQUA, omp.getStaffRank().getPrefix() + omp.getName() + " §7» §f§l" + event.getMessage().substring(1));
                } else {
                    omp.sendMessage("Staff", Color.RED, "§7Use §9@<message>§7.");
                }

                toDiscord(event, omp);
            }
        } else if (!omp.isLoggedIn()) {
            event.setCancelled(true);
            omp.sendMessage(Message.ENTER_2FA);
        } else {
            /* Message is a command */
            Command command = Command.getCommand(a[0]);

            if (command == null)
                return;

            event.setCancelled(true);
            command.dispatch(event, omp, a);

            bungee.getDiscord().getChannel(bungee.getToken(), DiscordBot.ChannelType.command_log).sendMessage(SkinLibrary.getEmote(bungee.getDiscord().getGuild(bungee.getToken()), omp.getUUID()).getAsMention() + getDiscordRankPrefix(omp.getUUID()) + " **" + omp.getName(true) + "** executed command **" + event.getMessage() + "**.").queue();
        }
    }

    private String getDiscordRankPrefix(UUID uuid) {
        CachedPlayer player = CachedPlayer.getPlayer(uuid);

        if (player == null)
            return "";

        StaffRank staffRank = player.getStaffRank();
        VipRank vipRank = player.getVipRank();

        if (staffRank == StaffRank.NONE)
            return vipRank != VipRank.NONE ? " " + bungee.getDiscord().getEmote(bungee.getToken(), vipRank).getAsMention() + "**" + player.getRankName() + "**" : "";

        return " **" + player.getRankName() + "**";
    }

    private void toDiscord(ChatEvent event, BungeePlayer omp) {
        Guild guild = bungee.getDiscord().getGuild(bungee.getToken());

        CharSequence text = SkinLibrary.getEmote(guild, omp.getUUID()).getAsMention() + getDiscordRankPrefix(omp.getUUID()) + " **" + omp.getName(true) + "** » ";

        String message = event.getMessage().substring(1);/* Remove @ */

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

        bungee.getDiscord().getChannel(bungee.getToken(), DiscordBot.ChannelType.staff).sendMessage(text + message).queue();
    }
}
