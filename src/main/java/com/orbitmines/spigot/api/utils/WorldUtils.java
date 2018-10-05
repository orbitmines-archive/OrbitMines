package com.orbitmines.spigot.api.utils;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class WorldUtils {

    private static final BlockFace[] signFaces = { BlockFace.DOWN, BlockFace.UP, BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH };

    public static Chest getChestAtSign(Location signLocation) {
        Block sign = signLocation.getBlock();

            /* First check chest behind sign, then we check for other nearby chests */
            if (sign.getState().getBlockData() instanceof WallSign) {
                WallSign signData = (WallSign) sign.getState().getBlockData();
                BlockFace opposite = signData.getFacing().getOppositeFace();

                Block block = sign.getRelative(opposite);

                if (block.getState() instanceof Chest)
                    return (Chest) block.getState();
            }

            /* Otherwise switch through all nearby chest locations */
            for (BlockFace face : signFaces) {
                Block block = sign.getRelative(face);

                if (block.getState() instanceof Chest)
                    return (Chest) block.getState();
        }

        return null;
    }

    public static void removeAllEntities() {
        for (World world : Bukkit.getWorlds()) {
            removeEntities(world);
        }
    }

    public static void removeEntities(World world) {
        for (Entity en : world.getEntities()) {
            if (en instanceof Player)
                continue;

            en.remove();
        }
    }

    private final static Map<UUID, Entity> cache = new HashMap<>();

    public static Entity getEntityByUUID(UUID uuid) {
        if (cache.containsKey(uuid))
            return cache.get(uuid);

        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getUniqueId().equals(uuid)) {
                    cache.put(uuid, entity);
                    return entity;
                }
            }
        }
        return null;
    }

    public static <T extends Entity> T getEntityByUUID(UUID uuid, Class<T> aClass) {
        if (cache.containsKey(uuid))
            return (T) cache.get(uuid);

        for (World world : Bukkit.getWorlds()) {
            for (T entity : world.getEntitiesByClass(aClass)) {
                if (entity.getUniqueId().equals(uuid)) {
                    cache.put(uuid, entity);
                    return entity;
                }
            }
        }
        return null;
    }

    public static void lookAt(LivingEntity entity, double x, double z) {
        lookAt(entity, x, entity.getLocation().getY() + 1.75, z);
    }

    public static void lookAt(LivingEntity entity, double x, double y, double z) {
        Location location = entity.getLocation();

        double vX = location.getX() - x;
        double vY = location.getY() - y;
        double vZ = location.getZ() - z;

        double d = Math.sqrt(vX * vX + vY * vY + vZ * vZ);

        vX /= d;
        vY /= d;
        vZ /= d;

        double yaw = Math.toDegrees(Math.atan2(vZ, vX)) + 90;
        double pitch = Math.toDegrees(Math.asin(vY));

        location.setYaw((float) yaw);
        location.setPitch((float) pitch);

        entity.teleport(location);
    }

    public static float getYaw(Location loc1, Location loc2) {
        double vX = loc1.getX() - loc2.getX();
        double vY = loc1.getY() - loc2.getY();
        double vZ = loc1.getZ() - loc2.getZ();

        double d = Math.sqrt(vX * vX + vY * vY + vZ * vZ);

        vX /= d;
        vY /= d;
        vZ /= d;

        return (float) (Math.toDegrees(Math.atan2(vZ, vX)) + 90);
    }

    public static BlockFace fromYaw(float yaw) {
        if (yaw >= -22.5 && yaw <= 22.5)
            return BlockFace.SOUTH;
        else if (yaw >= 22.5 && yaw <= 67.5)
            return BlockFace.SOUTH_WEST;
        else if (yaw >= 67.5 && yaw <= 112.5)
            return BlockFace.WEST;
        else if (yaw >= 112.5 && yaw <= 157.5)
            return BlockFace.NORTH_WEST;
        else if (yaw >= -112.5 && yaw <= -67.5)
            return BlockFace.EAST;
        else if (yaw >= -67.5 && yaw <= -22.5)
            return BlockFace.SOUTH_EAST;
        else if (yaw >= -157.5 && yaw <= -112.5 || yaw >= 202.5) /* ? */
            return BlockFace.NORTH_EAST;
        else
            return BlockFace.NORTH;
    }

    public static double yawToDegree(LivingEntity livingEntity) {
        return ((livingEntity.getLocation().getYaw() + 90) * Math.PI) / 180;
    }

    public static double pitchToDegree(LivingEntity livingEntity) {
        return ((livingEntity.getLocation().getPitch() + 90) * Math.PI) / 180;
    }

    public static OMPlayer getClosestPlayer(Location location) {
        OMPlayer omp = null;
        double distance = 0;

        for (OMPlayer player : OMPlayer.getPlayers()) {
            double d = location.distance(player.getLocation());

            if (omp == null || d < distance) {
                omp = player;
                distance = d;
            }
        }

        return omp;
    }
//
//    public static MiniGamePlayer getClosestPlayer(Location location, boolean spectator) {
//        MiniGamePlayer omp = null;
//        double distance = 0;
//
//        for (MiniGamePlayer player : MiniGamePlayer.getMiniGamePlayers()) {
//            if (!spectator && player.isSpectator())
//                continue;
//
//            double d = location.distance(player.getLocation());
//
//            if (omp == null || d < distance) {
//                omp = player;
//                distance = d;
//            }
//        }
//
//        return omp;
//    }

    /* Returns the nearest entity within 100 blocks */
    public static Entity getClosestEntity(Location loc) {
        Entity e = null;
        double d = 100;
        for (Entity o : loc.getWorld().getEntities()) {
            if (loc.distance(o.getLocation()) < d)
                e = o;
        }
        return e;
    }

    /* Returns the nearest entity within 100 blocks */
    public static Entity getClosestEntity(Location loc, Entity otherThan) {
        Entity e = null;
        double d = 100;
        for (Entity o : loc.getWorld().getEntities()) {
            if (o == otherThan)
                continue;
            if (loc.distance(o.getLocation()) < d)
                e = o;
        }
        return e;
    }

    public static List<Player> getNearbyPlayers(Location loc, double range) {
        List<Player> list = new ArrayList<>();

        for (Player p : loc.getWorld().getPlayers()) {
            Location loc2 = p.getLocation();
            double distance = loc.distance(loc2);

            if (distance <= range)
                list.add(p);
        }

        return list;
    }

//
//    /* Returns a list of OMPlayers nearby a location */
//    public static List<OMPlayer> getNearbyPlayers(Location loc, double range, boolean includeSpectators) {
//        List<OMPlayer> list = new ArrayList<>();
//        for (int i = OMPlayer.getPlayers().size(); i > 0; i--) {
//            OMPlayer closest = null;
//            for (Player p : loc.getWorld().getPlayers()) {
//                OMPlayer omp = OMPlayer.getPlayer(p);
//                if (!includeSpectators && omp instanceof MiniGamePlayer && ((MiniGamePlayer) omp).isSpectator())
//                    continue;
//                Location loc2 = omp.getLocation();
//                double distance = loc.distance(loc2);
//                if (distance > range)
//                    continue;
//                if (list.contains(omp))
//                    continue;
//                if (closest == null)
//                    closest = omp;
//                if (distance < closest.getLocation().distance(loc))
//                    closest = omp;
//            }
//            if (closest == null)
//                break;
//            list.add(closest);
//        }
//        return list;
//    }
}
