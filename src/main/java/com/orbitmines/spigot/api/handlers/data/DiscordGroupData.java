package com.orbitmines.spigot.api.handlers.data;

import com.orbitmines.api.*;
import com.orbitmines.api.database.*;
import com.orbitmines.discordbot.handlers.DiscordSquad;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.PluginMessageHandler;
import com.orbitmines.spigot.api.utils.Serializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.orbitmines.api.database.tables.TableDiscordGroupData.*;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class DiscordGroupData extends Data {

    private PluginMessageHandler messageHandler;

    private final int DATABASE_FRIEND_LIMIT = 1500;

    private UUID selected;
    private List<UUID> sentRequests;
    private List<UUID> invites;

    public DiscordGroupData(UUID uuid) {
        super(Table.DISCORD_GROUP_DATA, Type.DISCORD_GROUPS, uuid);

        messageHandler = OrbitMines.getInstance().getServerHandler().getMessageHandler();

        selected = null;
        sentRequests = new ArrayList<>();
        invites = new ArrayList<>();
    }

    @Override
    public void load() {
        if (!Database.get().contains(table, new Column[] { UUID }, new Where(UUID, getUUID().toString()))) {
            Database.get().insert(table, uuid.toString(), selected == null ? "" : selected.toString(), Serializer.serializeUUIDList(sentRequests), Serializer.serializeUUIDList(invites));
        } else {
            Map<Column, String> values = Database.get().getValues(table, new Column[] {
                    SELECTED,
                    SENT_INVITES,
                    INVITES
            }, new Where(UUID, getUUID().toString()));

            selected = !values.get(SELECTED).equals("") ? java.util.UUID.fromString(values.get(SELECTED)) : null;
            sentRequests = Serializer.parseUUIDList(values.get(SENT_INVITES));
            invites = Serializer.parseUUIDList(values.get(INVITES));
        }
    }

    /* Selected */
    public java.util.UUID getSelected() {
        return selected;
    }

    public void setSelected(java.util.UUID selected) {
        this.selected = selected;

        Database.get().update(table, new Set(SELECTED, selected == null ? "" : selected.toString()), new Where(UUID, getUUID().toString()));

        messageHandler.dataTransfer(PluginMessage.UPDATE_DISCORD_GROUP_DATA, CachedPlayer.getPlayer(getUUID()).getServer(), getUUID().toString());
    }

    /* Inventory Interactions */
    public void onAccept(CachedPlayer owner) {
        onMemberAddition(owner);
        forceUpdateFor(owner);
    }

    public void onDeny(CachedPlayer owner) {
        clearInvite(owner);
    }

    public void onRequestCancel(CachedPlayer member) {
        clearRequest(member);
    }

    public void onRequest(UUID member) {
        OMPlayer omp = OMPlayer.getPlayer(uuid);

        DiscordSquad group = DiscordSquad.getFromDatabase(OrbitMines.getInstance().getServerHandler().getDiscord(), this.uuid);

        if (member.toString().equals(this.uuid.toString())) {
            omp.sendMessage("Discord", Color.RED, "§7Je kan jezelf niet als een member toevoegen. :(", "§7You cannot add yourself as a member. :(");
        } else if (sentRequests.contains(member)) {
            CachedPlayer f = CachedPlayer.getPlayer(member);
            String name = f.getRankPrefixColor().getChatColor() + f.getPlayerName();

            omp.sendMessage("Discord", Color.RED, "§7Je hebt al een verzoek naar " + name + "§7gestuurd!", "§7You have already sent a request to " + name + "§7!");
        } else if (group.getMembers().contains(member)) {
            CachedPlayer f = CachedPlayer.getPlayer(member);
            String name = f.getRankPrefixColor().getChatColor() + f.getPlayerName();

            omp.sendMessage("Discord", Color.RED, name + "§7 zit al in " + group.getDisplayName() + "§7.", name + "§7is already in " + group.getDisplayName() + "§7.");
        } else if (group.getMembers().size() >= DATABASE_FRIEND_LIMIT) {
            omp.sendMessage("Discord", Color.RED, "§7Je hebt het member limiet bereikt!", "§7You have exceeded the member limit.");
        } else {
            CachedPlayer f = CachedPlayer.getPlayer(member);
            DiscordGroupData data = getDataFor(member);

            addSentRequests(member);
            data.addInvites(this.uuid);

            String name = f.getRankPrefixColor().getChatColor() + f.getPlayerName();
            omp.sendMessage("Discord", Color.LIME, "§7Je hebt een verzoek gestuurd naar " + name + "§7 om " + group.getDisplayName() + " §7te joinen.", "§7You have sent a request to " + name + "§7 to join " + group.getDisplayName() + "§7.");

            forceUpdateFor(f, new Message("Discord", Color.BLUE, omp.getName() + "§7 heeft je een verzoek gestuurd om " + group.getDisplayName() + " §7te joinen.", omp.getName() + " §7has sent you a request to join " + group.getDisplayName() + "§7!"));
        }
    }

    public void onMemberAddition(CachedPlayer owner) {
        DiscordSquad group = DiscordSquad.getFromDatabase(OrbitMines.getInstance().getServerHandler().getDiscord(), owner.getUUID());

        if (group.getMembers().contains(getUUID()))
            return;

        DiscordGroupData data = getDataFor(owner.getUUID());

        removeInvites(data.getUUID());
        data.removeSentRequests(uuid);

        messageHandler.dataTransfer(PluginMessage.DISCORD_GROUP_ACTION, owner.getUUID().toString(), "ADD", uuid.toString());
    }

    public void onMemberRemoval(CachedPlayer member, boolean forced) {
        DiscordSquad group = DiscordSquad.getFromDatabase(OrbitMines.getInstance().getServerHandler().getDiscord(), uuid);

        if (!group.getMembers().contains(member.getUUID()))
            return;

        messageHandler.dataTransfer(PluginMessage.DISCORD_GROUP_ACTION, uuid.toString(), "REMOVE", member.getUUID().toString(), forced + "");

        forceUpdateFor(member);
    }

    private void forceUpdateFor(CachedPlayer friend) {
        forceUpdateFor(friend, null);
    }

    private void forceUpdateFor(CachedPlayer friend, Message message) {
        /* Player is currently not online, so we do not send the update */
        Server server = friend.getServer();
        if (server == null)
            return;

        /* Force update on another server */
        messageHandler.dataTransfer(PluginMessage.UPDATE_DISCORD_GROUP_DATA, server, friend.getUUID().toString());

        if (message != null)
            messageHandler.dataTransfer(PluginMessage.MESSAGE_PLAYER, friend.getUUID().toString(), message.lang(friend.getLanguage()).replaceAll("§", "&"));
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

    private void removeSentRequests(UUID member) {
        sentRequests.remove(member);
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

        DiscordSquad group = DiscordSquad.getFromDatabase(OrbitMines.getInstance().getServerHandler().getDiscord(), friend.getUUID());

        forceUpdateFor(friend, new Message("Discord", Color.BLUE, omp.getName() + "§7 heeft je verzoek om " + group.getDisplayName() + " §7te joinen afgewezen.", omp.getName() + "§7 denied your request to join " + group.getDisplayName() + "§7."));
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
    public DiscordGroupData getDataFor(UUID uuid) {
        OMPlayer omp = OMPlayer.getPlayer(uuid);

        DiscordGroupData data;
        if (omp != null) {
            data = (DiscordGroupData) omp.getData(Type.DISCORD_GROUPS);
        } else {
            data = new DiscordGroupData(uuid);
            data.load();
        }

        return data;
    }
}
