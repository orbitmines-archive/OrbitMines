package com.orbitmines.bungeecord.commands;

import com.orbitmines.api.Color;
import com.orbitmines.api.PluginMessage;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.cmd.Command;
import net.md_5.bungee.api.event.ChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class CommandDiscordLink extends Command {

    private String[] alias = { "/discordlink" };

    private OrbitMinesBungee bungee;

    public CommandDiscordLink(OrbitMinesBungee bungee) {
        this.bungee = bungee;
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(BungeePlayer omp) {
        return "<Name>#<Id>";
    }

    @Override
    public void dispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        if (a.length != 2 || !a[1].contains("#")) {
            getHelpMessage(omp).send(omp);
            return;
        }

        String[] parts = a[1].split("#");

        switch (bungee.getDiscord().minecraftLink(omp.getUUID(), parts[0], parts[1])) {

            case INVALID_USER:
                omp.sendMessage("Discord", Color.RED, "Ongeldige Discord User, gebruik §9" + getHelp(omp) + "§7.", "Invalid Discord User, make sure to use §9" + getHelp(omp) + "§7.");
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
