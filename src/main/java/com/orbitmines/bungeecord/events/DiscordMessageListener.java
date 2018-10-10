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
import com.orbitmines.discordbot.handlers.DiscordSquad;
import com.orbitmines.discordbot.utils.DiscordBungeeUtils;
import com.orbitmines.discordbot.utils.DiscordUtils;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.md_5.bungee.api.ChatColor;
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
            DiscordSquad group = DiscordSquad.getGroup(event.getTextChannel());

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

        String rankPrefix = staffRank != StaffRank.NONE ? staffRank.getPrefix(staffRank.getPrefixColor()) : vipRank.getPrefix(vipRank.getPrefixColor());
        Color chatColor = staffRank != StaffRank.NONE ? staffRank.getChatColor() : vipRank.getChatColor();

        cM.add(new ComponentMessage.TempTextComponent(new com.orbitmines.api.Message(com.orbitmines.api.Message.FORMAT("Staff", Color.AQUA, "")), HoverEvent.Action.SHOW_TEXT, new com.orbitmines.api.Message("§bStaff Chat\n§7" + StaffRank.MODERATOR.toString() + "+ kunnen berichten versturen in deze channel door @<message> te typen.", "§bStaff Chat\n§7" + StaffRank.MODERATOR.toString() + "+ can send messages in this channel by typing @<message>.")).setChatColor(Color.AQUA.getMd5()));

        String name = rankPrefix + "@" + member.getEffectiveName();

        UUID uuid = bungee.getDiscord().getLinkedUUID(member.getUser());
        CachedPlayer sender = null;
        String playerName = null;

        if (uuid != null) {
            sender = CachedPlayer.getPlayer(uuid);
            playerName = sender.getRankPrefixColor().getChatColor() + sender.getPlayerName();
        }

        cM.add(new com.orbitmines.api.Message(name), HoverEvent.Action.SHOW_TEXT, new com.orbitmines.api.Message(rankPrefix + "@" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + "\n§7IGN: " + (playerName == null ? StaffRank.NONE.getDisplayName() : playerName)));

        cM.add(new com.orbitmines.api.Message(" §7» "));

        /* Filter discord mentions */
        String filtered = DiscordUtils.filterFromDiscord(chatColor.getChatColor() + "§l", message);

        /* File Attachment */
        ComponentMessage.TempTextComponent attachmentComponent = null;
        if (message.getAttachments().size() != 0) {
            net.dv8tion.jda.core.entities.Message.Attachment attachment = message.getAttachments().get(0);
            attachmentComponent = new ComponentMessage.TempTextComponent(new com.orbitmines.api.Message((filtered.equals("") ? "" : " ") + "(File: §l" + attachment.getFileName() + "§r§3)"), ClickEvent.Action.OPEN_URL, new com.orbitmines.api.Message(attachment.getUrl()), HoverEvent.Action.SHOW_TEXT, new com.orbitmines.api.Message("§7Klik hier om §3§l" + attachment.getFileName() + "§7 te openen.", "§7Click here to open §3§l" + attachment.getFileName() + "§7.")).setChatColor(ChatColor.DARK_AQUA);
        }

        /* Send per player for commands&mentions */
        for (BungeePlayer player : players) {
            ComponentMessage componentMessage = new ComponentMessage(cM);

            for (ComponentMessage.TempTextComponent component : DiscordBungeeUtils.formatMessage(sender, player, chatColor, true, filtered)) {
                componentMessage.add(component);
            }

            if (attachmentComponent != null)
                componentMessage.add(attachmentComponent);

            componentMessage.send(player);
        }
    }

    public void fromDiscordGroup(DiscordSquad group, Member member, net.dv8tion.jda.core.entities.Message message) {
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

        String rankPrefix = staffRank != StaffRank.NONE ? staffRank.getPrefix(staffRank.getPrefixColor()) : vipRank.getPrefix(vipRank.getPrefixColor());
        Color chatColor = staffRank != StaffRank.NONE ? staffRank.getChatColor() : vipRank.getChatColor();

        cM.add(new ComponentMessage.TempTextComponent(new com.orbitmines.api.Message(com.orbitmines.api.Message.FORMAT("@" + group.getName(), group.getColor(), "")), HoverEvent.Action.SHOW_TEXT, new com.orbitmines.api.Message(group.getColor().getChatColor() + "@" + group.getName() + " Chat\n§7Je kan berichten versturen in deze Discord Squad door !<message> te typen.", group.getColor().getChatColor() + "@" + group.getName() + " Chat\n§7You can send messages in this Discord Squad by typing !<message>.")).setChatColor(group.getColor().getMd5()));

        String name = rankPrefix + "@" + member.getEffectiveName();

        UUID uuid = bungee.getDiscord().getLinkedUUID(member.getUser());
        CachedPlayer sender = null;
        String playerName = null;

        if (uuid != null) {
            sender = CachedPlayer.getPlayer(uuid);
            playerName = sender.getRankPrefixColor().getChatColor() + sender.getPlayerName();
        }

        cM.add(new com.orbitmines.api.Message(name), HoverEvent.Action.SHOW_TEXT, new com.orbitmines.api.Message(rankPrefix + "@" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + "\n§7IGN: " + (playerName == null ? StaffRank.NONE.getDisplayName() : playerName)));

        cM.add(new com.orbitmines.api.Message(" §7» "));

        /* Filter discord mentions */
        String filtered = DiscordUtils.filterFromDiscord(chatColor.getChatColor() + "§l", message);

        /* File Attachment */
        ComponentMessage.TempTextComponent attachmentComponent = null;
        if (message.getAttachments().size() != 0) {
            net.dv8tion.jda.core.entities.Message.Attachment attachment = message.getAttachments().get(0);
            attachmentComponent = new ComponentMessage.TempTextComponent(new com.orbitmines.api.Message((filtered.equals("") ? "" : " ") + "(File: §l" + attachment.getFileName() + "§r§3)"), ClickEvent.Action.OPEN_URL, new com.orbitmines.api.Message(attachment.getUrl()), HoverEvent.Action.SHOW_TEXT, new com.orbitmines.api.Message("§7Klik hier om §3§l" + attachment.getFileName() + "§7 te openen.", "§7Click here to open §3§l" + attachment.getFileName() + "§7.")).setChatColor(ChatColor.DARK_AQUA);
        }

        /* Send per player for commands&mentions */
        for (BungeePlayer player : online) {
            ComponentMessage componentMessage = new ComponentMessage(cM);

            for (ComponentMessage.TempTextComponent component : DiscordBungeeUtils.formatMessage(sender, player, chatColor, true, filtered)) {
                componentMessage.add(component);
            }

            if (attachmentComponent != null)
                componentMessage.add(attachmentComponent);

            componentMessage.send(player);
        }
    }
}
