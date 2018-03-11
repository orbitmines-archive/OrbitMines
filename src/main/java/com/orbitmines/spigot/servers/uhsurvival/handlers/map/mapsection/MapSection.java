package com.orbitmines.spigot.servers.uhsurvival.handlers.map.mapsection;

import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.Dungeon;
import com.orbitmines.spigot.servers.uhsurvival.handlers.mob.Mob;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Robin on 2/27/2018.
 */
public class MapSection {

    private World world;

    private int minX;
    private int maxX;

    private int minZ;
    private int maxZ;

    private int x;
    private int z;

    private boolean pvp;
    private boolean dungeon;

    private List<Dungeon> dungeons;
    private HashMap<UUID, UHPlayer> players;
    private HashMap<UUID, Mob> mobs;

    private String title;
    private String subTitle;

    private String leavingTitle;
    private String leavingSubTitle;

    public MapSection(World world, int minX, int maxX, int minZ, int maxZ, int x, int z){
        this.world = world;
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
        this.x = x;
        this.z = z;

        this.pvp = false;

        this.players = new HashMap<>();
        this.dungeons = new ArrayList<>();
        this.mobs = new HashMap<>();
    }

    /* GETTERS */
    public World getWorld() {
        return world;
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

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public void setPvP(boolean pvp) {
        this.pvp = pvp;
    }

    public boolean canPvP(){
        return pvp;
    }

    /* PLAYER METHODS */
    public void addPlayer(UHPlayer player){
        this.players.put(player.getUUID(), player);
    }

    public void removePlayer(UUID id){
        this.players.remove(id);
    }

    public HashMap<UUID, UHPlayer> getPlayers() {
        return players;
    }

    public void setEnteringMessage(String title, String subTitle){
        this.title = title;
        this.subTitle = subTitle;
    }

    public void setLeavingMessage(String title, String subTitle){
        this.leavingTitle = title;
        this.leavingSubTitle = subTitle;
    }

    public void enter(UHPlayer player){
        addPlayer(player);
        if(hasEnteringMessage()) {
            //TODO: FIGURE OUT HOW TO SEND TITLES! xD
        }
    }

    public void leave(UHPlayer player){
        removePlayer(player.getUUID());
        if(!player.getSection().hasEnteringMessage()) {
            if (hasLeavingMessage()) {
                //TODO: FIGURE OUT HOW TO SEND TITLES!
            }
        }
    }

    public boolean hasLeavingMessage(){
        return leavingTitle != null && leavingSubTitle != null;
    }

    public boolean hasEnteringMessage(){
        return title != null && subTitle != null;
    }
    /* DUNGEON METHODS */
    public void addDungeon(Dungeon dungeon) {
        this.dungeons.add(dungeon);
    }

    public List<Dungeon> getDungeons() {
        return dungeons;
    }

    public Dungeon getDungeon(Location location){
        for(Dungeon dungeon : dungeons){
            int minX = dungeon.getFirstLocation().getBlockX();
            int minY = dungeon.getFirstLocation().getBlockY();
            int minZ = dungeon.getFirstLocation().getBlockZ();
            int maxX = dungeon.getSecondLocation().getBlockX();
            int maxY = dungeon.getSecondLocation().getBlockY();
            int maxZ = dungeon.getSecondLocation().getBlockZ();
            if(minX < location.getX() && location.getX() < maxX){
                if(minY < location.getY() && location.getY() < maxY){
                    if(minZ < location.getZ() && location.getZ() < maxZ){
                        return dungeon;
                    }
                }
            }
        }
        return null;
    }

    public void removeDungeon(Dungeon dungeon){
        this.dungeons.remove(dungeon);
    }

    public void canSpawnDungeon(boolean canSpawnDungeon){
        this.dungeon = canSpawnDungeon;
    }

    public boolean canSpawnDungeon(){
        return dungeon;
    }

    /* MOB METHODS */
    public void addMob(Mob mob){
        this.mobs.put(mob.getEntity().getUniqueId(), mob);
    }

    public Mob getMob(UUID id){
        for(UUID uuid : mobs.keySet()){
            if(uuid.equals(id)){
                return mobs.get(uuid);
            }
        }
        return null;
    }

    public Mob getMob(Entity entity){
        return getMob(entity.getUniqueId());
    }

    public Mob removeMob(UUID id){
        return mobs.get(id);
    }

    public Mob removeMob(Entity entity){
        return removeMob(entity.getUniqueId());
    }
}
