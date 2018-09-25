package com.orbitmines.spigot.servers.uhsurvival2.handler.map;

import com.orbitmines.spigot.servers.uhsurvival2.handler.map.section.MapSection;

import java.util.ArrayList;
import java.util.List;

public class Map {

    private List<MapSection> sections;

    private int minX, maxX, minZ, maxZ, tiles;

    public Map(int minX, int maxX, int minZ, int maxZ, int tiles){
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
        this.tiles = tiles;
        this.sections = new ArrayList<>();
        calculateSections();
    }

    public List<MapSection> getSections() {
        return sections;
    }

    private void calculateSections(){
        int x = maxX - minX;
        int z = maxZ - minZ;

        int xPerTile = x / tiles;
        int zPerTile = z / tiles;

        int currentX = minX;
        int currentZ;

        int X = 0;
        int Z;

        for(int x1 = 0; x1 < tiles; x1++) {
            currentZ = minZ;
            Z = 0;
            currentX += xPerTile;
            for (int z1 = 0; z1 < tiles; z1++) {
                currentZ += zPerTile;
                sections.add(new MapSection(X, Z, currentX - xPerTile, currentZ - zPerTile, currentX, currentZ));
                Z++;
            }
            X++;
        }
    }
}
