package com.orbitmines.discordbot.commands;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Language;
import com.orbitmines.api.Server;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.TablePlayers;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.api.utils.TimeUtils;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.handlers.Command;
import com.orbitmines.discordbot.utils.BotToken;
import com.orbitmines.discordbot.utils.ColorUtils;
import com.orbitmines.discordbot.utils.SkinLibrary;
import com.orbitmines.api.CachedPlayer;
import com.orbitmines.spigot.api.handlers.data.PlayTimeData;
import com.orbitmines.spigot.api.handlers.data.VoteData;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.concurrent.TimeUnit;

public class CommandStats extends Command {

    private String[] alias = { "stats" };

    private final DiscordBot bot;

    public CommandStats(DiscordBot bot) {
        super(BotToken.DEFAULT, "Display General Stats for a specific Player.");

        this.bot = bot;
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp() {
        return "<player>";
    }

    @Override
    public boolean isBungeeCommand() {
        return true;
    }

    @Override
    public void dispatch(MessageReceivedEvent event, User user, MessageChannel channel, Message msg, String[] a) {
        if (a.length != 2) {
            channel.sendMessage(user.getAsMention() + " Use " + Command.PREFIX + alias[0] + " " + getHelp()).queue();
            return;
        }

        CachedPlayer player = CachedPlayer.getPlayer(a[1]);

        if (player == null) {
            channel.sendMessage(user.getAsMention() + " Player cannot be found in the OrbitMines database.").queue();
            return;
        }

        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(player.getPlayerName());
//        builder.setTitle("Title");
        builder.setDescription("Global Stats");
//        builder.setFooter("Footer", "");

//        builder.addBlankField(false);

        VipRank vipRank = player.getVipRank();
        StaffRank staffRank = player.getStaffRank();

        builder.setColor(ColorUtils.from(staffRank != StaffRank.NONE ? staffRank.getPrefixColor() : vipRank.getPrefixColor()));

        builder.addField("Rank", staffRank == StaffRank.NONE ? vipRank.getName() : (vipRank == VipRank.NONE ? staffRank.getName() : (staffRank.getName() + " / " + vipRank.getName())), false);

        //TODO CHECK SETTINGS

        {
            PlayTimeData data = new PlayTimeData(player.getUUID());
            data.load();

            long totalTimePlayed = 0;
            for (Server server : Server.values()) {
                totalTimePlayed += data.getPlayTime().get(server);
            }

            builder.addField("Time Played", TimeUtils.limitTimeUnitBy(totalTimePlayed * 1000, TimeUnit.HOURS, Language.ENGLISH), true);
        }

        builder.addField("Member Since", DateUtils.FORMAT.format(DateUtils.parse(DateUtils.FORMAT, player.getFirstLogin())), true);

        {
            VoteData data = new VoteData(player.getUUID());
            data.load();

            builder.addField("Votes in " + DateUtils.getMonth() + " " + DateUtils.getYear(), data.getVotes() + "", true);
            builder.addField("Total Votes", NumberUtils.locale(data.getTotalVotes()), true);
        }

        builder.addField("Prisms", NumberUtils.locale(Database.get().getInt(Table.PLAYERS, TablePlayers.PRISMS, new Where(TablePlayers.UUID, player.getUUID().toString()))), true);
        builder.addField("Solars", NumberUtils.locale(Database.get().getInt(Table.PLAYERS, TablePlayers.SOLARS, new Where(TablePlayers.UUID, player.getUUID().toString()))), true);

        builder.setThumbnail(SkinLibrary.getSkinUrl(SkinLibrary.Type.BODY_3D, player.getUUID()));

        channel.sendMessage(builder.build()).queue();
    }
}
