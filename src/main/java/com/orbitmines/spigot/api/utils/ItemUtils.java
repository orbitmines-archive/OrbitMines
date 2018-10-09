package com.orbitmines.spigot.api.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class ItemUtils {

    public static boolean isNull(ItemStack item) {
        return isNull(item, false);
    }

    public static boolean isNull(ItemStack item, boolean checkLore){
        return item == null || item.getItemMeta() == null || item.getItemMeta().getDisplayName() == null && (!checkLore || item.getItemMeta().getLore() != null);
    }

    public static final Set<Material> FARM_MATERIALS = new HashSet<>(Arrays.asList(
            Material.BEETROOTS,
//            Material.CACTUS,
            Material.CARROTS,
            Material.CHORUS_FLOWER,
//            Material.FROSTED_ICE,
//            Material.KELP,
            Material.MELON_STEM,
            Material.NETHER_WART,
            Material.POTATOES,
            Material.PUMPKIN_STEM,
            Material.SUGAR_CANE,
            Material.WHEAT
    ));
    
    public static final Set<Material> INTERACTABLE = new HashSet<>(Arrays.asList(
            Material.CHEST,
            Material.ENDER_CHEST, 
            Material.TRAPPED_CHEST, 
            Material.FURNACE, 
            Material.CRAFTING_TABLE, 
            Material.ANVIL, 
            Material.ENCHANTING_TABLE, 
            Material.DISPENSER, 
            Material.HOPPER,
            Material.DROPPER,
            Material.BEACON,

            Material.FIRE,
            
            Material.ACACIA_TRAPDOOR,
            Material.BIRCH_TRAPDOOR,
            Material.DARK_OAK_TRAPDOOR,
            Material.JUNGLE_TRAPDOOR,
            Material.OAK_TRAPDOOR,
            Material.SPRUCE_TRAPDOOR,
            
            Material.LEVER,
            Material.STONE_BUTTON, 
            
            Material.ACACIA_BUTTON, 
            Material.BIRCH_BUTTON, 
            Material.DARK_OAK_BUTTON,
            Material.JUNGLE_BUTTON,
            Material.OAK_BUTTON,
            Material.SPRUCE_BUTTON,
            
            Material.ACACIA_DOOR,
            Material.BIRCH_DOOR,
            Material.DARK_OAK_DOOR,
            Material.JUNGLE_DOOR,
            Material.OAK_DOOR,
            Material.SPRUCE_DOOR,

            Material.ACACIA_FENCE_GATE,
            Material.BIRCH_FENCE_GATE,
            Material.DARK_OAK_FENCE_GATE,
            Material.JUNGLE_FENCE_GATE,
            Material.OAK_FENCE_GATE,
            Material.SPRUCE_FENCE_GATE,
            
            Material.BLACK_BED,
            Material.BLUE_BED,
            Material.BROWN_BED,
            Material.CYAN_BED,
            Material.GRAY_BED,
            Material.GREEN_BED,
            Material.LIGHT_BLUE_BED,
            Material.LIGHT_GRAY_BED,
            Material.LIME_BED,
            Material.MAGENTA_BED,
            Material.ORANGE_BED,
            Material.PINK_BED,
            Material.PURPLE_BED,
            Material.RED_BED,
            Material.WHITE_BED,
            Material.YELLOW_BED,
            
            Material.BREWING_STAND,
            Material.CAKE,
            Material.CAULDRON, 

            Material.COMMAND_BLOCK,
            Material.CHAIN_COMMAND_BLOCK,
            Material.REPEATING_COMMAND_BLOCK,

            Material.END_PORTAL_FRAME,
            Material.FIRE, 
            Material.FLOWER_POT, 
            Material.JUKEBOX, 
            Material.OBSERVER, 
            Material.COMPARATOR,

            Material.RAIL,
            Material.ACTIVATOR_RAIL, 
            Material.DETECTOR_RAIL, 
            Material.POWERED_RAIL,

            Material.SHULKER_BOX,
            Material.BLACK_SHULKER_BOX, 
            Material.BLUE_SHULKER_BOX, 
            Material.BROWN_SHULKER_BOX, 
            Material.CYAN_SHULKER_BOX, 
            Material.GRAY_SHULKER_BOX, 
            Material.GREEN_SHULKER_BOX, 
            Material.LIGHT_BLUE_SHULKER_BOX,
            Material.LIGHT_GRAY_SHULKER_BOX,
            Material.LIME_SHULKER_BOX,
            Material.MAGENTA_SHULKER_BOX,
            Material.ORANGE_SHULKER_BOX, 
            Material.PINK_SHULKER_BOX,
            Material.PURPLE_SHULKER_BOX,
            Material.RED_SHULKER_BOX,
            Material.WHITE_SHULKER_BOX,
            Material.YELLOW_SHULKER_BOX
    ));

    public static final Set<Material> RAILS = new HashSet<>(Arrays.asList(
            Material.RAIL,
            Material.ACTIVATOR_RAIL,
            Material.DETECTOR_RAIL,
            Material.POWERED_RAIL
    ));

    public static final Set<Material> MINECARTS = new HashSet<>(Arrays.asList(
            Material.MINECART,
            Material.CHEST_MINECART,
            Material.COMMAND_BLOCK_MINECART,
            Material.FURNACE_MINECART,
            Material.HOPPER_MINECART,
            Material.TNT_MINECART
    ));

    public static final Set<Material> EGGS = new HashSet<>(Arrays.asList(
            Material.EGG,
            Material.DRAGON_EGG,
            Material.TURTLE_EGG,

            Material.BAT_SPAWN_EGG,
            Material.BLAZE_SPAWN_EGG,
            Material.CAVE_SPIDER_SPAWN_EGG,
            Material.CHICKEN_SPAWN_EGG,
            Material.COD_SPAWN_EGG,
            Material.COW_SPAWN_EGG,
            Material.CREEPER_SPAWN_EGG,
            Material.DOLPHIN_SPAWN_EGG,
            Material.DONKEY_SPAWN_EGG,
            Material.DROWNED_SPAWN_EGG,
            Material.ELDER_GUARDIAN_SPAWN_EGG,
            Material.ENDERMAN_SPAWN_EGG,
            Material.ENDERMITE_SPAWN_EGG,
            Material.EVOKER_SPAWN_EGG,
            Material.GHAST_SPAWN_EGG,
            Material.GUARDIAN_SPAWN_EGG,
            Material.HORSE_SPAWN_EGG,
            Material.HUSK_SPAWN_EGG,
            Material.LLAMA_SPAWN_EGG,
            Material.MAGMA_CUBE_SPAWN_EGG,
            Material.MOOSHROOM_SPAWN_EGG,
            Material.MULE_SPAWN_EGG,
            Material.OCELOT_SPAWN_EGG,
            Material.PARROT_SPAWN_EGG,
            Material.PHANTOM_SPAWN_EGG,
            Material.PIG_SPAWN_EGG,
            Material.POLAR_BEAR_SPAWN_EGG,
            Material.PUFFERFISH_SPAWN_EGG,
            Material.RABBIT_SPAWN_EGG,
            Material.SALMON_SPAWN_EGG,
            Material.SHEEP_SPAWN_EGG,
            Material.SHULKER_SPAWN_EGG,
            Material.SILVERFISH_SPAWN_EGG,
            Material.SKELETON_HORSE_SPAWN_EGG,
            Material.SKELETON_SPAWN_EGG,
            Material.SLIME_SPAWN_EGG,
            Material.SPIDER_SPAWN_EGG,
            Material.SQUID_SPAWN_EGG,
            Material.STRAY_SPAWN_EGG,
            Material.TROPICAL_FISH_SPAWN_EGG,
            Material.TURTLE_SPAWN_EGG,
            Material.VEX_SPAWN_EGG,
            Material.VILLAGER_SPAWN_EGG,
            Material.VINDICATOR_SPAWN_EGG,
            Material.WITCH_SPAWN_EGG,
            Material.WITHER_SKELETON_SPAWN_EGG,
            Material.WOLF_SPAWN_EGG,
            Material.ZOMBIE_HORSE_SPAWN_EGG,
            Material.ZOMBIE_PIGMAN_SPAWN_EGG,
            Material.ZOMBIE_SPAWN_EGG,
            Material.ZOMBIE_VILLAGER_SPAWN_EGG
    ));

    public static Set<Material> PRESSURE_PLATES = new HashSet<>(Arrays.asList(
            Material.HEAVY_WEIGHTED_PRESSURE_PLATE,
            Material.LIGHT_WEIGHTED_PRESSURE_PLATE,

            Material.ACACIA_PRESSURE_PLATE,
            Material.BIRCH_PRESSURE_PLATE,
            Material.DARK_OAK_PRESSURE_PLATE,
            Material.JUNGLE_PRESSURE_PLATE,
            Material.OAK_PRESSURE_PLATE,
            Material.SPRUCE_PRESSURE_PLATE,

            Material.STONE_PRESSURE_PLATE
    ));

    public static Set<Material> BUCKETS = new HashSet<>(Arrays.asList(
            Material.BUCKET,
            Material.LAVA_BUCKET,
            Material.MILK_BUCKET,
            Material.WATER_BUCKET,

            Material.COD_BUCKET,
            Material.PUFFERFISH_BUCKET,
            Material.SALMON_BUCKET,
            Material.TROPICAL_FISH_BUCKET
    ));

    public static Set<Material> WATER_BUCKETS = new HashSet<>(Arrays.asList(
            Material.WATER_BUCKET,

            Material.COD_BUCKET,
            Material.PUFFERFISH_BUCKET,
            Material.SALMON_BUCKET,
            Material.TROPICAL_FISH_BUCKET
    ));

    public static boolean isFarmMaterial(Material material) {
        return FARM_MATERIALS.contains(material);
    }

    public static final Set<PotionEffectType> POSITIVE_EFFECTS = new HashSet<>(Arrays.asList(
            PotionEffectType.SPEED,
            PotionEffectType.FAST_DIGGING,
            PotionEffectType.INCREASE_DAMAGE,
            PotionEffectType.HEAL,
            PotionEffectType.JUMP,
            PotionEffectType.REGENERATION,
            PotionEffectType.DAMAGE_RESISTANCE,
            PotionEffectType.FIRE_RESISTANCE,
            PotionEffectType.WATER_BREATHING,
            PotionEffectType.INVISIBILITY,
            PotionEffectType.NIGHT_VISION,
            PotionEffectType.HEALTH_BOOST,
            PotionEffectType.ABSORPTION,
            PotionEffectType.SATURATION,
            PotionEffectType.GLOWING,
            PotionEffectType.LUCK,
            PotionEffectType.CONDUIT_POWER,
            PotionEffectType.DOLPHINS_GRACE
    ));

    public static final Set<PotionEffectType> NEGATIVE_EFFECTS = new HashSet<>(Arrays.asList(
            PotionEffectType.SLOW,
            PotionEffectType.SLOW_DIGGING,
            PotionEffectType.HARM,
            PotionEffectType.CONFUSION,
            PotionEffectType.BLINDNESS,
            PotionEffectType.HUNGER,
            PotionEffectType.WEAKNESS,
            PotionEffectType.POISON,
            PotionEffectType.WITHER,
            PotionEffectType.LEVITATION,
            PotionEffectType.UNLUCK,
            PotionEffectType.SLOW_FALLING
    ));

    public static String getName(Material material) {
        StringBuilder stringBuilder = new StringBuilder();

        String[] parts = material.toString().split("_");
        for (int i = 0; i < parts.length; i++) {
            if (i != 0)
                stringBuilder.append(" ");

            stringBuilder.append(parts[i].substring(0, 1).toUpperCase());
            stringBuilder.append(parts[i].substring(1).toLowerCase());
        }

        return stringBuilder.toString();
    }

    public static String getName(PotionEffectType effectType) {
        if (effectType == PotionEffectType.SPEED) {
            return "Speed";
        } else if (effectType == PotionEffectType.FAST_DIGGING) {
            return "Haste";
        } else if (effectType == PotionEffectType.INCREASE_DAMAGE) {
            return "Strength";
        } else if (effectType == PotionEffectType.HEAL) {
            return "Instant Health";
        } else if (effectType == PotionEffectType.JUMP) {
            return "Jump Boost";
        } else if (effectType == PotionEffectType.REGENERATION) {
            return "Regeneration";
        } else if (effectType == PotionEffectType.DAMAGE_RESISTANCE) {
            return "Resistance";
        } else if (effectType == PotionEffectType.FIRE_RESISTANCE) {
            return "Fire Resistance";
        } else if (effectType == PotionEffectType.WATER_BREATHING) {
            return "Water Breathing";
        } else if (effectType == PotionEffectType.INVISIBILITY) {
            return "Invisibility";
        } else if (effectType == PotionEffectType.NIGHT_VISION) {
            return "Night Vision";
        } else if (effectType == PotionEffectType.HEALTH_BOOST) {
            return "Health Boost";
        } else if (effectType == PotionEffectType.ABSORPTION) {
            return "Absorption";
        } else if (effectType == PotionEffectType.SATURATION) {
            return "Saturation";
        } else if (effectType == PotionEffectType.GLOWING) {
            return "Glowing";
        } else if (effectType == PotionEffectType.LUCK) {
            return "Luck";
        } else if (effectType == PotionEffectType.CONDUIT_POWER) {
            return "Conduit Power";
        } else if (effectType == PotionEffectType.DOLPHINS_GRACE) {
            return "Dolphins Grace";
        } else if (effectType == PotionEffectType.SLOW) {
            return "Slowness";
        } else if (effectType == PotionEffectType.SLOW_DIGGING) {
            return "Mining Fatigue";
        } else if (effectType == PotionEffectType.HARM) {
            return "Harming";
        } else if (effectType == PotionEffectType.CONFUSION) {
            return "Nausea";
        } else if (effectType == PotionEffectType.BLINDNESS) {
            return "Blindness";
        } else if (effectType == PotionEffectType.HUNGER) {
            return "Hunger";
        } else if (effectType == PotionEffectType.WEAKNESS) {
            return "Weakness";
        } else if (effectType == PotionEffectType.POISON) {
            return "Poison";
        } else if (effectType == PotionEffectType.WITHER) {
            return "Wither";
        } else if (effectType == PotionEffectType.LEVITATION) {
            return "Levitation";
        } else if (effectType == PotionEffectType.UNLUCK) {
            return "Bad Luck";
        } else if (effectType == PotionEffectType.SLOW_FALLING) {
            return "Slow Falling";
        } else {
            return null;
        }
    }
}
