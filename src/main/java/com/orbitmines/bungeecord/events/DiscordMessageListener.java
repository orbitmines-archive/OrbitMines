package com.orbitmines.bungeecord.events;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.chat.ComponentMessage;
import com.orbitmines.discordbot.DiscordBot;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;

import java.util.ArrayList;
import java.util.List;

public class DiscordMessageListener extends ListenerAdapter {

    private final OrbitMinesBungee bungee;

    public DiscordMessageListener(OrbitMinesBungee bungee) {
        this.bungee = bungee;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Member member = event.getMember();
        MessageChannel channel = event.getChannel();
        Message message = event.getMessage();

        if (channel.getName().equals(DiscordBot.ChannelType.staff.toString()) && !isBot(member))
            fromDiscordStaff(member, message);
    }

    private boolean isBot(Member member) {
        for (Role role : member.getRoles()) {
            if (role.getName().equalsIgnoreCase("bots"))
                return true;
        }
        return false;
    }

    public void fromDiscordStaff(Member member, net.dv8tion.jda.core.entities.Message message) {
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
        //TODO DISCORD LINK GRAB CACHEDPLAYER

        ComponentMessage cM = new ComponentMessage();

        String rankPrefix = staffRank != StaffRank.NONE ? staffRank.getPrefix(staffRank.getPrefixColor()) : vipRank.getPrefix(vipRank.getPrefixColor());
        Color chatColor = staffRank != StaffRank.NONE ? staffRank.getChatColor() : vipRank.getChatColor();

        cM.add(new com.orbitmines.api.Message(com.orbitmines.api.Message.FORMAT("Staff", Color.AQUA, "")));

        String name = rankPrefix + "@" + member.getEffectiveName();
        cM.add(new com.orbitmines.api.Message(name), HoverEvent.Action.SHOW_TEXT, new com.orbitmines.api.Message(rankPrefix + "@" + member.getUser().getName() + "#" + member.getUser().getDiscriminator()));

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

        cM.add(new com.orbitmines.api.Message(" §7» " + chatColor.getChatColor() + "§l" + msg));

        if (message.getAttachments().size() != 0) {
            net.dv8tion.jda.core.entities.Message.Attachment attachment = message.getAttachments().get(0);
            cM.add(new com.orbitmines.api.Message(" (File: §9§l" + attachment.getFileName() + "§r" + chatColor.getChatColor() + ")"), ClickEvent.Action.OPEN_URL, new com.orbitmines.api.Message(attachment.getUrl()), HoverEvent.Action.SHOW_TEXT, new com.orbitmines.api.Message("§7Klik hier om §9§l" + attachment.getFileName() + "§7 te openen.", "§7Click here to open §9§l" + attachment.getFileName() + "§7."));
        }

        List<BungeePlayer> players = new ArrayList<>();
        for (BungeePlayer player : BungeePlayer.getPlayers()) {
            if (player.isEligible(StaffRank.MODERATOR))
                players.add(player);
        }

        cM.send(players);
    }
}
