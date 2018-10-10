package com.orbitmines.bungeecord.commands;

import com.orbitmines.api.Color;
import com.orbitmines.api.PluginMessage;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.cmd.Command;
import net.md_5.bungee.api.event.ChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class CommandDiscordLink extends Command {

    private OrbitMinesBungee bungee;

    public CommandDiscordLink(OrbitMinesBungee bungee) {
        super(CommandLibrary.DISCORDLINK);

        this.bungee = bungee;
    }

    @Override
    public void dispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        if (a.length == 1) {
            getHelpMessage(omp).send(omp);
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < a.length; i++) {
            if (i != 1)
                stringBuilder.append(" ");

            stringBuilder.append(a[i]);
        }
        String name = stringBuilder.toString();

        if (!name.contains("#")) {
            getHelpMessage(omp).send(omp);
            return;
        }

        String[] parts = name.split("#");

        switch (bungee.getDiscord().minecraftLink(omp.getUUID(), parts[0], parts[1])) {

            case INVALID_USER:
                omp.sendMessage("Discord", Color.RED, "Ongeldige Discord User.", "Invalid Discord User.");
                getHelpMessage(omp).send(omp);
                break;
            case SAME_USER:
                omp.sendMessage("Discord", Color.RED, "Die Discord User is al gelinkt aan je OrbitMines account.", "That Discord User is already linked to your OrbitMines account.");
                break;
            case SETTING_UP:
                omp.sendMessage("Discord", Color.RED, "Discord Link aan het opzetten... Gebruik nu §9!discordlink " + omp.getName(true) + " §7in Discord.", "Setting up Discord Link... Use §9!discordlink " + omp.getName(true) + " §7in Discord.");
                break;
            case WRONG_USER:
                omp.sendMessage("Discord", Color.RED, "Je hebt een andere Minecraft naam op Discord ingevoerd, voer de command daar nog een keer met jouw naam uit.", "You have used another Minecraft name on Discord, execute the Discord Link command in Discord again with your name.");
                break;
            case SETUP_COMPLETE:
                omp.sendMessage("Discord", Color.LIME, "Je hebt je Discord en OrbitMines account gelinkt!", "You have successfully linked your Discord and OrbitMines account!");
                bungee.getMessageHandler().dataTransfer(PluginMessage.CHECK_DISCORD_LINK_ACHIEVEMENT, omp.getPlayer(), omp.getUUID().toString());
                break;
        }
    }
}
