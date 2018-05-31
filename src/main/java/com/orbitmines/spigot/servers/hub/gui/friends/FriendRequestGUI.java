package com.orbitmines.spigot.servers.hub.gui.friends;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.handlers.CachedPlayer;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.data.FriendsData;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FriendRequestGUI extends GUI {

    private final int REQUESTS_PER_PAGE = 36;
    private final int NEW_PER_PAGE = REQUESTS_PER_PAGE / 9;

    private int page;

    public FriendRequestGUI() {
        this(0);
    }

    public FriendRequestGUI(int page) {
        this.page = page;

        newInventory(54, "§0§lFriend Requests");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        FriendsData data = (FriendsData) omp.getData(Data.Type.FRIENDS);
        List<UUID> invites = data.getInvites();
        List<UUID> sentRequests = data.getSentRequests();

        {
            add(0, 4, new ItemInstance(new PlayerSkullBuilder(() -> omp.getName(true), 1, "§b§lBack to Friends").build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    new FriendGUI().open(omp);
                }
            });
        }

        List<UUID> requests = new ArrayList<>(sentRequests);
        requests.addAll(invites);

        int slot = 18;
        for (UUID uuid : getFriendsForPage(requests)) {
            if (uuid != null) {
                boolean sent = sentRequests.contains(uuid);

                CachedPlayer friend = CachedPlayer.getPlayer(uuid);
                ItemBuilder item = new ItemBuilder(sent ? Material.EMPTY_MAP : Material.PAPER, 1, 0, friend.getRankPrefixColor().getChatColor() + friend.getPlayerName());
                List<String> lore = item.getLore();

                if (sent) {
                    lore.add(omp.lang("§7§oUitgaand Vriendschapsverzoek", "§7§oOutgoing Friend Request"));
                    lore.add("");
                    lore.add(omp.lang("§cKlik hier om te verwijderen.", "§cClick here to remove."));

                    add(slot, new ItemInstance(item.build()) {
                        @Override
                        public void onClick(InventoryClickEvent event, OMPlayer omp) {
                            data.onRequestCancel(friend);

                            if (data.getInvites().size() != 0 || data.getSentRequests().size() != 0)
                                reopen(omp);
                            else
                                new FriendGUI().open(omp);
                        }
                    });
                } else {
                    lore.add(omp.lang("§7§oInkomend Vriendschapsverzoek", "§7§oIncoming Friend Request"));
                    lore.add("");
                    lore.add(omp.lang("§aKlik hier om te accepteren/weigeren.", "§aClick here to add or remove."));

                    add(slot, new ItemInstance(item.build()) {
                        @Override
                        public void onClick(InventoryClickEvent event, OMPlayer omp) {
                            new FriendRequestDetailsGUI(friend).open(omp);
                        }
                    });
                }
            } else {
                inventory.setItem(slot, null);
            }

            slot++;
        }

        if (page != 0)
            add(45, new ItemInstance(new ItemBuilder(Material.SIGN, 1, 0, omp.lang("« §7Meer Vriendschapsverzoeken", "« §7More Friend Requests")).build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    page--;
                    reopen(omp);
                }
            });
        else
            inventory.setItem(45, null);

        if (canHaveMorePages(invites.size() + sentRequests.size()))
            add(53, new ItemInstance(new ItemBuilder(Material.SIGN, 1, 0, omp.lang("§7Meer Vriendschapsverzoeken »", "§7More Friend Requests »")).build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    page++;
                    reopen(omp);
                }
            });
        else
            inventory.setItem(53, null);

        return true;
    }

    @Override
    protected void onClick(InventoryClickEvent event, OMPlayer omp) {

    }

    private UUID[] getFriendsForPage(List<UUID> friends) {
        UUID[] pageFriends = new UUID[REQUESTS_PER_PAGE];

        for (int i = 0; i < REQUESTS_PER_PAGE; i++) {
            if (friends.size() > i)
                pageFriends[i] = friends.get(i);
        }

        if (page != 0) {
            for (int i = 0; i < page; i++) {
                for (int j = 0; j < REQUESTS_PER_PAGE; j++) {
                    int check = -1;
                    if ((j + 1) % 9 == 0)
                        check = (j + 1) / 9;

                    if (check != -1) {
                        int next = REQUESTS_PER_PAGE + check + (NEW_PER_PAGE * i);
                        pageFriends[j] = friends.size() > next ? friends.get(next) : null;
                    } else {
                        pageFriends[j] = pageFriends[j + 1];
                    }
                }
            }
        }

        return pageFriends;
    }

    private boolean canHaveMorePages(int count) {
        if (count <= REQUESTS_PER_PAGE)
            return false;

        int maxPage = count % NEW_PER_PAGE;
        maxPage = maxPage != 0 ? (count - REQUESTS_PER_PAGE + (NEW_PER_PAGE - maxPage)) / NEW_PER_PAGE : (count - REQUESTS_PER_PAGE) / NEW_PER_PAGE;

        return maxPage > page;
    }
}
