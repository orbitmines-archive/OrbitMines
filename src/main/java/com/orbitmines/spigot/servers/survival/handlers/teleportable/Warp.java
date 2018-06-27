package com.orbitmines.spigot.servers.survival.handlers.teleportable;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.database.*;
import com.orbitmines.api.database.tables.survival.TableSurvivalWarps;
import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.api.CachedPlayer;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.Teleportable;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.utils.Serializer;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Warp extends Teleportable {

    private static List<Warp> warps = new ArrayList<>();

    public static Color COLOR = Color.TEAL;
    public static int MAX_CHARACTERS = 20;

    private final long id;
    private final UUID uuid;
    private String name;
    private boolean enabled;
    private Type type;
    private Icon icon;
    private Location location;

    public Warp(long id, UUID uuid, String name, boolean enabled, Type type, Icon icon, Location location) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.enabled = enabled;
        this.type = type;
        this.icon = icon;
        this.location = location;

        warps.add(this);
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public int getDuration(OMPlayer omp) {
        return 3;
    }

    @Override
    public Color getColor() {
        return COLOR;
    }

    @Override
    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public UUID getUUID() {
        return uuid;
    }

    public CachedPlayer getOwner() {
        return CachedPlayer.getPlayer(uuid);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Type getType() {
        return type;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setName(String name) {
        this.name = name;

        Database.get().update(Table.SURVIVAL_WARPS, new Set(TableSurvivalWarps.NAME, name), new Where(TableSurvivalWarps.ID, this.id));
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        Database.get().update(Table.SURVIVAL_WARPS, new Set(TableSurvivalWarps.ENABLED, enabled), new Where(TableSurvivalWarps.ID, this.id));
    }

    public void setType(Type type) {
        this.type = type;

        Database.get().update(Table.SURVIVAL_WARPS, new Set(TableSurvivalWarps.TYPE, type.toString()), new Where(TableSurvivalWarps.ID, this.id));
    }

    public void setIcon(Icon icon) {
        this.icon = icon;

        Database.get().update(Table.SURVIVAL_WARPS, new Set(TableSurvivalWarps.ICON_ID, icon.ordinal()), new Where(TableSurvivalWarps.ID, this.id));
    }

    public void setLocation(Location location) {
        this.location = location;

        Database.get().update(Table.SURVIVAL_WARPS, new Set(TableSurvivalWarps.LOCATION, Serializer.serialize(location)), new Where(TableSurvivalWarps.ID, this.id));
    }

    public void delete() {
        Database.get().delete(Table.SURVIVAL_WARPS, new Where(TableSurvivalWarps.ID, this.id));
    }

    public static Warp createWarp(UUID uuid, String name, boolean enabled, Type type, Icon icon, Location location) {
        long nextId = 0;
        for (Warp warp : warps) {
            if (warp.getId() >= nextId)
                nextId = warp.getId() + 1;
        }

        Database.get().insert(Table.SURVIVAL_WARPS, nextId + "", uuid.toString(), name, enabled ? "1" : "0", type.toString(), icon.ordinal() + "", Serializer.serialize(location));

        return new Warp(nextId, uuid, name, enabled, type, icon, location);
    }

    public static List<Warp> getWarpsFor(UUID uuid) {
        List<Warp> list = new ArrayList<>();

        for (Warp warp : warps) {
            if (warp.getUUID().equals(uuid))
                list.add(warp);
        }

        return list;
    }

    public static int getWarpCount(UUID uuid) {
        return Database.get().getCount(Table.SURVIVAL_WARPS, new Where(TableSurvivalWarps.UUID, uuid.toString()));
    }

    public static Warp getWarp(long id) {
        for (Warp warp : warps) {
            if (warp.getId() == id)
                return warp;
        }
        return null;
    }

    public static Warp getWarp(String name) {
        for (Warp warp : warps) {
            if (warp.getName().equalsIgnoreCase(name))
                return warp;
        }
        return null;
    }

    public static List<Warp> getWarps() {
        return warps;
    }

    public static void setupWarps() {
        List<Map<Column, String>> entries = Database.get().getEntries(Table.SURVIVAL_WARPS);

        for (Map<Column, String> entry : entries) {
            new Warp(Long.parseLong(entry.get(TableSurvivalWarps.ID)), UUID.fromString(entry.get(TableSurvivalWarps.UUID)), entry.get(TableSurvivalWarps.NAME), entry.get(TableSurvivalWarps.ENABLED).equals("1"), Type.valueOf(entry.get(TableSurvivalWarps.TYPE)), Icon.fromId(Integer.parseInt(entry.get(TableSurvivalWarps.ICON_ID))), Serializer.parseLocation(entry.get(TableSurvivalWarps.LOCATION)));
        }
    }

    public enum Icon {

        POWERED_RAIL(new ItemBuilder(Material.POWERED_RAIL)),
        HORN_CORAL(new ItemBuilder(Material.STONE)),//
        IRON_CHESTPLATE(new ItemBuilder(Material.IRON_CHESTPLATE).addFlag(ItemFlag.HIDE_ATTRIBUTES)),
        STICKY_PISTON(new ItemBuilder(Material.PISTON_STICKY_BASE)),
        GLISTERING_MELON_SLICE(new ItemBuilder(Material.STONE)),//
        BLUE_ICE(new ItemBuilder(Material.STONE)),//
        FIREWORK_ROCKET(new ItemBuilder(Material.FIREWORK)),
        BLACK_GLAZED_TERRACOTTA(new ItemBuilder(Material.BLACK_GLAZED_TERRACOTTA)),
        GUN_POWDER(new ItemBuilder(Material.SULPHUR)),
        DARK_OAK_SAPLING(new ItemBuilder(Material.STONE)),//
        LIGHT_BLUE_CONCRETE_POWDER(new ItemBuilder(Material.STONE)),//
        ORANGE_TERRACOTTA(new ItemBuilder(Material.STONE)),//
        BOOKSHELF(new ItemBuilder(Material.BOOKSHELF)),
        ELYTRA(new ItemBuilder(Material.ELYTRA)),
        TOTEM_OF_UNDYING(new ItemBuilder(Material.TOTEM)),
        PUFFERFISH_BUCKET(new ItemBuilder(Material.STONE)),//
        GRASS_BLOCK(new ItemBuilder(Material.GRASS)),
        HAY_BALE(new ItemBuilder(Material.HAY_BLOCK)),
        GRAY_GLAZED_TERRACOTTA(new ItemBuilder(Material.GRAY_GLAZED_TERRACOTTA)),
        LEATHER(new ItemBuilder(Material.LEATHER)),
        PUMPKIN_PIE(new ItemBuilder(Material.PUMPKIN_PIE)),
        RED_WOOL(new ItemBuilder(Material.STONE)),//
        POPPY(new ItemBuilder(Material.RED_ROSE)),
        TRIDENT(new ItemBuilder(Material.STONE).addFlag(ItemFlag.HIDE_ATTRIBUTES)),//
        FIRE_CORAL(new ItemBuilder(Material.STONE)),//
        DARK_PRISMARINE(new ItemBuilder(Material.STONE)),//
        TNT(new ItemBuilder(Material.TNT)),
        SIGN(new ItemBuilder(Material.SIGN)),
        PURPUR_BLOCK(new ItemBuilder(Material.PURPUR_BLOCK)),
        MELON(new ItemBuilder(Material.MELON_BLOCK)),
        PAINTING(new ItemBuilder(Material.PAINTING)),
        COOKIE(new ItemBuilder(Material.COOKIE)),
        YELLOW_WOOL(new ItemBuilder(Material.STONE)),//
        TURTLE_EGG(new ItemBuilder(Material.STONE)),//
        OBSIDIAN(new ItemBuilder(Material.OBSIDIAN)),
        TUBE_CORAL_BLOCK(new ItemBuilder(Material.STONE)),//
        ARMOR_STAND(new ItemBuilder(Material.ARMOR_STAND)),
        BEACON(new ItemBuilder(Material.BEACON)),
        LIME_STAINED_GLASS(new ItemBuilder(Material.STONE)),//
        BLAZE_POWDER(new ItemBuilder(Material.BLAZE_POWDER)),
        ROSE_BUSH(new ItemBuilder(Material.STONE)),//
        MAGMA_BLOCK(new ItemBuilder(Material.STONE)),//
        CACTUS(new ItemBuilder(Material.CACTUS)),
        TROPICAL_FISH(new ItemBuilder(Material.STONE))//

        ;//TODO 1.13

        private final ItemBuilder itemBuilder;

        Icon(ItemBuilder itemBuilder) {
            this.itemBuilder = itemBuilder;
        }

        public ItemBuilder getItemBuilder() {
            return itemBuilder.clone();
        }

        public static Icon fromId(int ordinal) throws IndexOutOfBoundsException {
            return values()[ordinal];
        }

        public static Icon random() {
            return RandomUtils.randomFrom(values());
        }
    }

    public enum Type {

        VIP_SLOT(new Message(VipRank.EMERALD.getDisplayName()), new Message("§7Verkrijgbaar met de " + VipRank.EMERALD.getDisplayName() + "§7 Rank.", "§7Available with the " + VipRank.EMERALD.getDisplayName() + "§7 Rank.")) {
            @Override
            public boolean hasUnlocked(SurvivalPlayer omp) {
                return omp.isEligible(VipRank.EMERALD);
            }
        },
        BOUGHT_SLOT(new Message("§3§lOrbitMines Shop"), new Message("§7Verkrijgbaar in de §3§lOrbitMines Shop§7.", "§7Obtainable in the §3§lOrbitMines Shop§7.")) {
            @Override
            public boolean hasUnlocked(SurvivalPlayer omp) {
                return omp.warpSlotShop();
            }
        },
        PRISMS_SLOT(new Message("§9§lPrisms"), new Message("§7Verkrijgbaar met §9§lPrisms§7.", "§7Obtainable with §9§lPrisms§7.")) {
            @Override
            public boolean hasUnlocked(SurvivalPlayer omp) {
                return omp.warpSlotPrisms();
            }
        };

        private final Message slotName;
        private final Message description;

        Type(Message slotName, Message description) {
            this.slotName = slotName;
            this.description = description;
        }

        public Message getSlotName() {
            return slotName;
        }

        public Message getDescription() {
            return description;
        }

        public boolean hasUnlocked(SurvivalPlayer omp) {
            throw new IllegalStateException();
        }
    }
}
