package com.orbitmines.bungeecord.commands;

import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.AnnouncementHandler;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.chat.ComponentMessage;
import com.orbitmines.bungeecord.handlers.cmd.StaffCommand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.event.ChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class CommandAnnouncement extends StaffCommand {

    private OrbitMinesBungee bungee;

    private String[] alias = { "/announcement" };

    public CommandAnnouncement() {
        super(StaffRank.MODERATOR);

        this.bungee = OrbitMinesBungee.getBungee();
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(BungeePlayer omp) {
        return "list|delete|add|remove|title|subtitle";
    }

    @Override
    public void onDispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        if (a.length == 1) {
            omp.sendMessage("Announcements", Color.BLUE, "\n§7/announcement list\n§7/announcement delete <index>\n§7/announcement add\n§7/announcement remove <index>\n§7/announcement title <index> <title>\n§7/announcement subtitle <index> <title>");
            return;
        }

        String type = a[1].toLowerCase();

        AnnouncementHandler handler = bungee.getAnnouncementHandler();

        switch (type) {
            case "list": {
                omp.sendMessage("Announcements", Color.BLUE, "§7§lAnnouncements:");
                for (int i = 0; i < handler.getTitles().size(); i++) {
                    AnnouncementHandler.Title title = handler.getTitles().get(i);
                    omp.sendMessage("Announcements", Color.BLUE, "§7§l" + (i + 1) + ".");
                    {
                        ComponentMessage cM = new ComponentMessage();
                        cM.add(new Message("Announcements", Color.BLUE, "§7 - §f" + title.getTitle()), ClickEvent.Action.SUGGEST_COMMAND, new Message(a[0].toLowerCase() + " title " + (i + 1) + " " + title.getTitle().replace("§", "&")), HoverEvent.Action.SHOW_TEXT, new Message("§7Bewerken", "§7Edit"));
                        cM.send(omp);
                    } {
                        ComponentMessage cM = new ComponentMessage();
                        cM.add(new Message("Announcements", Color.BLUE, "§7 - §f" + title.getSubTitle()), ClickEvent.Action.SUGGEST_COMMAND, new Message(a[0].toLowerCase() + " subtitle " + (i + 1) + " " + title.getSubTitle().replace("§", "&")), HoverEvent.Action.SHOW_TEXT, new Message("§7Bewerken", "§7Edit"));
                        cM.send(omp);
                    }
                }
                break;
            }
            case "add": {
                handler.addTitle();
                omp.sendMessage("Announcements", Color.LIME, "§7Je hebt een nieuwe announcement gemaakt!", "§7Successfully added a new announcement!");
                break;
            }
            case "remove": {
                if (a.length != 3) {
                    omp.sendMessage("Announcements", Color.RED, "§7Gebruik " + a[0].toLowerCase() + " remove <index>.", "§7Use " + a[0].toLowerCase() + " remove <index>.");
                    break;
                }

                int index;
                try {
                    index = Integer.parseInt(a[2]);
                } catch (NumberFormatException ex) {
                    omp.sendMessage("Announcements", Color.RED, "§7Dat is geen nummer!", "§7That is not a valid number!");
                    break;
                }

                if (handler.getTitles().size() < index) {
                    omp.sendMessage("Announcements", Color.RED, "§7Die announcement bestaat niet!", "§7That announcement does not exist!");
                    break;
                }

                handler.removeTitle(index -1);
                omp.sendMessage("Announcements", Color.LIME, "§7Je hebt announcement " + index + " verwijderd!", "§7Successfully removed announcement " + index + "!");
                break;
            }
            case "subtitle":
            case "title": {
                if (a.length >= 4) {
                    int index;
                    try {
                        index = Integer.parseInt(a[2]);
                    } catch (NumberFormatException ex) {
                        omp.sendMessage("Announcements", Color.RED, "§7Dat is geen nummer!", "§7That is not a valid number!");
                        break;
                    }

                    if (handler.getTitles().size() < index) {
                        omp.sendMessage("Announcements", Color.RED, "§7Die announcement bestaat niet!", "§7That announcement does not exist!");
                        break;
                    }

                    StringBuilder stringBuilder = new StringBuilder();
                    for (int x = 3; x < a.length; x++) {
                        if (x != 3)
                            stringBuilder.append(" ");

                        stringBuilder.append(a[x].replaceAll(";", ":"));
                    }
                    String message = stringBuilder.toString();

                    if (type.equalsIgnoreCase("title"))
                        handler.setTitle(index -1, message);
                    else
                        handler.setSubTitle(index -1, message);

                    omp.sendMessage("Announcements", Color.LIME, "§7Announcement " + index + " van " + type + " veranderd naar: '" + message + "'.", "§7Successfully changed announcement " + index + "'s " + type + " to: '" + message + "'.");
                } else {
                    omp.sendMessage("Announcements", Color.RED, "§7Gebruik: " + a[0].toLowerCase() + " " + type + " <index> <" + type + ">.", "§7Use: " + a[0].toLowerCase() + " " + type + " <index> <" + type + ">.");
                }
                break;
            }
            default: {
                omp.sendMessage("Announcements", Color.BLUE, "\n§7/announcement list\n§7/announcement delete <index>\n§7/announcement add\n§7/announcement remove <index>\n§7/announcement title <index> <title>\n§7/announcement subtitle <index> <title>");
            }
        }
    }
}
