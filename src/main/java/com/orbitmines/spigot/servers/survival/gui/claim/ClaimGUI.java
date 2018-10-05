package com.orbitmines.spigot.servers.survival.gui.claim;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.api.Server;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.api.utils.uuid.UUIDUtils;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.data.FriendsData;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import com.orbitmines.spigot.api.nms.anvilgui.AnvilNms;
import com.orbitmines.spigot.api.utils.ColorUtils;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.claim.Claim;
import org.bukkit.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ClaimGUI extends GUI {

    private final int PLAYERS_PER_PAGE = 18;
    private final int NEW_PER_PAGE = PLAYERS_PER_PAGE / 9;

    private Survival survival;
    private Claim.Permission permission;
    private Claim claim;
    private int page;

    public ClaimGUI(Survival survival, Claim claim) {
        this(survival, claim, 0);
    }

    public ClaimGUI(Survival survival, Claim claim, int page) {
        this.survival = survival;
        permission = null;
        this.claim = claim;
        this.page = page;

        newInventory(54, "§0§l" + claim.getName());
    }

    @Override
    protected boolean onOpen(OMPlayer player) {
        SurvivalPlayer omp = (SurvivalPlayer) player;

        add(0, 4, new ItemInstance(getClaimIcon(survival, omp, claim).addLore("").addLore(omp.lang("§aKlik hier om te hernoemen.", "§aClick here to rename.")).build()) {
            @Override
            public void onClick(InventoryClickEvent e, OMPlayer omp) {
                AnvilNms anvil = survival.getOrbitMines().getNms().anvilGui(omp.getPlayer(), (event) -> {
                    if (event.getSlot() != AnvilNms.AnvilSlot.OUTPUT) {
                        event.setWillClose(false);
                        event.setWillDestroy(false);
                        return;
                    }

                    String claimName = event.getName();

                    if (claimName.length() > 20) {
                        event.setWillClose(false);
                        event.setWillDestroy(false);
                        omp.sendMessage("Claim", Color.RED, "§7Je mag maximaal maar §a20 karakters§7 gebruiken.", "§7You're only allowed to use §a20 characters§7.");
                        return;
                    }

                    for (int i = 0; i < claimName.length(); i++) {
                        char c = claimName.charAt(i);
                        if (!Character.isAlphabetic(c) && !Character.isDigit(c) && !Character.isSpaceChar(c)) {
                            event.setWillClose(false);
                            event.setWillDestroy(false);
                            omp.sendMessage("Claim", Color.RED, "§7Je §aclaim naam§7 kan alleen maar bestaan uit §aletters§7, §anummers§7 en §aspaties§7.", "§7Your §aclaim name§7 can only contain §aalphabetic§7 and §anumeric§7 characters and §aspaces§7.");
                            return;
                        }
                    }

                    event.setWillClose(true);
                    event.setWillDestroy(true);

                    claim.setName(claimName);
                    survival.getClaimHandler().saveClaim(claim);
                }, new AnvilNms.AnvilCloseEvent() {
                    @Override
                    public void onClose() {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                reopen(omp);
                            }
                        }.runTaskLater(survival.getOrbitMines(), 1);
                    }
                });

                anvil.getItems().put(AnvilNms.AnvilSlot.INPUT_LEFT, new ItemBuilder(Material.STONE_HOE, 1, claim.getName()).build());

                anvil.open();
            }
        });

        /* Teleport for Admins */
        if (survival.canEditOtherClaims(omp)) {
            int x1 = claim.getCorner1().getBlockX();
            int x2 = claim.getCorner2().getBlockX();
            int z1 = claim.getCorner1().getBlockZ();
            int z2 = claim.getCorner2().getBlockZ();

            int width = (Math.abs(x1 - x2) + 1);
            int height = (Math.abs(z1 - z2) + 1);

            int x = x1 > x2 ? x1 - (width / 2) : x1 + (width / 2);
            int z = z1 > z2 ? z1 - (height / 2) : z1 + (height / 2);
            int y = claim.getCorner1().getWorld().getHighestBlockYAt(x, z) + 1;

            add(0, 5, new ItemInstance(new ItemBuilder(Material.ENDER_PEARL, 1, omp.lang("§3§lTeleport naar Claim", "§3§lTeleport to Claim")).build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    omp.getPlayer().teleport(new Location(claim.getCorner1().getWorld(), x, y, z));
                    omp.sendMessage("Claim", Color.LIME, "Geteleporteerd naar §a§lClaim #" + claim.getId() + "§7.", "Teleported to §a§lClaim #" + claim.getId() + "§7.");

                    omp.playSound(Sound.ENTITY_ENDERMAN_TELEPORT);
                }
            });
        }

        add(0, 1, new ItemInstance(new ItemBuilder(Material.WRITABLE_BOOK, 1, omp.lang("§a§lVoeg Speler Toe", "§a§lAdd Player")).build()) {
            @Override
            public void onClick(InventoryClickEvent e, OMPlayer omp) {
                AnvilNms anvil = survival.getOrbitMines().getNms().anvilGui(omp.getPlayer(), (event) -> {
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

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                new ClaimPlayerDetailsGUI(survival, claim, CachedPlayer.getPlayer(uuid)).open(omp);
                            }
                        }.runTaskLater(survival.getOrbitMines(), 2);
                    } else {
                        event.setWillClose(false);
                        event.setWillDestroy(false);
                        omp.sendMessage("Claim", Color.RED, "§7Kan die speler niet vinden.", "§7That player cannot be found.");
                    }
                }, new AnvilNms.AnvilCloseEvent() {
                    @Override
                    public void onClose() {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                reopen(omp);
                            }
                        }.runTaskLater(survival.getOrbitMines(), 1);
                    }
                });

                anvil.getItems().put(AnvilNms.AnvilSlot.INPUT_LEFT, new PlayerSkullBuilder(() -> omp.getName(true), 1, omp.lang("Speler Naam", "Player Name")).build());

                anvil.open();
            }
        });

        {
            ItemBuilder item = new ItemBuilder(Material.BARRIER, 1, omp.lang("§c§lVerwijder Claim", "§c§lRemove Claim"));

            add(0, 7, new ItemInstance(item.build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    new ClaimRemovalGUI(survival, claim).open(omp);
                }
            });
        }

        int index = 0;
        for (Claim.Permission permission : Claim.Permission.values()) {
            String color = this.permission == permission ? "§a" : "§7";
            ItemBuilder item = permission.getIcon().setDisplayName(color + "§l" + permission.getName().lang(omp.getLanguage()));

            if (this.permission == permission)
                item.glow();

            for (Message desc : permission.getDescription()) {
                item.addLore("§7- " + color + desc.lang(omp.getLanguage()));
            }

            for (Claim.Permission perm : Claim.Permission.values()) {
                if (permission.ordinal() > perm.ordinal())
                    item.addLore("§7- " + color + "§l" + perm.getName().lang(omp.getLanguage()));
            }

            add(2, 3 + index, new ItemInstance(item.build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    if (ClaimGUI.this.permission == permission)
                        ClaimGUI.this.permission = null;
                    else
                        ClaimGUI.this.permission = permission;

                    omp.playSound(Sound.UI_BUTTON_CLICK);

                    reopen(omp);
                }
            });

            index++;
        }

        Set<UUID> players;
        if (this.permission == null) {
            players = new HashSet<>(claim.getMembers().keySet());
        } else {
            players = new HashSet<>();

            for (UUID member : claim.getMembers().keySet()) {
                if (claim.getPermission(member) == this.permission)
                    players.add(member);
            }
        }

        /* Order List Online, Offline */
        List<UUID> ordered = new ArrayList<>();

        for (UUID uuid : players) {
            CachedPlayer member = CachedPlayer.getPlayer(uuid);
            Server server = member.getServer();

            if (server != null)
                ordered.add(uuid);
        }
        players.removeAll(ordered); /* Remove all online friends from list */
        ordered.addAll(players); /* Add all offline friends to list */

        int slot = 27;

        for (UUID uuid : getPlayersForPage(ordered)) {
            if (uuid != null) {
                CachedPlayer member = CachedPlayer.getPlayer(uuid);
                String name = member.getRankPrefixColor().getChatColor() + member.getPlayerName();

                PlayerSkullBuilder item = new PlayerSkullBuilder(member::getPlayerName, 1, name, new ArrayList<>());
                ItemBuilder offlineItem = new ItemBuilder(Material.SKELETON_SKULL, 1, name, new ArrayList<>());
                List<String> lore = item.getLore();

                Server server = member.getServer();

                Claim.Permission permission = claim.getPermission(uuid);

                if (permission != null)
                    lore.add("§7Trust: §a§l" + omp.lang(permission.getName()));

                lore.add("");

                if (server != null) {
                    lore.add("§7Status: " + Server.Status.ONLINE.getDisplayName());
                    lore.add("§7Server: " + server.getDisplayName());
                } else {
                    lore.add("§7Status: " + Server.Status.OFFLINE.getDisplayName());
                    lore.add("§7" + omp.lang("Laatst gezien", "Last seen") + ": §b§l" + member.getLastOnlineInTimeUnit(omp.getLanguage()) + " " + omp.lang("geleden", "ago"));
                    offlineItem.setLore(lore);
                }
                lore.add("");
                lore.add(omp.lang("§aKlik hier om te veranderen.", "§aClick here to change."));

                add(slot, new ItemInstance(item.build()) {
                    @Override
                    public void onClick(InventoryClickEvent event, OMPlayer omp) {
                        new ClaimPlayerDetailsGUI(survival, claim, member).open(omp);
                    }
                });
            } else {
                clear(slot);
            }

            slot++;
        }

        /* Add all (favorite) friends to a certain permission category */
        FriendsData data = (FriendsData) omp.getData(Data.Type.FRIENDS);
        setupAddFriends(5, 3, FriendType.NORMAL, data, omp);
        setupAddFriends(5, 5, FriendType.FAVORITE, data, omp);

        if (page != 0)
            add(5, 0, new ItemInstance(new PlayerSkullBuilder(() -> "Lime Arrow Left", 1, omp.lang("§7« Meer Spelers", "§7« More Players")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjUzNDc0MjNlZTU1ZGFhNzkyMzY2OGZjYTg1ODE5ODVmZjUzODlhNDU0MzUzMjFlZmFkNTM3YWYyM2QifX19").build()) {
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
            add(5, 8, new ItemInstance(new PlayerSkullBuilder(() -> "Lime Arrow Right", 1, omp.lang("§7Meer Spelers »", "§7More Players »")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGVmMzU2YWQyYWE3YjE2NzhhZWNiODgyOTBlNWZhNWEzNDI3ZTVlNDU2ZmY0MmZiNTE1NjkwYzY3NTE3YjgifX19").build()) {
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

    private void setupAddFriends(int row, int slot, FriendType type, FriendsData data, SurvivalPlayer omp) {
        if (this.permission == null) {
            clear(row, slot);
            return;
        }

        List<UUID> toAdd = type.getUUIDList(data);

        if (toAdd.size() == 0) {
            clear(row, slot);
            return;
        }

        for (UUID friend : new ArrayList<>(toAdd)) {
            if (claim.hasPermission(friend, this.permission))
                toAdd.remove(friend);
        }

        if (toAdd.size() == 0) {
            clear(row, slot);
            return;
        }

        ItemBuilder item = new ItemBuilder(ColorUtils.getStainedGlassPaneMaterial(type.color), 1, type.color.getChatColor() + "§l" + omp.lang(type.title) + " " + omp.lang("aan", "to") + " §a§l" + omp.lang(this.permission.getName()));

        int showCount = 5;
        for (int i = 0; i < showCount; i++) {
            if (toAdd.size() < i + 1)
                continue;

            CachedPlayer player = CachedPlayer.getPlayer(toAdd.get(i));
            item.addLore("§7- " + player.getRankPrefixColor().getChatColor() + player.getPlayerName());
        }

        if (toAdd.size() > showCount)
            item.addLore("§7... " + omp.lang("en", "and") + " " + (toAdd.size() - showCount) + " " + omp.lang("meer", "more"));

        add(row, slot, new ItemInstance(item.build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new ClaimFriendsAdditionGUI(survival, claim, ClaimGUI.this.permission, type, toAdd).open(omp);
            }
        });
    }

    private UUID[] getPlayersForPage(List<UUID> players) {
        UUID[] pagePlayers = new UUID[PLAYERS_PER_PAGE];

        for (int i = 0; i < PLAYERS_PER_PAGE; i++) {
            if (players.size() > i)
                pagePlayers[i] = players.get(i);
        }

        if (page != 0) {
            for (int i = 0; i < page; i++) {
                for (int j = 0; j < PLAYERS_PER_PAGE; j++) {
                    int check = -1;
                    if ((j + 1) % 9 == 0)
                        check = (j + 1) / 9;

                    if (check != -1) {
                        int next = PLAYERS_PER_PAGE + check + (NEW_PER_PAGE * i) - 1;
                        pagePlayers[j] = players.size() > next ? players.get(next) : null;
                    } else {
                        pagePlayers[j] = pagePlayers[j + 1];
                    }
                }
            }
        }

        return pagePlayers;
    }

    private boolean canHaveMorePages(List<UUID> players) {
        int playerAmount = players.size();

        if (playerAmount <= PLAYERS_PER_PAGE)
            return false;

        int maxPage = playerAmount % NEW_PER_PAGE;
        maxPage = maxPage != 0 ? (playerAmount - PLAYERS_PER_PAGE + (NEW_PER_PAGE - maxPage)) / NEW_PER_PAGE : (playerAmount - PLAYERS_PER_PAGE) / NEW_PER_PAGE;

        return maxPage > page;
    }

    public static ItemBuilder getClaimIcon(Survival survival, OMPlayer omp, Claim claim) {
        int x1 = claim.getCorner1().getBlockX();
        int x2 = claim.getCorner2().getBlockX();
        int z1 = claim.getCorner1().getBlockZ();
        int z2 = claim.getCorner2().getBlockZ();

        int width = (Math.abs(x1 - x2) + 1);
        int height = (Math.abs(z1 - z2) + 1);
        int area = width * height;

        int x = x1 > x2 ? x1 - (width / 2) : x1 + (width / 2);
        int z = z1 > z2 ? z1 - (height / 2) : z1 + (height / 2);

        World world = claim.getCorner1().getWorld();
        String worldName;
        if (world.equals(survival.getWorld()))
            worldName = "§a§lOverworld";
        else if (world.equals(survival.getWorld_nether()))
            worldName = "§c§lThe Nether";
        else if (world.equals(survival.getWorld_the_end()))
            worldName = "§8§lThe End";
        else
            worldName = "§f§lUnknown";

        String color = ChatColor.getLastColors(worldName);

        return new ItemBuilder(Material.STONE_HOE, 1, "§a§l" + claim.getName(),
                "§7Claim Id: §a§l#" + NumberUtils.locale(claim.getId()),
                "§7" + omp.lang("Gemaakt door", "Created by") + ": " + claim.getOwnerName(),
                "§7" + omp.lang("Gemaakt op", "Created on") + ": §a§l" + DateUtils.SIMPLE_FORMAT.format(claim.getCreatedOn()),
                "",
                "§7" + omp.lang("Oppervlak", "Area") + ": §9§l" + NumberUtils.locale(width) + " x " + NumberUtils.locale(height),
                "§7Blocks: §9§l" + NumberUtils.locale(area),
                "§7World: " + worldName,
                "§7XZ: " + color + NumberUtils.locale(x) + " §7/ " + color + NumberUtils.locale(z)
        ).addFlag(ItemFlag.HIDE_ATTRIBUTES);
    }

    public enum FriendType {

        NORMAL(new Message("Voeg al je Vrienden toe", "Add all Friends"), Color.AQUA) {
            @Override
            public List<UUID> getUUIDList(FriendsData data) {
                return new ArrayList<>(data.getFriends(true));
            }
        },
        FAVORITE(new Message("Voeg al je Favoriete Vrienden toe", "Add all Favorite Friends"), Color.ORANGE) {
            @Override
            public List<UUID> getUUIDList(FriendsData data) {
                return new ArrayList<>(data.getFavoriteFriends());
            }
        };

        private final Message title;
        private final Color color;

        FriendType(Message title, Color color) {
            this.title = title;
            this.color = color;
        }

        public Message getTitle() {
            return title;
        }

        public Color getColor() {
            return color;
        }

        public List<UUID> getUUIDList(FriendsData data) {
            throw new IllegalStateException();
        }
    }
}
