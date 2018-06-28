package com.orbitmines.spigot.servers.uhsurvival2.handlers.map.dungeon;

import com.orbitmines.spigot.api.utils.Serializer;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.map.dungeon.block.ReplacedBlock;
import com.orbitmines.spigot.servers.uhsurvival2.utils.FileBuilder;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;


public class Dungeon {

    private List<ReplacedBlock> blocks;

    private String type;
    private Location corner, corner1;

    private int index;

    Dungeon(String type, Location corner, Location corner1, int index){
        this.type = type;
        this.corner = corner;
        this.corner1 = corner1;
        this.index = index;
        this.blocks = new ArrayList<>();
    }

    Dungeon(FileBuilder f){
        this.blocks = new ArrayList<>();
        this.type = (String) f.get("type");
        this.corner = Serializer.parseLocation((String) f.get("corner.0"));
        this.corner1 = Serializer.parseLocation((String) f.get("corner.1"));
        this.index = (int) f.get("index");
        int index = (int) f.get("blocks");
        for(int i = 0; i < index; i++){
            blocks.add(ReplacedBlock.fromString((String) f.get("block." + i)));
        }
    }

    /* DUNGEON METHODS */
    public void reverse(){
        for(ReplacedBlock block : blocks){
            block.replace(corner.getWorld());
        }
    }

    /* SETTERS */
    void addReplaceBlocks(ReplacedBlock block){
        this.blocks.add(block);
    }

    /* FILE METHODS */
    void toFile(FileBuilder f){
        f.set("type", type);
        f.set("index", index);
        f.set("corner.0", Serializer.serialize(corner));
        f.set("corner.1", Serializer.serialize(corner1));
        int index = 0;
        for(ReplacedBlock block : blocks){
            f.set("block." + index, block.toString());
        }
        f.set("blocks", index);
        f.save();
    }

    /* GETTERS */
    public Location getLowerCorner() {
        return corner;
    }

    public Location getHigherCorner() {
        return corner1;
    }

    public String getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }
}
