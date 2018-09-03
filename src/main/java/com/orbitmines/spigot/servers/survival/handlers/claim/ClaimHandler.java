package com.orbitmines.spigot.servers.survival.handlers.claim;

import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Set;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.TableServerData;
import com.orbitmines.api.database.tables.survival.TableSurvivalClaim;
import com.orbitmines.api.database.tables.survival.TableSurvivalPlayers;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.spigot.api.handlers.chat.ActionBar;
import com.orbitmines.spigot.api.utils.Serializer;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import org.bukkit.*;

import java.util.*;
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

    public boolean canBuild(SurvivalPlayer omp, Location location) {
        return canBuild(omp, location, location.getBlock().getType());
    }

    public boolean canBuild(SurvivalPlayer omp, Location location, Material material) {
        if (!survival.canClaimIn(location.getWorld()))
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

    public Claim getClaim(long id) {
        for (Claim claim : claims) {
            if (claim.isRegistered() && claim.getId() == id)
                return claim;
        }
        return null;
    }

    public List<Claim> getClaims(UUID owner) {
        List<Claim> claims = new ArrayList<>();
        for (Claim claim : this.claims) {
            if (claim.isRegistered() && (claim.getOwner() != null && claim.getOwner().equals(owner)))
                claims.add(claim);
        }
        return claims;
    }

    public Collection<Claim> getClaims() {
        return Collections.unmodifiableCollection(this.claims);
    }

    public Collection<Claim> getClaims(long x, long z) {
        ArrayList<Claim> chunkClaims = this.chunkClaims.get(getChunkHash(x, z));

        if (chunkClaims != null)
            return Collections.unmodifiableCollection(chunkClaims);

        return Collections.unmodifiableCollection(new ArrayList<>());
    }
    
    public java.util.Set<Claim> getNearbyClaims(Location location) {
        java.util.Set<Claim> claims = new HashSet<>();

        Chunk chunk1 = location.getWorld().getChunkAt(location.subtract(150, 0, 150));
        Chunk chunk2 = location.getWorld().getChunkAt(location.add(300, 0, 300));

        for (int chunk_x = chunk1.getX(); chunk_x <= chunk2.getX(); chunk_x++) {
            for (int chunk_z = chunk1.getZ(); chunk_z <= chunk2.getZ(); chunk_z++) {
                
                Chunk chunk = location.getWorld().getChunkAt(chunk_x, chunk_z);
                Long chunkID = getChunkHash(chunk.getBlock(0, 0, 0).getLocation());
                ArrayList<Claim> claimsInChunk = chunkClaims.get(chunkID);
                
                if (claimsInChunk != null) {
                    for (Claim claim : claimsInChunk) {
                        if (claim.isRegistered() && claim.getCorner1().getWorld().equals(location.getWorld()))
                            claims.add(claim);

                    }
                }
            }
        }

        return claims;
    }

    public Long getChunkHash(long x, long z) {
        return (z ^ (x << 32));
    }

    public Long getChunkHash(Location location) {
        return getChunkHash(location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    public int getRemaining(UUID owner, int claimBlocks) {
        int remaining = claimBlocks;

        for (Claim claim : survival.getClaimHandler().getClaims()) {
            if (claim.isRegistered() && owner.equals(claim.getOwner()))
                remaining -= claim.getArea();
        }

        return remaining;
    }

    public Claim.CreateResult createClaim(World world, SurvivalPlayer omp, UUID owner, Claim parent, Long id, int x1, int x2, int y1, int y2, int z1, int z2) {
        /* Parent 2D, Children 3D */
        if (parent == null) {
            y1 = 0;
            y2 = world.getMaxHeight();
        }

        int smallX, bigX, smallY, bigY, smallZ, bigZ;

        /* Small/Big */
        if (x1 < x2) {
            smallX = x1;
            bigX = x2;
        } else {
            smallX = x2;
            bigX = x1;
        }

        if (y1 < y2) {
            smallY = y1;
            bigY = y2;
        } else {
            smallY = y2;
            bigY = y1;
        }

        if (z1 < z2) {
            smallZ = z1;
            bigZ = z2;
        } else {
            smallZ = z2;
            bigZ = z1;
        }

        Claim claim = new Claim(survival, id, null, DateUtils.now(), new Location(world, smallX, smallY, smallZ), new Location(world, bigX, bigY, bigZ), owner, new HashMap<>(), new HashMap<>());
        claim.setParent(parent);

        Claim.CreateResult result = new Claim.CreateResult();

        List<Claim> toCheck;
        if (claim.hasParent()) {
            toCheck = claim.getParent().getChildren();
        } else {
            toCheck = this.claims;
        }

        for (Claim check : toCheck) {
            if (check.getId() != claim.getId() && check.isRegistered() && check.overlaps(claim)) {
                result.setSucceeded(false);
                result.setClaim(claim);
                return result;
            }
        }

        addClaim(claim, true);

        result.setSucceeded(true);
        result.setClaim(claim);
        return result;
    }

    public Claim.CreateResult resizeClaim(Claim claim, SurvivalPlayer omp, int newX1, int newX2, int newY1, int newY2, int newZ1, int newZ2) {
        Claim.CreateResult result = createClaim(claim.getCorner1().getWorld(), omp, claim.getOwner(), claim.getParent(), claim.getId(), newX1, newX2, newY1, newY2, newZ1, newZ2);

        if (!result.isSucceeded())
            return result;

        result.getClaim().setMembers(new HashMap<>(claim.getMembers()));
        result.getClaim().setSettings(new HashMap<>(claim.getSettings()));

        for (Claim child : claim.getChildren()) {
            child.setParent(result.getClaim());
            result.getClaim().getChildren().add(child);
        }

        saveClaim(result.getClaim());

        claim.setRegistered(false);
        
        return result;
    }

    public void resizeClaimWithChecks(SurvivalPlayer omp, int newX1, int newX2, int newY1, int newY2, int newZ1, int newZ2) {
        Claim claim = omp.getResizingClaim();

        int newWidth = (Math.abs(newX1 - newX2) + 1);
        int newHeight = (Math.abs(newZ1 - newZ2) + 1);
        int newArea = newWidth * newHeight;

        if (newWidth < Claim.MIN_WIDTH || newHeight < Claim.MIN_WIDTH) {
            new ActionBar(omp, () -> omp.lang("§c§lEen claim moet minimaal 3x3 zijn.", "§c§lA claim must at least be 3x3."), 60).send();
            return;
        }

        if (newArea < Claim.MIN_AREA) {
            new ActionBar(omp, () -> omp.lang("§c§lEen claim moet minimaal 3x3 zijn.", "§c§lA claim must at least be 3x3."), 60).send();
            return;
        }

        if (claim.hasOwner() && omp.getUUID().equals(claim.getOwner())) {
            int remaining = omp.getRemainingClaimBlocks() + claim.getArea() - newArea;

            if (remaining < 0) {
                new ActionBar(omp, () -> omp.lang("§c§lJe hebt nog §6§l" + Math.abs(remaining) + " Claimblocks§c§l nodig om dit te claimen.", "§c§lYou need §6§l" + Math.abs(remaining) + " Claimblocks§c§l in order to claim this."), 60).send();
                return;
            }
        }

        Claim.CreateResult result = resizeClaim(claim, omp, newX1, newX2, newY1, newY2, newZ1, newZ2);
        claim = result.getClaim();

        if (result.isSucceeded()) {
            int remaining = 0;
            if (claim.hasOwner()) {
                UUID owner = claim.getOwner();

                if (omp.getUUID().equals(owner)) {
                    remaining = omp.getRemainingClaimBlocks();
                } else {
                    //TODO MAYBE SHARE CLAIMBLOCKS?
                    SurvivalPlayer ownerPlayer = SurvivalPlayer.getPlayer(owner);

                    if (ownerPlayer != null)
                        remaining = ownerPlayer.getRemainingClaimBlocks();
                    else
                        remaining = getRemaining(owner, Database.get().getInt(Table.SURVIVAL_PLAYERS, TableSurvivalPlayers.CLAIM_BLOCKS, new Where(TableSurvivalPlayers.UUID, owner.toString())));
                }
            }

            int fRemaining = remaining;
            new ActionBar(omp, () -> omp.lang("§a§lClaim grootte gewijzigd. Claimblocks over: §6§l" + fRemaining + "§a§l.", "§a§lClaim resized. Available Claimblocks: §6§l" + fRemaining + "§a§l."), 100).send();

            Visualization.show(omp, result.getClaim(), omp.getPlayer().getEyeLocation().getBlockY(), Visualization.Type.CLAIM, omp.getLocation());

            omp.setResizingClaim(null);
            omp.setLastClaimToolLocation(null);
            omp.playSound(Sound.ENTITY_ARROW_HIT_PLAYER);
            return;
        }

        new ActionBar(omp, () -> omp.lang("§c§lJouw claim overlapt een andere claim!", "§c§lYour claim overlaps another claim!"), 60).send();

        if (result.getClaim() == null)
            return;

        Visualization.show(omp, result.getClaim(), omp.getPlayer().getEyeLocation().getBlockY(), Visualization.Type.INVALID, omp.getLocation());
    }

    public void saveClaim(Claim claim) {
        if (claim.getId() == null || claim.getId() == -1) {
            claim.setId(Claim.NEXT_ID);

            Claim.NEXT_ID++;
            Database.get().update(Table.SERVER_DATA, new Set(TableServerData.DATA, Claim.NEXT_ID), new Where(TableServerData.SERVER, survival.getServer().toString()), new Where(TableServerData.TYPE, "NEXT_ID"));
        }

        write(claim);
    }

    public void deleteClaim(Claim claim) {
        for (Claim child : new ArrayList<>(claim.getChildren())) {
            deleteClaim(child);
        }

        if (claim.hasParent()) {
            Claim parent = claim.getParent();
            parent.getChildren().remove(claim);

            saveClaim(parent);
        }

        claim.setRegistered(false);

        for (Claim cl : new ArrayList<>(this.claims)) {
            if (cl.getId().equals(claim.getId()))
                this.claims.remove(cl);
        }

        for (Long chunkHash : claim.getChunkHashes()) {
            ArrayList<Claim> claimsInChunk = chunkClaims.get(chunkHash);

            if (claimsInChunk != null) {
                for (Claim cl : new ArrayList<>(claimsInChunk)) {
                    if (cl.getId().equals(claim.getId()))
                        claimsInChunk.remove(cl);
                }
            }
        }

        delete(claim);
    }

    public void deleteClaims(UUID owner) {
        for (Claim claim : new ArrayList<>(this.claims)) {
            if (owner.equals(claim.getOwner()))
                deleteClaim(claim);
        }
    }

    public void deleteClaims(World world) {
        for (Claim claim : new ArrayList<>(this.claims)) {
            if (claim.getCorner1().getWorld().equals(world))
                deleteClaim(claim);
        }
    }

    public void switchOwner(Claim claim, UUID newOwner) {
        if (claim.hasParent())
            throw new IllegalStateException();

        claim.setOwner(newOwner);
        saveClaim(claim);
    }

    public void abandonClaim(Claim claim, SurvivalPlayer omp, boolean deleteParent) {
        if (claim == null) {
            new ActionBar(omp, () -> omp.lang("§c§lJe bent niet in een claim!", "§c§lYou are not standing in a claim!"), 60).send();
        } else if (!claim.canModify(omp)) {
            new ActionBar(omp, () -> omp.lang("§c§lDit is niet jouw claim!", "§c§lThis is not your claim!"), 60).send();
        } else if (claim.getChildren().size() > 0 && !deleteParent) {
            new ActionBar(omp, () -> omp.lang("§c§lJe bent niet in een hoofd claim!", "§c§lYou are not in a top level claim!"), 60).send();
        } else {
            deleteClaim(claim);

            new ActionBar(omp, () -> omp.lang("§a§lDeze claim is verwijderd. Jouw claimblocks: §6§l" + omp.getRemainingClaimBlocks() + "§a§l.", "§a§lThis claim has been abandoned. Your claimblocks: §6§l" + omp.getRemainingClaimBlocks() + "§a§l."), 60).send();

            Visualization.revert(omp);
        }
    }

    public void write(Claim claim) {
        String owner = claim.getOwner() == null ? "" : claim.getOwner().toString();
        Long parentId = !claim.hasParent() ? -1L : claim.getParent().getId();

        if (!Database.get().contains(Table.SURVIVAL_CLAIM, TableSurvivalClaim.ID, new Where(TableSurvivalClaim.ID, claim.getId()))) {
            Database.get().insert(Table.SURVIVAL_CLAIM,
                    claim.getId() + "",
                    claim.getName(),
                    DateUtils.FORMAT.format(claim.getCreatedOn()),
                    Serializer.serialize(claim.getCorner1()),
                    Serializer.serialize(claim.getCorner2()),
                    owner,
                    serializeMembers(claim),
                    serializeSettings(claim),
                    parentId + ""
            );
        } else {
            Database.get().update(Table.SURVIVAL_CLAIM, new Set[]{
                    new Set(TableSurvivalClaim.NAME, claim.getName()),
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
        for (Claim.Settings settings : claim.getSettings().keySet()) {
            if (i != 0)
                stringBuilder.append("|");

            stringBuilder.append(settings.toString()).append(";").append(claim.getSettings().get(settings).toString());

            i++;
        }

        return stringBuilder.toString();
    }
}
