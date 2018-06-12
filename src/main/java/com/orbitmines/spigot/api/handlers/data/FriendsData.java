package com.orbitmines.spigot.api.handlers.data;

import com.orbitmines.api.*;
import com.orbitmines.api.database.*;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.CachedPlayer;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.PluginMessageHandler;
import com.orbitmines.spigot.api.utils.Serializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.orbitmines.api.database.tables.TableFriends.*;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class FriendsData extends Data {

    private PluginMessageHandler messageHandler;

    private final int DATABASE_FRIEND_LIMIT = 1500;

    private List<UUID> friends;
    private List<UUID> favoriteFriends;
    private List<UUID> sentRequests;
    private List<UUID> invites;

    public FriendsData(UUID uuid) {
        super(Table.FRIENDS, Type.FRIENDS, uuid);

        messageHandler = OrbitMines.getInstance().getServerHandler().getMessageHandler();

        friends = new ArrayList<>();
        favoriteFriends = new ArrayList<>();
        sentRequests = new ArrayList<>();
        invites = new ArrayList<>();
    }

    @Override
    public void load() {
        if (!Database.get().contains(table, new Column[] { UUID }, new Where(UUID, getUUID().toString()))) {
            Database.get().insert(table, Table.FRIENDS.values(uuid.toString(), Serializer.serializeUUIDList(friends), Serializer.serializeUUIDList(favoriteFriends), Serializer.serializeUUIDList(sentRequests), Serializer.serializeUUIDList(invites)));
        } else {
            Map<Column, String> values = Database.get().getValues(table, new Column[] {
                    FRIENDS,
                    FAVORITE,
                    SENT_INVITES,
                    INVITES
            }, new Where(UUID, getUUID().toString()));

            friends = Serializer.parseUUIDList(values.get(FRIENDS));
            favoriteFriends = Serializer.parseUUIDList(values.get(FAVORITE));
            sentRequests = Serializer.parseUUIDList(values.get(SENT_INVITES));
            invites = Serializer.parseUUIDList(values.get(INVITES));
        }
    }

    public void sendJoinMessage(OMPlayer omp) {
        messageHandler.dataTransfer(PluginMessage.FAVORITE_FRIEND_MESSAGE, Serializer.serializeUUIDList(friends), getUUID().toString(), omp.getName());
    }

    /* Inventory Interactions */
    public void onAccept(CachedPlayer friend) {
        onFriendAddition(friend);
    }

    public void onDeny(CachedPlayer friend) {
        clearInvite(friend);
    }

    public void onRequestCancel(CachedPlayer friend) {
        clearRequest(friend);
    }

    public void onRequest(UUID friend) {
        OMPlayer omp = OMPlayer.getPlayer(uuid);

        if (friend.toString().equals(this.uuid.toString())) {
            omp.sendMessage("Friends", Color.RED, "§7Je kan jezelf niet als een vriend toevoegen. :(", "§7You cannot add yourself as a friend. :(");
        } else if (sentRequests.contains(friend)) {
            CachedPlayer f = CachedPlayer.getPlayer(friend);
            String name = f.getRankPrefixColor().getChatColor() + f.getPlayerName();

            omp.sendMessage("Friends", Color.RED, "§7Je hebt al een vriendschapsverzoek naar " + name + "§7gestuurd!", "§7You have already sent a friend request to " + name + "§7!");
        } else if (friends.contains(friend)) {
            CachedPlayer f = CachedPlayer.getPlayer(friend);
            String name = f.getRankPrefixColor().getChatColor() + f.getPlayerName();

            omp.sendMessage("Friends", Color.RED, "§7Je bent al vrienden met " + name + "§7.", "§7You're already friends with " + name + "§7.");
        } else if (friends.size() >= DATABASE_FRIEND_LIMIT) {
            omp.sendMessage("Friends", Color.RED, "§7Je hebt het vrienden limiet bereikt!", "§7You have exceeded the friend limit.");
        } else {
            CachedPlayer f = CachedPlayer.getPlayer(friend);
            FriendsData data = getDataFor(friend);

            if (data.getSentRequests().contains(uuid)) {
                /* Both request, so we make them friends */
                onFriendAddition(f);
            } else {
                addSentRequests(friend);
                data.addInvites(this.uuid);

                String name = f.getRankPrefixColor().getChatColor() + f.getPlayerName();

                omp.sendMessage("Friends", Color.LIME, "§7Je hebt een vriendschapsverzoek gestuurd naar " + name + "§7.", "§7You have sent a friend request to " + name + "§7.");

                forceUpdateFor(f, new Message("Friends", Color.BLUE, "§7" + omp.getName() + "§7 heeft je een vriendschapsverzoek gestuurd.", "§7" + omp.getName() + " §7has sent you a friend request!"));
            }
        }
    }

    public void onFriendToggle(CachedPlayer friend, boolean favorite) {
        if (favorite)
            addToFavoriteFriends(friend.getUUID());
        else
            removeFromFavoriteFriends(friend.getUUID());
    }

    public void onFriendAddition(CachedPlayer friend) {
        if (friends.contains(friend.getUUID()))
            return;

        FriendsData data = getDataFor(friend.getUUID());

        addToFriends(data.getUUID());
        data.addToFriends(uuid);

        OMPlayer omp = OMPlayer.getPlayer(uuid);
        String name = friend.getRankPrefixColor().getChatColor() + friend.getPlayerName();

        omp.sendMessage("Friends", Color.LIME, "§7Je bent nu vrienden met " + name + "§7.", "§7You are now friends with " + name + "§7.");

        forceUpdateFor(friend, new Message("Friends", Color.BLUE, "§7Je bent nu vrienden met " + omp.getName() + "§7.", "§7You are now friends with " + omp.getName() + "§7."));
    }

    public void onFriendRemoval(CachedPlayer friend) {
        if (!friends.contains(friend.getUUID()))
            return;

        FriendsData data = getDataFor(friend.getUUID());

        removeFromFriends(friend.getUUID());
        data.removeFromFriends(uuid);

        OMPlayer omp = OMPlayer.getPlayer(uuid);
        String name = friend.getRankPrefixColor().getChatColor() + friend.getPlayerName();

        omp.sendMessage("Friends", Color.LIME, "§7Je bent geen vrienden meer met " + name + "§7.", "§7You are no longer friends with " + name + "§7.");

        forceUpdateFor(friend, new Message("Friends", Color.BLUE, "§7Je bent geen vrienden meer met " + omp.getName() + "§7.", "§7You are no longer friends with " + omp.getName() + "§7."));
    }

    private void forceUpdateFor(CachedPlayer friend) {
        forceUpdateFor(friend, null);
    }

    private void forceUpdateFor(CachedPlayer friend, Message message) {
        /* Player is on current server, so no need to update */
        OMPlayer omp = OMPlayer.getPlayer(friend.getUUID());
        if (omp != null) {
            if (message != null)
                omp.sendMessage(message);

            return;
        }

        /* Player is currently not online, so we do not send the update */
        Server server = friend.getServer();
        if (server == null)
            return;

        /* Force update on another server */
        messageHandler.dataTransfer(PluginMessage.UPDATE_FRIENDS, server, friend.getUUID().toString());

        if (message != null)
            messageHandler.dataTransfer(PluginMessage.MESSAGE_PLAYER, friend.getUUID().toString(), message.lang(friend.getLanguage()).replaceAll("§", "&"));
    }

    /* Friends */
    public List<UUID> getFriends(boolean withFavorites) {
        if (withFavorites)
            return friends;

        List<UUID> friends = new ArrayList<>(this.friends);
        friends.removeAll(favoriteFriends);

        return friends;
    }

    private void addToFriends(UUID friend) {
        friends.add(friend);
        invites.remove(friend);
        sentRequests.remove(friend);

        Database.get().update(table, new Set[] {
            new Set(FRIENDS, Serializer.serializeUUIDList(friends)),
            new Set(SENT_INVITES, Serializer.serializeUUIDList(sentRequests)),
            new Set(INVITES, Serializer.serializeUUIDList(invites))
        }, new Where(UUID, uuid.toString()));
    }

    private void removeFromFriends(UUID friend) {
        friends.remove(friend);
        favoriteFriends.remove(friend);

        Database.get().update(table, new Set[] {
            new Set(FRIENDS, Serializer.serializeUUIDList(friends)),
            new Set(FAVORITE, Serializer.serializeUUIDList(favoriteFriends))
        }, new Where(UUID, uuid.toString()));
    }

    /* Favorites */
    public List<UUID> getFavoriteFriends() {
        return favoriteFriends;
    }

    private void addToFavoriteFriends(UUID friend) {
        favoriteFriends.add(friend);
        updateFavoriteFriends();
    }

    private void removeFromFavoriteFriends(UUID friend) {
        favoriteFriends.remove(friend);
        updateFavoriteFriends();
    }

    private void updateFavoriteFriends() {
        Database.get().update(table, new Set(FAVORITE, Serializer.serializeUUIDList(favoriteFriends)), new Where(UUID, uuid.toString()));
    }

    /* Requests */
    public List<UUID> getSentRequests() {
        return sentRequests;
    }

    private void clearRequest(CachedPlayer friend) {
        removeSentRequests(friend.getUUID());
        getDataFor(friend.getUUID()).removeInvites(uuid);

        forceUpdateFor(friend);
    }

    private void addSentRequests(UUID friend) {
        sentRequests.add(friend);

        /* If database limit exceeded, clear first request for both players */
        if (sentRequests.size() > DATABASE_FRIEND_LIMIT)
            clearRequest(CachedPlayer.getPlayer(sentRequests.get(0)));

        updateSentRequests();
    }

    private void removeSentRequests(UUID friend) {
        sentRequests.remove(friend);
        updateSentRequests();
    }

    private void updateSentRequests() {
        Database.get().update(table, new Set[] { new Set(SENT_INVITES, Serializer.serializeUUIDList(sentRequests)) }, new Where(UUID, uuid.toString()));
    }

    /* Invites */
    public List<UUID> getInvites() {
        return invites;
    }

    private void clearInvite(CachedPlayer friend) {
        removeInvites(friend.getUUID());
        getDataFor(friend.getUUID()).removeSentRequests(uuid);

        OMPlayer omp = OMPlayer.getPlayer(this.uuid);
        forceUpdateFor(friend, new Message("Friends", Color.BLUE, "§7" + omp.getName() + "§7 heeft je vriendschapsverzoek afgewezen.", "§7" + omp.getName() + "§7 denied your friend request."));
    }

    private void addInvites(UUID friend) {
        invites.add(friend);
        updateInvites();
    }

    private void removeInvites(UUID friend) {
        invites.remove(friend);
        updateInvites();
    }

    private void updateInvites() {
        Database.get().update(table, new Set(INVITES, Serializer.serializeUUIDList(invites)), new Where(UUID, uuid.toString()));
    }

    /* Get Stats for player */
    private FriendsData getDataFor(UUID uuid) {
        OMPlayer omp = OMPlayer.getPlayer(uuid);

        FriendsData data;
        if (omp != null) {
            data = (FriendsData) omp.getData(Type.FRIENDS);
        } else {
            data = new FriendsData(uuid);
            data.load();
        }

        return data;
    }
}
