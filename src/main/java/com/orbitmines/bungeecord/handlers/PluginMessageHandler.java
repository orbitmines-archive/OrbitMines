package com.orbitmines.bungeecord.handlers;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.orbitmines.api.Color;
import com.orbitmines.api.PluginMessage;
import com.orbitmines.api.Server;
import com.orbitmines.api.punishment.offences.Offence;
import com.orbitmines.api.punishment.offences.Severity;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.runnables.BungeeRunnable;
import com.orbitmines.bungeecord.utils.ConsoleUtils;
import com.orbitmines.discordbot.handlers.DiscordSquad;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.data.PlayTimeData;
import com.orbitmines.spigot.api.utils.Serializer;
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
            ConsoleUtils.msg(msg);

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
                        dataTransfer(message, bungee.getServer(server), data.toArray(new String[data.size()]));
                    } else {
                        /* No players on the server ? Let's queue it up until there are. */
                        new BungeeRunnable(BungeeRunnable.TimeUnit.SECOND, 2) {
                            @Override
                            public void run() {
                                if (server.getPlayers() != 0) {
                                    dataTransfer(message, bungee.getServer(server), data.toArray(new String[data.size()]));
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
                    BungeePlayer omp = BungeePlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (omp == null)
                        break;

                    String serverName = in.readUTF();
                    boolean notify = Boolean.parseBoolean(in.readUTF());

                    omp.connect(bungee.getServer(serverName), notify);

                    break;
                }
                case KICK: {
                    BungeePlayer omp = BungeePlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (omp == null)
                        break;

                    try {
                        omp.getPlayer().disconnect(new TextComponent(ChatColor.translateAlternateColorCodes('&', in.readUTF())));
                    } catch(IOException ex) {
                        omp.getPlayer().disconnect();
                    }
                    break;
                }
                case LOGIN_2FA: {
                    BungeePlayer omp = BungeePlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (omp == null)
                        break;

                    omp.setLoggedIn(true);
                    bungee.registerLogin(omp);
                    break;
                }
                case PUNISH: {
                    UUID uuid = UUID.fromString(in.readUTF());
                    Offence offence = Offence.valueOf(in.readUTF());
                    Severity severity = Severity.valueOf(in.readUTF());
                    String reason = in.readUTF();

                    BungeePlayer omp = BungeePlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (omp == null)
                        break;

                    omp.punish(uuid, offence, severity, reason);
                    break;
                }
                case PARDON: {
                    UUID uuid = UUID.fromString(in.readUTF());
                    Offence offence = Offence.valueOf(in.readUTF());
                    String reason = in.readUTF();

                    BungeePlayer omp = BungeePlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (omp == null)
                        break;

                    omp.pardon(uuid, offence, reason);
                    break;
                }
                case MUTE: {
                    BungeePlayer omp = BungeePlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (omp != null && omp.isMuted())
                        omp.muteOnSpigot(true);

                    break;
                }
                case SET_LOGGING_IN: {
                    BungeePlayer omp = BungeePlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (omp == null)
                        break;

                    omp.setLoggedIn(false);
                    break;
                }
                case MESSAGE_PLAYER: {
                    BungeePlayer omp = BungeePlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (omp != null)
                        omp.sendMessage(ChatColor.translateAlternateColorCodes('&', in.readUTF()));

                    break;
                }
                case DISCORD_GROUP_ACTION: {
                    UUID owner = UUID.fromString(in.readUTF());
                    DiscordSquad group = DiscordSquad.getGroup(owner);

                    switch(in.readUTF()) {
                        case "CREATE": {
                            if (group != null)
                                break;

                            BungeePlayer omp = BungeePlayer.getPlayer(owner);

                            if (omp == null)
                                break;

                            group = new DiscordSquad(bungee.getDiscord(), owner);
                            group.setup(omp);
                            break;
                        }
                        case "DESTROY": {
                            if (group == null)
                                break;

                            BungeePlayer omp = BungeePlayer.getPlayer(owner);

                            if (omp == null)
                                break;

                            group.destroy(omp);
                            break;
                        }
                        case "ADD": {
                            boolean next = true;
                            while (next) {
                                try {
                                    group.addMember(UUID.fromString(in.readUTF()));
                                } catch(IOException ex) {
                                    next = false;
                                }
                            }
                            break;
                        }
                        case "REMOVE": {
                            group.removeMember(UUID.fromString(in.readUTF()), Boolean.parseBoolean(in.readUTF()));
                            break;
                        }
                        case "NAME": {
                            group.setName(in.readUTF());
                            break;
                        }
                        case "COLOR": {
                            group.setColor(Color.valueOf(in.readUTF()));
                            break;
                        }
                    }

                    break;
                }
                case FAVORITE_FRIEND_MESSAGE: {
                    List<UUID> friends = Serializer.parseUUIDList(in.readUTF());
                    String joinedUuid = in.readUTF();
                    String joinedName = in.readUTF();

                    for (UUID friend : friends) {
                        BungeePlayer omp = BungeePlayer.getPlayer(friend);

                        if (omp != null)
                            dataTransfer(message, omp.getPlayer(), friend.toString(), joinedUuid, joinedName);
                    }

                    break;
                }
                case UPDATE_RANKS: {
                    BungeePlayer omp = BungeePlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (omp != null)
                        omp.updateRanks();

                    break;
                }
                case UPDATE_LANGUAGE: {
                    BungeePlayer omp = BungeePlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (omp != null)
                        omp.updateLanguage();

                    break;
                }
                case UPDATE_SILENT: {
                    BungeePlayer omp = BungeePlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (omp != null)
                        omp.updateSilent();

                    break;
                }
                case UPDATE_SETTINGS: {
                    BungeePlayer omp = BungeePlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (omp != null)
                        omp.getData(Data.Type.SETTINGS).load();

                    break;
                }
                case SERVER_SWITCH: {
                    BungeePlayer omp = BungeePlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (omp != null) {
                        Server server = Server.valueOf(in.readUTF());

                        ((PlayTimeData) omp.getData(Data.Type.PLAY_TIME)).startSession(server);
                    }
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
