package com.orbitmines.spigot.servers.uhsurvival2.handlers;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.servers.uhsurvival2.UHSurvival;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.map.Map;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.map.MapSection;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.mob.Attacker;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.tool.ToolInventory;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class UHPlayer extends OMPlayer implements Attacker {

    private static HashMap<UUID, UHPlayer> players = new HashMap<>();

    private MapSection mapSection;
    private ToolInventory inventory;

    private UHSurvival instance;

    public UHPlayer(UHSurvival uhSurvival, Player player) {
        super(player);
        players.put(getUUID(), this);
        this.instance = uhSurvival;
        this.updateMapLocation();
        this.inventory = new ToolInventory(getInventory());
    }

    /* MAP METHODS */
    public MapSection getMapLocation() {
        return mapSection;
    }

    public void updateMapLocation(){
        if(this.mapSection != null) {
            this.mapSection.removePlayer(this);
        }
        Map map = instance.getMap(this.getWorld());
        if(map != null){
            this.mapSection = map.getMapSection(this.getLocation());
            this.mapSection.addPlayer(this);
        }
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
    public boolean attack(Attacker attacker) {
        if(attacker instanceof UHPlayer){
            UHPlayer p = (UHPlayer) attacker;
            if(p.getMapLocation().canPvP() && getMapLocation().canPvP()){
                return p.defend(attacker);
            } else {
                return true;
            }
        } else {
            return attacker.defend(attacker);
        }
    }

    @Override
    public boolean defend(Attacker attacker) {
        //TODO: ADD BLOCK CHANCES IN THE TOOLS & ADD ENCHANTMENTS!
        return false;
    }

    @Override
    public ToolInventory getToolInventory() {
        return inventory;
    }

    @Override
    public boolean hasInventory() {
        return true;
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
