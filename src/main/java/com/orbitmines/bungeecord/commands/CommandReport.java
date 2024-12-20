package com.orbitmines.bungeecord.commands;

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.Color;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.cmd.Command;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.utils.BotToken;
import com.orbitmines.discordbot.utils.ColorUtils;
import com.orbitmines.discordbot.utils.DiscordUtils;
import com.orbitmines.discordbot.utils.SkinLibrary;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.md_5.bungee.api.event.ChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class CommandReport extends Command {

    private OrbitMinesBungee bungee;

    public CommandReport(OrbitMinesBungee bungee) {
        super(CommandLibrary.REPORT);

        this.bungee = bungee;
    }

    @Override
    public void dispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        if (a.length < 2) {
            getHelpMessage(omp).send(omp);
            return;
        }

        CachedPlayer player = CachedPlayer.getPlayer(a[1]);

        if (player == null) {
            omp.sendMessage("Report", Color.RED, a[1] + " is nog nooit op OrbitMines geweest.", a[1] + " has never been on OrbitMines before.");
            return;
        } else if (omp.getUUID().toString().equals(player.getUUID().toString())) {
            omp.sendMessage("Report", Color.RED, "Je kan jezelf niet reporten.", "You can't report yourself.");
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 2; i < a.length; i++) {
            if (i != 2)
                stringBuilder.append(" ");

            stringBuilder.append(a[i]);
        }
        String reason = stringBuilder.toString();

        String color = player.getRankPrefixColor().getChatColor();
        omp.sendMessage("Report", Color.LIME, "Je hebt " + color + player.getPlayerName() + " §7gereport voor §c" + reason + "§7.", "You have repored " + color + player.getPlayerName() + " §7for §c" + reason + "§7.");

        String server = omp.getServer().getName();
        String date = DateUtils.FORMAT.format(DateUtils.now());

        Database.get().insert(Table.REPORTS, player.getUUID().toString(), server, date, omp.getUUID().toString(), reason.replaceAll("'", "").replaceAll("`", ""));

        {
            TextChannel channel = bungee.getDiscord().getChannel(bungee.getToken(), DiscordBot.ChannelType.reports);

            DiscordBot discord = bungee.getDiscord();
            BotToken token = bungee.getToken();
            channel.sendMessage(DiscordUtils.getDisplay(discord, token, omp.getUUID()) + " has reported " + DiscordUtils.getDisplay(discord, token, player.getUUID()) + "!").queue();

            EmbedBuilder builder = new EmbedBuilder();
            builder.setAuthor("REPORT");
            builder.setDescription("");
            builder.setColor(ColorUtils.from(Color.RED));

            builder.addField("Player", player.getPlayerName(), true);
            builder.addField("Server", server, true);
            builder.addField("Date", date, true);
            builder.addField("Reported By", omp.getName(true), true);
            builder.addField("Reason", reason, true);

            builder.setThumbnail(SkinLibrary.getSkinUrl(SkinLibrary.Type.BODY_3D, player.getUUID()));

            channel.sendMessage(builder.build()).queue();
        }
    }
}
