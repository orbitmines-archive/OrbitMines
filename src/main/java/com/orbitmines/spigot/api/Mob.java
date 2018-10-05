package com.orbitmines.spigot.api;

import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.nms.Nms;
import com.orbitmines.spigot.api.nms.npc.MobNpcNms;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Arrays;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public enum Mob {

    BAT(EntityType.BAT, Material.BAT_SPAWN_EGG, "Bat"),
    BLAZE(EntityType.BLAZE, Material.BLAZE_SPAWN_EGG, "Blaze"),
    CAVE_SPIDER(EntityType.CAVE_SPIDER, Material.CAVE_SPIDER_SPAWN_EGG, "Cave Spider"),
    CHICKEN(EntityType.CHICKEN, Material.CHICKEN_SPAWN_EGG, "Chicken"),
    COD(EntityType.COD, Material.COD_SPAWN_EGG, "Cod"),
    COW(EntityType.COW, Material.COW_SPAWN_EGG, "Cow"),
    CREEPER(EntityType.CREEPER, Material.CREEPER_SPAWN_EGG, "Creeper"),
    DOLPHIN(EntityType.DOLPHIN, Material.DOLPHIN_SPAWN_EGG, "Dolphin"),
    DONKEY(EntityType.DONKEY, Material.DONKEY_SPAWN_EGG, "Donkey"),
    DROWNED(EntityType.DROWNED, Material.DROWNED_SPAWN_EGG, "Drowned"),
    ELDER_GUARDIAN(EntityType.ELDER_GUARDIAN, Material.ELDER_GUARDIAN_SPAWN_EGG, "Elder Guardian"),
    ENDER_DRAGON(EntityType.ENDER_DRAGON, Material.DRAGON_EGG, "Ender Dragon"),
    ENDERMAN(EntityType.ENDERMAN, Material.ENDERMAN_SPAWN_EGG, "Enderman"),
    ENDERMITE(EntityType.ENDERMITE, Material.ENDERMITE_SPAWN_EGG, "Endermite"),
    EVOKER(EntityType.EVOKER, Material.ELDER_GUARDIAN_SPAWN_EGG, "Evoker"),
    GHAST(EntityType.GHAST, Material.GHAST_SPAWN_EGG, "Ghast"),
    GIANT(EntityType.GIANT, Material.ZOMBIE_SPAWN_EGG, "Giant"),
    GUARDIAN(EntityType.GUARDIAN, Material.GUARDIAN_SPAWN_EGG, "Guardian"),
    HORSE(EntityType.HORSE, Material.HORSE_SPAWN_EGG, "Horse"),
    HUSK(EntityType.HUSK, Material.HUSK_SPAWN_EGG, "Husk"),
    ILLUSIONER(EntityType.ILLUSIONER, Material.POLAR_BEAR_SPAWN_EGG, "Illusioner"),
    IRON_GOLEM(EntityType.IRON_GOLEM, Material.POLAR_BEAR_SPAWN_EGG, "Iron Golem"),
    LLAMA(EntityType.LLAMA, Material.LLAMA_SPAWN_EGG, "Llama"),
    MAGMA_CUBE(EntityType.MAGMA_CUBE, Material.MAGMA_CUBE_SPAWN_EGG, "Magma Cube"),
    MULE(EntityType.MULE, Material.MULE_SPAWN_EGG, "Mule"),
    MUSHROOM_COW(EntityType.MUSHROOM_COW, Material.MOOSHROOM_SPAWN_EGG, "Mushroom Cow"),
    OCELOT(EntityType.OCELOT, Material.OCELOT_SPAWN_EGG, "Ocelot"),
    PHANTOM(EntityType.PHANTOM, Material.PHANTOM_SPAWN_EGG, "Phantom"),
    PARROT(EntityType.PARROT, Material.PARROT_SPAWN_EGG, "Parrot"),
    PIG(EntityType.PIG, Material.PIG_SPAWN_EGG, "Pig"),
    PIG_ZOMBIE(EntityType.PIG_ZOMBIE, Material.ZOMBIE_PIGMAN_SPAWN_EGG, "Zombie Pigman"),
    POLAR_BEAR(EntityType.POLAR_BEAR, Material.POLAR_BEAR_SPAWN_EGG, "Polar Bear"),
    PUFFERFISH(EntityType.PUFFERFISH, Material.PUFFERFISH_SPAWN_EGG, "Pufferfish"),
    RABBIT(EntityType.RABBIT, Material.RABBIT_SPAWN_EGG, "Rabbit"),
    SALMON(EntityType.SALMON, Material.SALMON_SPAWN_EGG, "Salmon"),
    SHEEP(EntityType.SHEEP, Material.SHEEP_SPAWN_EGG, "Sheep"),
    SHULKER(EntityType.SHULKER, Material.SHULKER_SPAWN_EGG, "Shulker"),
    SILVERFISH(EntityType.SILVERFISH, Material.SILVERFISH_SPAWN_EGG, "Silverfish"),
    SKELETON(EntityType.SKELETON, Material.SKELETON_SPAWN_EGG, "Skeleton"),
    SKELETON_HORSE(EntityType.SKELETON_HORSE, Material.SKELETON_HORSE_SPAWN_EGG, "Skeleton Horse"),
    SLIME(EntityType.SLIME, Material.SLIME_SPAWN_EGG, "Slime"),
    SNOWMAN(EntityType.SNOWMAN, Material.POLAR_BEAR_SPAWN_EGG, "Snowman"),
    SPIDER(EntityType.SPIDER, Material.SPIDER_SPAWN_EGG, "Spider"),
    SQUID(EntityType.SQUID, Material.SQUID_SPAWN_EGG, "Squid"),
    STRAY(EntityType.STRAY, Material.STRAY_SPAWN_EGG, "Stray"),
    TROPICAL_FISH(EntityType.TROPICAL_FISH, Material.TROPICAL_FISH, "Tropical Fish"),
    TURTLE(EntityType.TURTLE, Material.TURTLE_SPAWN_EGG, "Turtle"),
    VEX(EntityType.VEX, Material.VEX_SPAWN_EGG, "Vex"),
    VILLAGER(EntityType.VILLAGER, Material.VILLAGER_SPAWN_EGG, "Villager"),
    VINDICATOR(EntityType.VINDICATOR, Material.VINDICATOR_SPAWN_EGG, "Vindicator"),
    WITCH(EntityType.WITCH, Material.WITCH_SPAWN_EGG, "Witch"),
    WITHER(EntityType.WITHER, Material.WITHER_SKELETON_SPAWN_EGG, "Wither"),
    WITHER_SKELETON(EntityType.WITHER_SKELETON, Material.WITHER_SKELETON_SPAWN_EGG, "Wither Skeleton"),
    WOLF(EntityType.WOLF, Material.WOLF_SPAWN_EGG, "Wolf"),
    ZOMBIE(EntityType.ZOMBIE, Material.ZOMBIE_SPAWN_EGG, "Zombie"),
    ZOMBIE_HORSE(EntityType.ZOMBIE_HORSE, Material.ZOMBIE_HORSE_SPAWN_EGG, "Zombie Horse"),
    ZOMBIE_VILLAGER(EntityType.ZOMBIE_VILLAGER, Material.ZOMBIE_VILLAGER_SPAWN_EGG, "Zombie Villager");

    private final EntityType type;
    private final Material spawnEggMaterial;
    private final String name;

    Mob(EntityType type, Material spawnEggMaterial, String name) {
        this.type = type;
        this.spawnEggMaterial = spawnEggMaterial;
        this.name = name;
    }

    public EntityType getType() {
        return type;
    }

    public Material getSpawnEggMaterial() {
        return spawnEggMaterial;
    }

    public String getName() {
        return name;
    }

    public Entity spawn(Location location, MobNpcNms.Option... optionsArray) {
        return nms().spawn(location, Arrays.asList(optionsArray));
    }

    public Entity spawnRideable(Location location, float speed, float backMultiplier, float sideMultiplier, float walkHeight, float jumpHeight) {
        return nms().spawnRideable(location, speed, backMultiplier, sideMultiplier, walkHeight, jumpHeight);
    }
    
    public MobNpcNms nms() {
        Nms nms = OrbitMines.getInstance().getNms();

        switch (this) {

            case BAT:
                return nms.getBatNpc();
            case BLAZE:
                return nms.getBlazeNpc();
            case CAVE_SPIDER:
                return nms.getCaveSpiderNpc();
            case CHICKEN:
                return nms.getChickenNpc();
            case COD:
                return nms.getCodNpc();
            case COW:
                return nms.getCowNpc();
            case CREEPER:
                return nms.getCreeperNpc();
            case DOLPHIN:
                return nms.getDolphinNpc();
            case DONKEY:
                return nms.getDonkeyNpc();
            case DROWNED:
                return nms.getDrownedNpc();
            case ELDER_GUARDIAN:
                return nms.getElderGuardianNpc();
            case ENDER_DRAGON:
                return nms.getEnderDragonNpc();
            case ENDERMAN:
                return nms.getEndermanNpc();
            case ENDERMITE:
                return nms.getEndermiteNpc();
            case EVOKER:
                return nms.getEvokerNpc();
            case GHAST:
                return nms.getGhastNpc();
            case GIANT:
                return nms.getGiantNpc();
            case GUARDIAN:
                return nms.getGuardianNpc();
            case HORSE:
                return nms.getHorseNpc();
            case HUSK:
                return nms.getHuskNpc();
            case ILLUSIONER:
                return nms.getIllusionerNpc();
            case IRON_GOLEM:
                return nms.getIronGolemNpc();
            case LLAMA:
                return nms.getLlamaNpc();
            case MAGMA_CUBE:
                return nms.getMagmaCubeNpc();
            case MULE:
                return nms.getMuleNpc();
            case MUSHROOM_COW:
                return nms.getMushroomCowNpc();
            case OCELOT:
                return nms.getOcelotNpc();
            case PHANTOM:
                return nms.getPhantomNpc();
            case PARROT:
                return nms.getParrotNpc();
            case PIG:
                return nms.getPigNpc();
            case PIG_ZOMBIE:
                return nms.getPigZombieNpc();
            case POLAR_BEAR:
                return nms.getPolarBearNpc();
            case PUFFERFISH:
                return nms.getPufferFishNpc();
            case RABBIT:
                return nms.getRabbitNpc();
            case SALMON:
                return nms.getSalmonNpc();
            case SHEEP:
                return nms.getSheepNpc();
            case SHULKER:
                return nms.getShulkerNpc();
            case SILVERFISH:
                return nms.getSilverfishNpc();
            case SKELETON:
                return nms.getSkeletonNpc();
            case SKELETON_HORSE:
                return nms.getSkeletonHorseNpc();
            case SLIME:
                return nms.getSlimeNpc();
            case SNOWMAN:
                return nms.getSnowmanNpc();
            case SPIDER:
                return nms.getSpiderNpc();
            case SQUID:
                return nms.getSquidNpc();
            case STRAY:
                return nms.getStrayNpc();
            case TROPICAL_FISH:
                return nms.getTropicalFishNpc();
            case TURTLE:
                return nms.getTurtleNpc();
            case VEX:
                return nms.getVexNpc();
            case VILLAGER:
                return nms.getVillagerNpc();
            case VINDICATOR:
                return nms.getVindicatorNpc();
            case WITCH:
                return nms.getWitchNpc();
            case WITHER:
                return nms.getWitherNpc();
            case WITHER_SKELETON:
                return nms.getWitherSkeletonNpc();
            case WOLF:
                return nms.getWolfNpc();
            case ZOMBIE:
                return nms.getZombieNpc();
            case ZOMBIE_HORSE:
                return nms.getZombieHorseNpc();
            case ZOMBIE_VILLAGER:
                return nms.getZombieVillagerNpc();
        }
        throw new IllegalStateException();
    }

    @Deprecated
    public static Mob from(short mobId) {
        for (Mob mob : Mob.values()) {
            if (mob.getType().getTypeId() == mobId)
                return mob;
        }
        return null;
    }

    public static Mob from(String name) {
        for (Mob mob : Mob.values()) {
            if (mob.getName().equals(name))
                return mob;
        }
        return null;
    }

    public static Mob from(EntityType type) {
        for (Mob mob : Mob.values()) {
            if (mob.getType() == type)
                return mob;
        }
        return null;
    }

    public static Mob random() {
        return RandomUtils.randomFrom(values());
    }
}
