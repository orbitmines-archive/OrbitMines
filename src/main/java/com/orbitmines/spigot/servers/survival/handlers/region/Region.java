package com.orbitmines.spigot.servers.survival.handlers.region;

import com.orbitmines.api.Color;
import com.orbitmines.api.Cooldown;
import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.spigot.api.handlers.chat.Title;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.utils.BiomeUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class Region {

    /*
        Regions are setup in a square, example:
        1 1 1
        1 1 1
        1 1 1
        3x3

        ...
        30x30 (900 regions, this will make the regions range from (30 - 1) * 1500 = 43,500 -> -21,750 <-> 21,750)
     */
    public static final int REGION_SIZE = 30;
    public static final int START_X = 0;
    public static final int START_Z = 0;
    public static final int OFFSET = 1500;
    /* Blocks away from Region */
    public static final int PROTECTION = 8;

    public static final int REGION_COUNT = REGION_SIZE * REGION_SIZE;
    public static final int LAST_REGION_DISTANCE = ((REGION_SIZE -1) / 2) * OFFSET;
    public static int TELEPORTABLE = 100;

    private static List<Region> regions = new ArrayList<>();

    private static Cooldown REGION_INTERACT = new Cooldown(2000);

    private final int id;
    private final Location location;
    private final int inventoryX;
    private final int inventoryY;

    private final Biome biome;
    private final ItemStack itemStack;

    public Region(int id, Location location, int inventoryX, int inventoryY) {
        regions.add(this);

        this.id = id;
        this.location = location;
        this.inventoryX = inventoryX;
        this.inventoryY = inventoryY;
        this.biome = location.getWorld().getBiome(location.getBlockX(), location.getBlockZ());
        this.itemStack = toItemStack();
    }

    public int getId() {
        return id;
    }

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

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void teleport(SurvivalPlayer omp) {
        Player p = omp.getPlayer();

        p.teleport(this.location);
        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 5, 1);

        Title t = new Title("", "§7" + omp.lang("Geteleporteerd naar", "Teleported to") + " §aRegion " + (id + 1) + "§7.", 20, 40, 20);
        t.send(omp);
    }

    public static Region getRegion(int id) {
        for (Region region : regions) {
            if (region.getId() == id)
                return region;
        }
        return null;
    }

    public static boolean isInRegion(SurvivalPlayer omp, Location location) {
        if (omp.isOpMode())
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

        if (inRegion && !omp.onCooldown(REGION_INTERACT)) {
            omp.sendMessage("Region", Color.RED, "§7Je kan dat niet zo dichtbij een Region doen!", "§7You cannot do such things that close to a Region!");

            omp.resetCooldown(REGION_INTERACT);
        }
        return inRegion;
    }

    public static Region random() {
        return regions.get(RandomUtils.RANDOM.nextInt(regions.size()));
    }

    public static List<Region> getRegions() {
        return regions;
    }

    private ItemStack toItemStack() {
        ItemBuilder item = BiomeUtils.item(biome);
        item.setAmount(id + 1 > 64 ? 64 : id + 1);
        item.setDisplayName("§7§lRegion §a§l" + (id + 1));
        item.setLore(Arrays.asList(" §7Biome: " + BiomeUtils.name(biome), " §7XZ: §a" + location.getBlockX() + " §7/ §a" + location.getBlockZ()));

        if (id == 0)
            item.glow();

        return item.build();
    }
}
