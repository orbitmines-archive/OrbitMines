package com.orbitmines.spigot.api.cmds.moderator;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.NewsHologram;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.StaffCommand;
import org.bukkit.entity.Player;

public class CommandHologram extends StaffCommand {

    public CommandHologram() {
        super(CommandLibrary.HOLOGRAM);
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        Player p = omp.getPlayer();

        if (!p.getWorld().getName().equals(OrbitMines.getInstance().getLobby().getWorld().getName())) {
            omp.sendMessage("Hologram", Color.RED, "Je kan alleen holograms in de lobby/spawn neerzetten.", "You can only spawn holograms in the lobby/spawn.");
            return;
        }

        if (a.length == 1) {
            omp.sendMessage("Hologram", Color.BLUE, "\n§7/hologram list\n§7/hologram <name> lines\n/hologram <name> relocate\n§7/hologram <name> delete\n§7/hologram <name> create\n§7/hologram <name> add <line>\n§7/hologram <name> remove <line number>\n§7/hologram <name> edit <line number> <line>");
            return;
        }

        String name = a[1];

        if (name.equalsIgnoreCase("list")) {
            p.sendMessage("§7§lHolograms:");
            for (String news : NewsHologram.getHolograms().keySet()) {
                p.sendMessage("§7 - " + news);
            }
            return;
        }

        if (name.length() > 20) {
            omp.sendMessage("Hologram", Color.RED, "§7Naam can niet niet meer dan 20 karakters hebben.", "§7Name cannot be longer than 20 characters.");
            return;
        }

        for (char ch : name.toCharArray()) {
            if (Character.isAlphabetic(ch))
                continue;

            omp.sendMessage("Hologram", Color.RED, "§7Je kan hier alleen letters gebruiken!", "§7You can only use letters here!");
            return;
        }

        if (a.length < 3) {
            omp.sendMessage("Announcements", Color.BLUE, "\n§7/hologram list\n§7/hologram <name> lines\n/hologram <name> relocate\n§7/hologram <name> delete\n§7/hologram <name> create\n§7/hologram <name> add <line>\n§7/hologram <name> remove <line number>\n§7/hologram <name> edit <line number> <line>");
            return;
        }

        String type = a[2].toLowerCase();

        switch (type) {

            case "lines": {
                NewsHologram news = NewsHologram.getHologram(name);

                if (news != null) {
                    omp.sendMessage("Hologram", Color.BLUE, "\n§7§lLines: §7(" + name + ")");

                    int lineNumber = 0;
                    for (String line : news.getHologram().getLines()) {
                        lineNumber++;

                        omp.sendMessage(" §7- §l" + lineNumber + ". §f" + line);
                    }
                } else {
                    omp.sendMessage("Hologram", Color.RED, "§7Die hologram bestaat niet!", "§7That hologram does not exist!");
                }
                break;
            }
            case "relocate": {
                NewsHologram news = NewsHologram.getHologram(name);

                if (news != null) {
                    news.relocate(p.getPlayer().getLocation());

                    omp.sendMessage("Hologram", Color.LIME, "§7Je hebt '" + name + "' verplaatst naar jouw positie!", "§7Successfully relocated '" + name + "' to your position!");
                } else {
                    omp.sendMessage("Hologram", Color.RED, "§7Die hologram bestaat niet!", "§7That hologram does not exist!");
                }
                break;
            }
            case "create": {
                if (NewsHologram.getHologram(name) == null) {
                    new NewsHologram(name, p.getPlayer().getLocation());
                    omp.sendMessage("Hologram", Color.LIME, "§7Nieuwe hologram gemaakt! (" + name + ")", "§7Successfully created new news hologram! (" + name + ")");
                } else {
                    omp.sendMessage("Hologram", Color.RED, "§7Die hologram bestaat al! Gebruik '/hologram " + name + " add|remove|edit' om te wijzigen.", "§7That news hologram already exists! Use '/hologram " + name + " add|remove|edit' in order to edit.");
                }
                break;
            }
            case "delete": {
                NewsHologram news = NewsHologram.getHologram(name);

                if (news != null) {
                    news.delete(true);
                } else {
                    omp.sendMessage("Hologram", Color.RED, "§7Die hologram bestaat niet!", "§7That hologram does not exist!");
                }
                break;
            }
            case "add": {
                NewsHologram news = NewsHologram.getHologram(name);

                if (news == null) {
                    omp.sendMessage("Hologram", Color.RED, "§7Die hologram bestaat niet!", "§7That hologram does not exist!");
                    break;
                }

                if (a.length >= 4) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int x = 3; x < a.length; x++) {
                        stringBuilder.append(a[x]).append(" ");
                    }
                    String line = stringBuilder.toString().substring(0, stringBuilder.length() -1);

                    news.addLine(line);

                    omp.sendMessage("Hologram", Color.LIME, "§7Lijn toegevoegd: '" + line + "'.", "§7Successfully added line: '" + line + "'.");
                } else {
                    omp.sendMessage("Hologram", Color.BLUE, "§7" + omp.lang("Gebruik", "Use") + " §9/hologram <name> add <line>");
                }
                break;
            }
            case "remove": {
                NewsHologram news = NewsHologram.getHologram(name);

                if (news == null) {
                    omp.sendMessage("Hologram", Color.RED, "§7Die hologram bestaat niet!", "§7That hologram does not exist!");
                    break;
                }

                if (a.length == 4) {
                    int index;
                    try {
                        index = Integer.parseInt(a[3]);
                    } catch(NumberFormatException ex) {
                        omp.sendMessage("Hologram", Color.RED, "§7Dat is geen nummmer!", "§7That is not a valid line number.");
                        break;
                    }

                    if (news.getHologram().getLines().size() < index) {
                        omp.sendMessage("Hologram", Color.RED, "§7Die lijn nummer bestaat niet!", "§7That line number does not exist!");
                        break;
                    }

                    news.removeLine(index);

                    omp.sendMessage("Hologram", Color.LIME, "§7Lijn verwijderd: " + index + ".", "§7Successfully removed line " + index + ".");
                } else {
                    omp.sendMessage("Hologram", Color.BLUE, "§7" + omp.lang("Gebruik", "Use") + " §9/hologram <name> remove <line number>");
                }
                break;
            }
            case "edit": {
                NewsHologram news = NewsHologram.getHologram(name);

                if (news == null) {
                    omp.sendMessage("Hologram", Color.RED, "§7Die hologram bestaat niet!", "§7That hologram does not exist!");
                    break;
                }

                if (a.length >= 5) {
                    int index;
                    try {
                        index = Integer.parseInt(a[3]);
                    } catch(NumberFormatException ex) {
                        omp.sendMessage("Hologram", Color.RED, "§7Dat is geen nummmer!", "§7That is not a valid line number.");
                        break;
                    }

                    if (news.getHologram().getLines().size() < index) {
                        omp.sendMessage("Hologram", Color.RED, "§7Die lijn nummer bestaat niet!", "§7That line number does not exist!");
                        break;
                    }

                    StringBuilder stringBuilder = new StringBuilder();
                    for (int x = 4; x < a.length; x++) {
                        stringBuilder.append(a[x]).append(" ");
                    }
                    String line = stringBuilder.toString().substring(0, stringBuilder.length() -1);

                    news.setLine(index, line);

                    omp.sendMessage("Hologram", Color.LIME, "§7Lijn verandert: " + index + " naar: '" + line + "'.", "§7Successfully changed line " + index + " to: '" + line + "'.");
                } else {
                    omp.sendMessage("Hologram", Color.RED, "§7" + omp.lang("Gebruik", "Use") + " §9/hologram <name> edit <line number> <line>");
                }
                break;
            }
            default: {
                omp.sendMessage("Hologram", Color.BLUE, "\n§7/hologram list\n§7/hologram <name> lines\n/hologram <name> relocate\n§7/hologram <name> delete\n§7/hologram <name> create\n§7/hologram <name> add <line>\n§7/hologram <name> remove <line number>\n§7/hologram <name> edit <line number> <line>");
            }
        }
    }
}
