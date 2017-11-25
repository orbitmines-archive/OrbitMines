package com.orbitmines.spigot.servers.survival.handlers.claim;

import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Set;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.TableServerData;
import com.orbitmines.api.database.tables.survival.TableSurvivalClaim;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.spigot.api.utils.Serializer;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class ClaimHandler {

    private final Survival survival;

    private ArrayList<Claim> claims;
    private ConcurrentHashMap<Long, ArrayList<Claim>> chunkClaims;

    public ClaimHandler(Survival survival) {
        this.survival = survival;

        claims = new ArrayList<>();
        chunkClaims = new ConcurrentHashMap<>();
    }

    public ArrayList<Claim> getClaims() {
        return claims;
    }

    public boolean canBuild(SurvivalPlayer omp, Location location) {
        return canBuild(omp, location, location.getBlock().getType());
    }

    public boolean canBuild(SurvivalPlayer omp, Location location, Material material) {
        if (!survival.getWorld().equals(location.getWorld()))
            return false;

        Claim claim = getClaimAt(location, false, omp.getLastClaim());

        if (claim == null)
            return true;

        omp.setLastClaim(claim);

        return claim.canBuild(omp, material);
    }

    public void addClaim(Claim claim, boolean save) {
        if (claim.hasParent()) {
            if (!claim.getParent().getChildren().contains(claim))
                claim.getParent().getChildren().add(claim);

            claim.setRegistered(true);

            if (save)
                saveClaim(claim);

            return;
        }

        this.claims.add(claim);
        ArrayList<Long> chunkHashes = claim.getChunkHashes();
        for (Long chunkHash : chunkHashes) {
            ArrayList<Claim> claimsInChunk = chunkClaims.computeIfAbsent(chunkHash, key -> new ArrayList<>());
            claimsInChunk.add(claim);
        }

        claim.setRegistered(true);

        if (save)
            saveClaim(claim);
    }

    public Claim getClaimAt(Location location, boolean ignoreY, Claim cached) {
        if (cached != null && cached.isRegistered() && cached.inClaim(location, ignoreY, true))
            return cached;

        Long chunkHash = getChunkHash(location);
        ArrayList<Claim> claimsInChunk = chunkClaims.get(chunkHash);

        if (claimsInChunk == null)
            return null;

        for (Claim claim : claimsInChunk) {
            if (claim.isRegistered() && claim.inClaim(location, ignoreY, false)) {

                for (Claim child : claim.getChildren()) {
                    if (child.isRegistered() && child.inClaim(location, ignoreY, false))
                        return child;
                }

                return claim;
            }
        }

        return null;
    }

    public Long getChunkHash(long x, long z) {
        return (z ^ (x << 32));
    }

    public Long getChunkHash(Location location) {
        return getChunkHash(location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    public void saveClaim(Claim claim) {
        if (claim.getId() == null || claim.getId() == -1) {
            claim.setId(Claim.NEXT_ID);

            Claim.NEXT_ID++;
            Database.get().update(Table.SERVER_DATA, new Set(TableServerData.DATA, Claim.NEXT_ID), new Where(TableServerData.SERVER, survival.getServer().toString()), new Where(TableServerData.TYPE, "NEXT_ID"));
        }

        write(claim);
    }

    public void write(Claim claim) {
        String owner = claim.getOwner() == null ? "" : claim.getOwner().toString();
        Long parentId = !claim.hasParent() ? -1L : claim.getParent().getId();

        if (!Database.get().contains(Table.SURVIVAL_CLAIM, TableSurvivalClaim.ID, new Where(TableSurvivalClaim.ID, claim.getId()))) {
            Database.get().insert(Table.SURVIVAL_CLAIM, Table.SURVIVAL_CLAIM.values(
                    claim.getId() + "",
                    DateUtils.FORMAT.format(claim.getCreatedOn()),
                    Serializer.serialize(claim.getCorner1()),
                    Serializer.serialize(claim.getCorner2()),
                    owner,
                    serializeMembers(claim),
                    serializeSettings(claim),
                    parentId + ""
            ));
        } else {
            Database.get().update(Table.SURVIVAL_CLAIM, new Set[]{
                    new Set(TableSurvivalClaim.CORNER_1, Serializer.serialize(claim.getCorner1())),
                    new Set(TableSurvivalClaim.CORNER_2, Serializer.serialize(claim.getCorner2())),
                    new Set(TableSurvivalClaim.OWNER, owner),
                    new Set(TableSurvivalClaim.MEMBERS, serializeMembers(claim)),
                    new Set(TableSurvivalClaim.SETTINGS, serializeSettings(claim)),
                    new Set(TableSurvivalClaim.PARENT, parentId),
            }, new Where(TableSurvivalClaim.ID, claim.getId()));
        }
    }

    public void delete(Claim claim) {
        Database.get().delete(Table.SURVIVAL_CLAIM, new Where(TableSurvivalClaim.ID, claim.getId()));
    }

    private String serializeMembers(Claim claim) {
        if (claim.getMembers().size() == 0)
            return "";

        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        for (UUID member : claim.getMembers().keySet()) {
            if (i != 0)
                stringBuilder.append("|");

            stringBuilder.append(member.toString()).append(";").append(claim.getMembers().get(member).toString());

            i++;
        }

        return stringBuilder.toString();
    }

    private String serializeSettings(Claim claim) {
        if (claim.getSettings().size() == 0)
            return "";

        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        for (Claim.Settings setting : claim.getSettings()) {
            if (i != 0)
                stringBuilder.append("|");

            stringBuilder.append(setting.toString());

            i++;
        }

        return stringBuilder.toString();
    }
}
