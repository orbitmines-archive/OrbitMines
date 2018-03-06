package com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.block;

import com.orbitmines.spigot.api.utils.MathUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

/**
 * Created by Robin on 1/14/2018.
 */
public class ReplacedBlock {

    private Material m;
    private byte data;
    private int x;
    private int y;
    private int z;

    public ReplacedBlock(Location location){
        this(location.getBlock().getType(), location.getBlock().getData(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public ReplacedBlock(Material m, byte data, int x, int y, int z){
        this.m = m;
        this.data = data;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void replace(World w){
        org.bukkit.block.Block b = new Location(w, x,y,z).getBlock();
        b.setType(m);
        b.setData(data);
    }

    public String serialize(){
        return m.name() + "|" + data + "|" + x + "|" + y + "|" + z;
    }

    public static ReplacedBlock getBlock(String data){
        String[] s = data.split("\\|");
        return new ReplacedBlock(Material.getMaterial(s[0]), Byte.parseByte(s[1]), MathUtils.getInteger(s[2]), MathUtils.getInteger(s[3]), MathUtils.getInteger(s[4]));
    }
}
