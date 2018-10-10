package com.orbitmines.spigot.servers.uhsurvival2.handler;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.servers.uhsurvival2.UHSurvival;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class UHPlayer extends OMPlayer {

    private static HashMap<UUID, UHPlayer> players = new HashMap<>();

    private UHSurvival uhSurvival;

    public UHPlayer(UHSurvival uhSurvival, Player player) {
        super(player);
        this.uhSurvival = uhSurvival;
        players.put(getUUID(), this);
    }

    @Override
    protected void onLogin() {

    }

    @Override
    protected void onLogout() {

    }

    @Override
    protected void onFirstLogin() {

    }

    @Override
    public boolean canReceiveVelocity() {
        return false;
    }

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
