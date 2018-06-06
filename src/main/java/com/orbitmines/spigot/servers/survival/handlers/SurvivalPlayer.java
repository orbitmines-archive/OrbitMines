package com.orbitmines.spigot.servers.survival.handlers;

import com.orbitmines.api.Color;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.survival.TableSurvivalPlayers;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.claim.Claim;
import com.orbitmines.spigot.servers.survival.handlers.claim.Visualization;
import com.orbitmines.spigot.servers.survival.handlers.teleportable.Home;
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

    private List<Home> homes;

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

        homes = Home.getHomesFor(getUUID());

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

    public int getEarthMoney() {
        return getData().getEarthMoney();
    }

    public void addEarthMoney(int amount) {
        getData().addEarthMoney(amount);
    }

    public void removeEarthMoney(int amount) {
        getData().removeEarthMoney(amount);
    }

    /*
        ClaimBlocks
     */

    public int getClaimBlocks() {
        return getData().getClaimBlocks();
    }

    public void addClaimBlocks(int amount) {
        getData().addClaimBlocks(amount);
    }

    public void removeClaimBlocks(int amount) {
        getData().removeClaimBlocks(amount);
    }

    public int getRemainingClaimBlocks() {
        return survival.getClaimHandler().getRemaining(getUUID(), claimBlocks);
    }

    /*
        Extra Homes
     */

    public int getExtraHomes() {
        return getData().getExtraHomes();
    }

    public void setExtraHomes(int extraHomes) {
        getData().setExtraHomes(extraHomes);
    }

    public int getHomesAllowed() {
        return getData().getHomesAllowed(this);
    }

    /*
        Extra Warps
     */

    public int getExtraWarps() {
        return getData().getExtraWarps();
    }

    public void setExtraWarps(int extraWarps) {
        getData().setExtraWarps(extraWarps);
    }

    public int getWarpsAllowed() {
        return getData().getWarpsAllowed(this);
    }

    /*
        Homes
     */

    public List<Home> getHomes() {
        return homes;
    }

    public Home getHome(String name) {
        for (Home home : this.homes) {
            if (home.getName().equals(name))
                return home;
        }
        return null;
    }

    public void delHome(Home home) {
        home.delete();
    }

    public void setHome(String name) {
        Home home = getHome(name);

        String color = Home.COLOR.getChatColor();

        if (home == null) {
            this.homes.add(Home.createHome(getUUID(), name, player.getLocation()));
            sendMessage("Home", Color.LIME, "§7Nieuwe home " + color + "neergezet§7! (" + color + name + "§7)", "§7New home " + color + "set§7! (" + color + name + "§7)");
            return;
        }

        home.setLocation(player.getLocation());
        sendMessage("Home", Color.LIME, "§7Home " + color + "neergezet§7! (" + color + name + "§7)", "§7Home " + color + "set§7! (" + color + name + "§7)");
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

    public SurvivalData getData() {
        return (SurvivalData) getData(Data.Type.SURVIVAL);
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
