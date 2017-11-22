package com.orbitmines.bungeecord.commands;

import com.orbitmines.api.Color;
import com.orbitmines.api.StaffRank;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.cmd.StaffCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.event.ChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class CommandMotd extends StaffCommand {

    private OrbitMinesBungee bungee;

    private String[] alias = { "/motd" };

    public CommandMotd() {
        super(StaffRank.MODERATOR);

        bungee = OrbitMinesBungee.getBungee();
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(BungeePlayer mbp) {
        return "view|1|2";
    }

    @Override
    public void onDispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        if (a.length == 1) {
            omp.sendMessage("Motd", Color.RED, "§7Gebruik: " + a[0].toLowerCase() + " view|<line: 1/2> <message>", "§7Use: " + a[0].toLowerCase() + " view|<line: 1/2> <message>");
            return;
        }

        switch (a[1]) {
            case "view": {
                omp.sendMessage("Motd", Color.ORANGE, "§7§lMessage of the day:");
                omp.sendMessage("Motd", Color.ORANGE, "§7§l1. §7" + bungee.getMotdHandler().getFirstLine());
                omp.sendMessage("Motd", Color.ORANGE, "§7§l2. §7" + bungee.getMotdHandler().getSecondLine());
                break;
            }
            case "1": {
                if (a.length == 2) {
                    omp.sendMessage("Motd", Color.RED, "7Gebruik: " + a[0].toLowerCase() + " view|<line: 1/2> <message>", "§7Use: " + a[0].toLowerCase() + " view|<line: 1/2> <message>");
                    break;
                }

                StringBuilder stringBuilder = new StringBuilder();
                for (int x = 2; x < a.length; x++) {
                    stringBuilder.append(a[x]).append(" ");
                }
                String line = ChatColor.translateAlternateColorCodes('&', stringBuilder.toString());

                bungee.getMotdHandler().setFirstLine(line);

                omp.sendMessage("Motd", Color.LIME, "§7Motd lijn 1 veranderd naar: '" + line + "§7'.", "§7Successfully changed first motd line to: '" + line + "§7'.");
                break;
            }
            case "2": {
                if (a.length == 2) {
                    omp.sendMessage("Motd", Color.RED, "§7Gebruik: " + a[0].toLowerCase() + " view|<line: 1/2> <message>", "§7Use: " + a[0].toLowerCase() + " view|<line: 1/2> <message>");
                    break;
                }

                StringBuilder stringBuilder = new StringBuilder();
                for (int x = 2; x < a.length; x++) {
                    stringBuilder.append(a[x]).append(" ");
                }
                String line = ChatColor.translateAlternateColorCodes('&', stringBuilder.toString());

                bungee.getMotdHandler().setSecondLine(line);

                omp.sendMessage("Motd", Color.LIME, "§7Motd lijn 2 veranderd naar: '" + line + "§7'.", "§7Successfully changed second motd line to: '" + line + "§7'.");
                break;
            }
            default: {
                omp.sendMessage("Motd", Color.RED, "§7Gebruik: " + a[0].toLowerCase() + " view|<line: 1/2> <message>", "§7Use: " + a[0].toLowerCase() + " view|<line: 1/2> <message>");
                break;
            }
        }
    }
}
