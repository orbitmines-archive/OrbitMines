package com.orbitmines.spigot.servers.uhsurvival.handlers.map;

import com.orbitmines.spigot.api.utils.LocationUtils;
import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.dungeon.Dungeon;
import com.orbitmines.spigot.servers.uhsurvival.handlers.mob.Mob;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class MapSection {

    private Map map;

    private List<Mob> mobs;
    private List<UHPlayer> players;
    private List<Dungeon> dungeons;

    private boolean buildable;
    private boolean pvp;

    private int x, z;

    private int maxDungeons;

    private int minX, maxX;
    private int minZ, maxZ;

    protected MapSection(Map map, int x, int z,  int minX, int minZ, int xOffSet, int zOffSet){
        this.map = map;
        this.x = x;
        this.z = z;
        this.minX = minX;
        this.maxX = minX + xOffSet;
        this.minZ = minZ;
        this.maxZ = minZ + zOffSet;
        this.mobs = new ArrayList<>();
        this.players = new ArrayList<>();
        this.dungeons = new ArrayList<>();
        this.pvp = false;
    }
    /* MOB */
    void addMob(Mob mob){
        this.mobs.add(mob);
    }

    Mob getMob(Entity entity){
        for(Mob mob : mobs){
            if(mob.equals(entity)){
                return mob;
            }
        }
        return null;
    }

    Mob removeMob(Entity entity){
        Mob mob = getMob(entity);
        mobs.remove(mob);
        return mob;
    }

    /* DUNGEON */
    public void spawnDungeon(Dungeon dungeon){
        this.dungeons.add(dungeon);
    }

    public boolean isInDungeon(Location loc){
        return getDungeon(loc) != null;
    }

    public Dungeon getDungeon(Location loc){
        for(Dungeon dungeon : dungeons){
            if(LocationUtils.isBetween(dungeon.getLowerCorner(), dungeon.getHigherCorner(), loc)){
                return dungeon;
            }
        }
        return null;
    }

    public List<Dungeon> getDungeons() {
        return dungeons;
    }

    public int getDungeonAmount(){
        return dungeons.size();
    }

    public List<Dungeon> getDungeons(String name){
        List<Dungeon> dungeons = new ArrayList<>();
        for(Dungeon dungeon : this.dungeons){
            if(dungeon.getType().equalsIgnoreCase(name)){
                dungeons.add(dungeon);
            }
        }
        return dungeons;
    }

    public int getDungeonAmount(String name){
        return getDungeons(name).size();
    }

    public void removeDungeon(Location location){
        Dungeon d = getDungeon(location);
        if(d != null){
            d.reverse();
            dungeons.remove(d);
        }
    }

    /* PLAYER METHODS */
    public void addPlayer(UHPlayer player){
        this.players.add(player);
    }

    public void removePlayer(UHPlayer player){
        this.players.remove(player);
    }

    public boolean isInSection(UHPlayer player){
        return players.contains(player) && isLocation(player.getLocation());
    }

    /* MOVE METHODS */
    public boolean isTransfering(Location location){
        return (location.getX() - minX < 0.000000001 || maxX - location.getX() < 0.000000001) || (location.getZ() - minZ < 0.000000001 || maxZ - location.getZ() < 0.000000001);
    }

    public boolean isLocation(Location location){
        return (minX <= location.getX() && maxX >= location.getX()) && (minZ <= location.getZ() && maxZ >= location.getZ());
    }

    /* SETTERS */
    public void setPvP(boolean pvp){
        this.pvp = pvp;
    }

    public void setBuildable(boolean buildable){
        this.buildable = buildable;
    }

    public void setMaxDungeons(int maxDungeons){
        this.maxDungeons = maxDungeons;
    }

    /* GETTERS */
    public Map getMap() {
        return map;
    }

    public boolean canPvP(){
        return pvp;
    }

    public boolean isBuildable() {
        return buildable;
    }

    public int getMaxDungeons() {
        return maxDungeons;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxZ() {
        return maxZ;
    }
}
