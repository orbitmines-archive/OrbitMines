package com.orbitmines.spigot.servers.uhsurvival2.handlers;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.servers.uhsurvival2.UHSurvival;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.map.MapSection;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.mob.Attacker;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class UHPlayer extends OMPlayer implements Attacker {

    private static HashMap<UUID, UHPlayer> players = new HashMap<>();

    private MapSection mapSection;

    private UHSurvival instance;

    public UHPlayer(UHSurvival uhSurvival, Player player) {
        super(player);
        players.put(getUUID(), this);
        this.instance = uhSurvival;
        this.mapSection = uhSurvival.getMap(getWorld()).getMapSection(getLocation());
    }

    /* MAP METHODS */
    public MapSection getMapLocation() {
        return mapSection;
    }

    /* OM-PLAYER METHODS */
    @Override
    protected void onLogin() {

    }

    @Override
    protected void onLogout() {

    }

    @Override
    public void onVote(int votes) {

    }

    @Override
    public boolean canReceiveVelocity() {
        return false;
    }

    /* ATTACKER METHODS TODO!*/
    @Override
    public void attack() {

    }

    @Override
    public void defend() {

    }

    /* STATIC METHODS */
    public static UHPlayer getUHPlayer(UUID id){
        return players.get(id);
    }

    public static UHPlayer getUHPlayer(Player player){
        return getUHPlayer(player.getUniqueId());
    }

    public static Collection<UHPlayer> getUHPlayers(){
        return players.values();
    }
}
