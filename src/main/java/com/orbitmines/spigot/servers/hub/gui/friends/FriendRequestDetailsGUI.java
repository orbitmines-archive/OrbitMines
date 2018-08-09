package com.orbitmines.spigot.servers.hub.gui.friends;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.data.FriendsData;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import com.orbitmines.spigot.servers.hub.handlers.HubPlayer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class FriendRequestDetailsGUI extends GUI {

    private CachedPlayer friend;

    public FriendRequestDetailsGUI(CachedPlayer friend) {
        this.friend = friend;

        newInventory(27, "§0§lFriend - " + friend.getPlayerName());
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        FriendsData data = (FriendsData) omp.getData(Data.Type.FRIENDS);

        add(1, 1, new ItemInstance(new ItemBuilder(Material.BOOK, 1, omp.lang("§a§lAccepteer Vriendschapsverzoek", "§a§lAccept Friend Request")).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                data.onAccept(friend);

                if (data.getInvites().size() != 0 || data.getSentRequests().size() != 0)
                    new FriendRequestGUI().open(omp);
                else
                    new FriendGUI().open(omp);

                if (omp instanceof HubPlayer)
                    ((HubPlayer) omp).updatePlayerVisibility(OMPlayer.getPlayers());
            }
        });

        add(1, 4, new ItemInstance(new PlayerSkullBuilder(() -> omp.getName(true), 1, omp.lang("§b« Terug naar Vrienden", "§b« Back to Friends")).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new FriendRequestGUI().open(omp);
            }
        });

        add(1, 7, new ItemInstance(new ItemBuilder(Material.BARRIER, 1, omp.lang("§c§lWeiger Vriendschapsverzoek", "§c§lDeny Friend Request")).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                data.onDeny(friend);

                if (data.getInvites().size() != 0 || data.getSentRequests().size() != 0)
                    new FriendRequestGUI().open(omp);
                else
                    new FriendGUI().open(omp);

                if (omp instanceof HubPlayer)
                    ((HubPlayer) omp).updatePlayerVisibility(OMPlayer.getPlayers());
            }
        });

        return true;
    }
}
