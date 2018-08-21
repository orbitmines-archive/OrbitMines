package com.orbitmines.bungeecord.events;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.Color;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.chat.ComponentMessage;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.handlers.Command;
import com.orbitmines.discordbot.handlers.DiscordGroup;
import com.orbitmines.discordbot.utils.DiscordUtils;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DiscordMessageListener extends ListenerAdapter {

    private final OrbitMinesBungee bungee;

    public DiscordMessageListener(OrbitMinesBungee bungee) {
        this.bungee = bungee;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!bungee.getDiscord().getServerId().equals(event.getGuild().getId()))
            return;

        Member member = event.getMember();
        MessageChannel channel = event.getChannel();
        Message message = event.getMessage();

        /* Don't send Discord commands in Minecraft chat. */
        if (Command.getCommandFromName(message.getContentRaw().split(" ")[0]) != null)
            return;

        if (isBot(member))
            return;

        if (channel.getName().equals(DiscordBot.ChannelType.staff.toString())) {
            fromDiscordStaff(member, message);
        } else {
            DiscordGroup group = DiscordGroup.getGroup(event.getTextChannel());

            if (group != null)
                fromDiscordGroup(group, member, message);
        }
    }

    private boolean isBot(Member member) {
        for (Role role : member.getRoles()) {
            if (role.getName().equalsIgnoreCase("bots"))
                return true;
        }
        return false;
    }

    public void fromDiscordStaff(Member member, net.dv8tion.jda.core.entities.Message message) {
        List<BungeePlayer> players = new ArrayList<>();
        for (BungeePlayer player : BungeePlayer.getPlayers()) {
            if (player.isEligible(StaffRank.MODERATOR))
                players.add(player);
        }

        if (players.size() == 0)
            return;

        StaffRank staffRank = StaffRank.NONE;
        VipRank vipRank = VipRank.NONE;

        for (Role role : member.getRoles()) {
            try {
                staffRank = StaffRank.valueOf(role.getName().toUpperCase());
            } catch (IllegalArgumentException ex) {
                try {
                    vipRank = VipRank.valueOf(role.getName().toUpperCase());
                } catch (IllegalArgumentException ex1) { }
            }
        }
        ComponentMessage cM = new ComponentMessage();

        String rankPrefix = (staffRank != StaffRank.NONE && staffRank != StaffRank.ADMIN) ? staffRank.getPrefix(staffRank.getPrefixColor()) : vipRank.getPrefix(vipRank.getPrefixColor());
        Color chatColor = (staffRank != StaffRank.NONE && staffRank != StaffRank.ADMIN) ? staffRank.getChatColor() : vipRank.getChatColor();

        cM.add(new com.orbitmines.api.Message(com.orbitmines.api.Message.FORMAT("Staff", Color.AQUA, "")));

        String name = rankPrefix + "@" + member.getEffectiveName();

        UUID uuid = bungee.getDiscord().getLinkedUUID(member.getUser());
        String playerName = null;

        if (uuid != null) {
            CachedPlayer player = CachedPlayer.getPlayer(uuid);
            playerName = player.getRankPrefixColor().getChatColor() + player.getPlayerName();
        }

        cM.add(new com.orbitmines.api.Message(name), HoverEvent.Action.SHOW_TEXT, new com.orbitmines.api.Message(rankPrefix + "@" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + "\n§7IGN: " + (playerName == null ? StaffRank.NONE.getDisplayName() : playerName)));

        cM.add(new com.orbitmines.api.Message(" §7» " + DiscordUtils.filterFromDiscord(chatColor.getChatColor() + "§l", message)));

        if (message.getAttachments().size() != 0) {
            net.dv8tion.jda.core.entities.Message.Attachment attachment = message.getAttachments().get(0);
            cM.add(new com.orbitmines.api.Message(" (File: §9§l" + attachment.getFileName() + "§r" + chatColor.getChatColor() + ")"), ClickEvent.Action.OPEN_URL, new com.orbitmines.api.Message(attachment.getUrl()), HoverEvent.Action.SHOW_TEXT, new com.orbitmines.api.Message("§7Klik hier om §9§l" + attachment.getFileName() + "§7 te openen.", "§7Click here to open §9§l" + attachment.getFileName() + "§7."));
        }

        cM.send(players);
    }

    public void fromDiscordGroup(DiscordGroup group, Member member, net.dv8tion.jda.core.entities.Message message) {
        List<BungeePlayer> online = group.getPlayers();

        if (online.size() == 0)
            return;

        StaffRank staffRank = StaffRank.NONE;
        VipRank vipRank = VipRank.NONE;

        for (Role role : member.getRoles()) {
            try {
                staffRank = StaffRank.valueOf(role.getName().toUpperCase());
            } catch (IllegalArgumentException ex) {
                try {
                    vipRank = VipRank.valueOf(role.getName().toUpperCase());
                } catch (IllegalArgumentException ex1) { }
            }
        }
        ComponentMessage cM = new ComponentMessage();

        String rankPrefix = (staffRank != StaffRank.NONE && staffRank != StaffRank.ADMIN) ? staffRank.getPrefix(staffRank.getPrefixColor()) : vipRank.getPrefix(vipRank.getPrefixColor());
        Color chatColor = (staffRank != StaffRank.NONE && staffRank != StaffRank.ADMIN) ? staffRank.getChatColor() : vipRank.getChatColor();

        cM.add(new com.orbitmines.api.Message(com.orbitmines.api.Message.FORMAT(group.getName(), group.getColor(), "")));

        String name = rankPrefix + "@" + member.getEffectiveName();

        UUID uuid = bungee.getDiscord().getLinkedUUID(member.getUser());
        String playerName = null;

        if (uuid != null) {
            CachedPlayer player = CachedPlayer.getPlayer(uuid);
            playerName = player.getRankPrefixColor().getChatColor() + player.getPlayerName();
        }

        cM.add(new com.orbitmines.api.Message(name), HoverEvent.Action.SHOW_TEXT, new com.orbitmines.api.Message(rankPrefix + "@" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + "\n§7IGN: " + (playerName == null ? StaffRank.NONE.getDisplayName() : playerName)));

        cM.add(new com.orbitmines.api.Message(" §7» " + DiscordUtils.filterFromDiscord(chatColor.getChatColor() + "§l", message)));

        if (message.getAttachments().size() != 0) {
            net.dv8tion.jda.core.entities.Message.Attachment attachment = message.getAttachments().get(0);
            cM.add(new com.orbitmines.api.Message(" (File: §9§l" + attachment.getFileName() + "§r" + chatColor.getChatColor() + ")"), ClickEvent.Action.OPEN_URL, new com.orbitmines.api.Message(attachment.getUrl()), HoverEvent.Action.SHOW_TEXT, new com.orbitmines.api.Message("§7Klik hier om §9§l" + attachment.getFileName() + "§7 te openen.", "§7Click here to open §9§l" + attachment.getFileName() + "§7."));
        }

        cM.send(online);
    }
}
