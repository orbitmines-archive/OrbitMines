package com.orbitmines.spigot.servers.survival.handlers;

import com.orbitmines.api.Color;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.chat.ActionBar;
import com.orbitmines.spigot.api.handlers.chat.ComponentMessage;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.claim.Claim;
import com.orbitmines.spigot.servers.survival.handlers.claim.Visualization;
import com.orbitmines.spigot.servers.survival.handlers.region.Region;
import com.orbitmines.spigot.servers.survival.handlers.teleportable.Home;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class SurvivalPlayer extends OMPlayer {
    
    protected static Map<Player, SurvivalPlayer> players = new HashMap<>();

    private final Survival survival;

    private List<Home> homes;

    private Set<String> tpRequests;
    private Set<String> tpHereRequests;

    private java.util.Set<Survival.Settings> settings;

    private Region lastRegion;

    private Claim.ToolType claimToolType;
    private Visualization visualization;
    private Claim lastClaim;
    private Claim resizingClaim;
    private Location lastClaimToolLocation;

    public SurvivalPlayer(Survival survival, Player player) {
        super(player);

        this.survival = survival;

        this.settings = new HashSet<>();

        this.claimToolType = Claim.ToolType.NORMAL;
    }

    @Override
    protected void onLogin() {
        players.put(player, this);

        Location logOutLocation = getData().getLogoutLocation();
        if (logOutLocation != null && (logOutLocation.getWorld().getName().equals(survival.getWorld_nether().getName()) || logOutLocation.getWorld().getName().equals(survival.getWorld_the_end().getName())))
            player.teleport(logOutLocation);

        if (getData().hasLogoutFly()) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }

        homes = Home.getHomesFor(getUUID());

        this.tpRequests = new HashSet<>();
        this.tpHereRequests = new HashSet<>();

        setScoreboard(new Survival.Scoreboard(survival, this));
    }

    @Override
    protected void onLogout() {
        updateLogoutLocation();
        updateLogoutFly();

        players.remove(player);
    }

    @Override
    protected void onFirstLogin() {
        if (player.getInventory().getItem(8) == null)
            player.getInventory().setItem(8, Claim.CLAIMING_TOOL.build());

        if (player.getInventory().getItem(7) == null)
            player.getInventory().setItem(7, new ItemStack(Material.COOKED_BEEF, 3));

        /* Action Bars so New Players know how to join the world */
        new ActionBar(this, () -> lang("§7§lGebruik §a§l/region random§7§l of §a§l/region§7§l om naar de wereld te gaan!", "§7§lUse §a§l/region random§7§l or §a§l/region§7§l to join the world!"), Integer.MAX_VALUE) {
            @Override
            public void onRun() {
                if (!player.getWorld().getName().equals(survival.getLobbySpawn().getWorld().getName()))
                    forceStop();
            }
        }.send();
    }

    @Override
    public boolean canReceiveVelocity() {
        return true;
    }

    @Override
    public Collection<ComponentMessage.TempTextComponent> getChatPrefix() {
        return Collections.emptyList();
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
        return survival.getClaimHandler().getRemaining(getUUID(), getClaimBlocks());
    }

    /*
        BackLocation
     */

    public Location getBackLocation() {
        return getData().getBackLocation();
    }

    public void setBackLocation(Location backLocation) {
        getData().setBackLocation(backLocation);
    }

    /*
        BackCharges
     */

    public int getBackCharges() {
        return getData().getBackCharges();
    }

    public void addBackCharges(int amount) {
        getData().addBackCharges(amount);
    }

    public void removeBackCharges(int amount) {
        getData().removeBackCharges(amount);
    }

    /*
        LastBedEnter
     */

    public void updateLastBedEnter() {
        getData().updateLastBedEnter();

        new ActionBar(this, () -> lang("§7§lPhantom spawning is uitgezet voor §a§ltwee uur§7§l.", "§7§lPhantom spawning has been disabled for §a§ltwo hours§7§l."), 100).send();
    }

    public boolean canSpawnPhantom() {
        return getData().canSpawnPhantom();
    }

    /*
        LogoutLocation
     */
    public void updateLogoutLocation() {
        getData().setLogoutLocation(player.getLocation());
    }

    /*
        LogoutFly
     */
    public void updateLogoutFly() {
        getData().setLogoutFly(player.isFlying());
    }

    /*
        Extra Homes
     */

    public int getExtraHomes() {
        return getData().getExtraHomes();
    }

    public void addExtraHomes(int extraHomes) {
        getData().setExtraHomes(getExtraHomes() + extraHomes);
    }

    public int getHomesAllowed() {
        return getData().getHomesAllowed(this);
    }

    /*
        Extra Warps
     */

    public boolean warpSlotShop() {
        return getData().warpSlotShop();
    }

    public boolean warpSlotPrisms() {
        return getData().warpSlotPrisms();
    }

    public void setWarpSlotShop(boolean warpSlotShop) {
        getData().setWarpSlotShop(warpSlotShop);
    }

    public void setWarpSlotPrisms(boolean warpSlotPrisms) {
        getData().setWarpSlotPrisms(warpSlotPrisms);
    }

    public int getWarpsAllowed() {
        return getData().getWarpsAllowed(this);
    }

    /*
        Favorite Warps
     */

    public List<Long> getFavoriteWarps() {
        return getData().getFavoriteWarps();
    }

    public void addFavoriteWarp(long id) {
        getData().addFavoriteWarp(id);
    }

    public void removeFavoriteWarp(long id) {
        getData().removeFavoriteWarp(id);
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
        Tp Requests
     */

    public Set<String> getTpRequests() {
        return tpRequests;
    }

    public boolean hasTpRequestFrom(String name) {
        return tpRequests.contains(name);
    }

    public Set<String> getTpHereRequests() {
        return tpHereRequests;
    }

    public boolean hasTpHereRequestFrom(String name) {
        return tpHereRequests.contains(name);
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
        Region
     */

    public Region getLastRegion() {
        return lastRegion;
    }

    public void setLastRegion(Region lastRegion) {
        this.lastRegion = lastRegion;
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
        return player == null ? null : players.getOrDefault(player, null);
    }

    public static SurvivalPlayer getPlayer(String name) {
        return getPlayer(Bukkit.getPlayer(name));
    }

    public static SurvivalPlayer getPlayer(UUID uuid) {
        return getPlayer(Bukkit.getPlayer(uuid));
    }

    public static Collection<SurvivalPlayer> getSurvivalPlayers() {
        return players.values();
    }
}
