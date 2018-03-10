package com.orbitmines.spigot.servers.uhsurvival.handlers.map;

import com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.DungeonManager;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.mapsection.MapSection;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.warzone.Warzone;
import com.orbitmines.spigot.servers.uhsurvival.handlers.mob.MobManager;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin & Marvin on 1/8/2018.
 */
public class Map {

    private static final int STANDARD_WIDTH = 27000;
    private static final int STANDARD_HEIGHT = 27000;

    private static final int STANDARD_WIDTH_SECTION = 3000;
    private static final int STANDARD_HEIGHT_SECTION = 3000;

    private World world;

    private int MAX_WIDTH;
    private int MAX_HEIGHT;

    private int MAX_HEIGHT_SECTION;
    private int MAX_WIDTH_SECTION;

    private DungeonManager dungeons;
    private MobManager mobManager;

    private List<MapSection> mapSections;

    private Warzone warzone;

    public Map(World world) {
        this.world = world;
        this.dungeons = new DungeonManager(this);
        this.mobManager = new MobManager(this);

        this.MAX_WIDTH = STANDARD_WIDTH;
        this.MAX_HEIGHT = STANDARD_HEIGHT;

        this.MAX_WIDTH_SECTION = STANDARD_WIDTH_SECTION;
        this.MAX_HEIGHT_SECTION = STANDARD_HEIGHT_SECTION;

        this.mapSections = new ArrayList<>();
        setupMapSections();
    }

    /* GETTERS */
    public MapSection getMapSection(Location location) {
        for (MapSection mapSection : mapSections) {
            if (mapSection.getMinX() <= location.getX() && location.getX() <= mapSection.getMaxX()) {
                if (mapSection.getMinZ() <= location.getZ() && location.getZ() <= mapSection.getMaxZ()) {
                    return mapSection;
                }
            }
        }
        return null;
    }

    public World getWorld() {
        return world;
    }

    public List<MapSection> getMapSections() {
        return mapSections;
    }

    public Warzone getWarzone() {
        return warzone;
    }

    public DungeonManager getDungeons() {
        return dungeons;
    }

    public MobManager getMobs() {
        return mobManager;
    }

    /* WIDTH & HEIGHT METHODS */
    public void setWidthHeight(int width, int height) {
        this.MAX_WIDTH = width;
        this.MAX_HEIGHT = height;
        this.mapSections.clear();
        setupMapSections();
    }

    public void setWidthHeightSection(int width, int height) {
        this.MAX_WIDTH_SECTION = width;
        this.MAX_HEIGHT_SECTION = height;
        this.mapSections.clear();
        setupMapSections();
    }

    /* SETUP MAP */
    private void setupMapSections() {
        int height = MAX_HEIGHT;
        int maxWidthSection = (MAX_WIDTH * 2) / MAX_WIDTH_SECTION;
        int maxHeightSections = (MAX_HEIGHT * 2) / MAX_HEIGHT_SECTION;
        int midWidthSection = maxHeightSections / 2;
        int midHeightSection = maxWidthSection / 2;
        for (int z = 0; z < maxHeightSections; z++) {
            int width = MAX_WIDTH;
            for (int x = 0; x < maxWidthSection; x++) {
                int maxX = MAX_WIDTH;
                int minX = width - MAX_WIDTH_SECTION;
                int maxZ = MAX_HEIGHT;
                int minZ = height - MAX_HEIGHT_SECTION;

                if (x == midWidthSection && z == midHeightSection) {
                    if(world == com.orbitmines.spigot.servers.uhsurvival.utils.enums.World.WORLD.getMap().getWorld()) {
                        this.warzone = new Warzone(world, minX, maxX, minZ, maxZ, x, z);
                        this.mapSections.add(warzone);
                    }
                } else {
                    this.mapSections.add(new MapSection(world, minX, maxX, minZ, maxZ, x, z));
                }
                width -= MAX_WIDTH_SECTION;

            }
            height -= MAX_HEIGHT_SECTION;
        }
    }
}
