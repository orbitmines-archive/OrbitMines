package com.orbitmines.spigot.servers.survival.handlers.claim;

import com.orbitmines.spigot.api.handlers.CachedPlayer;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.spigot.api.handlers.chat.ActionBar;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.utils.ItemUtils;
import com.orbitmines.spigot.servers.survival.Survival;
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

    public static Long NEXT_ID = (long) 0;

    public static int MIN_WIDTH = 3;
    public static int MIN_AREA = MIN_WIDTH * MIN_WIDTH;
    public static ItemBuilder CLAIMING_TOOL = new ItemBuilder(Material.STONE_HOE, 1, 0, "§a§lClaiming Tool").unbreakable(true);
//        "",
//        "§6§lRIGHT CLICK",
//        "§7Create claims by selecting",
//        "§7two opposite corners.",
//        "",
//        "§6§lLEFT CLICK",
//        "§7View claim information of",
//        "§7the claim you are currently",
//        "§7in.",
//        "",
//        "§6§lSHIFT + LEFT CLICK",
//        "§7View all nearby claims."

    private final Survival survival;

    private Long id;
    private final Date createdOn;

    private boolean registered;

    private Location[] corners;

    private UUID owner;
    private Map<UUID, Permission> members;

    private Map<Settings, Permission> settings;

    private Claim parent;
    private List<Claim> children;

    public Claim(Survival survival) {
        this.survival = survival;
        this.id = null;
        this.createdOn = DateUtils.now();

        this.members = new HashMap<>();
        this.settings = new HashMap<>();
        this.children = new ArrayList<>();
    }

    public Claim(Survival survival, Long id, Date createdOn, Location corner1, Location corner2, UUID owner, Map<UUID, Permission> members, Map<Settings, Permission> settings) {
        this.survival = survival;
        this.id = id;
        this.createdOn = createdOn;
        this.corners = new Location[] { corner1, corner2 };
        this.owner = owner;
        this.members = members;
        this.settings = settings;
        this.children = new ArrayList<>();
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
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
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

    public int getHeight() {
        return getCorner2().getBlockZ() - getCorner1().getBlockZ() + 1;
    }

    public int getArea() {
        return getWidth() * getHeight();
    }

    /*
        Owner
     */

    public UUID getOwner() {
        return hasParent() ? parent.getOwner() : owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
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

    public void setMembers(Map<UUID, Permission> members) {
        this.members = members;
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

    public Map<Settings, Permission> getSettings() {
        return settings;
    }

    public void setSettings(Map<Settings, Permission> settings) {
        this.settings = settings;
    }

    public Permission getPermission(Settings settings) {
        return this.settings.getOrDefault(settings, settings.defaultPermission);
    }

    public boolean hasPermission(SurvivalPlayer omp, Settings settings) {
        if (omp.isOpMode())
            return true;

        if (isOwner(omp.getUUID()))
            return true;

        Permission permission = getPermission(settings);

        if (permission == null)
            return true;

        return hasPermission(omp.getUUID(), permission);
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

    public void setParent(Claim parent) {
        this.parent = parent;
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

    public void setChildren(List<Claim> children) {
        this.children = children;
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

        String name = getOwnerName();
        new ActionBar(omp, () -> omp.lang("§c§lAlleen " + name + "§c§l kan deze claim wijzigen.", "§c§lOnly " + name + "§c§l can modify this claim."), 60).send();

        return false;
    }

    public boolean canInteract(SurvivalPlayer omp) {
        if (omp.isOpMode())
            return true;

        if (isOwner(omp.getUUID()))
            return true;

        if (hasPermission(omp.getUUID(), Permission.MANAGE))
            return true;

        String name = getOwnerName();
        new ActionBar(omp, () -> omp.lang(name + " §c§lheeft je geen toegang gegeven om dat hier te gebruiken.", name + " §c§ldidn't give you permission to use that here."), 60).send();

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

            String name = getOwnerName();
            new ActionBar(omp, () -> omp.lang(name + " §c§lheeft je geen toegang gegeven om hier te farmen.", name + " §c§ldidn't give you permission to farm here."), 60).send();

            return false;
        }

        String name = getOwnerName();
        new ActionBar(omp, () -> omp.lang(name + " §c§lheeft je geen toegang gegeven om hier te bouwen.", name + " §c§ldidn't give you permission to build here."), 60).send();

        return false;
    }

    public boolean canAccess(SurvivalPlayer omp) {
        if (omp.isOpMode())
            return true;

        if (isOwner(omp.getUUID()))
            return true;

        if (hasPermission(omp.getUUID(), Permission.ACCESS))
            return true;

        String name = getOwnerName();
        new ActionBar(omp, () -> omp.lang(name + " §c§lheeft je geen toegang gegeven om dat hier te gebruiken.", name + " §c§ldidn't give you permission to use that here."), 60).send();

        return false;
    }

    /*
        Claim Management
     */

    public boolean inClaim(Location location, boolean ignoreY, boolean excludeChildren) {
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
                (ignoreY ||        y >= corner1.getY() && y < corner2.getY() + 1)
                                && x >= corner1.getX() && x < corner2.getX() + 1
                                && z >= corner1.getZ() && z < corner2.getZ() + 1;

        if (!inClaim)
            return false;

        if (hasParent()) {
            return parent.inClaim(location, ignoreY, false);
        } else if (excludeChildren) {
            for (Claim child : children) {
                if (child.inClaim(location, ignoreY, true))
                    return false;
            }
        }

        return true;
    }

    public boolean overlaps(Claim claim) {
        if (!getCorner1().getWorld().equals(claim.getCorner1().getWorld()))
            return false;

        //TODO 3D Children

        if (claim.inClaim(getCorner1(), true, false))
            return true;
        if (claim.inClaim(getCorner2(), true, false))
            return true;
        if (claim.inClaim(new Location(getCorner1().getWorld(), getCorner1().getBlockX(), 0, getCorner2().getBlockZ()), true, false))
            return true;
        if (claim.inClaim(new Location(getCorner1().getWorld(), getCorner2().getBlockX(), 0, getCorner1().getBlockZ()), true, false))
            return true;

        if (inClaim(claim.getCorner1(), true, false))
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

    public ArrayList<Long> getChunkHashes() {
        ArrayList<Long> hashes = new ArrayList<>();
        int smallX = getCorner1().getBlockX() >> 4;
        int smallZ = getCorner1().getBlockZ() >> 4;
        int largeX = getCorner2().getBlockX() >> 4;
        int largeZ = getCorner2().getBlockZ() >> 4;

        for (int x = smallX; x <= largeX; x++) {
            for (int z = smallZ; z <= largeZ; z++) {
                hashes.add(survival.getClaimHandler().getChunkHash(x, z));
            }
        }

        return hashes;
    }

    public enum Permission {

        ACCESS, /* Use Doors, Buttons, Fly etc. */
        MANAGE, /* Access Chests & Plant Farms */
        BUILD, /* Build & Break Blocks */
        CO_OWNER; /* Manage Claims */

        public boolean hasPerms(Permission permission) {
            return this.ordinal() >= permission.ordinal();
        }
    }

    public enum Settings {

        ENDER_PEARL(Permission.ACCESS);

        private final Permission defaultPermission;

        Settings(Permission defaultPermission) {
            this.defaultPermission = defaultPermission;
        }

        public Permission defaultPermission() {
            return defaultPermission;
        }
    }

    public enum ToolType {

        NORMAL,
        CHILD,
        WITHOUT_OWNER;

    }

    public static class CreateResult {

        private  Claim claim;
        private boolean succeeded;

        public Claim getClaim() {
            return claim;
        }

        public void setClaim(Claim claim) {
            this.claim = claim;
        }

        public boolean isSucceeded() {
            return succeeded;
        }

        public void setSucceeded(boolean succeeded) {
            this.succeeded = succeeded;
        }
    }
}
