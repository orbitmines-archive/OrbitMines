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
import com.orbitmines.spigot.api.handlers.CachedPlayer;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.Teleportable;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.utils.Serializer;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import org.bukkit.Location;
import org.bukkit.Material;

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
            new Warp(Long.parseLong(entry.get(TableSurvivalWarps.ID)), UUID.fromString(entry.get(TableSurvivalWarps.UUID)), entry.get(TableSurvivalWarps.NAME), Boolean.parseBoolean(entry.get(TableSurvivalWarps.ENABLED)), Type.valueOf(entry.get(TableSurvivalWarps.TYPE)), Icon.fromId(Integer.parseInt(entry.get(TableSurvivalWarps.ICON_ID))), Serializer.parseLocation(entry.get(TableSurvivalWarps.LOCATION)));
        }
    }

    public enum Icon {

        STONE(new ItemBuilder(Material.STONE));//TODO Also with 1.13 icons? - total of 27 icons

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
