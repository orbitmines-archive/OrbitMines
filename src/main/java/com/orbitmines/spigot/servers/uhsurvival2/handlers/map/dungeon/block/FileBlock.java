package com.orbitmines.spigot.servers.uhsurvival2.handlers.map.dungeon.block;

import com.orbitmines.spigot.api.utils.MathUtils;
import org.bukkit.Location;
import org.bukkit.Material;

public class FileBlock {

    private Material m;
    private byte data;
    private int x, y, z, addedX, addedZ;

    public FileBlock(Location orginal, Location loc){
        this(loc.getBlock().getType(), loc.getBlock().getData(), loc.getBlockX() - orginal.getBlockX(), loc.getBlockY() - orginal.getBlockY(), loc.getBlockZ() - orginal.getBlockZ());
    }

    private FileBlock(Material m, byte data, int x, int y, int z){
        this.m = m;
        this.data = data;
        this.x = x;
        this.y = y;
        this.z = z;
        this.addedX = x;
        this.addedZ = z;
    }

    /* GETTERS */
    public int getX() {
        return addedX;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return addedZ;
    }

    /* BLOCK METHODS */
    public void rotate(int degrees){
        for(int i = 0; i < degrees / 90; i++){
            int x = addedX;
            this.addedX = addedZ;      //(x = 2) (z = 1)   (2,1)
            this.addedZ = -x;          //(x = 1) (z = -2) (1,-2)
        }                               //(x = -2) (z = -1) (-2 -1)
    }                                   //(x = -1) (z = 2) (-1, 2)

    public void reset(){
        this.addedX = x;
        this.addedZ = z;
    }

    /* FILE METHODS */
    @Override
    public String toString(){
        return m.name() + "|" + data + "|" + x + "|" + y + "|" + "|" + z;
    }

    public static FileBlock fromString(String data){
        String[] s = data.split("\\|");
        return new FileBlock(Material.getMaterial(s[0]), Byte.parseByte(s[1]), MathUtils.getInteger(s[2]), MathUtils.getInteger(s[3]), MathUtils.getInteger(s[4]));
    }
}
