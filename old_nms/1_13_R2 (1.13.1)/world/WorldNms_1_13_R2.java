package com.orbitmines.spigot.api.nms.world;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.Mob;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.entity.Player;

import java.util.Collection;

public class WorldNms_1_13_R2 implements WorldNms {

    @Override
    public void chestAnimation(Location location, boolean opened) {
        World world = ((CraftWorld) location.getWorld()).getHandle();
        BlockPosition position = new BlockPosition(location.getX(), location.getY(), location.getZ());

        TileEntityChest tileChest = (TileEntityChest) world.getTileEntity(position);
        world.playBlockAction(position, tileChest.getBlock().getBlock(), 1, opened ? 1 : 0);
    }

    @Override
    public void enderchestAnimation(Location location, boolean opened) {
        World world = ((CraftWorld) location.getWorld()).getHandle();
        BlockPosition position = new BlockPosition(location.getX(), location.getY(), location.getZ());

        TileEntityEnderChest tileChest = (TileEntityEnderChest) world.getTileEntity(position);
        world.playBlockAction(position, tileChest.getBlock().getBlock(), 1, opened ? 1 : 0);
    }

    @Override
    public void conduitAnimation(Location location, Collection<? extends Player> players) {
        World world = ((CraftWorld) location.getWorld()).getHandle();
        BlockPosition position = new BlockPosition(location.getX(), location.getY(), location.getZ());

        TileEntityConduit tileConduit = (TileEntityConduit) world.getTileEntity(position);
        tileConduit.Y_();//TODO FIX
    }

    @Override
    public void setSpawner(Location location, Mob mob) {
        location.getBlock().setType(org.bukkit.Material.SPAWNER);

        World world = ((CraftWorld) location.getWorld()).getHandle();
        BlockPosition position = new BlockPosition(location.getX(), location.getY(), location.getZ());

        TileEntityMobSpawner tileSpawner = (TileEntityMobSpawner) world.getTileEntity(position);
        tileSpawner.getSpawner().setMobName(getEntityTypes(mob));
    }

    @Override
    public Mob getSpawner(Location location) {
        World world = ((CraftWorld) location.getWorld()).getHandle();
        BlockPosition position = new BlockPosition(location.getX(), location.getY(), location.getZ());

        TileEntityMobSpawner tileSpawner = (TileEntityMobSpawner) world.getTileEntity(position);
        return getMob(tileSpawner.getSpawner().getMobName());
    }

    private Mob getMob(MinecraftKey key) {
        EntityTypes types = IRegistry.ENTITY_TYPE.get(key);

        if (types == EntityTypes.BAT)
            return Mob.BAT;
        else if (types == EntityTypes.BLAZE)
            return Mob.BLAZE;
        else if (types == EntityTypes.CAVE_SPIDER)
            return Mob.CAVE_SPIDER;
        else if (types == EntityTypes.CHICKEN)
            return Mob.CHICKEN;
        else if (types == EntityTypes.COD)
            return Mob.COD;
        else if (types == EntityTypes.COW)
            return Mob.COW;
        else if (types == EntityTypes.CREEPER)
            return Mob.CREEPER;
        else if (types == EntityTypes.DOLPHIN)
            return Mob.DOLPHIN;
        else if (types == EntityTypes.DONKEY)
            return Mob.DONKEY;
        else if (types == EntityTypes.DROWNED)
            return Mob.DROWNED;
        else if (types == EntityTypes.ELDER_GUARDIAN)
            return Mob.ELDER_GUARDIAN;
        else if (types == EntityTypes.ENDER_DRAGON)
            return Mob.ENDER_DRAGON;
        else if (types == EntityTypes.ENDERMAN)
            return Mob.ENDERMAN;
        else if (types == EntityTypes.ENDERMITE)
            return Mob.ENDERMITE;
        else if (types == EntityTypes.EVOKER)
            return Mob.EVOKER;
        else if (types == EntityTypes.GHAST)
            return Mob.GHAST;
        else if (types == EntityTypes.GIANT)
            return Mob.GIANT;
        else if (types == EntityTypes.GUARDIAN)
            return Mob.GUARDIAN;
        else if (types == EntityTypes.HORSE)
            return Mob.HORSE;
        else if (types == EntityTypes.HUSK)
            return Mob.HUSK;
        else if (types == EntityTypes.ILLUSIONER)
            return Mob.ILLUSIONER;
        else if (types == EntityTypes.IRON_GOLEM)
            return Mob.IRON_GOLEM;
        else if (types == EntityTypes.LLAMA)
            return Mob.LLAMA;
        else if (types == EntityTypes.MAGMA_CUBE)
            return Mob.MAGMA_CUBE;
        else if (types == EntityTypes.MULE)
            return Mob.MULE;
        else if (types == EntityTypes.MOOSHROOM)
            return Mob.MUSHROOM_COW;
        else if (types == EntityTypes.OCELOT)
            return Mob.OCELOT;
        else if (types == EntityTypes.PHANTOM)
            return Mob.PHANTOM;
        else if (types == EntityTypes.PARROT)
            return Mob.PARROT;
        else if (types == EntityTypes.PIG)
            return Mob.PIG;
        else if (types == EntityTypes.ZOMBIE_PIGMAN)
            return Mob.PIG_ZOMBIE;
        else if (types == EntityTypes.POLAR_BEAR)
            return Mob.POLAR_BEAR;
        else if (types == EntityTypes.PUFFERFISH)
            return Mob.PUFFERFISH;
        else if (types == EntityTypes.RABBIT)
            return Mob.RABBIT;
        else if (types == EntityTypes.SALMON)
            return Mob.SALMON;
        else if (types == EntityTypes.SHEEP)
            return Mob.SHEEP;
        else if (types == EntityTypes.SHULKER)
            return Mob.SHULKER;
        else if (types == EntityTypes.SILVERFISH)
            return Mob.SILVERFISH;
        else if (types == EntityTypes.SKELETON)
            return Mob.SKELETON;
        else if (types == EntityTypes.SKELETON_HORSE)
            return Mob.SKELETON_HORSE;
        else if (types == EntityTypes.SLIME)
            return Mob.SLIME;
        else if (types == EntityTypes.SNOW_GOLEM)
            return Mob.SNOWMAN;
        else if (types == EntityTypes.SPIDER)
            return Mob.SPIDER;
        else if (types == EntityTypes.SQUID)
            return Mob.SQUID;
        else if (types == EntityTypes.STRAY)
            return Mob.STRAY;
        else if (types == EntityTypes.TROPICAL_FISH)
            return Mob.TROPICAL_FISH;
        else if (types == EntityTypes.TURTLE)
            return Mob.TURTLE;
        else if (types == EntityTypes.VEX)
            return Mob.VEX;
        else if (types == EntityTypes.VILLAGER)
            return Mob.VILLAGER;
        else if (types == EntityTypes.VINDICATOR)
            return Mob.VINDICATOR;
        else if (types == EntityTypes.WITCH)
            return Mob.WITCH;
        else if (types == EntityTypes.WITHER)
            return Mob.WITHER;
        else if (types == EntityTypes.WITHER_SKELETON)
            return Mob.WITHER_SKELETON;
        else if (types == EntityTypes.WOLF)
            return Mob.WOLF;
        else if (types == EntityTypes.ZOMBIE)
            return Mob.ZOMBIE;
        else if (types == EntityTypes.ZOMBIE_HORSE)
            return Mob.ZOMBIE_HORSE;
        else if (types == EntityTypes.ZOMBIE_VILLAGER)
            return Mob.ZOMBIE_VILLAGER;
        else
            return Mob.PIG;
    }

    private EntityTypes getEntityTypes(Mob mob) {
        switch (mob) {

            case BAT:
                return EntityTypes.BAT;
            case BLAZE:
                return EntityTypes.BLAZE;
            case CAVE_SPIDER:
                return EntityTypes.CAVE_SPIDER;
            case CHICKEN:
                return EntityTypes.CHICKEN;
            case COD:
                return EntityTypes.COD;
            case COW:
                return EntityTypes.COW;
            case CREEPER:
                return EntityTypes.CREEPER;
            case DOLPHIN:
                return EntityTypes.DOLPHIN;
            case DONKEY:
                return EntityTypes.DONKEY;
            case DROWNED:
                return EntityTypes.DROWNED;
            case ELDER_GUARDIAN:
                return EntityTypes.ELDER_GUARDIAN;
            case ENDER_DRAGON:
                return EntityTypes.ENDER_DRAGON;
            case ENDERMAN:
                return EntityTypes.ENDERMAN;
            case ENDERMITE:
                return EntityTypes.ENDERMITE;
            case EVOKER:
                return EntityTypes.EVOKER;
            case GHAST:
                return EntityTypes.GHAST;
            case GIANT:
                return EntityTypes.GIANT;
            case GUARDIAN:
                return EntityTypes.GUARDIAN;
            case HORSE:
                return EntityTypes.HORSE;
            case HUSK:
                return EntityTypes.HUSK;
            case ILLUSIONER:
                return EntityTypes.ILLUSIONER;
            case IRON_GOLEM:
                return EntityTypes.IRON_GOLEM;
            case LLAMA:
                return EntityTypes.LLAMA;
            case MAGMA_CUBE:
                return EntityTypes.MAGMA_CUBE;
            case MULE:
                return EntityTypes.MULE;
            case MUSHROOM_COW:
                return EntityTypes.MOOSHROOM;
            case OCELOT:
                return EntityTypes.OCELOT;
            case PHANTOM:
                return EntityTypes.PHANTOM;
            case PARROT:
                return EntityTypes.PARROT;
            case PIG:
                return EntityTypes.PIG;
            case PIG_ZOMBIE:
                return EntityTypes.ZOMBIE_PIGMAN;
            case POLAR_BEAR:
                return EntityTypes.POLAR_BEAR;
            case PUFFERFISH:
                return EntityTypes.PUFFERFISH;
            case RABBIT:
                return EntityTypes.RABBIT;
            case SALMON:
                return EntityTypes.SALMON;
            case SHEEP:
                return EntityTypes.SHEEP;
            case SHULKER:
                return EntityTypes.SHULKER;
            case SILVERFISH:
                return EntityTypes.SILVERFISH;
            case SKELETON:
                return EntityTypes.SKELETON;
            case SKELETON_HORSE:
                return EntityTypes.SKELETON_HORSE;
            case SLIME:
                return EntityTypes.SLIME;
            case SNOWMAN:
                return EntityTypes.SNOW_GOLEM;
            case SPIDER:
                return EntityTypes.SPIDER;
            case SQUID:
                return EntityTypes.SQUID;
            case STRAY:
                return EntityTypes.STRAY;
            case TROPICAL_FISH:
                return EntityTypes.TROPICAL_FISH;
            case TURTLE:
                return EntityTypes.TURTLE;
            case VEX:
                return EntityTypes.VEX;
            case VILLAGER:
                return EntityTypes.VILLAGER;
            case VINDICATOR:
                return EntityTypes.VINDICATOR;
            case WITCH:
                return EntityTypes.WITCH;
            case WITHER:
                return EntityTypes.WITHER;
            case WITHER_SKELETON:
                return EntityTypes.WITHER_SKELETON;
            case WOLF:
                return EntityTypes.WOLF;
            case ZOMBIE:
                return EntityTypes.ZOMBIE;
            case ZOMBIE_HORSE:
                return EntityTypes.ZOMBIE_HORSE;
            case ZOMBIE_VILLAGER:
                return EntityTypes.ZOMBIE_VILLAGER;
        }
        throw new IllegalStateException();
    }
}
