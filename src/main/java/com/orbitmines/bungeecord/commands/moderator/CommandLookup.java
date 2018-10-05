package com.orbitmines.bungeecord.commands.moderator;

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.Color;
import com.orbitmines.api.IP;
import com.orbitmines.api.Server;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.api.utils.uuid.UUIDUtils;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.chat.ComponentMessage;
import com.orbitmines.bungeecord.handlers.cmd.StaffCommand;
import net.firefang.ip2c.IpUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.event.ChatEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class CommandLookup extends StaffCommand {

    private OrbitMinesBungee bungee;

    public CommandLookup(OrbitMinesBungee bungee) {
        super(CommandLibrary.LOOKUP);

        this.bungee = bungee;
    }

    @Override
    public void onDispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        if (a.length < 2) {
            getHelpMessage(omp).send(omp);
            return;
        }

        if (a[1].contains(".")) {
            /* IP */
            lookup(omp, Type.IP, a[1]);
        } else if (a[1].length() <= 16) {
            /* Name */
            lookup(omp, Type.PLAYER, a[1]);
        } else {
            /* UUID */
            lookup(omp, Type.UUID, a[1]);
        }
    }

    private synchronized void lookup(BungeePlayer omp, Type type, String info) {
        omp.sendMessage("§6§m---------------------------------------------");
        omp.sendMessage("");
        omp.sendMessage(" §8§lOrbit§7§lMines §6§lAdvanced Lookup");
        omp.sendMessage("  §7§oLoading §l" + type.toString() + ": " + info  + "§7§o info...");
        omp.sendMessage("");

        switch (type) {

            case IP: {
                List<IP> list = IP.getIpInfo(info);

                if (list.size() == 0) {
                    omp.sendMessage("Lookup", Color.RED, "Unable to find data for §l" + type.toString() + ": " + info + "§7.");
                    break;
                }

                int size = list.size();
                omp.sendMessage("Lookup", Color.LIME, "Found §l" + size + " " + (size == 1 ? "Player" : "Players") + " §7with §l" + type.toString() + ": " + info + "§7.");
                omp.sendMessage(" §7§lPlayers:");

                for (IP ip : list) {
                    CachedPlayer player = CachedPlayer.getPlayer(ip.getUuid());

                    ComponentMessage cM = new ComponentMessage();
                    cM.add(" §7- ");

                    String playerName = player.getPlayerName();
                    String displayName = player.getRankPrefix() + playerName;
                    int historyCount = ip.getAllIps().size();

                    String lastLoginDisplay = getLastLoginDisplay(ip);

                    cM.add(displayName, ClickEvent.Action.RUN_COMMAND, getAlias()[0] + " " + playerName, HoverEvent.Action.SHOW_TEXT,
                            "§6§lAdvanced Lookup\n" +
                            "§7Name: " + displayName + "\n" +
                            "§7UUID: §6" + player.getUUID().toString() + "\n" +
                            "§7Last Login: §6" + lastLoginDisplay + "\n" +
                            "\n" +
                            "§7§o" + historyCount + " " + (historyCount == 1 ? "IP" : "IPs") + " listed under this name.\n" +
                            "\n" +
                            "§aClick here to lookup."
                    );

                    cM.send(omp);

                    ComponentMessage cM2 = new ComponentMessage();
                    cM2.add("    §7Last Login: ");
                    int ipHistoryCount = IP.getIpInfo(ip.getLastIp()).size();
                    cM2.add("§6" + lastLoginDisplay + "§7 / §6" + ip.getLastIp() + " §7(§o" + IpUtils.getCountry(ip.getLastIp()) + "§7)", ClickEvent.Action.RUN_COMMAND, getAlias()[0] + " " + playerName, HoverEvent.Action.SHOW_TEXT,
                            "§6§lAdvanced Lookup\n" +
                            "§7IP: " + ip.getLastIp() + "\n" +
                            "\n" +
                            "§7§o" + ipHistoryCount + " " + (ipHistoryCount == 1 ? "Player" : "Players") + " listed under this IP.\n" +
                            "\n" +
                            "§aClick here to lookup.");

                    cM2.send(omp);
                }

                break;
            }
            case UUID: {
                UUID uuid;
                try {
                    uuid = UUID.fromString(info);
                } catch (IllegalArgumentException ex) {
                    try {
                        uuid = UUIDUtils.parse(info);
                    } catch (IllegalArgumentException ex1) {
                        omp.sendMessage("Lookup", Color.RED, "Invalid §l" + type.toString() + ": " + info + "§7.");
                        break;
                    }
                }

                lookup(omp, type, info, uuid);
                break;
            }
            case PLAYER: {
                UUID uuid = UUIDUtils.getUUID(info);

                if (uuid == null) {
                    omp.sendMessage("Lookup", Color.RED, "Invalid §l" + type.toString() + ": " + info + "§7.");
                    break;
                }

                lookup(omp, type, info, uuid);
                break;
            }
        }
        omp.sendMessage("");
        omp.sendMessage("§6§m---------------------------------------------");
    }

    private void lookup(BungeePlayer omp, Type type, String info, UUID uuid) {
        Map<String, String> nameHistory = UUIDUtils.getNames(uuid);
        if (nameHistory == null || nameHistory.size() == 0) {
            omp.sendMessage("Lookup", Color.RED, "Unable to find data for §l" + type.toString() + ": " + info + "§7.");
            return;
        }

        int size = nameHistory.size();
        omp.sendMessage("Lookup", Color.LIME, "Found §l" + size + " " + (size == 1 ? "Name" : "Names") + " §7in their §lName History§7.");

        omp.sendMessage(" §7§lName History:");
        for (String name : nameHistory.keySet()) {
            String changedAt = nameHistory.get(name);

            omp.sendMessage(" §7- §6" + name + " §7(§o" + (changedAt == null ? "Initial Name" : nameHistory.get(name)) + "§7)");
        }

        {
            ComponentMessage cM = new ComponentMessage();
            cM.add(" §7§lUUID: ");
            cM.add("§6" + uuid.toString(), ClickEvent.Action.SUGGEST_COMMAND, uuid.toString(), HoverEvent.Action.SHOW_TEXT, "§7Click here to copy §6UUID§7.");
            cM.send(omp);
        }

        omp.sendMessage("");

        CachedPlayer player = CachedPlayer.getPlayer(uuid);

        if (player == null) {
            omp.sendMessage("Lookup", Color.BLUE, "Player has never been on OrbitMines before.");
            return;
        }

        omp.sendMessage("Lookup", Color.LIME, "Found §lAccount: §7" + player.getRankPrefix() + player.getPlayerName() + "§7.");

        IP ip = IP.getIp(uuid);

        omp.sendMessage(" §7§lLast Seen: §6" + getLastLoginDisplay(ip) + "§7 / §6" + ip.getLastIp() + " §7(§o" + IpUtils.getCountry(ip.getLastIp()) + "§7)");

        Map<String, String> ips = ip.getAllIps();

        omp.sendMessage(" §7§lIPs:");
        for (String ipString : ips.keySet()) {
            ComponentMessage cM = new ComponentMessage();
            cM.add(" §7- ");

            int ipHistoryCount = IP.getIpInfo(ipString).size();

            cM.add("§6" + ipString + " §7(§o" + IpUtils.getCountry(ipString) + "§7) / §6" + ips.get(ipString), ClickEvent.Action.RUN_COMMAND, getAlias()[0] + " " + ipString, HoverEvent.Action.SHOW_TEXT,
                    "§6§lAdvanced Lookup\n" +
                    "§7IP: " + ipString + "\n" +
                    "\n" +
                    "§7§o" + ipHistoryCount + " " + (ipHistoryCount == 1 ? "Player" : "Players") + " listed under this IP.\n" +
                    "\n" +
                    "§aClick here to lookup."
            );

            cM.send(omp);
        }
    }

    private String getLastLoginDisplay(IP ip) {
        ip.updateCurrentServer();
        String serverName = ip.getCurrentServer();

        if (serverName == null)
            return ip.getLastLogin();

        return Server.Status.ONLINE.getDisplayName() + "§r §7(" + Server.valueOf(serverName).getDisplayName() + "§r§7)";
    }

    enum Type {

        IP,
        UUID,
        PLAYER;

    }
}
