package com.orbitmines.discordbot.utils;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.Color;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.chat.ComponentMessage;
import com.orbitmines.discordbot.DiscordBot;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;

import java.util.ArrayList;
import java.util.List;

public class DiscordBungeeUtils {

    public static String getDisplay(DiscordBot discord, BotToken token, BungeePlayer omp) {
        return getDisplay(discord, token, omp, false);
    }

    public static String getDisplay(DiscordBot discord, BotToken token, BungeePlayer omp, boolean withServerIcon) {
        Guild guild = discord.getGuild(token);

        String prefix;

        if (omp.getStaffRank() == StaffRank.NONE)
            prefix = omp.getVipRank() != VipRank.NONE ? " " + discord.getEmote(token, omp.getVipRank()).getAsMention() + "**" + omp.getRankName() + "**" : "";
        else
            prefix = " **" + omp.getRankName() + "**";

        return (withServerIcon ? discord.getEmote(token, DiscordBot.CustomEmote.from(omp.getServer())).getAsMention() : "") + SkinLibrary.getEmote(guild, omp.getUUID()).getAsMention() + prefix + " **" + omp.getName(true) + "**";
    }

    public static List<ComponentMessage.TempTextComponent> formatMessage(CachedPlayer sender, BungeePlayer receiver, Color color, boolean bold, String message) {
        boolean mentioned = message.toLowerCase().contains(receiver.getName(true).toLowerCase());

        Color chat = mentioned ? Color.ORANGE : color;
        Color cmd = Color.BLUE;

        List<ComponentMessage.TempTextComponent> tcs = new ArrayList<>();

        /* This fixes the issue of color disappearing on the second line */
        String[] part = message.split(" ");

        loop:
        for (int i = 0; i < part.length; i++) {
            if (i != 0)
                tcs.add(new ComponentMessage.TempTextComponent(" "));

            String string = part[i];

//              Too heavy?
//            /* Player Mention */
//            OMPlayer mentionedPlayer = OMPlayer.getPlayer(string);
//            if (mentionedPlayer != null) {
//                tcs.add(getPlayerMention(mentionedPlayer, mentionedPlayer.getName() + space).setBold(bold));
//                continue;
//            }

            /* Command Mentions */
            if (string.contains("/")) {
                for (CommandLibrary command : CommandLibrary.getLibraryAll(receiver.getServer())) {
                    if (command.getStaffRank() != null && !receiver.isEligible(command.getStaffRank()))
                        continue;

                    for (String alias : command.getAlias()) {
                        if (!string.contains(alias))
                            continue;

                        int index = string.indexOf(alias);
                        tcs.add(new ComponentMessage.TempTextComponent(chat.getChatColor() + string.substring(0, index)).setChatColor(chat.getMd5()).setBold(bold));
                        tcs.add(getCommandMention(receiver, command, alias).setChatColor(cmd.getMd5()).setBold(bold));
                        tcs.add(new ComponentMessage.TempTextComponent(string.substring(index + alias.length())).setChatColor(chat.getMd5()).setBold(bold));

                        continue loop;
                    }
                }
            }

            tcs.add(new ComponentMessage.TempTextComponent(string).setChatColor(chat.getMd5()).setBold(bold));
        }

        return tcs;
    }

    public static ComponentMessage.TempTextComponent getPlayerMention(BungeePlayer omp, String text) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(omp.getRankPrefixColor().getChatColor()).append(omp.getName(true));
        stringBuilder.append("\n§7Rank: ");
        if (omp.getStaffRank() != StaffRank.NONE) {
            stringBuilder.append(omp.getStaffRank().getDisplayName());

            if (omp.getVipRank() != VipRank.NONE)
                stringBuilder.append(" §7/ ").append(omp.getVipRank().getDisplayName());
        } else {
            stringBuilder.append(omp.getVipRank().getDisplayName());
        }

        OrbitMinesBungee bungee = OrbitMinesBungee.getBungee();
        User user = bungee.getDiscord().getLinkedUser(bungee.getToken(), omp.getUUID());
        stringBuilder.append("\n§7Discord: ").append((user != null ? omp.getRankPrefixColor().getChatColor() + user.getName() + "#" + user.getDiscriminator() : StaffRank.NONE.getDisplayName()));

//        if (omp.hasNickname())
//            stringBuilder.append("\n§7Nickname: ").append(omp.getName());

        stringBuilder.append("\n\n§a").append(omp.lang("Klik hier om een pm te sturen.", "Click here to send a pm."));

        return new ComponentMessage.TempTextComponent(text, ClickEvent.Action.SUGGEST_COMMAND, "/msg " + omp.getName(true) + " ", HoverEvent.Action.SHOW_TEXT, stringBuilder.toString());
    }

    public static ComponentMessage.TempTextComponent getCommandMention(BungeePlayer omp, CommandLibrary command, String text) {
        String firstCmd = command.getAlias()[0];
        String help = command.getArgsHelp(omp.getStaffRank(), omp.getVipRank());

        if (help == null)
            help = "";
        else
            help = " §7" + help;

        StringBuilder aliases = new StringBuilder();
        if (command.getVipRank() != null)
            aliases.append("\n§7Required: ").append(command.getVipRank().getDisplayName());
        else if (command.getStaffRank() != null)
            aliases.append("\n§7Required: ").append(command.getStaffRank().getDisplayName());

        if (command.getAlias().length > 1) {
            aliases.append("\n§7Alias: ");

            for (int i = 1; i < command.getAlias().length; i++) {
                if (i != 1)
                    aliases.append("§7, ");

                aliases.append("§9").append(command.getAlias()[i]);
            }
        }

        aliases.append("\n\n§7§o").append(command.getDescription(omp.getStaffRank(), omp.getVipRank()));

        return new ComponentMessage.TempTextComponent(text, ClickEvent.Action.SUGGEST_COMMAND, firstCmd + " ", HoverEvent.Action.SHOW_TEXT, "§9" + firstCmd + help + aliases.toString());
    }
}
