package com.orbitmines.spigot.servers.survival.handlers;

import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Set;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.TablePlayers;
import com.orbitmines.api.database.tables.survival.TableSurvivalPlayers;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.claim.Claim;
import com.orbitmines.spigot.servers.survival.handlers.claim.Visualization;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class SurvivalPlayer extends OMPlayer {
    
    protected static List<SurvivalPlayer> players = new ArrayList<>();

    private final Survival survival;

    private int earthMoney;
    private int claimBlocks;

    private Visualization visualization;
    private Claim lastClaim;

    public SurvivalPlayer(Survival survival, Player player) {
        super(player);

        this.survival = survival;

        this.earthMoney = 0;
    }

    @Override
    protected void onLogin() {
        players.add(this);

        setScoreboard(new Survival.Scoreboard(orbitMines, this));

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
        Database.get().update(Table.SURVIVAL_PLAYERS, new Set(TableSurvivalPlayers.EARTH_MONEY, this.earthMoney), new Where(TablePlayers.UUID, getUUID().toString()));
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

    private void updateClaimBlocks() {
        Database.get().update(Table.SURVIVAL_PLAYERS, new Set(TableSurvivalPlayers.CLAIM_BLOCKS, this.claimBlocks), new Where(TablePlayers.UUID, getUUID().toString()));
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
