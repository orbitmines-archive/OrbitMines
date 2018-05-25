package com.orbitmines.spigot.api;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.nms.Nms;
import com.orbitmines.spigot.api.nms.npc.MobNpcNms;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public enum Mob {

    //v1_8_R1
    BAT(EntityType.BAT, "Bat"),
    BLAZE(EntityType.BLAZE, "Blaze"),
    CAVE_SPIDER(EntityType.CAVE_SPIDER, "Cave Spider"),
    CHICKEN(EntityType.CHICKEN, "Chicken"),
    COW(EntityType.COW, "Cow"),
    CREEPER(EntityType.CREEPER, "Creeper"),
    ENDER_DRAGON(EntityType.ENDER_DRAGON, "Ender Dragon"),
    ENDERMAN(EntityType.ENDERMAN, "Enderman"),
    ENDERMITE(EntityType.ENDERMITE, "Endermite"),
    GHAST(EntityType.GHAST, "Ghast"),
    GIANT(EntityType.GIANT, "Giant"),//TODO nms
    GUARDIAN(EntityType.GUARDIAN, "Guardian"),
    HORSE(EntityType.HORSE, "Horse"),
    IRON_GOLEM(EntityType.IRON_GOLEM, "Iron Golem"),
    MAGMA_CUBE(EntityType.MAGMA_CUBE, "Magma Cube"),
    MUSHROOM_COW(EntityType.MUSHROOM_COW, "Mushroom Cow"),
    OCELOT(EntityType.OCELOT, "Ocelot"),
    PIG(EntityType.PIG, "Pig"),
    PIG_ZOMBIE(EntityType.PIG_ZOMBIE, "Zombie Pigman"),
    RABBIT(EntityType.RABBIT, "Rabbit"),
    SHEEP(EntityType.SHEEP, "Sheep"),
    SILVERFISH(EntityType.SILVERFISH, "Silverfish"),
    SKELETON(EntityType.SKELETON, "Skeleton"),
    SLIME(EntityType.SLIME, "Slime"),
    SNOWMAN(EntityType.SNOWMAN, "Snowman"),
    SPIDER(EntityType.SPIDER, "Spider"),
    SQUID(EntityType.SQUID, "Squid"),
    VILLAGER(EntityType.VILLAGER, "Villager"),
    WITCH(EntityType.WITCH, "Witch"),
    WITHER(EntityType.WITHER, "Wither"),
    WOLF(EntityType.WOLF, "Wolf"),
    ZOMBIE(EntityType.ZOMBIE, "Zombie"),

    //TODO v1_10_R1
    POLAR_BEAR(EntityType.POLAR_BEAR, "Polar Bear"),

    //TODO v1_11_R1
    DONKEY(EntityType.DONKEY, "Donkey"),//TODO nms
    ELDER_GUARDIAN(EntityType.ELDER_GUARDIAN, "Elder Guardian"),//TODO nms (id; 4)
    EVOKER(EntityType.EVOKER, "Evoker"),
    HUSK(EntityType.HUSK, "Husk"),
    ILLUSIONER(EntityType.ILLUSIONER, "Illusioner"), //TODO nms (id; 37)
    LLAMA(EntityType.LLAMA, "Llama"),
    MULE(EntityType.MULE, "Mule"),
    SKELETON_HORSE(EntityType.SKELETON_HORSE, "Skeleton Horse"),
    STRAY(EntityType.STRAY, "Stray"),
    VEX(EntityType.VEX, "Vex"),
    VINDICATOR(EntityType.VINDICATOR, "Vindicator"),
    WITHER_SKELETON(EntityType.WITHER_SKELETON, "Wither Skeleton"),
    ZOMBIE_HORSE(EntityType.ZOMBIE_HORSE, "Zombie Horse"),
    ZOMBIE_VILLAGER(EntityType.ZOMBIE_VILLAGER, "Zombie Villager"),//TODO nms
    //TODO ALL THESE horses etc, for 1.8-1.10 in another way.

    //TODO v1_12_R1
    PARROT(EntityType.PARROT, "Parrot"), //TODO nms (id; 105)

    ;//TODO EVOKER FANGS?, SHULKER?

    private final EntityType type;
    private final String name;

    Mob(EntityType type, String name) {
        this.type = type;
        this.name = name;
    }

    public EntityType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Entity spawn(Location location, MobNpcNms.Option... optionsArray) {
        Nms nms = OrbitMines.getInstance().getNms();
        List<MobNpcNms.Option> options = Arrays.asList(optionsArray);

        switch (this) {

            case BAT:
                break;
            case BLAZE:
                break;
            case CAVE_SPIDER:
                break;
            case CHICKEN:
                return nms.getChickenNpc().spawn(location, options);
            case COW:
                break;
            case CREEPER:
                break;
            case ENDER_DRAGON:
                break;
            case ENDERMAN:
                break;
            case ENDERMITE:
                break;
            case GHAST:
                break;
            case GIANT:
                break;
            case GUARDIAN:
                break;
            case HORSE:
                break;
            case IRON_GOLEM:
                break;
            case MAGMA_CUBE:
                break;
            case MUSHROOM_COW:
                break;
            case OCELOT:
                break;
            case PIG:
                break;
            case PIG_ZOMBIE:
                break;
            case RABBIT:
                break;
            case SHEEP:
                break;
            case SILVERFISH:
                break;
            case SKELETON:
                break;
            case SLIME:
                break;
            case SNOWMAN:
                break;
            case SPIDER:
                break;
            case SQUID:
                break;
            case VILLAGER:
                break;
            case WITCH:
                break;
            case WITHER:
                break;
            case WOLF:
                break;
            case ZOMBIE:
                break;
            case POLAR_BEAR:
                break;
            case DONKEY:
                break;
            case ELDER_GUARDIAN:
                break;
            case EVOKER:
                break;
            case HUSK:
                break;
            case ILLUSIONER:
                break;
            case LLAMA:
                break;
            case MULE:
                break;
            case SKELETON_HORSE:
                break;
            case STRAY:
                break;
            case VEX:
                break;
            case VINDICATOR:
                break;
            case WITHER_SKELETON:
                break;
            case ZOMBIE_HORSE:
                break;
            case ZOMBIE_VILLAGER:
                break;
            case PARROT:
                break;
        }
        throw new IllegalArgumentException();
    }

    public Entity spawnRideable(Location location, float speed, float backMultiplier, float sideMultiplier, float walkHeight, float jumpHeight) {
        Nms nms = OrbitMines.getInstance().getNms();

        //TODO RIDEABLE ALSO COMBATMODE?
        switch (this) {

            case BAT:
                break;
            case BLAZE:
                break;
            case CAVE_SPIDER:
                break;
            case CHICKEN:
                return nms.getChickenNpc().spawnRideable(location, speed, backMultiplier, sideMultiplier, walkHeight, jumpHeight);
            case COW:
                break;
            case CREEPER:
                break;
            case ENDER_DRAGON:
                break;
            case ENDERMAN:
                break;
            case ENDERMITE:
                break;
            case GHAST:
                break;
            case GIANT:
                break;
            case GUARDIAN:
                break;
            case HORSE:
                break;
            case IRON_GOLEM:
                break;
            case MAGMA_CUBE:
                break;
            case MUSHROOM_COW:
                break;
            case OCELOT:
                break;
            case PIG:
                break;
            case PIG_ZOMBIE:
                break;
            case RABBIT:
                break;
            case SHEEP:
                break;
            case SILVERFISH:
                break;
            case SKELETON:
                break;
            case SLIME:
                break;
            case SNOWMAN:
                break;
            case SPIDER:
                break;
            case SQUID:
                break;
            case VILLAGER:
                break;
            case WITCH:
                break;
            case WITHER:
                break;
            case WOLF:
                break;
            case ZOMBIE:
                break;
            case POLAR_BEAR:
                break;
            case DONKEY:
                break;
            case ELDER_GUARDIAN:
                break;
            case EVOKER:
                break;
            case HUSK:
                break;
            case ILLUSIONER:
                break;
            case LLAMA:
                break;
            case MULE:
                break;
            case SKELETON_HORSE:
                break;
            case STRAY:
                break;
            case VEX:
                break;
            case VINDICATOR:
                break;
            case WITHER_SKELETON:
                break;
            case ZOMBIE_HORSE:
                break;
            case ZOMBIE_VILLAGER:
                break;
            case PARROT:
                break;
        }
        throw new IllegalArgumentException();
    }
}
