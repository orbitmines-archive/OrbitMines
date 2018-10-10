package com.orbitmines.spigot.api.handlers;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.Color;
import com.orbitmines.api.PluginMessage;
import com.orbitmines.api.Server;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.achievements.EmptyAchievementHandler;
import com.orbitmines.spigot.api.handlers.data.FriendsData;
import com.orbitmines.spigot.api.utils.ConsoleUtils;
import com.orbitmines.spigot.servers.hub.gui.discordgroup.DiscordGroupGUIInstance;
import com.orbitmines.spigot.servers.hub.gui.friends.FriendGUIInstance;
import com.orbitmines.spigot.servers.hub.handlers.HubAchievements;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public abstract class PluginMessageHandler implements PluginMessageListener {

    private OrbitMines orbitMines;

    public PluginMessageHandler() {
        orbitMines = OrbitMines.getInstance();

        Messenger messenger = orbitMines.getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(orbitMines, PluginMessage.CHANNEL_BUNGEECORD);
        messenger.registerIncomingPluginChannel(orbitMines, PluginMessage.CHANNEL_BUNGEECORD, this);

        /* Send data to Bungeecord through custom channel */
        messenger.registerOutgoingPluginChannel(orbitMines, PluginMessage.CHANNEL);
        messenger.registerIncomingPluginChannel(orbitMines, PluginMessage.CHANNEL, this);
    }

    public void dataTransfer(PluginMessage message, String... data) {
        dataTransfer(message, null, data);
    }

    public void dataTransfer(PluginMessage message, Server server, String... data) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        if (message != null)
            out.writeUTF(message.toString());

        /* To send a message to a certain server (will be redirected in BungeeCord) */
        if (server != null)
            out.writeUTF(server.toString());

        for (String string : data) {
            out.writeUTF(string);
        }

        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);

        if (player != null)
            player.sendPluginMessage(orbitMines, PluginMessage.CHANNEL, out.toByteArray());
    }

    public abstract void onReceive(ByteArrayDataInput in, PluginMessage message);

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (!channel.equals(PluginMessage.CHANNEL) && !channel.equals(PluginMessage.CHANNEL_BUNGEECORD))
            return;

        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);

        if (channel.equals(PluginMessage.CHANNEL)) {
            String msg = in.readUTF();
            ConsoleUtils.msg(msg);//debug

            PluginMessage message;
            try {
                message = PluginMessage.valueOf(msg);
            } catch (IllegalArgumentException ex) {
                ConsoleUtils.warn("Received unknown PluginMessage '" + msg + "'.");
                return;
            }

            switch (message) {

                case LOGIN_2FA: {
                    OMPlayer omp = OMPlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (omp != null)
                        orbitMines.get2FA().initiateLogin(omp);

                    break;
                }
                case MUTE: {
                    OMPlayer omp = OMPlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (omp != null)
                        omp.setMuted(Boolean.parseBoolean(in.readUTF()));

                    break;
                }
                case CHECK_VOTE_CACHE: {
                    OMPlayer omp = OMPlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (omp != null)
                        omp.checkCachedVotes();

                    break;
                }
                case UPDATE_DISCORD_GROUP_DATA: {
                    OMPlayer omp = OMPlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (omp == null)
                        break;

                    omp.getData(Data.Type.DISCORD_GROUPS).load();

                    /* Update Inventory */
                    GUI gui = omp.getLastInventory();
                    if (gui == null)
                        break;
                    else if (gui instanceof DiscordGroupGUIInstance && gui.hasOpened(omp))
                        gui.reopen(omp);

                    break;
                }
                case UPDATE_FRIENDS: {
                    OMPlayer omp = OMPlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (omp == null)
                        break;

                    omp.getData(Data.Type.FRIENDS).load();

                    /* Update Inventory */
                    GUI gui = omp.getLastInventory();
                    if (gui == null)
                        break;
                    else if (gui instanceof FriendGUIInstance && gui.hasOpened(omp))
                        gui.reopen(omp);

                    break;
                }
                case FAVORITE_FRIEND_MESSAGE: {
                    UUID uuid = UUID.fromString(in.readUTF());
                    OMPlayer omp = OMPlayer.getPlayer(uuid);

                    if (omp == null)
                        break;

                    FriendsData data = (FriendsData) omp.getData(Data.Type.FRIENDS);
                    if (data.getFavoriteFriends().contains(UUID.fromString(in.readUTF()))) {
                        String name = in.readUTF();

                        CachedPlayer friend = CachedPlayer.getPlayer(name);
                        String color = friend.getRankPrefixColor().getChatColor();

                        omp.sendMessage("Friends", Color.BLUE, color + name + "§7 is online gekomen.", color + name + "§7 has come online.");
                    }

                    break;
                }
                case UPDATE_RANKS: {
                    OMPlayer omp = OMPlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (omp != null)
                        omp.updateRanks();

                    break;
                }
                case CHECK_DISCORD_LINK_ACHIEVEMENT: {
                    OMPlayer omp = OMPlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (omp != null) {
                        EmptyAchievementHandler handler = (EmptyAchievementHandler) HubAchievements.DISCORD_LINK.getHandler();
                        handler.complete(omp, true);
                    }
                    break;
                }
                case UPDATE_LANGUAGE: {
                    OMPlayer omp = OMPlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (omp != null)
                        omp.updateLanguage();

                    break;
                }
                case UPDATE_SILENT: {
                    OMPlayer omp = OMPlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (omp != null)
                        omp.updateSilent();

                    break;
                }
                case UPDATE_SETTINGS: {
                    OMPlayer omp = OMPlayer.getPlayer(UUID.fromString(in.readUTF()));

                    if (omp != null)
                        omp.getData(Data.Type.SETTINGS).load();

                    break;
                }
                default: {
                    /* Is not a default PluginMessage, pass it on the GameServer */
                    onReceive(in, message);
                    break;
                }
            }

            return;
        }

        /* BungeeCord Channel */
    }
}
