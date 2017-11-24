package com.orbitmines.spigot.servers.survival.handlers.claim;

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.Color;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.spigot.api.utils.ItemUtils;
import com.orbitmines.spigot.servers.survival.griefprevention.DataStore;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class Claim {

    private Long id;
    private final Date createdOn;

    private Location[] corners;

    private UUID owner;
    private Map<UUID, Permission> members;

    private Map<Settings, Boolean> settings;

    private Claim parent;
    private List<Claim> children;

    public Claim() {
        this.id = null;
        this.createdOn = DateUtils.now();
    }

    public Claim(Long id, Date createdOn, Location corner1, Location corner2, UUID owner, Map<UUID, Permission> members, Map<Settings, Boolean> settings) {
        this.id = id;
        this.createdOn = createdOn;
        this.corners = new Location[] { corner1, corner2 };
        this.owner = owner;
        this.members = members;
        this.settings = settings;
    }

    /*
        Id
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isRegistered() {
        return id != null;
    }

    /*
        CreatedOn
     */

    public Date getCreatedOn() {
        return createdOn;
    }

    /*
        Corners
     */

    public Location[] getCorners() {
        return corners;
    }

    public Location getCorner1() {
        return corners[0];
    }

    public Location getCorner2() {
        return corners[1];
    }

    public int getWidth() {
        return getCorner2().getBlockX() - getCorner1().getBlockX() + 1;
    }

    public int getDepth() {
        return getCorner2().getBlockZ() - getCorner1().getBlockZ() + 1;
    }

    public int getHeight() {
        return getCorner2().getBlockY() - getCorner1().getBlockY() + 1;
    }

    public int getArea() {
        return getWidth() * getDepth();
    }

    public int getVolume() {
        return getArea() * getHeight();
    }

    /*
        Owner
     */

    public UUID getOwner() {
        return hasParent() ? parent.getOwner() : owner;
    }

    public boolean hasOwner() {
        return hasParent() && parent.hasOwner() || owner != null;
    }

    public boolean isOwner(UUID owner) {
        return owner.equals(getOwner());
    }

    public String getOwnerName() {
        if (hasParent())
            return parent.getOwnerName();

        if (!hasOwner())
            return StaffRank.MODERATOR.getPrefixColor().getChatColor() + "§lModerators";

        CachedPlayer player = CachedPlayer.getPlayer(owner);
        return player.getRankPrefixColor().getChatColor() + player.getPlayerName();
    }

    /*
        Members
     */

    public Map<UUID, Permission> getMembers() {
        return members;
    }

    public boolean hasPermission(UUID member, Permission permission) {
        return hasParent() && parent.hasPermission(member, permission) || members.containsKey(member) && members.get(member).hasPerms(permission);
    }

    public void setPermission(UUID member, Permission permission) {
        members.put(member, permission);
    }

    public void clearPermission(UUID member) {
        members.remove(member);
    }

    /*
        Settings
     */

    public Map<Settings, Boolean> getSettings() {
        return settings;
    }

    /*
        Parent
     */

    public boolean hasParent() {
        return parent != null;
    }

    public Claim getParent() {
        return parent;
    }

    /*
        Children
     */

    public boolean hasChildren() {
        return children.size() != 0;
    }

    public List<Claim> getChildren() {
        return children;
    }

    /*
        Edit Permissions
     */

    public boolean canModify(SurvivalPlayer omp) {
        if (omp.isOpMode())
            return true;

        if (isOwner(omp.getUUID()))
            return true;

        if (hasPermission(omp.getUUID(), Permission.CO_OWNER))
            return true;

        omp.sendMessage("Claims", Color.RED, "§7Alleen " + getOwnerName() + "§7 kan deze claim wijzigen.", "§7Only " + getOwnerName() + "§7 can modify this claim.");

        return false;
    }

    public boolean canInteract(SurvivalPlayer omp) {
        if (omp.isOpMode())
            return true;

        if (isOwner(omp.getUUID()))
            return true;

        if (hasPermission(omp.getUUID(), Permission.MANAGE))
            return true;

        omp.sendMessage("Claims", Color.RED, getOwnerName() + " §7heeft je geen toegang gegeven om dat hier te gebruiken.", getOwnerName() + " §7didn't give you permission to use that here.");

        return false;
    }

    public boolean canBuild(SurvivalPlayer omp, Material material) {
        if (omp.isOpMode())
            return true;

        if (isOwner(omp.getUUID()))
            return true;

        if (hasPermission(omp.getUUID(), Permission.BUILD))
            return true;

        if (ItemUtils.isFarmMaterial(material)) {
            if (hasPermission(omp.getUUID(), Permission.MANAGE))
                return true;

            omp.sendMessage("Claims", Color.RED, getOwnerName() + " §7heeft je geen toegang gegeven om hier te farmen.", getOwnerName() + " §7didn't give you permission to farm here.");

            return false;
        }

        omp.sendMessage("Claims", Color.RED, getOwnerName() + " §7heeft je geen toegang gegeven om hier te bouwen.", getOwnerName() + " §7didn't give you permission to build here.");

        return false;
    }

    public boolean canAccess(SurvivalPlayer omp) {
        if (omp.isOpMode())
            return true;

        if (isOwner(omp.getUUID()))
            return true;

        if (hasPermission(omp.getUUID(), Permission.ACCESS))
            return true;

        omp.sendMessage("Claims", Color.RED, getOwnerName() + " §7heeft je geen toegang gegeven om dat hier te gebruiken.", getOwnerName() + " §7didn't give you permission to use that here.");

        return false;
    }

    /*
        Claim Management
     */

    public boolean inClaim(Location location) {
        if (!location.getWorld().equals(getCorner1().getWorld()))
            return false;

        Location corner1 = getCorner1();
        Location corner2 = getCorner2();

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        /*
            Parent : 2D
            Child : 3D
         */

        boolean inClaim =
                (!hasParent() ||    y >= corner1.getY() && y < corner2.getY() + 1)
                                && x >= corner1.getX() && x < corner2.getX() + 1
                                && z >= corner1.getZ() && z < corner2.getZ() + 1;

        if (!inClaim)
            return false;

        if (hasParent())
            return parent.inClaim(location);

        return true;
    }

    public boolean overlaps(Claim claim) {
        if (!getCorner1().getWorld().equals(claim.getCorner1().getWorld()))
            return false;

        //TODO 3D Children

        if (claim.inClaim(getCorner1()))
            return true;
        if (claim.inClaim(getCorner2()))
            return true;
        if (claim.inClaim(new Location(getCorner1().getWorld(), getCorner1().getBlockX(), 0, getCorner2().getBlockZ())))
            return true;
        if (claim.inClaim(new Location(getCorner1().getWorld(), getCorner2().getBlockX(), 0, getCorner1().getBlockZ())))
            return true;

        if (inClaim(claim.getCorner1()))
            return true;

        if (getCorner1().getBlockZ() <= claim.getCorner2().getBlockZ() &&
                getCorner1().getBlockZ() >= claim.getCorner1().getBlockZ() &&
                getCorner1().getBlockX() < claim.getCorner1().getBlockX() &&
                getCorner2().getBlockX() > claim.getCorner2().getBlockX())
            return true;

        if (getCorner2().getBlockZ() <= claim.getCorner2().getBlockZ() &&
                getCorner2().getBlockZ() >= claim.getCorner1().getBlockZ() &&
                getCorner1().getBlockX() < claim.getCorner1().getBlockX() &&
                getCorner2().getBlockX() > claim.getCorner2().getBlockX())
            return true;

        if (getCorner1().getBlockX() <= claim.getCorner2().getBlockX() &&
                getCorner1().getBlockX() >= claim.getCorner1().getBlockX() &&
                getCorner1().getBlockZ() < claim.getCorner1().getBlockZ() &&
                getCorner2().getBlockZ() > claim.getCorner2().getBlockZ())
            return true;

        if (getCorner2().getBlockX() <= claim.getCorner2().getBlockX() &&
                getCorner2().getBlockX() >= claim.getCorner1().getBlockX() &&
                getCorner1().getBlockZ() < claim.getCorner1().getBlockZ() &&
                getCorner2().getBlockZ() > claim.getCorner2().getBlockZ())
            return true;

        return false;
    }

    public List<Chunk> getChunks() {
        List<Chunk> chunks = new ArrayList<>();

        World world = getCorner1().getWorld();
        Chunk lesserChunk = getCorner1().getChunk();
        Chunk greaterChunk = getCorner2().getChunk();

        for (int x = lesserChunk.getX(); x <= greaterChunk.getX(); x++) {
            for (int z = lesserChunk.getZ(); z <= greaterChunk.getZ(); z++) {
                chunks.add(world.getChunkAt(x, z));
            }
        }

        return chunks;
    }

    public List<Long> getChunkHashes() {
        List<Long> hashes = new ArrayList<>();
        int smallX = getCorner1().getBlockX() >> 4;
        int smallZ = getCorner1().getBlockZ() >> 4;
        int largeX = getCorner2().getBlockX() >> 4;
        int largeZ = getCorner2().getBlockZ() >> 4;

        for (int x = smallX; x <= largeX; x++) {
            for (int z = smallZ; z <= largeZ; z++) {
                hashes.add(DataStore.getChunkHash(x, z));
            }
        }

        return hashes;
    }

    public enum Permission {

        ACCESS, /* Use Doors, Buttons etc. */
        MANAGE, /* Access Chests & Plant Farms */
        BUILD, /* Build & Break Blocks */
        CO_OWNER; /* Manage Claims */

        public boolean hasPerms(Permission permission) {
            return this.ordinal() >= permission.ordinal();
        }
    }

    public enum Settings {

        EXPLOSIONS(false);

        private final boolean defaultSetting;

        Settings(boolean defaultSetting) {
            this.defaultSetting = defaultSetting;
        }

        public boolean defaultSetting() {
            return defaultSetting;
        }
    }
}
