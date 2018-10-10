package com.orbitmines.spigot.servers.uhsurvival.handlers.map.dungeon.block;

import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.dungeon.loottable.LootTable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;

public class FileBlock {

    private Material m;
    private int x, y, z, addedX, addedZ;

    public FileBlock(Location orginal, Location loc){
        this(loc.getBlock().getType(), loc.getBlockX() - orginal.getBlockX(), loc.getBlockY() - orginal.getBlockY(), loc.getBlockZ() - orginal.getBlockZ());
    }

    private FileBlock(Material m, int x, int y, int z){
        this.m = m;
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

    public void build(Location org, LootTable lootTable){
        Location loc = org.add(addedX, y, addedZ);
        loc.getBlock().setType(m);
        if(m == Material.CHEST && lootTable != null){
            lootTable.randomizeInventory(((Chest) loc).getBlockInventory());
        }
        org.subtract(addedX, y, addedZ);
    }

    /* FILE METHODS */
    @Override
    public String toString(){
        return m.name() + "|" + x + "|" + y + "|" + z;
    }

    public static FileBlock fromString(String data){
        String[] s = data.split("\\|");
        return new FileBlock(Material.getMaterial(s[0]), MathUtils.getInteger(s[2]), MathUtils.getInteger(s[3]), MathUtils.getInteger(s[4]));
    }
}
