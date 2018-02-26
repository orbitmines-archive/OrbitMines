package com.orbitmines.spigot.servers.survival.handlers;

import com.orbitmines.api.database.*;
import com.orbitmines.api.database.Set;
import com.orbitmines.api.database.tables.survival.TableSurvivalPlayers;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.claim.Claim;
import com.orbitmines.spigot.servers.survival.handlers.claim.Visualization;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class SurvivalPlayer extends OMPlayer {
    
    protected static List<SurvivalPlayer> players = new ArrayList<>();

    private final Survival survival;

    private int earthMoney;
    private int claimBlocks;

    private java.util.Set<Survival.Settings> settings;

    private Claim.ToolType claimToolType;
    private Visualization visualization;
    private Claim lastClaim;
    private Claim resizingClaim;
    private Location lastClaimToolLocation;

    public SurvivalPlayer(Survival survival, Player player) {
        super(player);

        this.survival = survival;

        this.earthMoney = 0;
        this.claimBlocks = 250 /* TODO */* 10;

        this.settings = new HashSet<>();

        this.claimToolType = Claim.ToolType.NORMAL;
    }

    @Override
    protected void onLogin() {
        players.add(this);

        if (!Database.get().contains(Table.SURVIVAL_PLAYERS, TableSurvivalPlayers.UUID, new Where(TableSurvivalPlayers.UUID, getUUID().toString()))) {
            Database.get().insert(Table.SURVIVAL_PLAYERS, Table.SURVIVAL_PLAYERS.values(getUUID().toString(), earthMoney + "", claimBlocks + ""));
        } else {
            Map<Column, String> values = Database.get().getValues(Table.SURVIVAL_PLAYERS, new Column[]{
                    TableSurvivalPlayers.EARTH_MONEY,
                    TableSurvivalPlayers.CLAIM_BLOCKS,
            }, new Where(TableSurvivalPlayers.UUID, getUUID().toString()));

            earthMoney = Integer.parseInt(values.get(TableSurvivalPlayers.EARTH_MONEY));
            claimBlocks = Integer.parseInt(values.get(TableSurvivalPlayers.CLAIM_BLOCKS));
        }

        setScoreboard(new Survival.Scoreboard(orbitMines, this));

        //TODO remove
        player.getInventory().setItem(0, Claim.CLAIMING_TOOL.build());
    }

    @Override
    protected void onLogout() {

        players.remove(this);
    }

    @Override
    public void onVote(int votes) {

    }

    @Override
    public boolean canReceiveVelocity() {
        return true;
    }
    
    /*


        Getters & Setters


     */
    
    /*
        EarthMoney
     */

    public int getEarthMoney() {
        return earthMoney;
    }

    public void addEarthMoney(int amount) {
        earthMoney += amount;

        updateEarthMoney();
    }

    public void removeEarthMoney(int amount) {
        earthMoney -= amount;

        updateEarthMoney();
    }

    private void updateEarthMoney() {
        Database.get().update(Table.SURVIVAL_PLAYERS, new Set(TableSurvivalPlayers.EARTH_MONEY, this.earthMoney), new Where(TableSurvivalPlayers.UUID, getUUID().toString()));
    }
    
    /*
        ClaimBlocks
     */

    public int getClaimBlocks() {
        return claimBlocks;
    }

    public void addClaimBlocks(int amount) {
        claimBlocks += amount;

        updateClaimBlocks();
    }

    public void removeClaimBlocks(int amount) {
        claimBlocks -= amount;

        updateClaimBlocks();
    }

    public int getRemainingClaimBlocks() {
        return survival.getClaimHandler().getRemaining(getUUID(), claimBlocks);
    }

    private void updateClaimBlocks() {
        Database.get().update(Table.SURVIVAL_PLAYERS, new Set(TableSurvivalPlayers.CLAIM_BLOCKS, this.claimBlocks), new Where(TableSurvivalPlayers.UUID, getUUID().toString()));
    }

    /*
        Settings
     */

    public java.util.Set<Survival.Settings> getSettings() {
        return settings;
    }

    public boolean hasEnabled(Survival.Settings settings) {
        return this.settings.contains(settings);
    }

    public void toggle(Survival.Settings settings, boolean enable) {
        if (enable)
            this.settings.add(settings);
        else
            this.settings.remove(settings);

        //TODO UPDATE
    }

    /*
        Claim.ToolType
     */

    public Claim.ToolType getClaimToolType() {
        return claimToolType;
    }

    public void setClaimToolType(Claim.ToolType claimToolType) {
        this.claimToolType = claimToolType;
    }

    /*
        Visualisation
     */

    public Visualization getVisualization() {
        return visualization;
    }

    public void setVisualization(Visualization visualization) {
        this.visualization = visualization;
    }

    /*
        LastClaim
     */

    public Claim getLastClaim() {
        return lastClaim;
    }

    public void setLastClaim(Claim lastClaim) {
        this.lastClaim = lastClaim;
    }

    /*
        Resizing Claim
     */

    public Claim getResizingClaim() {
        return resizingClaim;
    }

    public void setResizingClaim(Claim resizingClaim) {
        this.resizingClaim = resizingClaim;
    }

    /*
        ClaimTool
     */

    public Location getLastClaimToolLocation() {
        return lastClaimToolLocation;
    }

    public void setLastClaimToolLocation(Location lastClaimToolLocation) {
        this.lastClaimToolLocation = lastClaimToolLocation;
    }

    /*


        SurvivalPlayer Getters


     */

    public static SurvivalPlayer getPlayer(Player player) {
        for (SurvivalPlayer omp : players) {
            if (omp.getPlayer() == player)
                return omp;
        }
        throw new IllegalStateException();
    }

    public static SurvivalPlayer getPlayer(String name) {
        for (SurvivalPlayer omp : players) {
            if (omp.getName(true).equalsIgnoreCase(name))
                return omp;
        }
        throw new IllegalStateException();
    }

    public static SurvivalPlayer getPlayer(UUID uuid) {
        for (SurvivalPlayer omp : players) {
            if (omp.getUUID().toString().equals(uuid.toString()))
                return omp;
        }
        throw new IllegalStateException();
    }

    public static List<SurvivalPlayer> getSurvivalPlayers() {
        return players;
    }
}
