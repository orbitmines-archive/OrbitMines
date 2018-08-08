package com.orbitmines.spigot.servers.uhsurvival2.handlers.map;

import com.orbitmines.spigot.servers.uhsurvival2.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.map.dungeon.DungeonManager;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.mob.Arrow;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.mob.Mob;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.mob.MobManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class Map {

    private List<MapSection> mapSections;
    private List<Arrow> arrows;

    private World world;

    private DungeonManager dungeonManager;
    private MobManager mobManager;

    private final int tiles = 9;
    private final int minX, maxX;
    private final int minZ, maxZ;

    public Map(World world, int minX, int maxX, int minZ, int maxZ){
        this.mapSections = new ArrayList<>();
        this.arrows = new ArrayList<>();
        this.world = world;
        this.dungeonManager = new DungeonManager(this);
        this.mobManager = new MobManager(this);
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
        initMapSections();
    }

    /* MOB METHODS */
    public void spawn(Mob mob) {
        if (mob != null) {
            //TODO: CHANGE THIS IF STATEMENT TO MUCH EASIER ONE!
            if (mob.hasMobType() && mob.getType().getType() == EntityType.ARROW) {
                arrows.add((Arrow) mob);
                System.out.println("Arrow added [#" + arrows.size() + "]");
            } else {
                MapSection mapSection = getMapSection(mob.getLocation());
                if (mapSection != null) {
                    mapSection.addMob(mob);
                }
            }
        }
    }

    public Mob getMob(Entity en){
        if(en.getType() == EntityType.ARROW) {
            for (Mob mob : arrows) {
                if(mob.equals(en)){
                    return mob;
                }
            }
        } else {
            MapSection mapSection = getMapSection(en.getLocation());
            if(mapSection != null){
                return mapSection.getMob(en);
            }
        }
        return null;
    }

    public void die(Entity entity){
        MapSection mapSection = getMapSection(entity.getLocation());
        if(mapSection != null){
            Mob mob = mapSection.removeMob(entity);
            mobManager.onDeath(mob);
        }
    }

    /* BOOLEANS */
    public boolean isInMap(UHPlayer player){
        for(MapSection mapSection : mapSections){
            if(mapSection.isInSection(player)){
                return true;
            }
        }
        return false;
    }

    /* GETTERS */
    public int getTiles() {
        return tiles;
    }

    public World getWorld() {
        return world;
    }

    public MapSection getMapSection(Location location) {
        for (MapSection mapSection : mapSections) {
            if (mapSection.isLocation(location)) {
                return mapSection;
            }
        }
        return null;
    }

    public List<MapSection> getMapSections() {
        return mapSections;
    }

    public DungeonManager getDM(){
        return dungeonManager;
    }

    public MobManager getMM(){
        return mobManager;
    }

    /* INIT METHODS */
    private void initMapSections(){
        int xOffSet = (maxX - minX) / tiles;
        int zOffSet = (maxZ - minZ) / tiles;
        for(int x = 0; x < tiles; x++){
            for(int z = 0; z < tiles; z++){
                int _x = minX + (xOffSet * x);
                int _z = minZ + (zOffSet * z);
                mapSections.add(new MapSection(this, x, z, _x, _z, xOffSet, zOffSet));
            }
        }
    }
}

