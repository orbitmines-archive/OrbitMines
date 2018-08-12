package com.orbitmines.bungeecord.events;

import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.cmd.Command;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.handlers.DiscordGroup;
import com.orbitmines.discordbot.utils.BotToken;
import com.orbitmines.discordbot.utils.DiscordBungeeUtils;
import com.orbitmines.discordbot.utils.DiscordUtils;
import net.dv8tion.jda.core.entities.TextChannel;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

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
                    omp.sendMessage("Staff", Color.RED, "§7Gebruik §9@<message>§7.", "§7Use §9@<message>§7.");
                    return;
                }

                toDiscord(event, omp);
            } else if (a[0].startsWith("!")) {
                /* Discord Channel message */
                event.setCancelled(true);

                DiscordGroup selected = DiscordGroup.getSelected(omp.getUUID());

                if (selected == null) {
                    omp.sendMessage("Discord", Color.RED, "Je moet een §9/discordserver §7selecteren.", "You have to select a §9/discordserver§7.");
                    return;
                }

                if (event.getMessage().length() != 1) {
                    selected.sendMessage("@" + selected.getName(), selected.getColor(), omp.getRankPrefix() + omp.getName() + " §7» §f§l" + event.getMessage().substring(1));
                } else {
                    omp.sendMessage("@" + selected.getName(), selected.getColor(), "§7Gebruik §9!<message>§7.", "§7Use §9!<message>§7.");
                    return;
                }

                toDiscord(selected.getTextChannel(), event, omp);
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

            bungee.getDiscord().getChannel(bungee.getToken(), DiscordBot.ChannelType.command_log).sendMessage(DiscordBungeeUtils.getDisplay(bungee.getDiscord(), bungee.getToken(), omp) + " executed command **" + event.getMessage() + "**.").queue();
        }
    }

    private void toDiscord(ChatEvent event, BungeePlayer omp) {
        DiscordBot discord = bungee.getDiscord();
        BotToken token = bungee.getToken();
        toDiscord(discord.getChannel(token, DiscordBot.ChannelType.staff), event, omp);
    }

    private void toDiscord(TextChannel channel, ChatEvent event, BungeePlayer omp) {
        DiscordBot discord = bungee.getDiscord();
        BotToken token = bungee.getToken();
        channel.sendMessage(DiscordBungeeUtils.getDisplay(discord, token, omp, true) + " » " + DiscordUtils.filterToDiscord(discord, token, event.getMessage().substring(1))).queue();
    }
}
