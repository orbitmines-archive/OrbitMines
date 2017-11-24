package com.orbitmines.spigot.servers.survival.handlers.claim;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class Visualization {

    private static Map<SurvivalPlayer, Visualization> visualizations = new HashMap<>();

    private List<Element> elements;

    public Visualization() {
        elements = new ArrayList<>();
    }

    public List<Element> getElements() {
        return elements;
    }

    public static void show(SurvivalPlayer omp, Claim claim, int height, Type type, Location location) {
        if (claim.hasParent()) {
            show(omp, claim.getParent(), height, type, location);
            return;
        }

        Visualization visualization = new Visualization();
        for (int i = 0; i < claim.getChildren().size(); i++) {
            Claim child = claim.getChildren().get(i);
            if (!child.isRegistered())
                continue;

            visualization.addClaimElements(child, height, Type.CHILDREN, location);
        }

        if (!claim.hasOwner())
            type = Type.NONE;

        visualization.addClaimElements(claim, height, type, location);

        show(omp, visualization);
    }

    public static void show(SurvivalPlayer omp, Iterable<Claim> claims, int height, Type type, Location location) {
        Visualization visualization = new Visualization();

        for (Claim claim : claims) {
            visualization.addClaimElements(claim, height, type, location);
        }

        show(omp, visualization);
    }

    public static void show(SurvivalPlayer omp, Visualization visualization) {
        if (visualizations.containsKey(omp))
            revert(omp);

        if (omp.getPlayer().isOnline() && visualization.elements.size() > 0 && visualization.elements.get(0).location.getWorld().equals(omp.getWorld())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (int i = 0; i < visualization.elements.size(); i++) {
                        Element element = visualization.elements.get(i);

                        if (!element.location.getChunk().isLoaded())
                            continue;

                        omp.getPlayer().sendBlockChange(element.location, element.visualizedMaterial, element.visualizedData);
                    }

                    visualizations.put(omp, visualization);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (visualizations.get(omp) == visualization)
                                revert(omp);
                        }
                    }.runTaskLater(OrbitMines.getInstance(), 60 * 20);
                }
            }.runTaskLater(OrbitMines.getInstance(), 1);
        }
    }

    public static void revert(SurvivalPlayer omp) {
        if (!omp.getPlayer().isOnline()) {
            visualizations.remove(omp);
            return;
        }

        Visualization visualization = visualizations.get(omp);

        if (visualization == null)
            return;

        int minX = omp.getLocation().getBlockX() - 100;
        int minZ = omp.getLocation().getBlockZ() - 100;
        int maxX = omp.getLocation().getBlockX() + 100;
        int maxZ = omp.getLocation().getBlockZ() + 100;

        visualization.removeElementsOutOfRange(minX, minZ, maxX, maxZ);

        for (int i = 0; i < visualization.elements.size(); i++) {
            Element element = visualization.elements.get(i);

            if (i == 0) {
                if (!omp.getWorld().equals(element.location.getWorld()))
                    return;
            }

            omp.getPlayer().sendBlockChange(element.location, element.realMaterial, element.realData);
        }

        visualizations.remove(omp);
    }

    private void addClaimElements(Claim claim, int height, Type type, Location location) {
        Location corner1 = claim.getCorner1();
        Location corner2 = claim.getCorner2();
        World world = corner1.getWorld();
        boolean waterIsTransparent = location.getBlock().getType() == Material.STATIONARY_WATER;

        int smallX = corner1.getBlockX();
        int smallZ = corner1.getBlockZ();
        int bigX = corner2.getBlockX();
        int bigZ = corner2.getBlockZ();

        ItemBuilder corner = type.getCorner();
        ItemBuilder accent = type.getAccent();

        List<Element> newElements = new ArrayList<>();

        int minX = location.getBlockX() - 75;
        int minZ = location.getBlockZ() - 75;
        int maxX = location.getBlockX() + 75;
        int maxZ = location.getBlockZ() + 75;

        final int STEP = 10;

        //top line
        newElements.add(new Element(new Location(world, smallX, 0, bigZ), corner.getMaterial(), (byte) corner.getDurability(), Material.AIR, (byte) 0));
        newElements.add(new Element(new Location(world, smallX + 1, 0, bigZ), accent.getMaterial(), (byte) accent.getDurability(), Material.AIR, (byte) 0));
        for (int x = smallX + STEP; x < bigX - STEP / 2; x += STEP) {
            if (x > minX && x < maxX)
                newElements.add(new Element(new Location(world, x, 0, bigZ), accent.getMaterial(), (byte) accent.getDurability(), Material.AIR, (byte) 0));
        }
        newElements.add(new Element(new Location(world, bigX - 1, 0, bigZ), accent.getMaterial(), (byte) accent.getDurability(), Material.AIR, (byte) 0));

        //bottom line
        newElements.add(new Element(new Location(world, smallX + 1, 0, smallZ), accent.getMaterial(), (byte) accent.getDurability(), Material.AIR, (byte) 0));
        for (int x = smallX + STEP; x < bigX - STEP / 2; x += STEP) {
            if (x > minX && x < maxX)
                newElements.add(new Element(new Location(world, x, 0, smallZ), accent.getMaterial(), (byte) accent.getDurability(), Material.AIR, (byte) 0));
        }
        newElements.add(new Element(new Location(world, bigX - 1, 0, smallZ), accent.getMaterial(), (byte) accent.getDurability(), Material.AIR, (byte) 0));

        //left line
        newElements.add(new Element(new Location(world, smallX, 0, smallZ), corner.getMaterial(), (byte) corner.getDurability(), Material.AIR, (byte) 0));
        newElements.add(new Element(new Location(world, smallX, 0, smallZ + 1), accent.getMaterial(), (byte) accent.getDurability(), Material.AIR, (byte) 0));
        for (int z = smallZ + STEP; z < bigZ - STEP / 2; z += STEP) {
            if (z > minZ && z < maxZ)
                newElements.add(new Element(new Location(world, smallX, 0, z), accent.getMaterial(), (byte) accent.getDurability(), Material.AIR, (byte) 0));
        }
        newElements.add(new Element(new Location(world, smallX, 0, bigZ - 1), accent.getMaterial(), (byte) accent.getDurability(), Material.AIR, (byte) 0));

        //right line
        newElements.add(new Element(new Location(world, bigX, 0, smallZ), corner.getMaterial(), (byte) corner.getDurability(), Material.AIR, (byte) 0));
        newElements.add(new Element(new Location(world, bigX, 0, smallZ + 1), accent.getMaterial(), (byte) accent.getDurability(), Material.AIR, (byte) 0));
        for (int z = smallZ + STEP; z < bigZ - STEP / 2; z += STEP) {
            if (z > minZ && z < maxZ)
                newElements.add(new Element(new Location(world, bigX, 0, z), accent.getMaterial(), (byte) accent.getDurability(), Material.AIR, (byte) 0));
        }
        newElements.add(new Element(new Location(world, bigX, 0, bigZ - 1), accent.getMaterial(), (byte) accent.getDurability(), Material.AIR, (byte) 0));
        newElements.add(new Element(new Location(world, bigX, 0, bigZ), corner.getMaterial(), (byte) corner.getDurability(), Material.AIR, (byte) 0));

        this.removeElementsOutOfRange(minX, minZ, maxX, maxZ);

        for (int i = 0; i < newElements.size(); i++) {
            Element element = newElements.get(i);
            if (!claim.inClaim(element.location)) {
                newElements.remove(i--);
            }
        }

        for (Element element : newElements) {
            Location tempLocation = element.location;
            element.location = getVisibleLocation(tempLocation.getWorld(), tempLocation.getBlockX(), height, tempLocation.getBlockZ(), waterIsTransparent);
            height = element.location.getBlockY();
            
            element.realMaterial = element.location.getBlock().getType();
            element.realData = element.location.getBlock().getData();
        }

        this.elements.addAll(newElements);
    }

    private void removeElementsOutOfRange(int minX, int minZ, int maxX, int maxZ) {
        for (int i = 0; i < elements.size(); i++) {
            Location location = elements.get(i).location;
            if (location.getX() < minX || location.getX() > maxX || location.getZ() < minZ || location.getZ() > maxZ) {
                elements.remove(i--);
            }
        }
    }

    private Location getVisibleLocation(World world, int x, int y, int z, boolean waterIsTransparent) {
        Block block = world.getBlockAt(x, y, z);
        BlockFace direction = (isTransparent(block, waterIsTransparent)) ? BlockFace.DOWN : BlockFace.UP;

        while (block.getY() >= 1 && block.getY() < world.getMaxHeight() - 1 && (!isTransparent(block.getRelative(BlockFace.UP), waterIsTransparent) || isTransparent(block, waterIsTransparent))) {
            block = block.getRelative(direction);
        }

        return block.getLocation();
    }

    private boolean isTransparent(Block block, boolean waterIsTransparent) {
        //Blacklist
        switch (block.getType()) {
            case SNOW:
                return false;
        }

        //Whitelist
        switch (block.getType()) {
            case AIR:
            case FENCE:
            case ACACIA_FENCE:
            case BIRCH_FENCE:
            case DARK_OAK_FENCE:
            case JUNGLE_FENCE:
            case NETHER_FENCE:
            case SPRUCE_FENCE:
            case FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case SIGN:
            case SIGN_POST:
            case WALL_SIGN:
                return true;
        }

        if ((waterIsTransparent && block.getType() == Material.STATIONARY_WATER) || block.getType().isTransparent())
            return true;

        return false;
    }


    public class Element {

        private Location location;
        private final Material visualizedMaterial;
        private final byte visualizedData;
        private Material realMaterial;
        private byte realData;

        public Element(Location location, Material visualizedMaterial, byte visualizedData, Material realMaterial, byte realData) {
            this.location = location;
            this.visualizedMaterial = visualizedMaterial;
            this.visualizedData = visualizedData;
            this.realData = realData;
            this.realMaterial = realMaterial;
        }

        public Location getLocation() {
            return location;
        }

        public Material getVisualizedMaterial() {
            return visualizedMaterial;
        }

        public byte getVisualizedData() {
            return visualizedData;
        }

        public Material getRealMaterial() {
            return realMaterial;
        }

        public byte getRealData() {
            return realData;
        }
    }

    public enum Type {

        CLAIM(new ItemBuilder(Material.SEA_LANTERN), new ItemBuilder(Material.STAINED_CLAY, 1, 4)),
        CHILDREN(new ItemBuilder(Material.SEA_LANTERN), new ItemBuilder(Material.STAINED_CLAY, 1, 3)),
        NONE(new ItemBuilder(Material.SEA_LANTERN), new ItemBuilder(Material.STAINED_CLAY, 1, 15)),
        INVALID(new ItemBuilder(Material.SEA_LANTERN), new ItemBuilder(Material.STAINED_CLAY, 1, 14));

        private final ItemBuilder corner;
        private final ItemBuilder accent;

        Type(ItemBuilder corner, ItemBuilder accent) {
            this.corner = corner;
            this.accent = accent;
        }

        public ItemBuilder getCorner() {
            return corner;
        }

        public ItemBuilder getAccent() {
            return accent;
        }
    }
}
