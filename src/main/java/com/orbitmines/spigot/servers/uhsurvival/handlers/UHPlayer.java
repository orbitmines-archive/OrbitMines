package com.orbitmines.spigot.servers.uhsurvival.handlers;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.servers.uhsurvival.UHSurvival;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.mapsection.MapSection;
import com.orbitmines.spigot.servers.uhsurvival.handlers.tool.ToolInventory;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Robin on 2/27/2018.
 */
public class UHPlayer extends OMPlayer {

    private UHSurvival uhSurvival;

    private static HashMap<UUID, UHPlayer> players = new HashMap<>();

    private World world;
    private MapSection section;

    private ToolInventory inventory;

    public UHPlayer(UHSurvival uhSurvival, Player player) {
        super(player);
        this.uhSurvival = uhSurvival;

    }

    @Override
    protected void onLogin() {
        players.put(getUUID(), this);
        this.world = World.getWorldByEnvironment(getWorld().getEnvironment());
        if(world != null && world.getMap() != null) {
            this.section = world.getMap().getMapSection(getLocation());
        }
    }

    @Override
    protected void onLogout() {
        players.remove(getUUID(), this);
    }

    @Override
    public void onVote(int votes) {

    }

    @Override
    public boolean canReceiveVelocity() {
        return false;
    }

    /* TOOL METHODS */
    public ToolInventory getUHInventory() {
        return inventory;
    }

    /* MAP METHODS */
    public MapSection getSection(){
        return section;
    }

    public World getUHWorld() {
        return world;
    }

    /* STATIC METHODS */
    public static HashMap<UUID, UHPlayer> getUHPlayers() {
        return players;
    }

    public static UHPlayer getUHPlayer(UUID id){
        return players.get(id);
    }

    public static UHPlayer getUHPlayer(Player p){
        return getUHPlayer(p.getUniqueId());
    }
}