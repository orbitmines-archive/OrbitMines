package com.orbitmines.bungeecord.handlers;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.orbitmines.api.PluginMessage;
import com.orbitmines.api.Server;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.runnables.BungeeRunnable;
import com.orbitmines.bungeecord.utils.ConsoleUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class PluginMessageHandler implements Listener {

    private OrbitMinesBungee bungee;

    public PluginMessageHandler() {
        bungee = OrbitMinesBungee.getBungee();
        bungee.getProxy().registerChannel(PluginMessage.CHANNEL);

        bungee.getProxy().getPluginManager().registerListener(bungee, this);
    }

    public void dataTransfer(PluginMessage message, ProxiedPlayer player, String... data) {
        if (player.getServer() == null)
            ConsoleUtils.msg("DataTransfer (" + message.toString() + ") did not go through, " + player.getName() + " is not connected to a server.");
        else
            dataTransfer(message, player.getServer().getInfo(), data);
    }

    public void dataTransfer(PluginMessage message, ServerInfo serverInfo, String... data) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        if (message != null)
            out.writeUTF(message.toString());

        for (String string : data) {
            out.writeUTF(string);
        }

        serverInfo.sendData(PluginMessage.CHANNEL, out.toByteArray());
    }

    @EventHandler
    public void onDataReceive(PluginMessageEvent event) {
        if (!event.getTag().equals(PluginMessage.CHANNEL) || !(event.getSender() instanceof net.md_5.bungee.api.connection.Server))
            return;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(event.getData());
        DataInputStream in = new DataInputStream(inputStream);

        try {
            String msg = in.readUTF();
            PluginMessage message;
            ConsoleUtils.warn(msg);

            try {
                message = PluginMessage.valueOf(msg);
            } catch (IllegalArgumentException ex) {
                ConsoleUtils.warn("Received unknown PluginMessage '" + msg + "'.");
                return;
            }

            switch (message.getTarget()) {

                case SPIGOT:
                    /* Target is spigot, so we redirect the message to the right server */
                    String target = in.readUTF();

                    Server server;

                    try {
                        server = Server.valueOf(target);
                    } catch(IllegalArgumentException ex) {
                        ConsoleUtils.warn("Received PluginMessage with target: '" + target + "', but that server cannot be found.");
                        return;
                    }

                    List<String> data = new ArrayList<>();

                    while (true) {
                        try {
                            data.add(in.readUTF());
                        } catch (IOException ex) {
                            break;
                        }
                    }

                    if (server.getStatus() != Server.Status.ONLINE)
                        return;

                    if (server.getPlayers() != 0) {
                        bungee.getMessageHandler().dataTransfer(message, bungee.getServer(server), data.toArray(new String[data.size()]));
                    } else {
                        /* No players on the server ? Let's queue it up until there are. */
                        new BungeeRunnable(BungeeRunnable.TimeUnit.SECOND, 2) {
                            @Override
                            public void run() {
                                if (server.getPlayers() != 0) {
                                    bungee.getMessageHandler().dataTransfer(message, bungee.getServer(server), data.toArray(new String[data.size()]));
                                    cancel();
                                }
                            }
                        };
                    }
                    return;
                default:
                    /* Target is BungeeCord, so we move on. */
                    break;
            }

            switch (message) {
                case CONNECT: {
                    BungeePlayer mbp = BungeePlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (mbp == null)
                        break;

                    String serverName = in.readUTF();
                    boolean notify = Boolean.parseBoolean(in.readUTF());

                    mbp.connect(bungee.getServer(serverName), notify);

                    break;
                }
                case KICK: {
                    BungeePlayer mbp = BungeePlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (mbp == null)
                        break;

                    try {
                        mbp.getPlayer().disconnect(new TextComponent(ChatColor.translateAlternateColorCodes('&', in.readUTF())));
                    } catch(IOException ex) {
                        mbp.getPlayer().disconnect();
                    }
                    break;
                }
                case LOGIN_2FA: {
                    BungeePlayer mbp = BungeePlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (mbp == null)
                        break;

                    mbp.setLoggedIn(true);
                    bungee.registerLogin(mbp);
                    break;
                }
                case UPDATE_RANKS: {
                    BungeePlayer mbp = BungeePlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (mbp != null)
                        mbp.updateRanks();

                    break;
                }
                case UPDATE_LANGUAGE: {
                    BungeePlayer mbp = BungeePlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (mbp != null)
                        mbp.updateLanguage();

                    break;
                }
                case UPDATE_SILENT: {
                    BungeePlayer mbp = BungeePlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (mbp != null)
                        mbp.updateSilent();

                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
