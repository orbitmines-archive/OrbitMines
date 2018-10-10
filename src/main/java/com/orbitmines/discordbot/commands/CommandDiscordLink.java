package com.orbitmines.discordbot.commands;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.PluginMessage;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.handlers.Command;
import com.orbitmines.discordbot.utils.BotToken;
import com.orbitmines.discordbot.utils.DiscordUtils;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandDiscordLink extends Command {

    private String[] alias = { "discordlink" };

    private final DiscordBot bot;

    public CommandDiscordLink(DiscordBot bot) {
        super(BotToken.DEFAULT, "Link your Discord User to your OrbitMines account.");

        this.bot = bot;
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp() {
        return "<minecraft name>";
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

        switch (bot.discordLink(user, a[1])) {

            case INVALID_USER:
                channel.sendMessage(user.getAsMention() + " That player has never been on OrbitMines before!").queue();
                break;
            case SAME_USER:
                channel.sendMessage(user.getAsMention() + " That player is already to with your Discord account!").queue();
                break;
            case SETTING_UP:
                channel.sendMessage(user.getAsMention() + " Setting up Discord Link... Use **/discordlink " + user.getName() + "#" + user.getDiscriminator() + "** on OrbitMines.").queue();
                break;
            case WRONG_USER:
                channel.sendMessage(user.getAsMention() + " You have used another Discord name on OrbitMines, execute the Discord Link command on OrbitMines again with your name.").queue();
                break;
            case SETUP_COMPLETE:
                CachedPlayer player = CachedPlayer.getPlayer(a[1]);

                channel.sendMessage(user.getAsMention() + " You have successfully linked " + DiscordUtils.getDisplay(bot, BotToken.DEFAULT, player.getUUID()) + " to your account.").queue();

                BungeePlayer omp = BungeePlayer.getPlayer(player.getUUID());
                if (omp != null)
                    OrbitMinesBungee.getBungee().getMessageHandler().dataTransfer(PluginMessage.CHECK_DISCORD_LINK_ACHIEVEMENT, omp.getPlayer(), omp.getUUID().toString());

                break;
        }
    }
}
