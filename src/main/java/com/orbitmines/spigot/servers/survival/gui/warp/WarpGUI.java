package com.orbitmines.spigot.servers.survival.gui.warp;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.api.CachedPlayer;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.teleportable.Warp;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class WarpGUI extends GUI {

    private final int WARPS_PER_PAGE = 27;
    private final int NEW_PER_PAGE = WARPS_PER_PAGE / 9;

    private Survival survival;

    private Type type;
    private int page;

    public WarpGUI(Survival survival) {
        this(survival, 0);
    }

    public WarpGUI(Survival survival, int page) {
        this.survival = survival;
        this.type = Type.NORMAL;
        this.page = page;

        newInventory(54, "§0§lWarps");
    }

    @Override
    protected boolean onOpen(OMPlayer player) {
        SurvivalPlayer omp = (SurvivalPlayer) player;

        {
            int warps = Warp.getWarpsFor(omp.getUUID()).size();
            ItemBuilder item = new ItemBuilder(Material.BOOK_AND_QUILL, 1, 0, "§7§l" + omp.lang("Jouw Warps", "Your Warps") + " " + Warp.COLOR.getChatColor() + "§l" + warps + " §7§l/ " + Warp.Type.values().length);

            if (warps < omp.getWarpsAllowed())
                item.glow();

            add(0, 3, new ItemInstance(item.build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    new WarpSlotsGUI(survival).open(omp);
                }
            });
        }

        {
            ItemBuilder item = new ItemBuilder(Material.DIAMOND, 1, 0, omp.lang("§6§lVoeg toe aan Favorieten", "§6§lFavorite Warp"));

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

        /* Order List Favorite, Non-Favorite */
        List<Warp> warps = new ArrayList<>(Warp.getWarps());
        List<Warp> ordered = new ArrayList<>();

        for (long l : new ArrayList<>(omp.getFavoriteWarps())) {
            Warp warp = Warp.getWarp(l);

            ordered.add(warp);
        }
        warps.removeAll(ordered); /* Remove all favorite warps from list */
        ordered.addAll(warps); /* Add all non-favorite warps to list */

        /* Put the ones that are not teleportable at the end */
        List<Warp> notTeleportable = new ArrayList<>();
        for (Warp warp : new ArrayList<>(ordered)) {
            if (warp.isEnabled())
                continue;

            notTeleportable.add(warp);
            ordered.remove(warp);
        }

        ordered.addAll(notTeleportable);

        int slot = 18;

        for (Warp warp : getWarpsForPage(ordered)) {
            if (warp != null) {
                ItemBuilder item = warp.getIcon().getItemBuilder();

                if (!warp.isEnabled()) {
                    item.setMaterial(Material.STAINED_GLASS_PANE);
                    item.setDurability((short) 15);
                }

                item.setDisplayName("§7§lWarp " + Warp.COLOR.getChatColor() + "§l" + warp.getName());

                boolean favorite = omp.getFavoriteWarps().contains(warp.getId());
                if (favorite)
                    item.glow();

                CachedPlayer owner = warp.getOwner();
                item.addLore(" §7" + omp.lang("Eigenaar", "Owner") + ": " + owner.getRankPrefixColor().getChatColor() + owner.getPlayerName());
                item.addLore(" §7XZ: " + (warp.getLocation() == null ? omp.lang("§c§lNIET INGESTELD", "§c§lNOT SET") : Warp.COLOR.getChatColor() + "§l" + NumberUtils.locale(warp.getLocation().getBlockX()) + "§7 / " + Warp.COLOR.getChatColor() + "§l" +  NumberUtils.locale(warp.getLocation().getBlockZ())));

                item.addLore("");

                switch (type) {

                    case NORMAL:
                        if (favorite) {
                            item.addLore(omp.lang("§6§lFavoriete Warp", "§6§lFavorite Warp"));
                            item.addLore("");
                        }

                        if (warp.isEnabled())
                            item.addLore(omp.lang("§aKlik hier om te teleporteren.", "§aClick here to teleport."));
                        else
                            item.addLore(omp.lang("§cTeleporteren uitgeschakeld.", "§cTeleporting disabled."));
                        break;
                    case FAVORITE:
                        if (favorite)
                            item.addLore(omp.lang("§cVerwijder van favorieten.", "§cRemove from favorites."));
                        else
                            item.addLore(omp.lang("§6Voeg toe aan favorieten.", "§6Add to favorites."));
                        break;
                }

                ItemInstance itemInstance;

                switch (type) {

                    case NORMAL:
                        if (warp.isEnabled())
                            itemInstance = new ItemInstance(item.build()) {
                                @Override
                                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                                    warp.teleport(omp);
                                    omp.getPlayer().closeInventory();
                                }
                            };
                        else
                            itemInstance = new EmptyItemInstance(item.build());
                        break;
                    case FAVORITE:
                        itemInstance = new ItemInstance(item.build()) {
                            @Override
                            public void onClick(InventoryClickEvent event, OMPlayer player) {
                                SurvivalPlayer omp = (SurvivalPlayer) player;

                                if (favorite)
                                    omp.removeFavoriteWarp(warp.getId());
                                else
                                    omp.addFavoriteWarp(warp.getId());

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
            add(5, 0, new ItemInstance(new PlayerSkullBuilder(() -> "Cyan Arrow Left", 1, omp.lang("§7« Meer Vrienden", "§7« More Friends")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjc2OGVkYzI4ODUzYzQyNDRkYmM2ZWViNjNiZDQ5ZWQ1NjhjYTIyYTg1MmEwYTU3OGIyZjJmOWZhYmU3MCJ9fX0=").build()) {
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
            add(5, 8, new ItemInstance(new PlayerSkullBuilder(() -> "Cyan Arrow Right", 1, omp.lang("§7Meer Vrienden »", "§7More Friends »")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZmNTVmMWIzMmMzNDM1YWMxYWIzZTVlNTM1YzUwYjUyNzI4NWRhNzE2ZTU0ZmU3MDFjOWI1OTM1MmFmYzFjIn19fQ==").build()) {
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

    private Warp[] getWarpsForPage(List<Warp> warps) {
        Warp[] pageWarps = new Warp[WARPS_PER_PAGE];

        for (int i = 0; i < WARPS_PER_PAGE; i++) {
            if (warps.size() > i)
                pageWarps[i] = warps.get(i);
        }

        if (page != 0) {
            for (int i = 0; i < page; i++) {
                for (int j = 0; j < WARPS_PER_PAGE; j++) {
                    int check = -1;
                    if ((j + 1) % 9 == 0)
                        check = (j + 1) / 9;

                    if (check != -1) {
                        int next = WARPS_PER_PAGE + check + (NEW_PER_PAGE * i);
                        pageWarps[j] = warps.size() > next ? warps.get(next) : null;
                    } else {
                        pageWarps[j] = pageWarps[j + 1];
                    }
                }
            }
        }

        return pageWarps;
    }

    private boolean canHaveMorePages(List<Warp> warps) {
        int warpAmount = warps.size();

        if (warpAmount <= WARPS_PER_PAGE)
            return false;

        int maxPage = warpAmount % NEW_PER_PAGE;
        maxPage = maxPage != 0 ? (warpAmount - WARPS_PER_PAGE + (NEW_PER_PAGE - maxPage)) / NEW_PER_PAGE : (warpAmount - WARPS_PER_PAGE) / NEW_PER_PAGE;

        return maxPage > page;
    }

    public enum Type {

        NORMAL,
        FAVORITE;

    }
}
