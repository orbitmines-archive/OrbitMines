package com.orbitmines.spigot.servers.uhsurvival2.handler.map.section;

import org.bukkit.Location;

public class MapSection {

    private int x, z;
    private int minX, minZ;
    private int maxX, maxZ;

    public MapSection(int x, int z, int minX, int minZ, int maxX, int maxZ) {
        this.x = x;
        this.z = z;
        this.minX = minX;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxZ = maxZ;
    }

//    public boolean isSection(Location location){
//        if(minX < location.getX() && location.getX() < maxX){
//            if(minZ < location.getZ() && location.getZ() < maxZ){
//                return true;
//            }
//        }
//        return false;
//    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }
}
