package com.orbitmines.spigot.api.events;

import com.orbitmines.api.Message;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.utils.BotToken;
import com.orbitmines.discordbot.utils.DiscordSpigotUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class CommandPreprocessEvent implements Listener {

    private final OrbitMines orbitMines;

    public CommandPreprocessEvent(OrbitMines orbitMines) {
        this.orbitMines = orbitMines;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();
        OMPlayer omp = OMPlayer.getPlayer(p);

        String[] a = event.getMessage().split(" ");
        Command command = Command.getCommand(a[0]);


        if (command == null) {
            if (omp.isOpMode())
                return;

            event.setCancelled(true);
            omp.sendMessage(Message.UNKNOWN_COMMAND);
            return;
        }

        event.setCancelled(true);

        command.dispatch(omp, a);

        DiscordBot discord = orbitMines.getServerHandler().getDiscord();
        BotToken token = orbitMines.getServerHandler().getToken();

        discord.getChannel(token, DiscordBot.ChannelType.command_log).sendMessage(DiscordSpigotUtils.getDisplay(discord, token, omp) + " executed command **" + event.getMessage() + "**.").queue();
    }
}
