package com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon;

import com.orbitmines.spigot.api.utils.LocationUtils;
import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.block.FileBlock;
import com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.block.ReplacedBlock;
import com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.loottable.LootTableManager;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.Map;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.mapsection.MapSection;
import com.orbitmines.spigot.servers.uhsurvival.utils.FileBuilder;

import com.orbitmines.spigot.servers.uhsurvival.utils.enums.World;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Robin on 1/11/2018.
 */
public class DungeonManager {

    private static LootTableManager lootTableManager = LootTableManager.get();

    private Map map;

    private HashMap<DungeonFile, FileBuilder> files;
    private HashMap<DungeonFile, Integer> dungeonCount;

    /* CONSTRUCTOR */
    public DungeonManager(Map map){
        this.map = map;
        this.files = new HashMap<>();
        this.dungeonCount = new HashMap<>();
        deserialize();
    }

    /* DUNGEON METHODS (create, build, delete, get) */
    public void createDungeon( Location loc1, Location loc2, String name, String lootTable, Location location){
        List<Block> blocks = LocationUtils.getBlocksBetween(loc1, loc2);
        DungeonFile dungeonFile = new DungeonFile(loc1, loc2, name, lootTable);
        for(org.bukkit.block.Block b : blocks){
            dungeonFile.addBlock(new FileBlock(b.getLocation(), location));
        }
        FileBuilder file = new FileBuilder("dungeon_" + name, loc1.getWorld().getWorldFolder().getPath() + "/dungeonfiles");
        this.files.put(dungeonFile, file);
        this.dungeonCount.put(dungeonFile, 1);
    }

    public void buildDungeon(Location loc, DungeonFile file)    {
        MapSection mapsection = map.getMapSection(loc);
        if(mapsection.canSpawnDungeon()) {
            int maxX = 0, maxY = 0, maxZ = 0;
            int minX = 0, minY = 0, minZ = 0;
            List<ReplacedBlock> replacedBlocks = new ArrayList<>();
            for (FileBlock block : file.getBlocks()) {
                Location l = loc.add(block.getAddedX(), block.getY(), block.getAddedZ());
                block.setBlock(l);
                maxX = Math.max(block.getAddedX(), maxX);
                minX = Math.min(block.getAddedX(), minX);
                maxY = Math.max(block.getY(), maxY);
                minY = Math.min(block.getY(), minY);
                maxZ = Math.max(block.getAddedZ(), maxZ);
                minZ = Math.min(block.getAddedZ(), minZ);
                replacedBlocks.add(new ReplacedBlock(l));
                loc = loc.subtract(block.getAddedX(), block.getY(), block.getAddedZ());
            }
            Location l1 = new Location(loc.getWorld(), minX, minY, minZ);
            Location l2 = new Location(loc.getWorld(), maxX, maxY, maxZ);
            int index = dungeonCount.get(file);
            Dungeon dungeon = new Dungeon(index, file.getName(), new Location[]{l1, l2});
            dungeon.setReplacedBlocks(replacedBlocks);
            mapsection.addDungeon(dungeon);
            dungeonCount.put(file, index++);
            file.resetBlocks();
        }
    }

    public void buildRandomDungeon(Chunk chunk, int degrees){
        DungeonFile df = null;
        for(DungeonFile file : files.keySet()){
            if(MathUtils.randomize(0, 100, file.getSpawnrate())){
                df = file;
            }
        }
        if(df != null){
            int randomY;
            if(df.isSurface()){
                randomY = chunk.getChunkSnapshot().getHighestBlockYAt(8,8);
            } else {
                randomY = MathUtils.randomInteger(df.getMaxY() - df.getMinY()) + df.getMinY();
            }
            Location location = chunk.getBlock(8, randomY, 8).getLocation();
            df.rotateBlocks(degrees);
            buildDungeon(location, df);
        }
    }

    public boolean deleteDungeon(String name){
        for(DungeonFile file : files.keySet()){
            if(file.getName().equalsIgnoreCase(name)){
                files.remove(file);
                return true;
            }
        }
        return false;
    }

    public Set<DungeonFile> getDungeonFiles(){
        return files.keySet();
    }

    public DungeonFile getDungeonFile(String name){
        for(DungeonFile dungeon : files.keySet()){
            if(dungeon.getName().equalsIgnoreCase(name)){
                return dungeon;
            }
        }
        return null;
    }

    /* FILE METHODS (serialize, deserialize) */
    public void serialize(){
        for(DungeonFile dungeon : files.keySet()){
            dungeon.serialize(files.get(dungeon));
        }
        for(MapSection mapSection : map.getMapSections()){
            if(mapSection != null){
                for(Dungeon d : mapSection.getDungeons()){
                    if(d != null){
                        d.serialize();
                    }
                }
            }
        }
        lootTableManager.serialize();
    }

    public void deserialize() {
        {
            File[] files = new File(map.getWorld().getWorldFolder(), "/dungeonfiles").listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f != null && f.getName().startsWith("dungeonfile_")) {
                        FileBuilder fb = new FileBuilder(f);
                        DungeonFile df = new DungeonFile(fb);
                        this.files.put(df, fb);
                    }
                }
            }
        }
        {
            File[] files = new File(map.getWorld().getWorldFolder(), "/dungeons").listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f != null && f.getName().startsWith("dungeon_")) {
                        FileBuilder fb = new FileBuilder(f);
                        Dungeon dungeon = new Dungeon(fb);
                        MapSection mapSection = map.getMapSection(dungeon.getFirstLocation());
                        mapSection.addDungeon(dungeon);
                        this.dungeonCount.put(getDungeonFile(dungeon.getType()), dungeonCount.get(getDungeonFile(dungeon.getType())) + 1);
                    }
                }
            }
        }
        lootTableManager.deserialize(new File(map.getWorld().getWorldFolder(), "/dungeons/loottables"));
    }

    /* STATIC METHODS (getLootTableManager) */
    public static LootTableManager getLootTableManager() {
        return lootTableManager;
    }
}
