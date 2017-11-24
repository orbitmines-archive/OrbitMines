package com.orbitmines.spigot.servers.survival.handlers;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.servers.survival.Survival;
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
        Points
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
        //Database.get().update(Table.PLAYERS, new Set(TablePlayers.SOLARS, this.solars), new Where(TablePlayers.UUID, getUUID().toString()));
    }//TODO
    
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
