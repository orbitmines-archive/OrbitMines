package com.orbitmines.api.utils;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.*;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.utils.BotToken;
import com.orbitmines.discordbot.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.md_5.bungee.api.ChatColor;

import java.util.UUID;

public class LootUtils {

    public static final String DONATION = "DONATION";
    public static final String BUYCRAFT_VOUCHER = "BUYCRAFT_VOUCHER";
    public static final String PRISMS = "PRISMS";
    public static final String SOLARS = "SOLARS";
    public static final String SURVIVAL_CREDITS = "SURVIVAL_CREDITS";
    public static final String STAFF_RANK = "STAFF_RANK";

    public static void insert(DiscordBot discord, BotToken token, UUID uuid, String loot, Server server, Rarity rarity, String description, int count) {
        Database.get().insert(Table.LOOT, uuid.toString(), loot, rarity.toString(), count + "", description);
        log(discord, token, uuid, loot, server, rarity, description, count, Action.INSERT);
    }

    public static void log(DiscordBot discord, BotToken token, UUID uuid, String loot, Server server, Rarity rarity, String description, int count, Action action) {
        CachedPlayer player = CachedPlayer.getPlayer(uuid);

        TextChannel channel = discord.getChannel(token, DiscordBot.ChannelType.loot_log);

        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(action.toString() + " " + loot + " for " + player.getPlayerName());
        builder.setDescription("UUID: " + player.getUUID().toString());
        builder.setColor(ColorUtils.from(action.color));

        builder.addField("Loot", loot + (server != null ? " // " + server.toString() : ""), true);
        builder.addField("Rarity", rarity.toString(), true);

        String id;
        if (loot.equals(DONATION)) {
            id = count + " // " + ChatColor.stripColor(Donation.getById(count).getTitle());
        } else if (loot.equals(STAFF_RANK)) {
            id = count + " // " + StaffRank.getById(count).toString();
        } else {
            id = count + "";
        }

        builder.addField("Id", id, true);
        builder.addField("Date", DateUtils.FORMAT.format(DateUtils.now()), true);
        builder.addField("Stripped Description", ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', description)), true);
        builder.addField("Full Description", description, true);

        builder.setThumbnail(DiscordBot.Images.iconFrom(server).getUrl());

        channel.sendMessage(builder.build()).queue();
    }

    public enum Action {

        INSERT(Color.LIME),
        UPDATE(Color.GREEN),
        DELETE(Color.RED);

        private final Color color;

        Action(Color color) {
            this.color = color;
        }
    }
}
