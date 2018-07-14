package com.orbitmines.spigot.api.utils;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class WorldUtils {

    public static final List<Material> CANNOT_TRANSFORM = Arrays.asList(Material.LONG_GRASS, Material.YELLOW_FLOWER, Material.RED_ROSE, Material.DOUBLE_PLANT, Material.WOOD_STEP, Material.WOOD_STAIRS, Material.COBBLESTONE_STAIRS, Material.TRAP_DOOR, Material.IRON_TRAPDOOR, Material.IRON_TRAPDOOR, Material.SKULL, Material.WATER_LILY, Material.SIGN_POST, Material.WALL_SIGN, Material.TORCH, Material.FENCE, Material.WATER, Material.STATIONARY_WATER);

    private static final BlockFace[] signFaces = new BlockFace[] { BlockFace.DOWN, BlockFace.UP, BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH };

    public static Chest getChestAtSign(Location signLocation) {
        Block sign = signLocation.getBlock();

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
