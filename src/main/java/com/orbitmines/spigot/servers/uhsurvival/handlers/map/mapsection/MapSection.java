package com.orbitmines.spigot.servers.uhsurvival.handlers.map.mapsection;

import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import org.bukkit.World;

import java.util.HashMap;
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

    private HashMap<UUID, UHPlayer> players;

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
    }

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

    public void addPlayer(UHPlayer player){
        this.players.put(player.getUUID(), player);
    }

    public void removePlayer(UUID id){
        this.players.remove(id);
    }

    public HashMap<UUID, UHPlayer> getPlayers() {
        return players;
    }
}
