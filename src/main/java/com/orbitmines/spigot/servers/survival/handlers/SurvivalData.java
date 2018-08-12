package com.orbitmines.spigot.servers.survival.handlers;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.VipRank;
import com.orbitmines.api.database.*;
import com.orbitmines.api.database.tables.survival.TableSurvivalPlayers;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.utils.Serializer;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.orbitmines.api.database.tables.survival.TableSurvivalPlayers.*;

public class SurvivalData extends Data {

    public static long PHANTOM_DELAY = TimeUnit.HOURS.toMillis(2);

    private int earthMoney;
    private int claimBlocks;

    private Location backLocation;
    private int backCharges;

    private long lastBedEnter;
    private Location logoutLocation;
    private boolean logoutFly;

    private int extraHomes;
    private boolean warpSlotShop;
    private boolean warpSlotPrisms;

    private List<Long> favoriteWarps;

    public SurvivalData(UUID uuid) {
        super(Table.SURVIVAL_PLAYERS, Type.SURVIVAL, uuid);

        earthMoney = 175;
        claimBlocks = 250;

        backLocation = null;
        backCharges = 0;

        lastBedEnter = 0;
        logoutLocation = null;
        logoutFly = false;
        extraHomes = 0;
        warpSlotShop = false;
        warpSlotPrisms = false;
        favoriteWarps = new ArrayList<>();
    }

    @Override
    public void load() {
        if (!Database.get().contains(table, new Column[] { TableSurvivalPlayers.UUID }, new Where(TableSurvivalPlayers.UUID, getUUID().toString()))) {
            Database.get().insert(table, uuid.toString(), earthMoney + "", claimBlocks + "", "", backCharges + "", lastBedEnter + "", "", logoutFly ? "1" : "0", extraHomes + "", warpSlotShop ? "1" : "0", warpSlotPrisms ? "1" : "0", Serializer.serializeLongList(favoriteWarps));
        } else {
            Map<Column, String> values = Database.get().getValues(table, new Column[] {
                    EARTH_MONEY,
                    CLAIM_BLOCKS,
                    BACK_LOCATION,
                    BACK_CHARGES,
                    BED_ENTER,
                    LOGOUT_LOCATION,
                    LOGOUT_FLY,
                    EXTRA_HOMES,
                    WARP_SLOT_SHOP,
                    WARP_SLOT_PRISMS
            }, new Where(TableSurvivalPlayers.UUID, getUUID().toString()));

            earthMoney = Integer.parseInt(values.get(EARTH_MONEY));
            claimBlocks = Integer.parseInt(values.get(CLAIM_BLOCKS));

            backLocation = Serializer.parseLocation(values.get(BACK_LOCATION));
            backCharges = Integer.parseInt(values.get(BACK_CHARGES));

            lastBedEnter = Long.parseLong(values.get(BED_ENTER));

            logoutLocation = Serializer.parseLocation(values.get(LOGOUT_LOCATION));
            logoutFly = values.get(LOGOUT_FLY).equals("1");

            extraHomes = Integer.parseInt(values.get(EXTRA_HOMES));
            warpSlotShop = values.get(WARP_SLOT_SHOP).equals("1");
            warpSlotPrisms = values.get(WARP_SLOT_PRISMS).equals("1");
        }
    }

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

    private void updateClaimBlocks() {
        Database.get().update(Table.SURVIVAL_PLAYERS, new Set(TableSurvivalPlayers.CLAIM_BLOCKS, this.claimBlocks), new Where(TableSurvivalPlayers.UUID, getUUID().toString()));
    }

    /*
        BackLocation
     */

    public Location getBackLocation() {
        return backLocation;
    }

    public void setBackLocation(Location backLocation) {
        this.backLocation = backLocation;
        Database.get().update(Table.SURVIVAL_PLAYERS, new Set(TableSurvivalPlayers.BACK_LOCATION, this.backLocation == null ? "" : Serializer.serialize(this.backLocation)), new Where(TableSurvivalPlayers.UUID, getUUID().toString()));
    }

    /*
        BackCharges
     */

    public int getBackCharges() {
        return backCharges;
    }

    public void addBackCharges(int amount) {
        backCharges += amount;

        updateBackCharges();
    }

    public void removeBackCharges(int amount) {
        backCharges -= amount;

        updateBackCharges();
    }

    private void updateBackCharges() {
        Database.get().update(Table.SURVIVAL_PLAYERS, new Set(TableSurvivalPlayers.BACK_CHARGES, this.backCharges), new Where(TableSurvivalPlayers.UUID, getUUID().toString()));
    }

    /*
        LastBedEnter
     */

    public void updateLastBedEnter() {
        this.lastBedEnter = System.currentTimeMillis();
        Database.get().update(Table.SURVIVAL_PLAYERS, new Set(TableSurvivalPlayers.BED_ENTER, this.lastBedEnter), new Where(TableSurvivalPlayers.UUID, getUUID().toString()));
    }

    public boolean canSpawnPhantom() {
        return (System.currentTimeMillis() - this.lastBedEnter) >= PHANTOM_DELAY;
    }


    /*
        LogoutLocation
     */

    public Location getLogoutLocation() {
        return logoutLocation;
    }

    public void setLogoutLocation(Location logoutLocation) {
        Database.get().update(Table.SURVIVAL_PLAYERS, new Set(TableSurvivalPlayers.LOGOUT_LOCATION, Serializer.serialize(logoutLocation)), new Where(TableSurvivalPlayers.UUID, getUUID().toString()));
    }

    /*
        LogoutFly
     */

    public boolean hasLogoutFly() {
        return logoutFly;
    }

    public void setLogoutFly(boolean logoutFly) {
        Database.get().update(Table.SURVIVAL_PLAYERS, new Set(TableSurvivalPlayers.LOGOUT_FLY, logoutFly), new Where(TableSurvivalPlayers.UUID, getUUID().toString()));
    }

    /*
        Extra Homes
     */

    public int getExtraHomes() {
        return extraHomes;
    }

    public void setExtraHomes(int extraHomes) {
        this.extraHomes = extraHomes;

        Database.get().update(Table.SURVIVAL_PLAYERS, new Set(TableSurvivalPlayers.EXTRA_HOMES, this.extraHomes), new Where(TableSurvivalPlayers.UUID, getUUID().toString()));
    }

    public int getHomesAllowed(OMPlayer omp) {
        if (omp.isEligible(VipRank.EMERALD))
            return 100 + this.extraHomes;
        else if (omp.isEligible(VipRank.DIAMOND))
            return 50 + this.extraHomes;
        else if (omp.isEligible(VipRank.GOLD))
            return 25 + this.extraHomes;
        else if (omp.isEligible(VipRank.IRON))
            return 10 + this.extraHomes;
        else
            return 3 + this.extraHomes;
    }

    /*
        Extra Warps
     */

    public boolean warpSlotShop() {
        return warpSlotShop;
    }

    public boolean warpSlotPrisms() {
        return warpSlotPrisms;
    }

    public void setWarpSlotShop(boolean warpSlotShop) {
        this.warpSlotShop = warpSlotShop;

        Database.get().update(Table.SURVIVAL_PLAYERS, new Set(TableSurvivalPlayers.WARP_SLOT_SHOP, this.warpSlotShop), new Where(TableSurvivalPlayers.UUID, getUUID().toString()));
    }

    public void setWarpSlotPrisms(boolean warpSlotPrisms) {
        this.warpSlotPrisms = warpSlotPrisms;

        Database.get().update(Table.SURVIVAL_PLAYERS, new Set(TableSurvivalPlayers.WARP_SLOT_PRISMS, this.warpSlotPrisms), new Where(TableSurvivalPlayers.UUID, getUUID().toString()));
    }

    public int getWarpsAllowed(OMPlayer omp) {
        return (omp.isEligible(VipRank.EMERALD) ? 1 : 0) + (this.warpSlotShop ? 1 : 0) + (this.warpSlotPrisms ? 1 : 0);
    }

    /*
        Favorite Warps
     */

    public List<Long> getFavoriteWarps() {
        return favoriteWarps;
    }

    public void addFavoriteWarp(long id) {
        this.favoriteWarps.add(id);
        updateFavoriteWarps();
    }

    public void removeFavoriteWarp(long id) {
        this.favoriteWarps.remove(id);
        updateFavoriteWarps();
    }

    private void updateFavoriteWarps() {
        Database.get().update(Table.SURVIVAL_PLAYERS, new Set(TableSurvivalPlayers.FAVORITE_WARPS, Serializer.serializeLongList(this.favoriteWarps)), new Where(TableSurvivalPlayers.UUID, getUUID().toString()));
    }
}
