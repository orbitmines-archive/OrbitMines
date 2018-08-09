package com.orbitmines.bungeecord.commands.console;

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.Color;
import com.orbitmines.api.Donation;
import com.orbitmines.api.Rarity;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.cmd.ConsoleCommand;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.utils.ColorUtils;
import com.orbitmines.discordbot.utils.DiscordUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class CommandDonation extends ConsoleCommand {

    private final OrbitMinesBungee bungee;
    private final DiscordBot discord;

    public CommandDonation(OrbitMinesBungee bungee) {
        super("donation");

        this.bungee = bungee;
        this.discord = bungee.getDiscord();
    }

    /*
        /donation <id> {uuid} {price} {currency} {date} {time}
     */

    @Override
    public void execute(CommandSender sender, String[] a) {
        Donation donation = Donation.getById(Integer.parseInt(a[0]));
        String name = a[1];
        UUID uuid = UUID.fromString(a[2]);
        double price = Double.parseDouble(a[3]);
        String currency = a[4];
        String date = a[5].substring(0, 6) + "20" + a[5].substring(6, 8);
        String time = a[6] + ":00";
        boolean addToLoot = donation != Donation.DONATION && (donation.getRank() == null || CachedPlayer.getPlayer(uuid) == null || CachedPlayer.getPlayer(uuid).getVipRank().ordinal() < donation.getRank().ordinal()) && (a.length < 8 || Boolean.parseBoolean(a[7]));
        Donation lootDonation = a.length < 9 ? null : (a[8].equals("-1") ? null : Donation.getById(Integer.parseInt(a[8])));

        {
            String[] dates = date.split("/");
            String d = dates[2] + "-" + dates[1] + "-" + dates[0];

            Database.get().insert(Table.DONATIONS, uuid.toString(), donation.getId() + "", price + "", currency, d + " " + time);
        }

        if (donation == Donation.UNKNOWN)
            return;

        if (addToLoot) {
            Database.get().insert(Table.LOOT, uuid.toString(), "DONATION", Rarity.LEGENDARY.toString(), (lootDonation != null ? lootDonation.getId() : donation.getId()) + "", "&3&l&oDonation (" + date + ")");

            BungeePlayer omp = BungeePlayer.getPlayer(uuid);
            if (omp != null)
                omp.sendMessage("Shop", Color.LIME, "Thanks for donating! You can collect your items with §2/loot§7!");
        }

        TextChannel channel = discord.getChannel(bungee.getToken(), DiscordBot.ChannelType.donations);

        channel.sendMessage(DiscordUtils.getDisplay(discord, bungee.getToken(), uuid) + " has donated **" + String.format("%.2f", price) + " " + currency + "** to OrbitMines!").queue();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Donation by " + name);
        builder.setDescription(String.format("%.2f", price) + " " + currency);
        builder.setColor(ColorUtils.from(Color.TEAL));

        builder.addField("Item", ChatColor.stripColor(donation.getTitle()), true);
        builder.addField("Date", date + " " + time, true);

        builder.setThumbnail(donation.getIcon().getUrl());

        channel.sendMessage(builder.build()).queue();
    }
}
