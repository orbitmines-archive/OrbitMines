package com.orbitmines.bungeecord.events;

import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.chat.ComponentMessage;
import com.orbitmines.bungeecord.handlers.cmd.Command;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.handlers.DiscordSquad;
import com.orbitmines.discordbot.utils.BotToken;
import com.orbitmines.discordbot.utils.DiscordBungeeUtils;
import com.orbitmines.discordbot.utils.DiscordUtils;
import net.dv8tion.jda.core.entities.TextChannel;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

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
                    List<BungeePlayer> staff = BungeePlayer.getPlayersEligeble(StaffRank.MODERATOR);

                    bungee.toBungee(omp, new ComponentMessage.TempTextComponent(new Message(Message.FORMAT("Staff", Color.AQUA, "")), HoverEvent.Action.SHOW_TEXT, new Message("§bStaff Chat\n§7" + StaffRank.MODERATOR.toString() + "+ kunnen berichten versturen in deze channel door @<message> te typen.", "§bStaff Chat\n§7" + StaffRank.MODERATOR.toString() + "+ can send messages in this channel by typing @<message>.")).setChatColor(Color.AQUA.getMd5()), true, event.getMessage().substring(1), staff);
                } else {
                    omp.sendMessage("Staff", Color.RED, "§7Gebruik §9@<message>§7.", "§7Use §9@<message>§7.");
                    return;
                }

                toDiscord(event, omp);
            } else if (a[0].startsWith("!")) {
                /* Discord Channel message */
                event.setCancelled(true);

                DiscordSquad selected = DiscordSquad.getSelected(omp.getUUID());

                if (selected == null) {
                    omp.sendMessage("Discord", Color.RED, "Je moet een §9/discordsquad §7selecteren.", "You have to select a §9/discordsquad§7.");
                    return;
                }

                if (event.getMessage().length() != 1) {
                    bungee.toBungee(omp, new ComponentMessage.TempTextComponent(new Message(Message.FORMAT("@" + selected.getName(), selected.getColor(), "")), HoverEvent.Action.SHOW_TEXT, new Message(selected.getColor().getChatColor() + "@" + selected.getName() + " Chat\n§7Je kan berichten versturen in deze Discord Squad door !<message> te typen.", selected.getColor().getChatColor() + "@" + selected.getName() + " Chat\n§7You can send messages in this Discord Squad by typing !<message>.")).setChatColor(selected.getColor().getMd5()), true, event.getMessage().substring(1), selected.getPlayers());
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
