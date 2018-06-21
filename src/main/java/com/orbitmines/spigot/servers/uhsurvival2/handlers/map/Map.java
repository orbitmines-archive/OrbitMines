package com.orbitmines.spigot.servers.uhsurvival2.handlers.map;

import com.orbitmines.spigot.servers.uhsurvival2.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.map.dungeon.DungeonManager;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class Map {

    private List<MapSection> mapSections;

    private World world;

    private DungeonManager dungeonManager;

    private final int tiles = 9;
    private final int minX, maxX;
    private final int minZ, maxZ;

    public Map(World world, int minX, int maxX, int minZ, int maxZ){
        this.mapSections = new ArrayList<>();
        this.world = world;
        this.dungeonManager = new DungeonManager(this);
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
        initMapSections();
    }

    /* BOOLEANS */
    public boolean isInMap(UHPlayer player){
        for(MapSection mapSection : mapSections){
            if(mapSection.isInSection(player)){
                return true;
            }
        }
        return false;
    }

    /* GETTERS */
    public int getTiles() {
        return tiles;
    }

    public World getWorld() {
        return world;
    }

    public MapSection getMapSection(Location location) {
        for (MapSection mapSection : mapSections) {
            if (mapSection.isLocation(location)) {
                return mapSection;
            }
        }
        return null;
    }

    public List<MapSection> getMapSections() {
        return mapSections;
    }

    public DungeonManager getDM(){
        return dungeonManager;
    }

    /* INIT METHODS */
    private void initMapSections(){
        int xOffSet = (maxX - minX) / tiles;
        int zOffSet = (maxZ - minZ) / tiles;
        for(int x = 0; x < tiles; x++){
            for(int z = 0; z < tiles; z++){
                int _x = minX + (xOffSet * x);
                int _z = minZ + (zOffSet * z);
                mapSections.add(new MapSection(this, x, z, _x, _z, xOffSet, zOffSet));
            }
        }
    }
}

