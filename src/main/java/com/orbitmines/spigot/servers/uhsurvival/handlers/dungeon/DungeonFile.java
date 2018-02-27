package com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon;

import com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.block.FileBlock;
import com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.loottable.LootTable;
import com.orbitmines.spigot.servers.uhsurvival.utils.FileBuilder;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 1/7/2018.
 */
public class DungeonFile {

    private int width;
    private int height;
    private int depth;

    private int maxY;
    private int minY;
    private int spawnrate;

    private boolean surface;

    private List<FileBlock> blocks;
    private String name;
    private LootTable lootTable;

    /* CONSTRUCTORS */
    public DungeonFile(Location loc1, Location loc2, String name, String lootTable) {
        this.width = loc1.getBlockX() - loc2.getBlockX();
        this.height = loc1.getBlockY() - loc2.getBlockY();
        this.depth = loc1.getBlockZ() - loc2.getBlockZ();

        this.spawnrate = 0;
        this.maxY = 0;
        this.minY = 0;

        this.surface = false;

        this.name = name;
        this.lootTable = DungeonManager.getLootTableManager().getLootTable(lootTable);
        this.blocks = new ArrayList<>();
    }

    protected DungeonFile(FileBuilder f) {
        this.blocks = new ArrayList<>();
        this.name = (String) f.get("name");
        this.spawnrate = (int) f.get("spawnrate");
        this.lootTable = DungeonManager.getLootTableManager().getLootTable((String) f.get("loottable"));
        this.width = (int) f.get("width");
        this.height = (int) f.get("height");
        this.depth = (int) f.get("depth");
        this.minY = (int) f.get("minY");
        this.maxY = (int) f.get("maxY");
        this.surface = (boolean) f.get("surface");
        int index = (int) f.get("blocks");
        for (int i = 0; i < index; i++) {
            blocks.add(FileBlock.getBlock((String) f.get("block." + i)));
        }
    }

    /* BLOCK METHODS (add, rotate, reset) */
    public void addBlock(FileBlock block) {
        this.blocks.add(block);
    }

    public void rotateBlocks(int degrees) {
        int index = degrees / 90;
        for (FileBlock block : blocks) {
            block.rotate(index);
        }
    }

    public void resetBlocks() {
        for (FileBlock block : blocks) {
            block.reset();
        }
    }

    /* FILE METHODS (serialize) */
    public void serialize(FileBuilder f) {
        f.set("name", name);
        f.set("spawnrate", spawnrate);
        f.set("width", width);
        f.set("height", height);
        f.set("depth", depth);
        f.set("surface", String.valueOf(surface));
        f.set("minY", minY);
        f.set("maxY", maxY);
        f.set("loottable", lootTable.getName());
        int index = 0;
        for (FileBlock block : blocks) {
            f.set("block." + index, block.deserialize());
            index++;
        }
        f.set("blocks", index);
        f.save();
    }

    /* GETTERS */
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }

    public List<FileBlock> getBlocks() {
        return blocks;
    }

    public String getName() {
        return name;
    }

    public LootTable getLootTable() {
        return lootTable;
    }

    public boolean isSurface(){
        return surface;
    }

    public void setSurface(boolean surface){
        this.surface =  surface;
    }

    public int getSpawnrate() {
        return spawnrate;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }
}
