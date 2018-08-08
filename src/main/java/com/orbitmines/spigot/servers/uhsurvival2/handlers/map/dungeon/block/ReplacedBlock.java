package com.orbitmines.spigot.servers.uhsurvival2.handlers.map.dungeon.block;

import com.orbitmines.spigot.api.utils.MathUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class ReplacedBlock {

    private Material m;
    private byte data;
    private int x, y, z;

    public ReplacedBlock(Location loc){
        this(loc.getBlock().getType(), loc.getBlock().getData(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    private ReplacedBlock(Material m, byte data, int x, int y, int z){
        this.m = m;
        this.data = data;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /* BLOCK METHODS */
    public void replace(World w){
        Block b = w.getBlockAt(x,y,z);
        b.setType(m);
        b.setData(data);
    }

    /* FILE METHODS */
    @Override
    public String toString(){
        return m.name() + "|" + data + "|" + x + "|" + y + "|" + z;
    }

    public static ReplacedBlock fromString(String data){
        String[] s = data.split("\\|");
        return new ReplacedBlock(Material.getMaterial(s[0]), Byte.parseByte(s[1]), MathUtils.getInteger(s[2]), MathUtils.getInteger(s[3]), MathUtils.getInteger(s[4]));
    }
}
