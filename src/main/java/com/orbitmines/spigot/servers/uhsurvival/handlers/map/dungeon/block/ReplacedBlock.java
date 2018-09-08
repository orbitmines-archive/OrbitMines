package com.orbitmines.spigot.servers.uhsurvival.handlers.map.dungeon.block;

import com.orbitmines.spigot.api.utils.MathUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class ReplacedBlock {

    private Material m;
    private int x, y, z;

    public ReplacedBlock(Location loc){
        this(loc.getBlock().getType(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    private ReplacedBlock(Material m, int x, int y, int z){
        this.m = m;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /* BLOCK METHODS */
    public void replace(World w){
        Block b = w.getBlockAt(x,y,z);
        b.setType(m);
    }

    /* FILE METHODS */
    @Override
    public String toString(){
        return m.name() + "|" + x + "|" + y + "|" + z;
    }

    public static ReplacedBlock fromString(String data){
        String[] s = data.split("\\|");
        return new ReplacedBlock(Material.getMaterial(s[0]), MathUtils.getInteger(s[2]), MathUtils.getInteger(s[3]), MathUtils.getInteger(s[4]));
    }
}
