package com.orbitmines.spigot.servers.uhsurvival2.handlers.map.dungeon;

import com.orbitmines.api.StaffRank;
import com.orbitmines.spigot.api.utils.LocationUtils;
import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.map.Map;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.map.MapSection;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.map.dungeon.block.FileBlock;
import com.orbitmines.spigot.servers.uhsurvival2.utils.FileBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DungeonManager {

    private static List<DungeonFile> allDungeons = new ArrayList<>();
    private static DungeonSelector selector = new DungeonSelector(StaffRank.DEVELOPER, Material.ARROW, (byte) 0);

    private HashMap<DungeonFile, FileBuilder> dungeonFiles;
    private HashMap<Dungeon, FileBuilder> dungeons;

    private Map map;

    private String directory;
    private String directoryFiles;

    public DungeonManager(Map map){
        this.map = map;
        this.dungeonFiles = new HashMap<>();
        this.dungeons = new HashMap<>();
        this.directory = map.getWorld().getWorldFolder().getAbsolutePath() + "/dungeons";
        this.directoryFiles = directory + "/files";
        this.init();
    }

    /* DUNGEON METHODS */

        /* private */
        private void createDungeon(Location corner, Location corner1, String name, String loottable){
            List<Block> blocks = LocationUtils.getBlocksBetween(corner, corner1);
            Location highestCorner = getHighest(corner, corner1);
            Location lowestCorner = getLowest(corner, corner1);
            DungeonFile dungeonFile = new DungeonFile(name, loottable, highestCorner, lowestCorner);
            for(Block b : blocks){
                dungeonFile.addBlock(new FileBlock(b.getLocation(), lowestCorner));
            }
            FileBuilder file = new FileBuilder(directoryFiles, "dungeon_" + name);
            dungeonFiles.put(dungeonFile, file);
            allDungeons.add(dungeonFile);
        }

        private boolean buildDungeon(Location loc, DungeonFile file){
        MapSection mapSection = map.getMapSection(loc);
        boolean spawn = true;
        /* checking if no other dungeon spawned */
        if(mapSection.isInDungeon(loc)){
            spawn = false;
        }
        //TODO: CHECK IF OTHER STUFF LIKE CLAIMS ECT DID NOT INTERFERE!
        if(spawn) {
            Dungeon dungeon = file.buildDungeon(loc);
            FileBuilder f = new FileBuilder(directory, "dungeon_" + file.getName() + "_" + dungeon.getIndex());
            mapSection.spawnDungeon(dungeon);
            this.dungeons.put(dungeon, f);
        }
        return spawn;
    }

        private boolean buildDungeonRandomly(DungeonFile d){
        MapSection mapSection = map.getMapSections().get(MathUtils.randomInteger(map.getTiles()));
        if(d != null) {
            int x = MathUtils.randomInteger(mapSection.getMaxX() - mapSection.getMinX()) + mapSection.getMinX();
            int y;
            int z = MathUtils.randomInteger(mapSection.getMaxZ() - mapSection.getMinZ()) + mapSection.getMinZ();
            if (d.isSurface()) {
                y = map.getWorld().getHighestBlockYAt(x, z);
            } else {
                y = MathUtils.randomInteger(d.getMaxY() - d.getMinY()) + d.getMinY();
            }
            /* return value */
            return this.buildDungeon(new Location(map.getWorld(), x, y, z), d);
        }
        return false;
    }

        /* public */
        public boolean buildDungeon(UHPlayer player, String type){
        DungeonFile file = getDungeonFile(type);
        return file != null && this.buildDungeon(player.getLocation(), file);
    }

        public boolean buildDungeonRandomly(){
        MapSection mapSection = map.getMapSections().get(MathUtils.randomInteger(map.getTiles()));
        if(mapSection.getDungeonAmount() < mapSection.getMaxDungeons()){
            DungeonFile d = null;
            /* finding dungeon */
            while(d == null){
                for(DungeonFile file : dungeonFiles.keySet()){
                    if(MathUtils.randomize(0, 100, (int) file.getSpawnrate())){
                        d = file;
                    }
                }
            }
            /* return value */
            return this.buildDungeonRandomly(d);
        }
        return false;
    }

    /* STATIC METHODS */
    public static void createDungeon(UHPlayer player, String name, String loottable) {
        DungeonManager dm = player.getMapLocation().getMap().getDM();
        if (dm != null) {
            if (selector.hasSelectedArea(player)) {
                Location[] locations = selector.getLocations(player);
                dm.createDungeon(locations[0], locations[1], name, loottable);
            }
        }
    }

    public static DungeonSelector getDungeonSelector(){
        return selector;
    }

    /* GETTERS */
    private DungeonFile getDungeonFile(String type){
        for(DungeonFile dungeonFile : allDungeons){
            if(dungeonFile.getName().equalsIgnoreCase(type)){
                return dungeonFile;
            }
        }
        return null;
    }

    private Location getLowest(Location loc, Location loc1){
        double x = (double) loc.getBlockX() > loc1.getBlockX() ? loc1.getBlockX() : loc.getBlockX();
        double y = (double) loc.getBlockY() > loc1.getBlockY() ? loc1.getBlockY() : loc.getBlockY();
        double z = (double) loc.getBlockZ() > loc1.getBlockZ() ? loc1.getBlockZ() : loc.getBlockZ();
        return new Location(loc.getWorld(), x,y,z);
    }

    private Location getHighest(Location loc, Location loc1){
        double x = (double) loc.getBlockX() < loc1.getBlockX() ? loc1.getBlockX() : loc.getBlockX();
        double y = (double) loc.getBlockY() < loc1.getBlockY() ? loc1.getBlockY() : loc.getBlockY();
        double z = (double) loc.getBlockZ() < loc1.getBlockZ() ? loc1.getBlockZ() : loc.getBlockZ();
        return new Location(loc.getWorld(), x,y,z);
    }


    /* FILE METHODS */
    public void serialize(){
        for(DungeonFile file : dungeonFiles.keySet()){
            file.toFile(dungeonFiles.get(file));
        }
        for(Dungeon dungeon : dungeons.keySet()){
            dungeon.toFile(dungeons.get(dungeon));
        }
    }

    private void init() {
        {
            File file = new File(directory);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        if (!f.isDirectory()) {
                            if (f.getName().startsWith("dungeon_")) {
                                FileBuilder fileBuilder = new FileBuilder(f);
                                dungeons.put(new Dungeon(fileBuilder), fileBuilder);
                            }
                        }
                    }
                }
            }
        }
        {
            File file = new File(directoryFiles);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        if (!f.isDirectory()) {
                            if (f.getName().startsWith("dungeon_")) {
                                FileBuilder fileBuilder = new FileBuilder(f);
                                dungeonFiles.put(new DungeonFile(fileBuilder), fileBuilder);
                            }
                        }
                    }
                }
            }
        }
    }
}
