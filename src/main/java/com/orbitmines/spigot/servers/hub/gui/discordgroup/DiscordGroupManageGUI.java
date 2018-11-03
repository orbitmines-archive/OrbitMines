package com.orbitmines.spigot.servers.hub.gui.discordgroup;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.*;
import com.orbitmines.api.utils.uuid.UUIDUtils;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.handlers.DiscordSquad;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.OrbitMinesServer;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.data.DiscordGroupData;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import com.orbitmines.spigot.api.nms.anvilgui.AnvilNms;
import net.dv8tion.jda.core.entities.User;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DiscordGroupManageGUI extends GUI {

    private final int MEMBERS_PER_PAGE = 36;
    private final int NEW_PER_PAGE = MEMBERS_PER_PAGE / 9;

    private OrbitMines orbitMines;
    private int page;

    public DiscordGroupManageGUI() {
        this(0);
    }

    public DiscordGroupManageGUI(int page) {
        orbitMines = OrbitMines.getInstance();
        this.page = page;

        newInventory(54, "§0§lDiscord Squads");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        DiscordGroupData data = (DiscordGroupData) omp.getData(Data.Type.DISCORD_GROUPS);

        DiscordSquad group = DiscordSquad.getFromDatabase(orbitMines.getServerHandler().getDiscord(), omp.getUUID());

        {
            ItemBuilder item = new ItemBuilder(Material.BOOK, 1, omp.lang("§7§lUitnodigingen", "§7§lInvites"), "§7  " + omp.lang("Uitgaand", "Outgoing") + " (§9§l" + data.getSentRequests().size() + "§7)");

            if (data.getSentRequests().size() != 0) {
                item.glow();

                add(0, 1, new ItemInstance(item.build()) {
                    @Override
                    public void onClick(InventoryClickEvent event, OMPlayer omp) {
                        new DiscordGroupRequestGUI().open(omp);
                    }
                });
            } else {
                add(0, 1, new EmptyItemInstance(item.build()));
            }
        }

        add(0, 2, new ItemInstance(new ItemBuilder(Material.WRITABLE_BOOK, 1, "§9§l" + omp.lang("Stuur Join Verzoek", "Send Join Request")).build()) {
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
                        omp.sendMessage("Discord", Color.RED, "§7Kan die speler niet vinden.", "§7That player cannot be found.");
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
            ItemBuilder item = new PlayerSkullBuilder(() -> omp.getName(true), 1, group.getDisplayName());

            DiscordGroupGUI.setOnlineLore(item, group);

            item.addLore("");
            item.addLore("§9§o« " + omp.lang("Terug naar Overzicht", "Back to Overview"));

            add(0, 4, new ItemInstance(item.build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    new DiscordGroupGUI().open(omp);
                }
            });

            add(1, 4, new ItemInstance(new ItemBuilder(Material.BARRIER, 1, "§c§l" + omp.lang("Verwijder Squad", "Delete Squad")).build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    new DiscordGroupRemoveGUI() {
                        @Override
                        protected void onCancel() {
                            DiscordGroupManageGUI.this.reopen(omp);
                        }
                    }.open(omp);
                }
            });
        }

        add(0, 6, new ItemInstance(new ItemBuilder(Material.FILLED_MAP, 1, "§7§l" + omp.lang("Verander Naam", "Change Name")).build()) {
            @Override
            public void onClick(InventoryClickEvent e, OMPlayer omp) {
                AnvilNms anvil = orbitMines.getNms().anvilGui(omp.getPlayer(), (event) -> {
                    if (event.getSlot() != AnvilNms.AnvilSlot.OUTPUT) {
                        event.setWillClose(false);
                        event.setWillDestroy(false);
                        return;
                    }

                    String name = event.getName();

                    for (int i = 0; i < name.length(); i++) {
                        char c = name.charAt(i);
                        if (!Character.isAlphabetic(c) && !Character.isDigit(c) && c != '_' && c != ' ') {
                            event.setWillClose(false);
                            event.setWillDestroy(false);
                            omp.sendMessage("Discord", Color.RED, "§7Je Discord Squad naam kan alleen maar bestaan uit letters en nummers.", "§7Your Discord Squad name can only contain alphabetic and numeric characters.");
                            return;
                        }
                    }

                    if (!DiscordSquad.exists(orbitMines.getServerHandler().getDiscord(), orbitMines.getServerHandler().getToken(), name)) {
                        event.setWillClose(true);
                        event.setWillDestroy(true);

                        OrbitMines.getInstance().getServerHandler().getMessageHandler().dataTransfer(PluginMessage.DISCORD_GROUP_ACTION, omp.getUUID().toString(), "NAME", name);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                omp.getPlayer().closeInventory();
                            }
                        }.runTaskLater(orbitMines, 2);
                    } else {
                        event.setWillClose(false);
                        event.setWillDestroy(false);
                        omp.sendMessage("Discord", Color.RED, "§7Er is al een Discord Squad met die naam.", "§7There already is a Discord Squad with that name.");
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

                anvil.getItems().put(AnvilNms.AnvilSlot.INPUT_LEFT, new PlayerSkullBuilder(() -> omp.getName(true), 1, group.getName()).build());

                anvil.open();
            }
        });

        add(0, 7, new ItemInstance(new ItemBuilder(getMaterial(group.getColor()), 1, "§7§l" + omp.lang("Verander Kleur", "Change Color")).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new DiscordGroupColorGUI() {
                    @Override
                    public void openOverview() {
                        new DiscordGroupManageGUI().open(omp);
                    }
                }.open(omp);
            }
        });

        List<UUID> offline = new ArrayList<>(group.getMembers());

        /* Order List Online, Offline */
        List<UUID> ordered = new ArrayList<>();

        for (UUID uuid : offline) {
            CachedPlayer friend = CachedPlayer.getPlayer(uuid);
            Server server = friend.getServer();

            if (server != null)
                ordered.add(uuid);
        }
        offline.removeAll(ordered); /* Remove all online friends from list */
        ordered.addAll(offline); /* Add all offline friends to list */

        int slot = 18;

        for (UUID member : getMembersForPage(ordered)) {
            if (member != null) {
                CachedPlayer player = CachedPlayer.getPlayer(member);
                String name = player.getRankPrefixColor().getChatColor() + player.getPlayerName();

                PlayerSkullBuilder item = new PlayerSkullBuilder(player::getPlayerName, 1, name, new ArrayList<>());
                ItemBuilder offlineItem = new ItemBuilder(Material.SKELETON_SKULL, 1, name, new ArrayList<>());
                List<String> lore = item.getLore();

                Server server = player.getServer();

                OrbitMinesServer handler = orbitMines.getServerHandler();
                User user = handler.getDiscord().getLinkedUser(handler.getToken(), player.getUUID());
                item.addLore("§7Discord: " + (user != null ? "§9§l" + user.getName() + "#" + user.getDiscriminator() : StaffRank.NONE.getDisplayName()));

                lore.add("");

                if (server != null) {
                    lore.add("§7Status: " + Server.Status.ONLINE.getDisplayName());
                    lore.add("§7Server: " + server.getDisplayName());
                } else {
                    offlineItem.setLore(lore);

                    lore.add("§7Status: " + Server.Status.OFFLINE.getDisplayName());
                    lore.add("§7" + omp.lang("Laatst gezien", "Last seen") + ": §9§l" + player.getLastOnlineInTimeUnit(omp.getLanguage()) + " " + omp.lang("geleden", "ago"));
                }
                lore.add("");
                lore.add(omp.lang("§cKlik hier om te verwijderen.", "§cClick here to remove."));

                add(slot, new ItemInstance(server != null ? item.build() : offlineItem.build()) {
                    @Override
                    public void onClick(InventoryClickEvent event, OMPlayer omp) {
                        omp.getPlayer().closeInventory();

                        data.onMemberRemoval(player, true);
                    }
                });
            } else {
                clear(slot);
            }

            slot++;
        }

        if (page != 0)
            add(5, 0, new ItemInstance(new PlayerSkullBuilder(() -> "Blue Arrow Left", 1, omp.lang("§7« Meer Members", "§7« More Members")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWFlNzg0NTFiZjI2Y2Y0OWZkNWY1NGNkOGYyYjM3Y2QyNWM5MmU1Y2E3NjI5OGIzNjM0Y2I1NDFlOWFkODkifX19").build()) {
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
            add(5, 8, new ItemInstance(new PlayerSkullBuilder(() -> "Blue Arrow Right", 1, omp.lang("§7Meer Members »", "§7More Members »")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTE3ZjM2NjZkM2NlZGZhZTU3Nzc4Yzc4MjMwZDQ4MGM3MTlmZDVmNjVmZmEyYWQzMjU1Mzg1ZTQzM2I4NmUifX19").build()) {
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

    private PlayerSkullBuilder getDiscordSkull() {
        return new PlayerSkullBuilder(() -> "Discord Skull").setTexture(DiscordBot.SKULL_TEXTURE);
    }

    private UUID[] getMembersForPage(List<UUID> groups) {
        UUID[] pageMembers = new UUID[MEMBERS_PER_PAGE];

        for (int i = 0; i < MEMBERS_PER_PAGE; i++) {
            if (groups.size() > i)
                pageMembers[i] = groups.get(i);
        }

        if (page != 0) {
            for (int i = 0; i < page; i++) {
                for (int j = 0; j < MEMBERS_PER_PAGE; j++) {
                    int check = -1;
                    if ((j + 1) % 9 == 0)
                        check = (j + 1) / 9;

                    if (check != -1) {
                        int next = MEMBERS_PER_PAGE + check + (NEW_PER_PAGE * i) - 1;
                        pageMembers[j] = groups.size() > next ? groups.get(next) : null;
                    } else {
                        pageMembers[j] = pageMembers[j + 1];
                    }
                }
            }
        }

        return pageMembers;
    }

    private boolean canHaveMorePages(List<UUID> groups) {
        int groupAmount = groups.size();

        if (groupAmount <= MEMBERS_PER_PAGE)
            return false;

        int maxPage = groupAmount % NEW_PER_PAGE;
        maxPage = maxPage != 0 ? (groupAmount - MEMBERS_PER_PAGE + (NEW_PER_PAGE - maxPage)) / NEW_PER_PAGE : (groupAmount - MEMBERS_PER_PAGE) / NEW_PER_PAGE;

        return maxPage > page;
    }

    public static Material getMaterial(Color color) {
        switch (color) {

            case AQUA:
                return Material.LIGHT_BLUE_TERRACOTTA;
            case BLACK:
                return Material.BLACK_TERRACOTTA;
            case BLUE:
                return Material.BLUE_TERRACOTTA;
            case FUCHSIA:
                return Material.PINK_TERRACOTTA;
            case GRAY:
                return Material.GRAY_TERRACOTTA;
            case GREEN:
                return Material.GREEN_TERRACOTTA;
            case LIME:
                return Material.LIME_TERRACOTTA;
            case MAROON:
                return Material.RED_NETHER_BRICKS;
            case NAVY:
                return Material.BLUE_CONCRETE;
            case ORANGE:
                return Material.ORANGE_TERRACOTTA;
            case PURPLE:
                return Material.PURPLE_TERRACOTTA;
            case RED:
                return Material.RED_TERRACOTTA;
            case SILVER:
                return Material.LIGHT_GRAY_TERRACOTTA;
            case TEAL:
                return Material.CYAN_TERRACOTTA;
            case WHITE:
                return Material.WHITE_TERRACOTTA;
            case YELLOW:
                return Material.YELLOW_TERRACOTTA;
        }

        throw new IllegalStateException();
    }

}
