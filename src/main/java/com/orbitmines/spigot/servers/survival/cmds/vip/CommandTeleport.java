package com.orbitmines.spigot.servers.survival.cmds.vip;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.*;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.chat.ComponentMessage;
import com.orbitmines.spigot.api.handlers.cmd.VipCommand;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CommandTeleport extends VipCommand {

    private String[] alias = { "/tp", "/teleport" };

    public CommandTeleport() {
        super(Server.SURVIVAL, VipRank.GOLD);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return "<player>";
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        Player p = omp.getPlayer();

        if (omp.isEligible(StaffRank.MODERATOR) && omp.isOpMode()) {
            if (a.length == 2) {
                Player p2 = Bukkit.getPlayer(a[1]);
                OMPlayer omp2 = OMPlayer.getPlayer(p2);

                if (p2 != null) {
                    if (p2 != p) {
                        p.teleport(p2);
                        omp.sendMessage("Teleport", Color.LIME, "§7Geteleporteerd naar " + omp2.getName() + "§7!", "§7Teleported to " + omp2.getName() + "§7!");
                    } else {
                        omp.sendMessage("Teleport", Color.RED, "§7Je kan niet naar jezelf toe §6teleporten§7!", "§7You can't §6teleport§7 to yourself!");
                    }
                } else {
                    omp.sendMessage("Teleport", Color.RED, "§7Player §6" + a[1] + " §7is niet online!", "§7Player §6" + a[1] + " §7isn't online!");
                }
            } else if (a.length == 3) {
                Player p2 = Bukkit.getPlayer(a[1]);
                Player p3 = Bukkit.getPlayer(a[2]);
                OMPlayer omp2 = OMPlayer.getPlayer(p2);
                OMPlayer omp3 = OMPlayer.getPlayer(p3);

                if (p2 != null) {
                    if (p3 != null) {
                        p2.teleport(p3);
                        omp.sendMessage("Teleport", Color.LIME, "§7Je hebt " + omp2.getName() + "§7 naar " + omp3.getName() + "§7 geteleporteerd!", "§7Teleported " + omp2.getName() + "§7 to " + omp3.getName() + "§7!");
                    } else {
                        omp.sendMessage("Teleport", Color.RED, "§7Player §6" + a[2] + " §7is niet §aOnline§7!", "§7Player §6" + a[2] + " §7isn't §aOnline§7!");
                    }
                } else {
                    omp.sendMessage("Teleport", Color.RED, "§7Player §6" + a[1] + " §7is niet online!", "§7Player §6" + a[1] + " §7isn't online!");
                }
            } else if (a.length == 5) {
                Player p2 = Bukkit.getPlayer(a[1]);
                OMPlayer omp2 = OMPlayer.getPlayer(p2);

                if (p2 != null) {
                    try {
                        int x = Integer.parseInt(a[2]);
                        int y = Integer.parseInt(a[3]);
                        int z = Integer.parseInt(a[4]);

                        Location l = new Location(p2.getWorld(), x, y, z, p2.getLocation().getYaw(), p2.getLocation().getPitch());

                        p2.teleport(l);

                        if (p2 != p) {
                            omp.sendMessage("Teleport", Color.LIME, "§7Je hebt " + omp2.getName() + "§7 naar §6" + x + "§7, §6" + y + "§7, §6" + z + "§7 geteleporteerd!", "§7Teleported " + omp2.getName() + "§7 to §6" + x + "§7, §6" + y + "§7, §6" + z + "§7!");
                        } else {
                            omp.sendMessage("Teleport", Color.LIME, "§7Geteleporteerd naar §6" + x + "§7, §6" + y + "§7, §6" + z + "§7!", "§7Teleported to §6" + x + "§7, §6" + y + "§7, §6" + z + "§7!");
                        }
                    } catch (NumberFormatException ex) {
                        getHelpMessage(omp).send(omp);
                    }
                } else {
                    omp.sendMessage("Teleport", Color.RED, "§7Player §6" + a[1] + " §7is niet online!", "§7Player §6" + a[1] + " §7isn't online!");
                }
            } else {
                getHelpMessage(omp).send(omp);
            }
        } else if (omp.isEligible(VipRank.GOLD)) {
            if (a.length == 2) {
                Player p2 = Bukkit.getPlayer(a[1]);

                if (p2 != null) {
                    if (p2 != p) {
                        SurvivalPlayer omp2 = SurvivalPlayer.getPlayer(p2);

                        omp2.getTpRequests().add(p.getName());
                        omp2.getTpHereRequests().remove(p.getName());

                        omp.sendMessage("Teleport", Color.LIME, "§7Teleport verzoek verzonden naar §6" + omp2.getName() + "§7!", "§7Teleport request sent to §6" + omp2.getName() + "§7!");
                        omp2.sendMessage("Teleport", Color.BLUE, omp.getName() + "§7 wilt naar jouw teleporteren.", omp.getName() + "§7 wants to teleport to you.");

                        ComponentMessage cm = new ComponentMessage();
                        cm.add(new Message("  §7Klik hier om te §aAccepteren§7.", "  §7Click here to §aAccept§7."), ClickEvent.Action.RUN_COMMAND, new Message("/accept " + p.getName()), HoverEvent.Action.SHOW_TEXT, new Message("§7Teleporteer " + omp.getName() + "§7 naar je toe.", "§7Teleport " + omp.getName() + "§7 to you."));
                        cm.send(omp2);
                    } else {
                        omp.sendMessage("Teleport", Color.RED, "§7Je kan niet naar jezelf toe §6teleporten§7!", "§7You can't §6teleport§7 to yourself!");
                    }
                } else {
                    omp.sendMessage("Teleport", Color.RED, "§6" + a[1] + "§7 is niet online!", "§6" + a[1] + "§7 is not online!");
                }
            } else {
                getHelpMessage(omp).send(omp);
            }
        } else {
            omp.sendMessage(Message.REQUIRE_RANK(VipRank.GOLD));
        }
    }
}
