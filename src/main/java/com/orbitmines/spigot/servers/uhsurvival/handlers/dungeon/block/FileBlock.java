package com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.block;

import com.orbitmines.spigot.api.utils.MathUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;

/**
 * Created by Robin on 1/8/2018.
 */
public class FileBlock {

    private Material m;
    private byte data;
    private int x;
    private int y;
    private int z;
    private int addedX;
    private int addedZ;

    /* CONSTRUCTORS */
    public FileBlock(Location original, Location newLocation){
        this(newLocation.getBlock().getType(), newLocation.getBlock().getData(), newLocation.getBlockX() - original.getBlockX(), newLocation.getBlockY() - original.getBlockY(), newLocation.getBlockZ() - original.getBlockZ());
    }

    public FileBlock(Material m, byte data, int addedX, int addedY, int addedZ) {
        this.m = m;
        this.data = data;
        this.x = addedX;
        this.y = addedY;
        this.z = addedZ;
    }

    /* GETTERS & SETTERS */
    public Chest getChest(Location location){
        return ((Chest) location.add(addedX, y, addedZ).getBlock());
    }

    public boolean isChest(){
        return m == Material.CHEST;
    }

    public void setBlock(Location location){
        location.getBlock().setType(m);
        location.getBlock().setData(data);
    }

    public int getAddedX() {
        return addedX;
    }

    public int getY() {
        return y;
    }

    public int getAddedZ(){
        return addedZ;
    }

    /* BLOCK METHODS (rotate, reset) */
    public void rotate(int i){
        for(int y = 0; y < i; y++) {
            int x = addedX;
            int z = addedZ;
            this.addedX = z;
            this.addedZ = -x;
        }
    }

    public void reset(){
        this.addedX = x;
        this.addedZ = z;
    }

    /* FILE METHODS (deserialize) */
    public String deserialize(){
        return m.name() + "|" + data + "|" + x + "|" + y + "|" + z;
    }

    /* STATIC METHODS (getBlock) */
    public static FileBlock getBlock(String data){
        String[] s = data.split("\\|");
        return new FileBlock(Material.getMaterial(s[0]), Byte.parseByte(s[1]), MathUtils.getInteger(s[2]), MathUtils.getInteger(s[3]), MathUtils.getInteger(s[4]));
    }
}
