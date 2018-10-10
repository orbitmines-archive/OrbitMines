package com.orbitmines.bungeecord.commands.moderator;

import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.chat.ComponentMessage;
import com.orbitmines.bungeecord.handlers.cmd.StaffCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.event.ChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class CommandMotd extends StaffCommand {

    private OrbitMinesBungee bungee;

    public CommandMotd() {
        super(CommandLibrary.MOTD);

        bungee = OrbitMinesBungee.getBungee();
    }

    @Override
    public void onDispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        if (a.length == 1) {
            omp.sendMessage("Motd", Color.RED, "§7Gebruik: " + a[0].toLowerCase() + " view|<line: 1/2> <message>", "§7Use: " + a[0].toLowerCase() + " view|<line: 1/2> <message>");
            return;
        }

        switch (a[1]) {
            case "view": {
                omp.sendMessage("Motd", Color.BLUE, "§7§lMessage of the day:");

                {
                    ComponentMessage cM = new ComponentMessage();
                    cM.add(new Message("Motd", Color.BLUE, "§7§l1.§r §7" + bungee.getMotdHandler().getFirstLine()), ClickEvent.Action.SUGGEST_COMMAND, new Message(a[0].toLowerCase() + " 1 " + bungee.getMotdHandler().getFirstLine().replace("§", "&")), HoverEvent.Action.SHOW_TEXT, new Message("§7Bewerken", "§7Edit"));
                    cM.send(omp);
                }
                {
                    ComponentMessage cM = new ComponentMessage();
                    cM.add(new Message("Motd", Color.BLUE, "§7§l2.§r §7" + bungee.getMotdHandler().getSecondLine()), ClickEvent.Action.SUGGEST_COMMAND, new Message(a[0].toLowerCase() + " 2 " + bungee.getMotdHandler().getSecondLine().replace("§", "&")), HoverEvent.Action.SHOW_TEXT, new Message("§7Bewerken", "§7Edit"));
                    cM.send(omp);
                }
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
