package com.orbitmines.spigot.servers.hub.gui.discordgroup;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.*;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.handlers.DiscordGroup;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.data.DiscordGroupData;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import com.orbitmines.spigot.api.nms.anvilgui.AnvilNms;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class DiscordGroupGUI extends GUI implements DiscordGroupGUIInstance {

    private final int GROUPS_PER_PAGE = 9;
    private final int NEW_PER_PAGE = GROUPS_PER_PAGE / 9;

    private OrbitMines orbitMines;
    private int page;

    public DiscordGroupGUI() {
        this(0);
    }

    public DiscordGroupGUI(int page) {
        orbitMines = OrbitMines.getInstance();
        this.page = page;

        newInventory(54, "§0§lPrivate Discord Servers");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        DiscordBot discord = orbitMines.getServerHandler().getDiscord();
        DiscordGroupData data = (DiscordGroupData) omp.getData(Data.Type.DISCORD_GROUPS);

        {
            UUID selected = data.getSelected();
            DiscordGroup group = selected != null ? DiscordGroup.getFromDatabase(discord, selected) : null;

            PlayerSkullBuilder item = getDiscordSkull();
            item.setDisplayName("§7§l" + omp.lang("Geselecteerde Server", "Selected Server"));
            item.addLore("§7Server: " + (group != null ? group.getDisplayName() : VipRank.NONE.getDisplayName()));
            if (group != null) {
                CachedPlayer owner = CachedPlayer.getPlayer(selected);
                item.addLore("§7" + omp.lang("Eigenaar", "Owner") + ": " + owner.getRankPrefixColor().getChatColor() + owner.getPlayerName());
            }
            add(1, 2, new EmptyItemInstance(item.build()));

            ItemBuilder item2 = new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE, 1, "§7Type §9!<message>§7 in " + omp.lang("de", "the") + " chat.");

            if (group != null)
                item2.glow();

            add(2, 2, new EmptyItemInstance(item2.build()));
        }

        {
            DiscordGroup group = DiscordGroup.getFromDatabase(discord, omp.getUUID());

            if (group == null) {
                {
                    boolean canCreate = omp.isEligible(VipRank.EMERALD);

                    ItemBuilder item = new ItemBuilder(Material.WRITABLE_BOOK, 1, omp.lang("§7§lJouw Privé Discord Server", "§7§lYour Private Discord Server"));
                    item.addLore("§7Required: " + VipRank.EMERALD.getDisplayName());

                    boolean hasLinked = CachedPlayer.getPlayer(omp.getUUID()).getDiscordId() != null;

                    if (!hasLinked) {
                        item.addLore("");
                        item.addLore(omp.lang(
                                "§7Je moet je §9§lDiscord Account",
                                "§7You have to link your §9§lDiscord"
                        ));
                        item.addLore(omp.lang(
                                "§7linken om je privé server aan",
                                "§9§lAccount§7 in order to create"
                        ));
                        item.addLore(omp.lang(
                                "§7te maken. (§9/discordlink§7)",
                                "§7your private server. (§9/discordlink§7)"
                        ));
                    }

                    if (canCreate) {
                        item.addLore("");
                        item.addLore(omp.lang("§aKlik hier om je discord server te maken.", "§aClick here to create your discord server."));

                        item.glow();
                    }

                    if (canCreate && hasLinked)
                        add(1, 5, new ItemInstance(item.build()) {
                            @Override
                            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                                omp.getPlayer().closeInventory();

                                orbitMines.getServerHandler().getMessageHandler().dataTransfer(PluginMessage.DISCORD_GROUP_ACTION, omp.getUUID().toString(), "CREATE");
                            }
                        });
                    else
                        add(1, 5, new EmptyItemInstance(item.build()));
                }

                ItemStack item = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, "§8" + omp.lang("Onbeschikbaar", "Unavailable")).build();
                add(2, 4, new EmptyItemInstance(item));
                add(2, 5, new EmptyItemInstance(item));
                add(2, 6, new EmptyItemInstance(item));
            } else {
                {
                    ItemBuilder item = new PlayerSkullBuilder(() -> omp.getName(true), 1, group.getDisplayName());

                    setOnlineLore(item, group);

                    add(1, 5, new EmptyItemInstance(item.build()));
                }

                add(2, 4, new ItemInstance(new ItemBuilder(Material.WRITABLE_BOOK, 1, "§7§l" + omp.lang("Beheer", "Manage") + " Members").build()) {
                    @Override
                    public void onClick(InventoryClickEvent event, OMPlayer omp) {
                        new DiscordGroupManageGUI().open(omp);
                    }
                });

                add(2, 5, new ItemInstance(new ItemBuilder(Material.FILLED_MAP, 1, "§7§l" + omp.lang("Verander Naam", "Change Name")).build()) {
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
                                if (!Character.isAlphabetic(c) && !Character.isDigit(c) && c != '_') {
                                    event.setWillClose(false);
                                    event.setWillDestroy(false);
                                    omp.sendMessage("Discord", Color.RED, "§7Je privé Discord server naam kan alleen maar bestaan uit letters en nummers.", "§7Your private Discord server name can only contain alphabetic and numeric characters.");
                                    return;
                                }
                            }

                            if (!DiscordGroup.exists(discord, orbitMines.getServerHandler().getToken(), name)) {
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
                                omp.sendMessage("Discord", Color.RED, "§7Er is al een privé Discord server met die naam.", "§7There already is a private Discord server with that name.");
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

                add(2, 6, new ItemInstance(new ItemBuilder(getMaterial(group.getColor()), 1, "§7§l" + omp.lang("Verander Kleur", "Change Color")).build()) {
                    @Override
                    public void onClick(InventoryClickEvent event, OMPlayer omp) {
                        new DiscordGroupColorGUI() {
                            @Override
                            public void openOverview() {
                                new DiscordGroupGUI().open(omp);
                            }
                        }.open(omp);
                    }
                });
            }
        }

        List<DiscordGroup> groups = DiscordGroup.getGroupsFromDatabase(discord, omp.getUUID());

        /* Order List Invites, Groups */
        List<DiscordGroup> ordered = new ArrayList<>();
        for (UUID invite : data.getInvites()) {
            ordered.add(DiscordGroup.getFromDatabase(discord, invite));
        }

        ordered.addAll(groups);

        int slot = 36;

        for (DiscordGroup group : getGroupsForPage(ordered)) {
            if (group != null) {
                CachedPlayer owner = CachedPlayer.getPlayer(group.getOwnerUUID());

                boolean invite = data.getInvites().contains(owner.getUUID());

                if (!invite) {
                    boolean isOwner = omp.getUUID().toString().equals(owner.getUUID().toString());

                    PlayerSkullBuilder item = new PlayerSkullBuilder(owner::getPlayerName, 1, group.getDisplayName(), new ArrayList<>());

                    setOnlineLore(item, group);

                    item.addLore("");

                    if (isOwner)
                        item.addLore(omp.lang("§aKlik hier om te selecteren.", "§aClick here to select."));
                    else
                        item.addLore(omp.lang("§aKlik hier om te selecteren/verwijderen.", "§aClick here to select or delete."));

                    if (isOwner)
                        add(slot, new ItemInstance(item.build()) {
                            @Override
                            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                                data.setSelected(owner.getUUID());

                                reopen(omp);
                            }
                        });
                    else
                        add(slot, new ItemInstance(item.build()) {
                            @Override
                            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                                new DiscordGroupDetailsGUI(owner, group.getDisplayName()).open(omp);
                            }
                        });
                } else {
                    ItemBuilder item = new ItemBuilder(Material.WRITABLE_BOOK, 1, "§9§l" + omp.lang("UITNODIGING", "INVITATION") + " " + group.getDisplayName(), new ArrayList<>());
                    item.glow();

                    setOnlineLore(item, group);

                    item.addLore("");
                    item.addLore(omp.lang("§aKlik hier om te accepteren/weigeren.", "§aClick here to accept or ignore."));

                    add(slot, new ItemInstance(item.build()) {
                        @Override
                        public void onClick(InventoryClickEvent event, OMPlayer omp) {
                            new DiscordGroupRequestDetailsGUI(owner, group.getDisplayName()).open(omp);
                        }
                    });
                }
            } else {
                clear(slot);
            }

            slot++;
        }

        if (page != 0)
            add(5, 0, new ItemInstance(new PlayerSkullBuilder(() -> "Blue Arrow Left", 1, omp.lang("§7« Meer Discord Servers", "§7« More Discord Servers")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWFlNzg0NTFiZjI2Y2Y0OWZkNWY1NGNkOGYyYjM3Y2QyNWM5MmU1Y2E3NjI5OGIzNjM0Y2I1NDFlOWFkODkifX19").build()) {
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
            add(5, 8, new ItemInstance(new PlayerSkullBuilder(() -> "Blue Arrow Right", 1, omp.lang("§7Meer Discord Servers »", "§7More Discord Servers »")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTE3ZjM2NjZkM2NlZGZhZTU3Nzc4Yzc4MjMwZDQ4MGM3MTlmZDVmNjVmZmEyYWQzMjU1Mzg1ZTQzM2I4NmUifX19").build()) {
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

    private DiscordGroup[] getGroupsForPage(List<DiscordGroup> groups) {
        DiscordGroup[] pageGroups = new DiscordGroup[GROUPS_PER_PAGE];

        for (int i = 0; i < GROUPS_PER_PAGE; i++) {
            if (groups.size() > i)
                pageGroups[i] = groups.get(i);
        }

        if (page != 0) {
            for (int i = 0; i < page; i++) {
                for (int j = 0; j < GROUPS_PER_PAGE; j++) {
                    int check = -1;
                    if ((j + 1) % 9 == 0)
                        check = (j + 1) / 9;

                    if (check != -1) {
                        int next = GROUPS_PER_PAGE + check + (NEW_PER_PAGE * i) - 1;
                        pageGroups[j] = groups.size() > next ? groups.get(next) : null;
                    } else {
                        pageGroups[j] = pageGroups[j + 1];
                    }
                }
            }
        }

        return pageGroups;
    }

    private boolean canHaveMorePages(List<DiscordGroup> groups) {
        int groupAmount = groups.size();

        if (groupAmount <= GROUPS_PER_PAGE)
            return false;

        int maxPage = groupAmount % NEW_PER_PAGE;
        maxPage = maxPage != 0 ? (groupAmount - GROUPS_PER_PAGE + (NEW_PER_PAGE - maxPage)) / NEW_PER_PAGE : (groupAmount - GROUPS_PER_PAGE) / NEW_PER_PAGE;

        return maxPage > page;
    }

    public static void setOnlineLore(ItemBuilder item, DiscordGroup group) {
        /* Sort all online/offline members */
        Map<Server, List<CachedPlayer>> onlineMembers = new HashMap<>();
        List<CachedPlayer> offlineMembers = new ArrayList<>();

        int online = 0;

        List<UUID> members = new ArrayList<>();
        /* Add owner first so they are always on top */
        members.add(group.getOwnerUUID());
        members.addAll(group.getMembers());

        for (UUID member : members) {
            CachedPlayer player = CachedPlayer.getPlayer(member);
            Server server = player.getServer();

            if (server == null) {
                offlineMembers.add(player);
                continue;
            }

            if (!onlineMembers.containsKey(server))
                onlineMembers.put(server, new ArrayList<>());

            onlineMembers.get(server).add(player);
            online++;
        }

        /* Add lore */
        List<String> lore = item.getLore();
        lore.add("§7§o" + online + " / " + (group.getMembers().size() + 1) + " Players Online");
        lore.add("");

        for (Server server : onlineMembers.keySet()) {
            lore.add(server.getDisplayName() + "§7(" + onlineMembers.get(server).size() + "):");

            for (CachedPlayer member : onlineMembers.get(server)) {
                /* Owner Prefix */
                String prefix = "";
                if (member.getUUID().toString().equals(group.getOwnerUUID().toString()))
                    prefix = " §e§l+ ";

                lore.add(" §7- " + prefix + member.getRankPrefix() + member.getPlayerName());
            }
        }

        if (offlineMembers.size() > 0) {
            lore.add("");
            lore.add(Server.Status.OFFLINE.getDisplayName() + "§7(" + offlineMembers.size() + "):");

            for (CachedPlayer member : offlineMembers) {
                /* Owner Prefix */
                String prefix = "";
                if (member.getUUID().toString().equals(group.getOwnerUUID().toString()))
                    prefix = " §e§l+ ";

                lore.add(" §7- " + prefix + member.getRankPrefix() + member.getPlayerName());
            }
        }
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
