package com.orbitmines.spigot.servers.survival.handlers.region;

import com.orbitmines.api.Color;
import com.orbitmines.api.Cooldown;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.Teleportable;
import com.orbitmines.spigot.api.handlers.chat.ActionBar;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.utils.BiomeUtils;
import org.bukkit.Location;
import org.bukkit.block.Biome;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class Region extends Teleportable {

    /*
        Regions are setup in a square, example:
        1 1 1
        1 1 1
        1 1 1
        3x3

        ...
        30x30 (900 regions, this will make the regions range from 30 * 1500 = 45,000 -> -22,500 <-> 22,500)
     */
    public static final int REGION_SIZE = 30;
    public static final int START_X = 0;
    public static final int START_Z = 0;
    public static final int OFFSET = 1500;
    /* Blocks away from Region (diameter) */
    public static final int PROTECTION = 8;

    public static final int REGION_COUNT = REGION_SIZE * REGION_SIZE;
    public static final int LAST_REGION_DISTANCE = (REGION_SIZE / 2) * OFFSET;
    public static final int WORLD_BORDER = REGION_SIZE * OFFSET + 10000; /* Additional 5k blocks surrounding the regions, *outer space* */
    public static int TELEPORTABLE = 100;

    private static List<Region> regions = new ArrayList<>();

    private static Cooldown REGION_INTERACT = new Cooldown(2000);

    private final int id;
    private final Location location;
    private final int inventoryX;
    private final int inventoryY;

    private final Biome biome;
    private final ItemBuilder icon;

    public Region(int id, Location location, int inventoryX, int inventoryY) {
        regions.add(this);

        this.id = id;
        this.location = location;
        this.inventoryX = inventoryX;
        this.inventoryY = inventoryY;
        this.biome = location.getWorld().getBiome(location.getBlockX(), location.getBlockZ());
        this.icon = toItemBuilder();
    }

    public int getId() {
        return id;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public int getInventoryX() {
        return inventoryX;
    }

    public int getInventoryY() {
        return inventoryY;
    }

    public Biome getBiome() {
        return biome;
    }

    public ItemBuilder getIcon() {
        return icon.clone();
    }

    @Override
    public int getDuration(OMPlayer omp) {
        return 3;
    }

    @Override
    public Color getColor() {
        return Color.LIME;
    }

    @Override
    public String getName() {
        return "Region " + (id + 1);
    }

    public static Region getRegion(int id) {
        for (Region region : regions) {
            if (region.getId() == id)
                return region;
        }
        return null;
    }

    public static boolean isInRegion(Location location) {
        return isInRegion(null, location);
    }

    public static boolean isInRegion(SurvivalPlayer omp, Location location) {
        if (omp != null && omp.isOpMode())
            return false;

        int x = Math.abs(location.getBlockX());
        int z = Math.abs(location.getBlockZ());

        if (x > LAST_REGION_DISTANCE + PROTECTION || z > LAST_REGION_DISTANCE + PROTECTION)
            return false;

        int xDistance = x % OFFSET;
        if (xDistance > OFFSET / 2)
            xDistance = Math.abs(xDistance - OFFSET);

        int zDistance = z % OFFSET;
        if (zDistance > OFFSET / 2)
            zDistance = Math.abs(zDistance - OFFSET);

        Location l1 = new Location(location.getWorld(), 0, 0, 0);
        Location l2 = new Location(location.getWorld(), xDistance, 0, zDistance);

        double distance = l1.distance(l2);

        boolean inRegion = distance <= PROTECTION;

        if (omp != null && inRegion && !omp.onCooldown(REGION_INTERACT)) {
            new ActionBar(omp, () -> omp.lang("§c§lJe kan dat niet zo dichtbij een Region doen!", "§c§lYou cannot do such things that close to a Region!"), 60).send();

            omp.resetCooldown(REGION_INTERACT);
        }
        return inRegion;
    }

    public static Region random() {
        return RandomUtils.randomFrom(regions);
    }

    public static Region randomTeleportable() {
        return RandomUtils.randomFrom(regions.subList(0, TELEPORTABLE - 1));
    }

    public static List<Region> getRegions() {
        return regions;
    }

    public static Region getRegion(int inventoryX, int inventoryY) {
        for (Region region : regions) {
            if (region.getInventoryX() == inventoryX && region.getInventoryY() == inventoryY)
                return region;
        }
        return null;
    }

    private ItemBuilder toItemBuilder() {
        ItemBuilder item = BiomeUtils.item(biome);
        item.setAmount(id + 1 > 64 ? 64 : id + 1);
        item.setDisplayName("§7§lRegion §a§l" + (id + 1));
        item.addLore(" §7Biome: " + BiomeUtils.name(biome));
        item.addLore(" §7XZ: §a§l" + NumberUtils.locale(location.getBlockX()) + " §7/ §a§l" + NumberUtils.locale(location.getBlockZ()));

        if (id == 0)
            item.glow();

        return item;
    }
}
