package com.orbitmines.spigot.servers.uhsurvival.handlers.map.mapsection;

import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.Dungeon;
import com.orbitmines.spigot.servers.uhsurvival.handlers.mob.Mob;
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

    /* DUNGEON METHODS */
    public void addDungeon(Dungeon dungeon) {
        this.dungeons.add(dungeon);
    }

    public List<Dungeon> getDungeons() {
        return dungeons;
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
