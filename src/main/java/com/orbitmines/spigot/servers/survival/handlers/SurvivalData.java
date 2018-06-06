package com.orbitmines.spigot.servers.survival.handlers;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.VipRank;
import com.orbitmines.api.database.*;
import com.orbitmines.api.database.tables.survival.TableSurvivalPlayers;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.OMPlayer;

import java.util.Map;
import java.util.UUID;

import static com.orbitmines.api.database.tables.survival.TableSurvivalPlayers.*;

public class SurvivalData extends Data {

    private int earthMoney;
    private int claimBlocks;

    private int extraHomes;
    private int extraWarps;

    public SurvivalData(UUID uuid) {
        super(Table.SURVIVAL_PLAYERS, Type.SURVIVAL, uuid);

        earthMoney = 0;
        claimBlocks = 250;
        extraHomes = 0;
        extraWarps = 0;
    }

    @Override
    public void load() {
        if (!Database.get().contains(table, new Column[] { TableSurvivalPlayers.UUID }, new Where(TableSurvivalPlayers.UUID, getUUID().toString()))) {
            Database.get().insert(table, table.values(uuid.toString(), earthMoney + "", claimBlocks + "", extraHomes + "", extraWarps + ""));
        } else {
            Map<Column, String> values = Database.get().getValues(table, new Column[] {
                    EARTH_MONEY,
                    CLAIM_BLOCKS,
                    EXTRA_HOMES,
                    EXTRA_WARPS
            }, new Where(TableSurvivalPlayers.UUID, getUUID().toString()));

            earthMoney = Integer.parseInt(values.get(EARTH_MONEY));
            claimBlocks = Integer.parseInt(values.get(CLAIM_BLOCKS));

            extraHomes = Integer.parseInt(values.get(EXTRA_HOMES));
            extraWarps = Integer.parseInt(values.get(EXTRA_WARPS));
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

    public int getExtraWarps() {
        return extraWarps;
    }

    public void setExtraWarps(int extraWarps) {
        this.extraWarps = extraWarps;

        Database.get().update(Table.SURVIVAL_PLAYERS, new Set(TableSurvivalPlayers.EXTRA_WARPS, this.extraWarps), new Where(TableSurvivalPlayers.UUID, getUUID().toString()));
    }

    public int getWarpsAllowed(OMPlayer omp) {
        if (omp.isEligible(VipRank.EMERALD))
            return 1 + this.extraWarps;
        else
            return this.extraWarps;
    }
}
