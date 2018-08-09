package com.orbitmines.spigot.servers.hub.gui.friends;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Server;
import com.orbitmines.api.utils.uuid.UUIDUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.api.CachedPlayer;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.data.FriendsData;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import com.orbitmines.spigot.api.nms.anvilgui.AnvilNms;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FriendGUI extends GUI {

    private final int FRIENDS_PER_PAGE = 27;
    private final int NEW_PER_PAGE = FRIENDS_PER_PAGE / 9;

    private OrbitMines orbitMines;
    private Type type;
    private int page;

    public FriendGUI() {
        this(0);
    }

    public FriendGUI(int page) {
        orbitMines = OrbitMines.getInstance();
        type = Type.NORMAL;
        this.page = page;

        newInventory(54, "§0§lFriends");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        FriendsData data = (FriendsData) omp.getData(Data.Type.FRIENDS);
        List<UUID> friends = data.getFriends(false);
        List<UUID> favorites = data.getFavoriteFriends();

        {
            ItemBuilder item = new ItemBuilder(Material.BOOK, 1, omp.lang("§7§lVriendschapsverzoeken", "§7§lPending Requests"), "§7  " + omp.lang("Inkomend", "Incoming") + " (§b§l" + data.getInvites().size() + "§7)", "§7  " + omp.lang("Uitgaand", "Outgoing") + " (§b§l" + data.getSentRequests().size() + "§7)");

            if (data.getInvites().size() != 0 || data.getSentRequests().size() != 0) {
                item.glow();

                add(0, 1, new ItemInstance(item.build()) {
                    @Override
                    public void onClick(InventoryClickEvent event, OMPlayer omp) {
                        new FriendRequestGUI().open(omp);
                    }
                });
            } else {
                add(0, 1, new EmptyItemInstance(item.build()));
            }
        }

        add(0, 3, new ItemInstance(new ItemBuilder(Material.WRITABLE_BOOK, 1, omp.lang("§b§lStuur een Vriendschapsverzoek", "§b§lSend Friend Request")).build()) {
            @Override
            public void onClick(InventoryClickEvent e, OMPlayer omp) {
                AnvilNms anvil = orbitMines.getNms().anvilGui(omp.getPlayer(), (event) -> {
                    if (event.getSlot() != AnvilNms.AnvilSlot.OUTPUT) {
                        event.setWillClose(false);
                        event.setWillDestroy(false);
                        return;
                    }

                    String playerName = event.getName();
                    UUID uuid = UUIDUtils.getUUID(playerName);

                    if (uuid != null && CachedPlayer.getPlayer(uuid) != null /* If the player joined OrbitMines before */) {
                        event.setWillClose(true);
                        event.setWillDestroy(true);

                        data.onRequest(uuid);
                    } else {
                        event.setWillClose(false);
                        event.setWillDestroy(false);
                        omp.sendMessage(omp.lang("Vrienden", "Friends"), Color.RED, "§7Kan die speler niet vinden.", "§7That player cannot be found.");
                    }
                }, new AnvilNms.AnvilCloseEvent() {
                    @Override
                    public void onClose() {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                reopen(omp);
                            }
                        }.runTaskLater(orbitMines, 1);
                    }
                });

                anvil.getItems().put(AnvilNms.AnvilSlot.INPUT_LEFT, new PlayerSkullBuilder(() -> omp.getName(true), 1, omp.lang("Speler Naam", "Player Name")).build());

                anvil.open();
            }
        });

        {
            ItemBuilder item = new ItemBuilder(Material.DIAMOND, 1, omp.lang("§6§lVoeg toe aan Favorieten", "§6§lFavorite Friend"));

            if (type == Type.FAVORITE)
                item.glow();

            add(0, 5, new ItemInstance(item.build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    if (type == Type.FAVORITE)
                        type = Type.NORMAL;
                    else
                        type = Type.FAVORITE;

                    reopen(omp);
                }
            });
        }

        {
            ItemBuilder item = new ItemBuilder(Material.BARRIER, 1, omp.lang("§c§lVerwijder Vriend", "§c§lRemove Friend"));

            if (type == Type.REMOVE)
                item.glow();

            add(0, 7, new ItemInstance(item.build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    if (type == Type.REMOVE)
                        type = Type.NORMAL;
                    else
                        type = Type.REMOVE;

                    reopen(omp);
                }
            });
        }

        /* Order List Favorite, Non-Favorite */
        List<UUID> offline = new ArrayList<>(favorites);
        offline.addAll(friends);

        /* Order List Online, Offline */
        List<UUID> ordered = new ArrayList<>();

        for (UUID uuid : offline) {
            CachedPlayer friend = CachedPlayer.getPlayer(uuid);
            Server server = type == Type.NORMAL ? friend.getServer() : null;

            if (server != null)
                ordered.add(uuid);
        }
        offline.removeAll(ordered); /* Remove all online friends from list */
        ordered.addAll(offline); /* Add all offline friends to list */

        int slot = 18;

        for (UUID uuid : getFriendsForPage(ordered)) {
            if (uuid != null) {
                CachedPlayer friend = CachedPlayer.getPlayer(uuid);
                String name = friend.getRankPrefixColor().getChatColor() + friend.getPlayerName();

                PlayerSkullBuilder item = new PlayerSkullBuilder(friend::getPlayerName, 1, name, new ArrayList<>());
                ItemBuilder offlineItem = new ItemBuilder(Material.SKELETON_SKULL, 1, name, new ArrayList<>());
                List<String> lore = item.getLore();

                Server server = type == Type.NORMAL ? friend.getServer() : null;

                switch (type) {

                    case NORMAL:
                        if (favorites.contains(uuid)) {
                            lore.add(omp.lang("§6§lFavoriete Vriend", "§6§lFavorite Friend"));
                            offlineItem.setMaterial(Material.WITHER_SKELETON_SKULL);
                        }

                        lore.add("");

                        if (server != null) {
                            lore.add("§7Status: " + Server.Status.ONLINE.getDisplayName());
                            lore.add("§7Server: " + server.getDisplayName());
                            lore.add("");
                            lore.add(omp.lang("§aKlik hier om te verbinden.", "§aClick here to connect."));
                        } else {
                            lore.add("§7Status: " + Server.Status.OFFLINE.getDisplayName());
                            lore.add("§7" + omp.lang("Laatst gezien", "Last seen") + ": §b§l" + friend.getLastOnlineInTimeUnit(omp.getLanguage()) + " " + omp.lang("geleden", "ago"));
                            offlineItem.setLore(lore);
                        }
                        break;
                    case REMOVE:
                        lore.add("");
                        lore.add(omp.lang("§cKlik hier om te verwijderen.", "§cClick here to remove."));
                        break;
                    case FAVORITE:
                        lore.add("");
                        if (favorites.contains(uuid))
                            lore.add(omp.lang("§cVerwijder van favorieten.", "§cRemove from favorites."));
                        else
                            lore.add(omp.lang("§6Voeg toe aan favorieten.", "§6Add to favorites."));
                        break;
                }

                ItemInstance itemInstance;

                switch (type) {

                    case NORMAL:
                        if (server != null)
                            itemInstance = new ItemInstance(item.build()) {
                                @Override
                                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                                    omp.connect(server, true);
                                    omp.getPlayer().closeInventory();
                                }
                            };
                        else
                            itemInstance = new EmptyItemInstance(offlineItem.build());
                        break;
                    case REMOVE:
                        itemInstance = new ItemInstance(item.build()) {
                            @Override
                            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                                data.onFriendRemoval(friend);

                                reopen(omp);
                            }
                        };
                        break;
                    case FAVORITE:
                        itemInstance = new ItemInstance(item.build()) {
                            @Override
                            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                                data.onFriendToggle(friend, !favorites.contains(uuid));

                                reopen(omp);
                            }
                        };
                        break;
                    default:
                        itemInstance = new EmptyItemInstance(item.build());
                        break;
                }

                add(slot, itemInstance);
            } else {
                clear(slot);
            }

            slot++;
        }

        if (page != 0)
            add(5, 0, new ItemInstance(new PlayerSkullBuilder(() -> "Light Blue Arrow Left", 1, omp.lang("§7« Meer Vrienden", "§7« More Friends")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM5NzExMjRiZTg5YWM3ZGM5YzkyOWZlOWI2ZWZhN2EwN2NlMzdjZTFkYTJkZjY5MWJmODY2MzQ2NzQ3N2M3In19fQ==").build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    page--;
                    omp.playSound(Sound.UI_BUTTON_CLICK);
                    reopen(omp);
                }
            });
        else
            clear(5, 0);

        if (canHaveMorePages(ordered))
            add(5, 8, new ItemInstance(new PlayerSkullBuilder(() -> "Light Blue Arrow Right", 1, omp.lang("§7Meer Vrienden »", "§7More Friends »")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjY3MWM0YzA0MzM3YzM4YTVjN2YzMWE1Yzc1MWY5OTFlOTZjMDNkZjczMGNkYmVlOTkzMjA2NTVjMTlkIn19fQ==").build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    page++;
                    omp.playSound(Sound.UI_BUTTON_CLICK);
                    reopen(omp);
                }
            });
        else
            clear(5, 8);

        return true;
    }

    private UUID[] getFriendsForPage(List<UUID> friends) {
        UUID[] pageFriends = new UUID[FRIENDS_PER_PAGE];

        for (int i = 0; i < FRIENDS_PER_PAGE; i++) {
            if (friends.size() > i)
                pageFriends[i] = friends.get(i);
        }

        if (page != 0) {
            for (int i = 0; i < page; i++) {
                for (int j = 0; j < FRIENDS_PER_PAGE; j++) {
                    int check = -1;
                    if ((j + 1) % 9 == 0)
                        check = (j + 1) / 9;

                    if (check != -1) {
                        int next = FRIENDS_PER_PAGE + check + (NEW_PER_PAGE * i) - 1;
                        pageFriends[j] = friends.size() > next ? friends.get(next) : null;
                    } else {
                        pageFriends[j] = pageFriends[j + 1];
                    }
                }
            }
        }

        return pageFriends;
    }

    private boolean canHaveMorePages(List<UUID> friends) {
        int friendAmount = friends.size();

        if (friendAmount <= FRIENDS_PER_PAGE)
            return false;

        int maxPage = friendAmount % NEW_PER_PAGE;
        maxPage = maxPage != 0 ? (friendAmount - FRIENDS_PER_PAGE + (NEW_PER_PAGE - maxPage)) / NEW_PER_PAGE : (friendAmount - FRIENDS_PER_PAGE) / NEW_PER_PAGE;

        return maxPage > page;
    }

    public enum Type {

        NORMAL,
        REMOVE,
        FAVORITE;

    }
}
