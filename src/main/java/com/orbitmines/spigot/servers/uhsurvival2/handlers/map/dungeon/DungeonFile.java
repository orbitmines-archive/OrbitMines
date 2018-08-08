package com.orbitmines.spigot.servers.uhsurvival2.handlers.map.dungeon;

import com.orbitmines.spigot.api.utils.LocationUtils;
import com.orbitmines.spigot.servers.uhsurvival2.UHSurvival;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.map.dungeon.block.FileBlock;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.map.dungeon.block.ReplacedBlock;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.map.dungeon.loottable.LootTable;
import com.orbitmines.spigot.servers.uhsurvival2.utils.FileBuilder;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class DungeonFile {

    private final int width, height, depth;

    private int adWidth, adDepth;

    private int minY, maxY;

    private double spawnrate;

    private boolean surface;

    private int amount = 0;

    private List<FileBlock> blocks;
    private LootTable lootTable;
    private String name;

    DungeonFile(String name, String loottable, Location corner, Location corner2){
        this.width = corner.getBlockX() - corner2.getBlockX();
        this.height = corner.getBlockY() - corner2.getBlockY();
        this.depth = corner.getBlockZ() - corner2.getBlockZ();

        this.minY = 0;
        this.maxY = 0;

        this.spawnrate = 0;

        this.surface = false;

        this.name = name;
        this.lootTable = UHSurvival.getLM().getLootTable(loottable);

        this.blocks = new ArrayList<>();

        this.adWidth = width;
        this.adDepth = depth;
    }

    DungeonFile(FileBuilder f){
        this.blocks = new ArrayList<>();
        this.name = (String) f.get("name");
        this.spawnrate = (double) f.get("spawnrate");
        this.lootTable = UHSurvival.getLM().getLootTable((String) f.get("loottable"));
        this.width = (int) f.get("width");
        this.height = (int) f.get("height");
        this.depth = (int) f.get("depth");
        this.minY = (int) f.get("minY");
        this.maxY = (int) f.get("maxY");
        this.surface = Boolean.valueOf((String) f.get("surface"));
        int index = (int) f.get("blocks");
        for(int i = 0; i < index; i++){
            this.blocks.add(FileBlock.fromString((String) f.get("block." + i)));
        }
        this.adWidth = width;
        this.adDepth = depth;
    }

    /* DUNGEON METHODS */
    Dungeon buildDungeon(Location location){
        amount++;
        Location loc1 = new Location(location.getWorld(), location.getBlockX() + adWidth, location.getBlockY() + height, location.getBlockZ() + adDepth);
        location = location.subtract(adWidth, height, adDepth);
        Dungeon dungeon = new Dungeon(name, loc1, location, amount);
        for(Block block : LocationUtils.getBlocksBetween(location, loc1)){
            dungeon.addReplaceBlocks(new ReplacedBlock(block.getLocation()));
        }
        for(FileBlock block : blocks) {
            block.build(location, lootTable);
        }
        return dungeon;
    }

    /* BLOCK METHODS */
    void addBlock(FileBlock fileBlock){
        this.blocks.add(fileBlock);
    }

    void rotate(int degrees){
        for(int i = 0; i < degrees / 90; i++){
            int width = adWidth;
            this.adWidth = adDepth;
            this.adDepth = -width;
        }
        for(FileBlock block : blocks){
            block.rotate(degrees);
        }
    }

    void reset(){
        for(FileBlock block : blocks){
            block.reset();
        }
        this.adWidth = width;
        this.adDepth = depth;
    }

    /* FILE METHODS */
    void toFile(FileBuilder f){
        f.set("name", name);
        f.set("spawnrate", spawnrate);
        f.set("width", width);
        f.set("height", height);
        f.set("depth", depth);
        f.set("surface", String.valueOf(surface));
        f.set("minY", minY);
        f.set("maxY", maxY);
        f.set("loottable", lootTable == null ? "null" : lootTable.getName());
        int index = 0;
        for(FileBlock block : blocks){
            f.set("block." + index, block.toString());
            index++;

        }
        f.set("blocks", index);
        f.save();
    }

    /* GETTERS */
    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    int getDepth() {
        return depth;
    }

    public String getName() {
        return name;
    }

    LootTable getLootTable() {
        return lootTable;
    }

    List<FileBlock> getBlocks() {
        return blocks;
    }

    boolean isSurface() {
        return surface;
    }

    double getSpawnrate() {
        return spawnrate;
    }

    int getMinY() {
        return minY;
    }

    int getMaxY() {
        return maxY;
    }

    /* SETTERS */
    public void setSpawnrate(double spawnrate) {
        this.spawnrate = spawnrate;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public void setSurface(boolean surface) {
        this.surface = surface;
    }

    public void setLootTable(String lootTable) {
        this.lootTable = UHSurvival.getLM().getLootTable(lootTable);
    }
}
