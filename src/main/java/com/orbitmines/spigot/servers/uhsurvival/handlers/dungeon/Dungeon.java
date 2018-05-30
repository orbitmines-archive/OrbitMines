package com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon;

import com.orbitmines.spigot.api.utils.Serializer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.block.ReplacedBlock;
import com.orbitmines.spigot.servers.uhsurvival.utils.FileBuilder;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 1/14/2018.
 */
public class Dungeon {

    //TODO: ADD MORE FEATURES LIKE TRAPS / MOB
    private FileBuilder f;
    private List<ReplacedBlock> replacedBlocks;
    private String type;
    private Location firstLocation;
    private Location secondLocation;
    private int index;

    /* CONSTRUCTOR */
    public Dungeon(int index, String type, Location[] locations) {
        this.replacedBlocks = new ArrayList<>();
        this.firstLocation = locations[0];
        this.secondLocation = locations[1];
        this.type = type;
        this.index = index;
        this.f = new FileBuilder(firstLocation.getWorld().getWorldFolder().getPath() + "/dungeon", "dungeon_" + type + "_" + index);
    }

    protected Dungeon(FileBuilder f){
        this.f = f;
        this.type = (String) f.get("type");
        this.firstLocation = Serializer.parseLocation((String) f.get("location.first"));
        this.secondLocation = Serializer.parseLocation((String) f.get("location.second"));
        int index = (int) f.get("block");
        this.replacedBlocks = new ArrayList<>();
        for(int i = 0; i < index; i++){
           this.replacedBlocks.add(ReplacedBlock.getBlock((String) f.get("block." + i)));
        }
        this.index = (int) f.get("index");
    }

    public void reverse(){
        for(ReplacedBlock block : replacedBlocks){
            block.replace(firstLocation.getWorld());
        }
    }

    /* GETTERS & SETTERS */
    public String getType() {
        return type;
    }

    public List<ReplacedBlock> getReplacedBlocks() {
        return replacedBlocks;
    }

    public void setReplacedBlocks(List<ReplacedBlock> replacedBlocks) {
        this.replacedBlocks = replacedBlocks;
    }

    public Location getFirstLocation() {
        return firstLocation;
    }

    public Location getSecondLocation() {
        return secondLocation;
    }

    public int getIndex() {
        return index;
    }

    /* FILE METHODS (serialize) */
    public void serialize(){
        f.set("location.first", Serializer.serialize(firstLocation));
        f.set("location.second", Serializer.serialize(secondLocation));
        f.set("type", type);
        f.set("index", index);
        int index = 0;
        for(ReplacedBlock replacedBlock : replacedBlocks){
            f.set("block." + index, replacedBlock.serialize());
        }
        f.set("block", index);
        f.save();
    }
}
