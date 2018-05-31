package com.orbitmines.spigot.servers.uhsurvival2.handlers.map;

import com.orbitmines.spigot.servers.uhsurvival2.handlers.UHPlayer;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class MapSection {

    private Map map;

    private List<UHPlayer> players;

    private boolean buildable;
    private boolean pvp;

    private int minX, maxX;
    private int minZ, maxZ;

    public MapSection(Map map, int minX, int minZ, int xOffSet, int zOffSet){
        this.map = map;
        this.minX = minX;
        this.maxX = minX + xOffSet;
        this.minZ = minZ;
        this.maxZ = minZ + zOffSet;
        this.players = new ArrayList<>();
        this.pvp = false;
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
        return (location.getX() - minX < 5 || maxX - location.getX() < 5) || (location.getZ() -minZ < 5 || maxZ - location.getZ() < 5);
    }

    public boolean isLocation(Location location){
        return (minX < location.getX() && maxX > location.getX()) && (minZ < location.getZ() && maxZ > location.getZ());
    }

    /* SETTERS */
    public void setPvP(boolean pvp){
        this.pvp = pvp;
    }

    public void setBuildable(boolean buildable){
        this.buildable = buildable;
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
}
