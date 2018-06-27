package com.orbitmines.spigot.api;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.nms.Nms;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public enum Mob {

    /* Mob-ish */
    ARMOR_STAND(EntityType.ARMOR_STAND, "ArmorStand", "ArmorStand"),

    BAT(EntityType.BAT, "Bat", "Bat"),
    BLAZE(EntityType.BLAZE, "Blaze", "Blaze"),
    CAVE_SPIDER(EntityType.CAVE_SPIDER, "Cave Spider", "CaveSpider"),
    CHICKEN(EntityType.CHICKEN, "Chicken", "Chicken"),
    COW(EntityType.COW, "Cow", "Cow"),
    CREEPER(EntityType.CREEPER, "Creeper", "Creeper"),
    DONKEY(EntityType.DONKEY, "Donkey", "Donkey"),
    ENDER_DRAGON(EntityType.ENDER_DRAGON, "Ender Dragon", "EnderDragon"),
    ENDERMAN(EntityType.ENDERMAN, "Enderman", "Enderman"),
    ENDERMITE(EntityType.ENDERMITE, "Endermite", "Endermite"),
    EVOKER(EntityType.EVOKER, "Evoker", "Evocation_Illager"),
    GHAST(EntityType.GHAST, "Ghast", "Ghast"),
    GIANT(EntityType.GIANT, "Giant", "Giant"),
    GUARDIAN(EntityType.GUARDIAN, "Guardian", "Guardian"),
    HORSE(EntityType.HORSE, "Horse", "EntityHorse"),
    HUSK(EntityType.HUSK, "Husk", "Husk"),
    IRON_GOLEM(EntityType.IRON_GOLEM, "Iron Golem", "VillagerGolem"),
    LLAMA(EntityType.LLAMA, "Llama", "Llama"),
    MAGMA_CUBE(EntityType.MAGMA_CUBE, "Magma Cube", "LavaSlime"),
    MULE(EntityType.MULE, "Mule", "Mule"),
    MUSHROOM_COW(EntityType.MUSHROOM_COW, "Mushroom Cow", "MushroomCow"),
    OCELOT(EntityType.OCELOT, "Ocelot", "Ozelot"),
    PIG(EntityType.PIG, "Pig", "Pig"),
    PIG_ZOMBIE(EntityType.PIG_ZOMBIE, "Zombie Pigman", "PigZombie"),
    POLAR_BEAR(EntityType.POLAR_BEAR, "Polar Bear", "PolarBear"),
    RABBIT(EntityType.RABBIT, "Rabbit", "Rabbit"),
    SHEEP(EntityType.SHEEP, "Sheep", "Sheep"),
    SILVERFISH(EntityType.SILVERFISH, "Silverfish", "Silverfish"),
    SKELETON(EntityType.SKELETON, "Skeleton", "Skeleton"),
    SKELETON_HORSE(EntityType.SKELETON_HORSE, "Skeleton Horse", "SkeletonHorse"),
    SLIME(EntityType.SLIME, "Slime", "Slime"),
    SNOWMAN(EntityType.SNOWMAN, "Snowman", "SnowMan"),
    SPIDER(EntityType.SPIDER, "Spider", "Spider"),
    SQUID(EntityType.SQUID, "Squid", "Squid"),
    STRAY(EntityType.STRAY, "Stray", "Stray"),
    VEX(EntityType.VEX, "Vex", "Vex"),
    VILLAGER(EntityType.VILLAGER, "Villager", "Villager"),
    VINDICATOR(EntityType.VINDICATOR, "Vindicator", "Vindication_Illager"),
    WITCH(EntityType.WITCH, "Witch", "Witch"),
    WITHER(EntityType.WITHER, "Wither", "WitherBoss"),
    WITHER_SKELETON(EntityType.WITHER_SKELETON, "Wither Skeleton", "WitherSkeleton"),
    WOLF(EntityType.WOLF, "Wolf", "Wolf"),
    ZOMBIE(EntityType.ZOMBIE, "Zombie", "Zombie"),
    ZOMBIE_HORSE(EntityType.ZOMBIE_HORSE, "Zombie Horse", "ZombieHorse"),
    ZOMBIE_VILLAGER(EntityType.ZOMBIE_VILLAGER, "Zombie Villager", "ZombieVillager");

    private final EntityType type;
    private final String name;
    private final String rawName;

    Mob(EntityType type, String name, String rawName) {
        this.type = type;
        this.name = name;
        this.rawName = rawName;
    }

    public EntityType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getRawName() {
        return rawName;
    }

    public short getEggId() {
        return type.getTypeId();
    }

    public Entity spawn(Location location) {
        return spawn(location, null);
    }

    public Entity spawn(Location location, String displayName) {
        return spawn(location, displayName, false, false);
    }

    public Entity spawnMoving(Location location) {
        return spawn(location, null);
    }

    public Entity spawnMoving(Location location, String displayName) {
        return spawn(location, displayName, true, false);
    }

    public Entity spawnNoAttack(Location location) {
        return spawn(location, null);
    }

    public Entity spawnNoAttack(Location location, String displayName) {
        return spawn(location, displayName, true, true);
    }

    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        Nms nms = OrbitMines.getInstance().getNms();
        switch (this) {
            case BAT:
                return nms.getBatNpc().spawn(location, displayName, moving, noAttack);
            case BLAZE:
                return nms.getBlazeNpc().spawn(location, displayName, moving, noAttack);
            case CAVE_SPIDER:
                return nms.getCaveSpiderNpc().spawn(location, displayName, moving, noAttack);
            case CHICKEN:
                return nms.getChickenNpc().spawn(location, displayName, moving, noAttack);
            case COW:
                return nms.getCowNpc().spawn(location, displayName, moving, noAttack);
            case CREEPER:
                return nms.getCreeperNpc().spawn(location, displayName, moving, noAttack);
            case ENDERMAN:
                return nms.getEndermanNpc().spawn(location, displayName, moving, noAttack);
            case ENDERMITE:
                return nms.getEndermiteNpc().spawn(location, displayName, moving, noAttack);
            case EVOKER:
                return nms.getEvokerNpc().spawn(location, displayName, moving, noAttack);
            case GHAST:
                return nms.getGhastNpc().spawn(location, displayName, moving, noAttack);
            case GUARDIAN:
                return nms.getGuardianNpc().spawn(location, displayName, moving, noAttack);
            case HORSE:
                return nms.getHorseNpc().spawn(location, displayName, moving, noAttack);
            case IRON_GOLEM:
                return nms.getIronGolemNpc().spawn(location, displayName, moving, noAttack);
            case LLAMA:
                return nms.getLlamaNpc().spawn(location, displayName, moving, noAttack);
            case MAGMA_CUBE:
                return nms.getMagmaCubeNpc().spawn(location, displayName, moving, noAttack);
            case MULE:
                return nms.getMuleNpc().spawn(location, displayName, moving, noAttack);
            case MUSHROOM_COW:
                return nms.getMushroomCowNpc().spawn(location, displayName, moving, noAttack);
            case OCELOT:
                return nms.getOcelotNpc().spawn(location, displayName, moving, noAttack);
            case PIG:
                return nms.getPigNpc().spawn(location, displayName, moving, noAttack);
            case PIG_ZOMBIE:
                return nms.getPigZombieNpc().spawn(location, displayName, moving, noAttack);
            case POLAR_BEAR:
                return nms.getPolarBearNpc().spawn(location, displayName, moving, noAttack);
            case RABBIT:
                return nms.getRabbitNpc().spawn(location, displayName, moving, noAttack);
            case SHEEP:
                return nms.getSheepNpc().spawn(location, displayName, moving, noAttack);
            case SILVERFISH:
                return nms.getSilverfishNpc().spawn(location, displayName, moving, noAttack);
            case SKELETON:
                return nms.getSkeletonNpc().spawn(location, displayName, moving, noAttack);
            case SKELETON_HORSE:
                return nms.getSkeletonHorseNpc().spawn(location, displayName, moving, noAttack);
            case SLIME:
                return nms.getSlimeNpc().spawn(location, displayName, moving, noAttack);
            case SNOWMAN:
                return nms.getSnowmanNpc().spawn(location, displayName, moving, noAttack);
            case SPIDER:
                return nms.getSpiderNpc().spawn(location, displayName, moving, noAttack);
            case SQUID:
                return nms.getSquidNpc().spawn(location, displayName, moving, noAttack);
            case STRAY:
                return nms.getStrayNpc().spawn(location, displayName, moving, noAttack);
            case VEX:
                return nms.getVexNpc().spawn(location, displayName, moving, noAttack);
            case VILLAGER:
                return nms.getVillagerNpc().spawn(location, displayName, moving, noAttack);
            case VINDICATOR:
                return nms.getVindicatorNpc().spawn(location, displayName, moving, noAttack);
            case WITCH:
                return nms.getWitchNpc().spawn(location, displayName, moving, noAttack);
            case WITHER:
                return nms.getWitherNpc().spawn(location, displayName, moving, noAttack);
            case WITHER_SKELETON:
                return nms.getWitherSkeletonNpc().spawn(location, displayName, moving, noAttack);
            case WOLF:
                return nms.getWolfNpc().spawn(location, displayName, moving, noAttack);
            case ZOMBIE:
                return nms.getZombieNpc().spawn(location, displayName, moving, noAttack);
            case ZOMBIE_HORSE:
                return nms.getZombieHorseNpc().spawn(location, displayName, moving, noAttack);
            case HUSK:
                return nms.getZombieHuskNpc().spawn(location, displayName, moving, noAttack);
            default:
                return null;
        }
    }

    public static Mob from(short mobId) {
        for (Mob mob : Mob.values()) {
            if (mob.getType().getTypeId() == mobId)
                return mob;
        }
        return null;
    }
}
