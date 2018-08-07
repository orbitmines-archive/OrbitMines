package com.orbitmines.spigot.servers.survival.handlers.region;

import com.orbitmines.api.Color;
import com.orbitmines.api.Cooldown;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.Teleportable;
import com.orbitmines.spigot.api.handlers.chat.ActionBar;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PotionBuilder;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import com.orbitmines.spigot.api.utils.WorldUtils;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.utils.BiomeUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

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
        30x30 (900 regions, this will make the regions range from 15 * 1500 = 22,500 -> -11,250 <-> 11,250)
     */
    public static final int REGION_SIZE = 15;
    public static final int START_X = 0;
    public static final int START_Z = 0;
    public static final int OFFSET = 1500;
    /* Blocks away from Region (diameter) */
    public static final int PROTECTION = 8;

    public static final int REGION_COUNT = REGION_SIZE * REGION_SIZE;
    public static final int LAST_REGION_DISTANCE = (REGION_SIZE / 2) * OFFSET;
    public static int TELEPORTABLE = 10 * 10;
    public static final int WORLD_BORDER = (int) Math.sqrt(TELEPORTABLE) * OFFSET + 15000; /* Additional 5k blocks surrounding the regions, *outer space* */

    public static PotionBuilder UNDERWATER_POTION = new PotionBuilder(PotionEffectType.CONDUIT_POWER, 260, 0, true, true);

    private static List<Region> regions = new ArrayList<>();
    private static List<Region> aboveWaterRegions = new ArrayList<>();

    private static Cooldown REGION_INTERACT = new Cooldown(2000);

    private final OrbitMines orbitMines;

    private final int id;
    private final Location location;
    private final int inventoryX;
    private final int inventoryY;

    private final boolean underWater;

    private final Biome biome;
    private final ItemBuilder icon;

    public Region(int id, Location location, int inventoryX, int inventoryY, boolean underWater) {
        regions.add(this);

        this.orbitMines = OrbitMines.getInstance();

        this.id = id;
        this.location = location;
        this.inventoryX = inventoryX;
        this.inventoryY = inventoryY;
        this.underWater = underWater;
        this.biome = location.getWorld().getBiome(location.getBlockX(), location.getBlockZ());
        this.icon = toItemBuilder();

        if (id >= Region.TELEPORTABLE)
            return;

        if (underWater) {
//            Location conduitLocation = location.clone().add(0, 2, 0).clone();

            new SpigotRunnable(SpigotRunnable.TimeUnit.SECOND, 1) {
                @Override
                public void run() {
                    List<Player> players = WorldUtils.getNearbyPlayers(location, Region.PROTECTION);
                    for (Player player : players) {
                        player.addPotionEffect(Region.UNDERWATER_POTION.build());
                        player.setRemainingAir(player.getMaximumAir());
                    }

//                    orbitMines.getNms().world().conduitAnimation(conduitLocation, players);
                }
            };
        } else {
            aboveWaterRegions.add(this);

            for (int i = 0; i < location.getBlockY(); i++) {
                Block block = location.clone().subtract(0, i, 0).getBlock();

                if (block.getType() != Material.BEACON)
                    continue;

                Beacon beacon = (Beacon) block.getState();
                beacon.setPrimaryEffect(PotionEffectType.SPEED);
                beacon.update(true);
                break;
            }
        }
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

    public boolean isUnderWater() {
        return underWater;
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

    public static Region randomTeleportable(boolean aboveWater) {
        if (aboveWater)
            return RandomUtils.randomFrom(aboveWaterRegions);

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
        ItemBuilder item = new ItemBuilder(BiomeUtils.material(biome));
        item.setAmount(id + 1 > 64 ? 64 : id + 1);
        item.setDisplayName("§7§lRegion §a§l" + (id + 1));
        item.addLore(" §7Biome: " + BiomeUtils.name(biome));
        item.addLore(" §7XZ: §a§l" + NumberUtils.locale(location.getBlockX()) + " §7/ §a§l" + NumberUtils.locale(location.getBlockZ()));

        if (id == 0)
            item.glow();

        return item;
    }
}
