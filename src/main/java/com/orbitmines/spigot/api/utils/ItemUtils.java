package com.orbitmines.spigot.api.utils;

import com.orbitmines.spigot.api.Mob;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class ItemUtils {

    public static int FLAG_ENCHANTMENTS = 1;
    public static int FLAG_ATTRIBUTES_MODIFIERS = 2;
    public static int FLAG_UNBREAKABLE = 4;
    public static int FLAG_CAN_DESTROY = 8;
    public static int FLAG_CAN_PLACE_ON = 16;
    public static int FLAG_POTIONS = 32;

    public static boolean isNull(ItemStack item) {
        return isNull(item, false);
    }

    public static boolean isNull(ItemStack item, boolean checkLore){
        return item == null || item.getItemMeta() == null || item.getItemMeta().getDisplayName() == null && (!checkLore || item.getItemMeta().getLore() != null);
    }

    private static final List<Material> FARM_MATERIALS = Arrays.asList(
            Material.PUMPKIN_STEM,
            Material.CROPS,
            Material.MELON_STEM,
            Material.CARROT,
            Material.POTATO,
            Material.NETHER_WARTS,
            Material.BEETROOT_BLOCK
    );

    public static boolean isFarmMaterial(Material material) {
        return FARM_MATERIALS.contains(material);
    }

    public static final Set<PotionEffectType> POSITIVE_EFFECTS = new HashSet<>(Arrays.asList(
                    PotionEffectType.ABSORPTION,
                    PotionEffectType.DAMAGE_RESISTANCE,
                    PotionEffectType.FAST_DIGGING,
                    PotionEffectType.FIRE_RESISTANCE,
                    PotionEffectType.HEAL,
                    PotionEffectType.HEALTH_BOOST,
                    PotionEffectType.INCREASE_DAMAGE,
                    PotionEffectType.INVISIBILITY,
                    PotionEffectType.JUMP,
                    PotionEffectType.NIGHT_VISION,
                    PotionEffectType.REGENERATION,
                    PotionEffectType.SATURATION,
                    PotionEffectType.SPEED,
                    PotionEffectType.WATER_BREATHING
            ));

    public static String getName(Material material, short durability, boolean max16) {
        switch (material) {

            case AIR:
                return "Air";
            case STONE:
                switch (durability) {
                    case 1:
                        return "Granite";
                    case 2:
                        return "Polished Granite";
                    case 3:
                        return "Diorite";
                    case 4:
                        return "Polished Diorite";
                    case 5:
                        return "Andesite";
                    case 6:
                        return max16 ? "PolishedAndesite" : "Polished Andesite";
                    default:
                        return "Stone";
                }
            case GRASS:
                return "Grass";
            case DIRT: switch (durability) {
                case 1:
                    return "Coarse Dirt";
                case 2:
                    return "Podzol";
                default:
                    return "Grass";
                }
            case COBBLESTONE:
                return "Cobblestone";
            case WOOD:
                switch (durability) {
                    case 1:
                        return max16 ? "Spruce WoodPlank" : "Spruce Wood Plank";
                    case 2:
                        return "Birch Wood Plank";
                    case 3:
                        return max16 ? "Jungle WoodPlank" : "Jungle Wood Plank";
                    case 4:
                        return max16 ? "Acacia WoodPlank" : "Acacia Wood Plank";
                    case 5:
                        return max16 ? "DarkOakWoodPlank" : "Dark Oak Wood Plank";
                    default:
                        return "Oak Wood Plank";
                }
            case SAPLING:
                switch (durability) {
                    case 1:
                        return "Spruce Sapling";
                    case 2:
                        return "Birch Sapling";
                    case 3:
                        return "Jungle Sapling";
                    case 4:
                        return "Acacia Sapling";
                    case 5:
                        return "Dark Oak Sapling";
                    default:
                        return "Oak Sapling";
                }
            case BEDROCK:
                return "Bedrock";
            case WATER:
                return "Flowing Water";
            case STATIONARY_WATER:
                return "Water";
            case LAVA:
                return "Flowing Lava";
            case STATIONARY_LAVA:
                return "Lava";
            case SAND:
                switch (durability) {
                    case 1:
                        return "Red Sand";
                    default:
                        return "Sand";
                }
            case GRAVEL:
                return "Gravel";
            case GOLD_ORE:
                return "Gold Ore";
            case IRON_ORE:
                return "Iron Ore";
            case COAL_ORE:
                return "Coal Ore";
            case LOG:
                switch (durability) {
                    case 1:
                        return "Spruce Wood";
                    case 2:
                        return "Birch Wood";
                    case 3:
                        return "Jungle Wood";
                    default:
                        return "Oak Wood";
                }
            case LEAVES:
                switch (durability) {
                    case 1:
                        return "Spruce Leaves";
                    case 2:
                        return "Birch Leaves";
                    case 3:
                        return "Jungle Leaves";
                    default:
                        return "Oak Leaves";
                }
            case SPONGE:
                switch (durability) {
                    case 1:
                        return "Wet Sponge";
                    default:
                        return "Sponge";
                }
            case GLASS:
                return "Glass";
            case LAPIS_ORE:
                return "Lapis Lazuli Ore";
            case LAPIS_BLOCK:
                return max16 ? "LapisLazuliBlock" : "Lapis Lazuli Block";
            case DISPENSER:
                return "Dispenser";
            case SANDSTONE:
                switch (durability) {
                    case 1:
                        return max16 ? "Chis. Sandstone" : "Chiseled Sandstone";
                    case 2:
                        return "Smooth Sandstone";
                    default:
                        return "Sandstone";
                }
            case NOTE_BLOCK:
                return "Note Block";
            case BED_BLOCK:
                return "Bed";
            case POWERED_RAIL:
                return "Powered Rail";
            case DETECTOR_RAIL:
                return "Detector Rail";
            case PISTON_STICKY_BASE:
                return "Sticky Piston";
            case WEB:
                return "Cobweb";
            case LONG_GRASS:
                switch (durability) {
                    case 1:
                        return "Grass";
                    case 2:
                        return "Fern";
                    default:
                        return "Dead Shrub";
                }
            case DEAD_BUSH:
                return "Dead Bush";
            case PISTON_BASE:
                return "Prison";
            case PISTON_EXTENSION:
                return "Piston Head";
            case WOOL:
                switch (durability) {
                    case 1:
                        return "Orange Wool";
                    case 2:
                        return "Magenta Wool";
                    case 3:
                        return "Light Blue Wool";
                    case 4:
                        return "Yellow Wool";
                    case 5:
                        return "Lime Wool";
                    case 6:
                        return "Pink Wool";
                    case 7:
                        return "Gray Wool";
                    case 8:
                        return "Light Gray Wool";
                    case 9:
                        return "Cyan Wool";
                    case 10:
                        return "Purple Wool";
                    case 11:
                        return "Blue Wool";
                    case 12:
                        return "Brown Wool";
                    case 13:
                        return "Green Wool";
                    case 14:
                        return "Red Wool";
                    case 15:
                        return "Black Wool";
                    default:
                        return "White Wool";
                }
            case PISTON_MOVING_PIECE:
                return "Piston Piece";
            case YELLOW_FLOWER:
                return "Dandelion";
            case RED_ROSE:
                switch (durability) {
                    case 1:
                        return "Blue Orchid";
                    case 2:
                        return "Allium";
                    case 3:
                        return "Azure Bluet";
                    case 4:
                        return "Red Tulip";
                    case 5:
                        return "Orange Tulip";
                    case 6:
                        return "White Tulip";
                    case 7:
                        return "Pink Tulip";
                    case 8:
                        return "Oxeye Daisy";
                    default:
                        return "Poppy";
                }
            case BROWN_MUSHROOM:
                return "Brown Mushroom";
            case RED_MUSHROOM:
                return "Red Mushrrom";
            case GOLD_BLOCK:
                return "Gold Block";
            case IRON_BLOCK:
                return "Iron Block";
            case DOUBLE_STEP:
                switch (durability) {
                    case 1:
                        return max16 ? "DoubleSandstSlab" : "Double Sandstone Slab";
                    case 2:
                        return max16 ? "DoubleWoodenSlab" : "Double Wooden Slab";
                    case 3:
                        return max16 ? "DoubleCobbleSlab" : "Double Cobblestone Slab";
                    case 4:
                        return max16 ? "Double BrickSlab" : "Double Brick Slab";
                    case 5:
                        return max16 ? "DoubleStoneBSlab" : "Double Stone Brick Slab";
                    case 6:
                        return max16 ? "DoubleNetherSlab" : "Double Nether Brick Slab";
                    case 7:
                        return max16 ? "DoubleQuartzSlab" : "Double Quartz Slab";
                    default:
                        return max16 ? "Double StoneSlab" : "Double Stone Slab";
                }
            case STEP:
                switch (durability) {
                    case 1:
                        return "Sandstone Slab";
                    case 2:
                        return "Sandstone Slab";
                    case 3:
                        return "Cobblestone Slab";
                    case 4:
                        return "Brick Slab";
                    case 5:
                        return "Stone Brick Slab";
                    case 6:
                        return max16 ? "NetherBrick Slab" : "Nether Brick Slab";
                    case 7:
                        return "Quartz Slab";
                    default:
                        return "Stone Slab";
                }
            case BRICK:
                return "Bricks";
            case TNT:
                return "TNT";
            case BOOKSHELF:
                return "Bookshelf";
            case MOSSY_COBBLESTONE:
                return "Moss Stone";
            case OBSIDIAN:
                return "Obsidian";
            case TORCH:
                return "Torch";
            case FIRE:
                return "Fire";
            case MOB_SPAWNER:
                return "Monster Spawner";
            case WOOD_STAIRS:
                return "Oak Wood Stairs";
            case CHEST:
                return "Chest";
            case REDSTONE_WIRE:
                return "Redstone Wire";
            case DIAMOND_ORE:
                return "Diamond Ore";
            case DIAMOND_BLOCK:
                return "Diamond Block";
            case WORKBENCH:
                return "Crafting Table";
            case CROPS:
                return "Wheat Crops";
            case SOIL:
                return "Farmland";
            case FURNACE:
                return "Furnace";
            case BURNING_FURNACE:
                return "Burning Furnace";
            case SIGN_POST:
                return max16 ? "Standing Sign" : "Standing Sign Block";
            case WOODEN_DOOR:
                return "Oak Door Block";
            case LADDER:
                return "Ladder";
            case RAILS:
                return "Rail";
            case COBBLESTONE_STAIRS:
                return max16 ? "Cobble Stairs" : "Cobblestone Stairs";
            case WALL_SIGN:
                return "Wall Sign Block";
            case LEVER:
                return "Lever";
            case STONE_PLATE:
                return max16 ? "Pressure Plate" : "Stone Pressure Plate";
            case IRON_DOOR_BLOCK:
                return "Iron Door Block";
            case WOOD_PLATE:
                return max16 ? "Pressure Plate" : "Wooden Pressure Plate";
            case REDSTONE_ORE:
                return "Redstone Ore";
            case GLOWING_REDSTONE_ORE:
                return max16 ? "Redstone Ore" : "Glowing Redtone Ore";
            case REDSTONE_TORCH_OFF:
                return "Redstone Torch";
            case REDSTONE_TORCH_ON:
                return "Redstone Torch";
            case STONE_BUTTON:
                return "Stone Button";
            case SNOW:
                return "Snow";
            case ICE:
                return "Ice";
            case SNOW_BLOCK:
                return "Snow Block";
            case CACTUS:
                return "Cactus";
            case CLAY:
                return "Clay";
            case SUGAR_CANE_BLOCK:
                return "Sugar Canes";
            case JUKEBOX:
                return "Jukebox";
            case FENCE:
                return "Oak Fence";
            case PUMPKIN:
                return "Pumpkin";
            case NETHERRACK:
                return "Netherrack";
            case SOUL_SAND:
                return "Soul Sand";
            case GLOWSTONE:
                return "Glowstone";
            case PORTAL:
                return "Nether Portal";
            case JACK_O_LANTERN:
                return "Jack o'Lantern";
            case CAKE_BLOCK:
                return "Cake Block";
            case DIODE_BLOCK_OFF:
                return "RedstoneRepeater";
            case DIODE_BLOCK_ON:
                return "RedstoneRepeater";
            case STAINED_GLASS:
                switch (durability) {
                    case 1:
                        return max16 ? "Orange Glass" : "Orange Stained Glass";
                    case 2:
                        return max16 ? "Magenta Glass" : "Magenta Stained Glass";
                    case 3:
                        return max16 ? "Light Blue Glass" : "Light Blue Stained Glass";
                    case 4:
                        return max16 ? "Yellow Glass" : "Yellow Stained Glass";
                    case 5:
                        return max16 ? "Lime Glass" : "Lime Stained Glass";
                    case 6:
                        return max16 ? "Pink Glass" : "Pink Stained Glass";
                    case 7:
                        return max16 ? "Gray Glass" : "Gray Stained Glass";
                    case 8:
                        return max16 ? "Light Gray Glass" : "Light Gray Stained Glass";
                    case 9:
                        return max16 ? "Cyan Glass" : "Cyan Stained Glass";
                    case 10:
                        return max16 ? "Purple Glass" : "Purple Stained Glass";
                    case 11:
                        return max16 ? "Blue Glass" : "Blue Stained Glass";
                    case 12:
                        return max16 ? "Brown Glass" : "Brown Stained Glass";
                    case 13:
                        return max16 ? "Green Glass" : "Green Stained Glass";
                    case 14:
                        return max16 ? "Red Glass" : "Red Stained Glass";
                    case 15:
                        return max16 ? "Black Glass" : "Black Stained Glass";
                    default:
                        return max16 ? "White Glass" : "White Stained Glass";
                }
            case TRAP_DOOR:
                return "Wooden Trapdoor";
            case MONSTER_EGGS:
                switch (durability) {
                    case 1:
                        return max16 ? "Cobblestone Egg" : "Cobblestone Monster Egg";
                    case 2:
                        return max16 ? "Stone Brick Egg" : "Stone Brick Monster Egg";
                    case 3:
                        return max16 ? "Mossy Stone Egg" : "Mossy Stone Brick Monster Egg";
                    case 4:
                        return max16 ? "CrackedStone Egg" : "Cracked Stone Brick Monster Egg";
                    case 5:
                        return max16 ? "Chis. Stone Egg" : "Chiseled Stone Brick Monster Egg";
                    default:
                        return max16 ? "Stone Egg" : "Stone Monster Egg";
                }
            case SMOOTH_BRICK:
                switch (durability) {
                    case 1:
                        return max16 ? "MossyStoneBricks" : "Mossy Stone Bricks";
                    case 2:
                        return max16 ? "CrackStoneBricks" : "Cracked Stone Bricks";
                    case 3:
                        return max16 ? "Chis.StoneBricks" : "Chiseled Stone Bricks";
                    default:
                        return "Stone Bricks";
                }
            case HUGE_MUSHROOM_1:
                return max16 ? "Mushroom Block" : "Brown Mushroom Block";
            case HUGE_MUSHROOM_2:
                return max16 ? "Mushroom Block" : "Red Mushroom Block";
            case IRON_FENCE:
                return "Iron Bars";
            case THIN_GLASS:
                return "Glass Pane";
            case MELON_BLOCK:
                return "Melon Block";
            case PUMPKIN_STEM:
                return "Pumpkin Stem";
            case MELON_STEM:
                return "Melon Stem";
            case VINE:
                return "Vines";
            case FENCE_GATE:
                return "Oak Fence Gate";
            case BRICK_STAIRS:
                return "Brick Stairs";
            case SMOOTH_STAIRS:
                return max16 ? "StoneBrickStairs" : "Stone Brick Stairs";
            case MYCEL:
                return "Mycelium";
            case WATER_LILY:
                return "Lily Pad";
            case NETHER_BRICK:
                return "Nether Brick";
            case NETHER_FENCE:
                return max16 ? "NetherBrickFence" : "Nether Brick Fence";
            case NETHER_BRICK_STAIRS:
                return max16 ? "NetherBrickStair" : "Nether Brick Stairs";
            case NETHER_WARTS:
                return "Nether Wart";
            case ENCHANTMENT_TABLE:
                return max16 ? "EnchantmentTable" : "Enchantment Table";
            case BREWING_STAND:
                return "Brewing Stand";
            case CAULDRON:
                return "Cauldron";
            case ENDER_PORTAL:
                return "End Portal";
            case ENDER_PORTAL_FRAME:
                return "End Portal Frame";
            case ENDER_STONE:
                return "End Stone";
            case DRAGON_EGG:
                return "Dragon Egg";
            case REDSTONE_LAMP_OFF:
                return "Redstone Lamp";
            case REDSTONE_LAMP_ON:
                return "Redstone Lamp";
            case WOOD_DOUBLE_STEP:
                switch (durability) {
                    case 1:
                        return max16 ? "DoubleSpruceSlab" : "Double Spruce Wood Slab";
                    case 2:
                        return max16 ? "DoubleBirchSlab" : "Double Birch Wood Slab";
                    case 3:
                        return max16 ? "DoubleJungleSlab" : "Double Jungle Wood Slab";
                    case 4:
                        return max16 ? "DoubleAcaciaSlab" : "Double Acacia Wood Slab";
                    case 5:
                        return max16 ? "Double Dark Slab" : "Double Dark Oak Wood Slab";
                    default:
                        return max16 ? "Double Oak Slab" : "Double Oak Wood Slab";
                }
            case WOOD_STEP:
                switch (durability) {
                    case 1:
                        return max16 ? "Spruce Slab" : "Spruce Wood Slab";
                    case 2:
                        return max16 ? "Birch Slab" : "Birch Wood Slab";
                    case 3:
                        return max16 ? "Jungle Slab" : "Jungle Wood Slab";
                    case 4:
                        return max16 ? "Acacia Slab" : "Acacia Wood Slab";
                    case 5:
                        return max16 ? "Dark Oak Slab" : "Dark Oak Wood Slab";
                    default:
                        return "Oak Wood Slab";
                }
            case COCOA:
                return "Cocoa";
            case SANDSTONE_STAIRS:
                return "Sandstone Stairs";
            case EMERALD_ORE:
                return "Emerald Ore";
            case ENDER_CHEST:
                return "Ender Chest";
            case TRIPWIRE_HOOK:
                return "Tripwire Hook";
            case TRIPWIRE:
                return "Tripwire";
            case EMERALD_BLOCK:
                return "Emerald Block";
            case SPRUCE_WOOD_STAIRS:
                return max16 ? "SpruceWoodStairs" : "Spruce Wood Stairs";
            case BIRCH_WOOD_STAIRS:
                return max16 ? "BirchWoodStairs" : "Birch Wood Stairs";
            case JUNGLE_WOOD_STAIRS:
                return max16 ? "JungleWoodStairs" : "Jungle Wood Stairs";
            case COMMAND:
                return "Command Block";
            case BEACON:
                return "Beacon";
            case COBBLE_WALL:
                switch (durability) {
                    case 1:
                        return max16 ? "MossyCobble Wall" : "Mossy Cobblestone Wall";
                    default:
                        return "Cobblestone Wall";
                }
            case FLOWER_POT:
                return "Flower Pot";
            case CARROT:
                return "Carrots";
            case POTATO:
                return "Potatoes";
            case WOOD_BUTTON:
                return "Wooden Button";
            case SKULL:
                return "Mob Head";
            case ANVIL:
                return "Anvil";
            case TRAPPED_CHEST:
                return "Trapped Chest";
            case GOLD_PLATE:
                return max16 ? "Pressure Plate" : "Golden Pressure Plate";
            case IRON_PLATE:
                return max16 ? "Pressure Plate" : "Iron Pressure Plate";
            case REDSTONE_COMPARATOR_OFF:
                return max16 ? "Redst.Comparator" : "Redstone Comparator";
            case REDSTONE_COMPARATOR_ON:
                return max16 ? "Redst.Comparator" : "Redstone Comparator";
            case DAYLIGHT_DETECTOR:
                return "Daylight Sensor";
            case REDSTONE_BLOCK:
                return "Redstone Block";
            case QUARTZ_ORE:
                return "NetherQuartz Ore";
            case HOPPER:
                return "Hopper";
            case QUARTZ_BLOCK:
                switch (durability) {
                    case 1:
                        return max16 ? "Chis.QuartzBlock" : "Chiseled Quartz Block";
                    case 2:
                        return max16 ? "Pillar Block" : "Pillar Quartz Block";
                    default:
                        return "Quartz Block";
                }
            case QUARTZ_STAIRS:
                return "Quartz Stairs";
            case ACTIVATOR_RAIL:
                return "Activator Rail";
            case DROPPER:
                return "Dropper";
            case STAINED_CLAY:
                switch (durability) {
                    case 1:
                        return max16 ? "Orange Clay" : "Orange Hardened Clay";
                    case 2:
                        return max16 ? "Magenta Clay" : "Magenta Hardened Clay";
                    case 3:
                        return max16 ? "Light Blue Clay" : "Light Blue Hardened Clay";
                    case 4:
                        return max16 ? "Yellow Clay" : "Yellow Hardened Clay";
                    case 5:
                        return max16 ? "Lime Clay" : "Lime Hardened Clay";
                    case 6:
                        return max16 ? "Pink Clay" : "Pink Hardened Clay";
                    case 7:
                        return max16 ? "Gray Clay" : "Gray Hardened Clay";
                    case 8:
                        return max16 ? "Light Gray Clay" : "Light Gray Hardened Clay";
                    case 9:
                        return max16 ? "Cyan Clay" : "Cyan Hardened Clay";
                    case 10:
                        return max16 ? "Purple Clay" : "Purple Hardened Clay";
                    case 11:
                        return max16 ? "Blue Clay" : "Blue Hardened Clay";
                    case 12:
                        return max16 ? "Brown Clay" : "Brown Hardened Clay";
                    case 13:
                        return max16 ? "Green Clay" : "Green Hardened Clay";
                    case 14:
                        return max16 ? "Red Clay" : "Red Hardened Clay";
                    case 15:
                        return max16 ? "Black Clay" : "Black Hardened Clay";
                    default:
                        return max16 ? "White Clay" : "White Hardened Clay";
                }
            case STAINED_GLASS_PANE:
                switch (durability) {
                    case 1:
                        return max16 ? "Orange Pane" : "Orange Stained Glass Pane";
                    case 2:
                        return max16 ? "Magenta Pane" : "Magenta Stained Glass Pane";
                    case 3:
                        return max16 ? "Light Blue Pane" : "Light Blue Stained Glass Pane";
                    case 4:
                        return max16 ? "Yellow Pane" : "Yellow Stained Glass Pane";
                    case 5:
                        return max16 ? "Lime Pane" : "Lime Stained Glass Pane";
                    case 6:
                        return max16 ? "Pink Pane" : "Pink Stained Glass Pane";
                    case 7:
                        return max16 ? "Gray Pane" : "Gray Stained Glass Pane";
                    case 8:
                        return max16 ? "Light Gray Pane" : "Light Gray Stained Glass Pane";
                    case 9:
                        return max16 ? "Cyan Pane" : "Cyan Stained Glass Pane";
                    case 10:
                        return max16 ? "Purple Pane" : "Purple Stained Glass Pane";
                    case 11:
                        return max16 ? "Blue Pane" : "Blue Stained Glass Pane";
                    case 12:
                        return max16 ? "Brown Pane" : "Brown Stained Glass Pane";
                    case 13:
                        return max16 ? "Green Pane" : "Green Stained Glass Pane";
                    case 14:
                        return max16 ? "Red Pane" : "Red Stained Glass Pane";
                    case 15:
                        return max16 ? "Black Pane" : "Black Stained Glass Pane";
                    default:
                        return max16 ? "White Pane" : "White Stained Glass Pane";
                }
            case LEAVES_2:
                switch (durability) {
                    case 1:
                        return "Dark Oak Leaves";
                    default:
                        return "Acacia Leaves";
                }
            case LOG_2:
                switch (durability) {
                    case 1:
                        return "Dark Oak Wood";
                    default:
                        return "Acacia Wood";
                }
            case ACACIA_STAIRS:
                return max16 ? "AcaciaWoodStairs" : "Acacia Wood Stairs";
            case DARK_OAK_STAIRS:
                return max16 ? "Dark Oak Stairs" : "Dark Oak Wood Stairs";
            case SLIME_BLOCK:
                return "Slime Block";
            case BARRIER:
                return "Barrier";
            case IRON_TRAPDOOR:
                return "Iron Trapdoor";
            case PRISMARINE:
                switch (durability) {
                    case 1:
                        return max16 ? "PrismarineBricks" : "Prismarine Bricks";
                    default:
                        return "Prismarine";
                }
            case SEA_LANTERN:
                return "Sea Lantern";
            case HAY_BLOCK:
                return "Hay Bale";
            case CARPET:
                switch (durability) {
                    case 1:
                        return "Orange Carpet";
                    case 2:
                        return "Magenta Carpet";
                    case 3:
                        return max16 ? "LightBlue Carpet" : "Light Blue Carpet";
                    case 4:
                        return "Yellow Carpet";
                    case 5:
                        return "Lime Carpet";
                    case 6:
                        return "Pink Carpet";
                    case 7:
                        return "Gray Carpet";
                    case 8:
                        return max16 ? "LightGray Carpet" : "Light Gray Carpet";
                    case 9:
                        return "Cyan Carpet";
                    case 10:
                        return "Purple Carpet";
                    case 11:
                        return "Blue Carpet";
                    case 12:
                        return "Brown Carpet";
                    case 13:
                        return "Green Carpet";
                    case 14:
                        return "Red Carpet";
                    case 15:
                        return "Black Carpet";
                    default:
                        return "White Carpet";
                }
            case HARD_CLAY:
                return "Hardened Clay";
            case COAL_BLOCK:
                return "Block of Coal";
            case PACKED_ICE:
                return "Packed Ice";
            case DOUBLE_PLANT:
                switch (durability) {
                    case 1:
                        return "Lilac";
                    case 2:
                        return "Double Tallgrass";
                    case 3:
                        return "Large Fern";
                    case 4:
                        return "Rose Bush";
                    case 5:
                        return "Peony";
                    default:
                        return "Sunflower";
                }
            case STANDING_BANNER:
                return "Banner";
            case WALL_BANNER:
                return "Banner";
            case DAYLIGHT_DETECTOR_INVERTED:
                return "Daylight Sensor";
            case RED_SANDSTONE:
                switch (durability) {
                    case 1:
                        return max16 ? "ChisRedSandstone" : "Chiseled Red Sandstone";
                    case 2:
                        return max16 ? "Smooth Red Sand" : "Smooth Red Sandstone";
                    default:
                        return "Red Sandstone";
                }
            case RED_SANDSTONE_STAIRS:
                return max16 ? "Red Sand Stairs" : "Red Sandstone Stairs";
            case DOUBLE_STONE_SLAB2:
                return max16 ? "Double Red Slab" : "Double Red Sandstone Slab";
            case STONE_SLAB2:
                return max16 ? "RedSandstoneSlab" : "Red Sandstone Slab";
            case SPRUCE_FENCE_GATE:
                return max16 ? "Spruce FenceGate" : "Spruce Fence Gate";
            case BIRCH_FENCE_GATE:
                return "Birch Fence Gate";
            case JUNGLE_FENCE_GATE:
                return max16 ? "Jungle FenceGate" : "Jungle Fence Gate";
            case DARK_OAK_FENCE_GATE:
                return max16 ? "Dark Fence Gate" : "Dark Oak Fence Gate";
            case ACACIA_FENCE_GATE:
                return max16 ? "Acacia FenceGate" : "Acacua Fence Gate";
            case SPRUCE_FENCE:
                return "Spruce Fence";
            case BIRCH_FENCE:
                return "Birch Fence";
            case JUNGLE_FENCE:
                return "Jungle Fence";
            case DARK_OAK_FENCE:
                return "Dark Oak Fence";
            case ACACIA_FENCE:
                return "Acacia Fence";
            case SPRUCE_DOOR:
                return "Spruce Door";
            case BIRCH_DOOR:
                return "Birch Door";
            case JUNGLE_DOOR:
                return "Jungle Door";
            case ACACIA_DOOR:
                return "Acacia Door";
            case DARK_OAK_DOOR:
                return "Dark Oak Door";
            case END_ROD:
                return "End Rod";
            case CHORUS_PLANT:
                return "Chorus Plant";
            case CHORUS_FLOWER:
                return "Chorus Flower";
            case PURPUR_BLOCK:
                return "Purpur Block";
            case PURPUR_PILLAR:
                return "Purpur Pillar";
            case PURPUR_STAIRS:
                return "Purpur Stairs";
            case PURPUR_DOUBLE_SLAB:
                return max16 ? "PurpurDoubleSlab" : "Purpur Double Slab";
            case PURPUR_SLAB:
                return "Purpur Slab";
            case END_BRICKS:
                return "End Stone Bricks";
            case BEETROOT_BLOCK:
                return "Beetroot Block";
            case GRASS_PATH:
                return "Grass Path";
            case END_GATEWAY:
                return "End Gateway";
            case COMMAND_REPEATING:
                return max16 ? "Command Block" : "Repeating Command Block";
            case COMMAND_CHAIN:
                return max16 ? "Command Block" : "Chain Command Block";
            case FROSTED_ICE:
                return "Frosted Ice";
            case MAGMA:
                return "Magma Block";
            case NETHER_WART_BLOCK:
                return max16 ? "NetherWart Block" : "Nether Wart Block";
            case RED_NETHER_BRICK:
                return "Red Nether Brick";
            case BONE_BLOCK:
                return "Bone Block";
            case STRUCTURE_VOID:
                return "Structure Void";
            case OBSERVER:
                return "Observer";
            case WHITE_SHULKER_BOX:
                return max16 ? "White Shulker" : "White Shulker Box";
            case ORANGE_SHULKER_BOX:
                return max16 ? "Orange Shulker" : "Orange Shulker Box";
            case MAGENTA_SHULKER_BOX:
                return max16 ? "Magenta Shulker" : "Magenta Shulker Box";
            case LIGHT_BLUE_SHULKER_BOX:
                return max16 ? "LightBlueShulker" : "Light Blue Shulker Box";
            case YELLOW_SHULKER_BOX:
                return max16 ? "Yellow Shulker" : "Yellow Shulker Box";
            case LIME_SHULKER_BOX:
                return max16 ? "Lime Shulker" : "Lime Shulker Box";
            case PINK_SHULKER_BOX:
                return max16 ? "Pink Shulker" : "Pink Shulker Box";
            case GRAY_SHULKER_BOX:
                return max16 ? "Gray Shulker" : "Gray Shulker Box";
            case SILVER_SHULKER_BOX:
                return max16 ? "Silver Shulker" : "Silver Shulker Box";
            case CYAN_SHULKER_BOX:
                return max16 ? "Cyan Shulker" : "Cyan Shulker Box";
            case PURPLE_SHULKER_BOX:
                return max16 ? "Purple Shulker" : "Purple Shulker Box";
            case BLUE_SHULKER_BOX:
                return max16 ? "Blue Shulker" : "Blue Shulker Box";
            case BROWN_SHULKER_BOX:
                return max16 ? "Brown Shulker" : "Brown Shulker Box";
            case GREEN_SHULKER_BOX:
                return max16 ? "Green Shulker" : "Green Shulker Box";
            case RED_SHULKER_BOX:
                return max16 ? "Red Shulker" : "Red Shulker Box";
            case BLACK_SHULKER_BOX:
                return max16 ? "Black Shulker" : "Black Shulker Box";
            case WHITE_GLAZED_TERRACOTTA:
                return max16 ? "White T'cotta" : "White Glazed Terracotta";
            case ORANGE_GLAZED_TERRACOTTA:
                return max16 ? "Orange T'cotta" : "Orange Glazed Terracotta";
            case MAGENTA_GLAZED_TERRACOTTA:
                return max16 ? "Magenta T'cotta" : "Magenta Glazed Terracotta";
            case LIGHT_BLUE_GLAZED_TERRACOTTA:
                return max16 ? "LightBlueT'cotta" : "Orange Glazed Terracotta";
            case YELLOW_GLAZED_TERRACOTTA:
                return max16 ? "Yellow T'cotta" : "Yellow Glazed Terracotta";
            case LIME_GLAZED_TERRACOTTA:
                return max16 ? "Lime T'cotta" : "Lime Glazed Terracotta";
            case PINK_GLAZED_TERRACOTTA:
                return max16 ? "Pink T'cotta" : "Pink Glazed Terracotta";
            case GRAY_GLAZED_TERRACOTTA:
                return max16 ? "Gray T'cotta" : "Gray Glazed Terracotta";
            case SILVER_GLAZED_TERRACOTTA:
                return max16 ? "Silver T'cotta" : "Silver Glazed Terracotta";
            case CYAN_GLAZED_TERRACOTTA:
                return max16 ? "Cyan T'cotta" : "Cyan Glazed Terracotta";
            case PURPLE_GLAZED_TERRACOTTA:
                return max16 ? "Purple T'cotta" : "Purple Glazed Terracotta";
            case BLUE_GLAZED_TERRACOTTA:
                return max16 ? "Blue T'cotta" : "Blue Glazed Terracotta";
            case BROWN_GLAZED_TERRACOTTA:
                return max16 ? "Brown T'cotta" : "Brown Glazed Terracotta";
            case GREEN_GLAZED_TERRACOTTA:
                return max16 ? "Green T'cotta" : "Green Glazed Terracotta";
            case RED_GLAZED_TERRACOTTA:
                return max16 ? "Red T'cotta" : "Red Glazed Terracotta";
            case BLACK_GLAZED_TERRACOTTA:
                return max16 ? "Black T'cotta" : "Black Glazed Terracotta";
            case CONCRETE:
                switch (durability) {
                    case 1:
                        return "Orange Concrete";
                    case 2:
                        return "Magenta Concrete";
                    case 3:
                        return max16 ? "Aqua Concrete" : "Light Blue Concrete";
                    case 4:
                        return "Yellow Concrete";
                    case 5:
                        return "Lime Concrete";
                    case 6:
                        return "Pink Concrete";
                    case 7:
                        return "Gray Concrete";
                    case 8:
                        return max16 ? "Silver Concrete" : "Light Gray Concrete";
                    case 9:
                        return "Cyan Concrete";
                    case 10:
                        return "Purple Concrete";
                    case 11:
                        return "Blue Concrete";
                    case 12:
                        return "Brown Concrete";
                    case 13:
                        return "Green Concrete";
                    case 14:
                        return "Red Concrete";
                    case 15:
                        return "Black Concrete";
                    default:
                        return "White Concrete";
                }
            case CONCRETE_POWDER:
                switch (durability) {
                    case 1:
                        return max16 ? "Orange C'Powder" : "Orange Concrete Powder";
                    case 2:
                        return max16 ? "Magenta C'Powder" : "Magenta Concrete Powder";
                    case 3:
                        return max16 ? "Aqua C'Powder" : "Light Blue Concrete Powder";
                    case 4:
                        return max16 ? "Yellow C'Powder" : "Yellow Concrete Powder";
                    case 5:
                        return max16 ? "Lime C'Powder" : "Lime Concrete Powder";
                    case 6:
                        return max16 ? "Pink C'Powder" : "Pink Concrete Powder";
                    case 7:
                        return max16 ? "Gray C'Powder" : "Gray Concrete Powder";
                    case 8:
                        return max16 ? "Silver C'Powder" : "Light Gray Concrete Powder";
                    case 9:
                        return max16 ? "Cyan C'Powder" : "Cyan Concrete Powder";
                    case 10:
                        return max16 ? "Purple C'Powder" : "Purple Concrete Powder";
                    case 11:
                        return max16 ? "Blue C'Powder" : "Blue Concrete Powder";
                    case 12:
                        return max16 ? "Brown C'Powder" : "Brown Concrete Powder";
                    case 13:
                        return max16 ? "Green C'Powder" : "Green Concrete Powder";
                    case 14:
                        return max16 ? "Red C'Powder" : "Red Concrete Powder";
                    case 15:
                        return max16 ? "Black C'Powder" : "Black Concrete Powder";
                    default:
                        return max16 ? "White C'Powder" : "White Concrete Powder";
                }
            case STRUCTURE_BLOCK:
                return "Structure Block";
            case IRON_SPADE:
                return "Iron Shovel";
            case IRON_PICKAXE:
                return "Iron Pickaxe";
            case IRON_AXE:
                return "Iron Axe";
            case FLINT_AND_STEEL:
                return "Flint and Steel";
            case APPLE:
                return "Appel";
            case BOW:
                return "Bow";
            case ARROW:
                return "Arrow";
            case COAL:
                return "Coal";
            case DIAMOND:
                return "Diamond";
            case IRON_INGOT:
                return "Iron Ingot";
            case GOLD_INGOT:
                return "Gold Ingot";
            case IRON_SWORD:
                return "Iron Sword";
            case WOOD_SWORD:
                return "Wooden Sword";
            case WOOD_SPADE:
                return "Wooden Shovel";
            case WOOD_PICKAXE:
                return "Wooden Pickaxe";
            case WOOD_AXE:
                return "Wooden Axe";
            case STONE_SWORD:
                return "Stone Sword";
            case STONE_SPADE:
                return "Stone Shovel";
            case STONE_PICKAXE:
                return "Stone Pickaxe";
            case STONE_AXE:
                return "Stone Axe";
            case DIAMOND_SWORD:
                return "Diamond Sword";
            case DIAMOND_SPADE:
                return "Diamond Shovel";
            case DIAMOND_PICKAXE:
                return "Diamond Pickaxe";
            case DIAMOND_AXE:
                return "Diamond Axe";
            case STICK:
                return "Stick";
            case BOWL:
                return "Bowl";
            case MUSHROOM_SOUP:
                return "Mushroom Stew";
            case GOLD_SWORD:
                return "Golden Sword";
            case GOLD_SPADE:
                return "Golden Shovel";
            case GOLD_PICKAXE:
                return "Golden Pickaxe";
            case GOLD_AXE:
                return "Golden Axe";
            case STRING:
                return "String";
            case FEATHER:
                return "Feather";
            case SULPHUR:
                return "Gunpowder";
            case WOOD_HOE:
                return "Wooden Hoe";
            case STONE_HOE:
                return "Stone Hoe";
            case IRON_HOE:
                return "Iron Hoe";
            case DIAMOND_HOE:
                return "Diamond Hoe";
            case GOLD_HOE:
                return "Golden Hoe";
            case SEEDS:
                return "Wheat Seeds";
            case WHEAT:
                return "Wheat";
            case BREAD:
                return "Bread";
            case LEATHER_HELMET:
                return "Leather Helmet";
            case LEATHER_CHESTPLATE:
                return max16 ? "Leather Chest" : "Leather Chestplate";
            case LEATHER_LEGGINGS:
                return "Leather Leggings";
            case LEATHER_BOOTS:
                return "Leather Boots";
            case CHAINMAIL_HELMET:
                return "Chainmail Helmet";
            case CHAINMAIL_CHESTPLATE:
                return max16 ? "Chainmail Chest" : "Chainmail Chestplate";
            case CHAINMAIL_LEGGINGS:
                return max16 ? "Chainmail Leggs" : "Chainmail Leggings";
            case CHAINMAIL_BOOTS:
                return "Chainmail Boots";
            case IRON_HELMET:
                return "Iron Helmet";
            case IRON_CHESTPLATE:
                return "Iron Chestplate";
            case IRON_LEGGINGS:
                return "Iron Leggings";
            case IRON_BOOTS:
                return "Iron Boots";
            case DIAMOND_HELMET:
                return "Diamond Helmet";
            case DIAMOND_CHESTPLATE:
                return max16 ? "Diamond Chest" : "Diamond Chestplate";
            case DIAMOND_LEGGINGS:
                return "Diamond Leggings";
            case DIAMOND_BOOTS:
                return "Diamond Boots";
            case GOLD_HELMET:
                return "Golden Helmet";
            case GOLD_CHESTPLATE:
                return max16 ? "Golden Chest" : "Golden Chestplate";
            case GOLD_LEGGINGS:
                return "Golden Leggings";
            case GOLD_BOOTS:
                return "Golden Boots";
            case FLINT:
                return "Flint";
            case PORK:
                return "Raw Porkchop";
            case GRILLED_PORK:
                return "Cooked Porkchop";
            case PAINTING:
                return "Painting";
            case GOLDEN_APPLE:
                switch (durability) {
                    case 1:
                        return max16 ? "Enchanted Apple" : "Enchanted Golden Apple";
                    default:
                        return "Golden Apple";
                }
            case SIGN:
                return "Sign";
            case WOOD_DOOR:
                return "Oak Door";
            case BUCKET:
                return "Bucket";
            case WATER_BUCKET:
                return "Water Bucket";
            case LAVA_BUCKET:
                return "Lava Bucket";
            case MINECART:
                return "Minecart";
            case SADDLE:
                return "Saddle";
            case IRON_DOOR:
                return "Iron Door";
            case REDSTONE:
                return "Redstone";
            case SNOW_BALL:
                return "Snowball";
            case BOAT:
                return "Oak Boat";
            case LEATHER:
                return "Leather";
            case MILK_BUCKET:
                return "Milk Bucket";
            case CLAY_BRICK:
                return "Brick";
            case CLAY_BALL:
                return "Clay";
            case SUGAR_CANE:
                return "Sugar Canes";
            case PAPER:
                return "Paper";
            case BOOK:
                return "Book";
            case SLIME_BALL:
                return "Slimeball";
            case STORAGE_MINECART:
                return max16 ? "Chest Minecart" : "Minecart with Chest";
            case POWERED_MINECART:
                return max16 ? "Furnace Minecart" : "Minecart with Furnace";
            case EGG:
                return "Egg";
            case COMPASS:
                return "Compass";
            case FISHING_ROD:
                return "Fishing Rod";
            case WATCH:
                return "Clock";
            case GLOWSTONE_DUST:
                return "Glowstone Dust";
            case RAW_FISH:
                switch (durability) {
                    case 1:
                        return "Raw Salmon";
                    case 2:
                        return "Clownfish";
                    case 3:
                        return "Pufferfish";
                    default:
                        return "Raw Fish";
                }
            case COOKED_FISH:
                switch (durability) {
                    case 1:
                        return "Cooked Salmon";
                    default:
                        return "Cooked Fish";
                }
            case INK_SACK:
                switch (durability) {
                    case 1:
                        return "Rose Red";
                    case 2:
                        return "Cactus Green";
                    case 3:
                        return "Coco Beans";
                    case 4:
                        return "Lapis Lazuli";
                    case 5:
                        return "Purple Dye";
                    case 6:
                        return "Cyan Dye";
                    case 7:
                        return "Light Gray Dye";
                    case 8:
                        return "Gray Dye";
                    case 9:
                        return "Pink Dye";
                    case 10:
                        return "Lime Dye";
                    case 11:
                        return "Dandelion Yellow";
                    case 12:
                        return "Light Blue Dye";
                    case 13:
                        return "Light Blue Dye";
                    case 14:
                        return "Magenta Dye";
                    case 15:
                        return "Orange Dye";
                    default:
                        return "Ink Sack";
                }
            case BONE:
                return "Bone";
            case SUGAR:
                return "Sugar";
            case CAKE:
                return "Cake";
            case BED:
                return "Bed";
            case DIODE:
                return max16 ? "RedstoneRepeater" : "Redstone Repeater";
            case COOKIE:
                return "Cookie";
            case MAP:
                return "Map";
            case SHEARS:
                return "Shears";
            case MELON:
                return "Melon";
            case PUMPKIN_SEEDS:
                return "Pumpkin Seeds";
            case MELON_SEEDS:
                return "Melon Seeds";
            case RAW_BEEF:
                return "Raw Beef";
            case COOKED_BEEF:
                return "Steak";
            case RAW_CHICKEN:
                return "Raw Chicken";
            case COOKED_CHICKEN:
                return "Cooked Chicken";
            case ROTTEN_FLESH:
                return "Rotten Flesh";
            case ENDER_PEARL:
                return "Ender Pearl";
            case BLAZE_ROD:
                return "Blaze Rod";
            case GHAST_TEAR:
                return "Ghast Tear";
            case GOLD_NUGGET:
                return "Gold Nugget";
            case NETHER_STALK:
                return "Nether Wart";
            case POTION:
                return "Potion";
            case GLASS_BOTTLE:
                return "Glass Bottle";
            case SPIDER_EYE:
                return "Spider Eye";
            case FERMENTED_SPIDER_EYE:
                return "Fermented Spider Eye";
            case BLAZE_POWDER:
                return "Blaze Powder";
            case MAGMA_CREAM:
                return "Magma Cream";
            case BREWING_STAND_ITEM:
                return "Brewing Stand";
            case CAULDRON_ITEM:
                return "Cauldron";
            case EYE_OF_ENDER:
                return "Eye of Ender";
            case SPECKLED_MELON:
                return "Glistering Melon";
            case MONSTER_EGG:
                Mob mob = Mob.from(durability);
                return mob == null ? "SurvivalSpawn Egg" : mob.getName();
            case EXP_BOTTLE:
                return max16 ? "EXP Bottle" : "Bottle o' Enchanting";
            case FIREBALL:
                return "Fire Charge";
            case BOOK_AND_QUILL:
                return "Book and Quill";
            case WRITTEN_BOOK:
                return "Written Book";
            case EMERALD:
                return "Emerald";
            case ITEM_FRAME:
                return "Item Frame";
            case FLOWER_POT_ITEM:
                return "Flower Pot";
            case CARROT_ITEM:
                return "Carrot";
            case POTATO_ITEM:
                return "Potato";
            case BAKED_POTATO:
                return "Baked Potato";
            case POISONOUS_POTATO:
                return "Poisonous Potato";
            case EMPTY_MAP:
                return "Empty Map";
            case GOLDEN_CARROT:
                return "Golden Carrot";
            case SKULL_ITEM:
                switch (durability) {
                    case 1:
                        return max16 ? "Wither Head" : "Wither Skeleton Head";
                    case 2:
                        return "Zombie Head";
                    case 3:
                        return "Player Head";
                    case 4:
                        return "Creeper Head";
                    case 5:
                        return "Dragon Head";
                    default:
                        return "Skeleton Head";
                }
            case CARROT_STICK:
                return max16 ? "Carrot Stick" : "Carrot on a Stick";
            case NETHER_STAR:
                return "Nether Star";
            case PUMPKIN_PIE:
                return "Pumpkin Pie";
            case FIREWORK:
                return "Firework Rocket";
            case FIREWORK_CHARGE:
                return "Firework Charge";
            case ENCHANTED_BOOK:
                return "Enchanted Book";
            case REDSTONE_COMPARATOR:
                return max16 ? "Redst.Comparator" : "Redstone Comparator";
            case NETHER_BRICK_ITEM:
                return "Nether Brick";
            case QUARTZ:
                return "Nether Quartz";
            case EXPLOSIVE_MINECART:
                return max16 ? "TNT Minecart" : "Minecart with TNT";
            case HOPPER_MINECART:
                return max16 ? "Hopper Minecart" : "Minecart with Hopper";
            case PRISMARINE_SHARD:
                return "Prismarine Shard";
            case PRISMARINE_CRYSTALS:
                return max16 ? "Prism. Crystals" : "Prismarine Crystals";
            case RABBIT:
                return "Raw Rabbit";
            case COOKED_RABBIT:
                return "Cooked Rabbit";
            case RABBIT_STEW:
                return "Rabbit Stew";
            case RABBIT_FOOT:
                return "Rabbit Foot";
            case RABBIT_HIDE:
                return "Rabbit Hide";
            case ARMOR_STAND:
                return "Armor Stand";
            case IRON_BARDING:
                return "Iron Horse Armor";
            case GOLD_BARDING:
                return max16 ? "Gold Horse Armor" : "Golden Horse Armor";
            case DIAMOND_BARDING:
                return max16 ? "Dia Horse Armor" : "Diamond Horse Armor";
            case LEASH:
                return "Lead";
            case NAME_TAG:
                return "Name Tag";
            case COMMAND_MINECART:
                return max16 ? "Command Minecart" : "Minecart with Command Block";
            case MUTTON:
                return "Raw Mutton";
            case COOKED_MUTTON:
                return "Cooked Mutton";
            case BANNER:
                return "Banner";
            case END_CRYSTAL:
                return "End Crystal";
            case SPRUCE_DOOR_ITEM:
                return "Spruce Door";
            case BIRCH_DOOR_ITEM:
                return "Birch Door";
            case JUNGLE_DOOR_ITEM:
                return "Jungle Door";
            case ACACIA_DOOR_ITEM:
                return "Acacia Door";
            case DARK_OAK_DOOR_ITEM:
                return "Dark Oak Door";
            case CHORUS_FRUIT:
                return "Chorus Fruit";
            case CHORUS_FRUIT_POPPED:
                return max16 ? "Popped Chorus" : "Popped Chorus Fruit";
            case BEETROOT:
                return "Beetroot";
            case BEETROOT_SEEDS:
                return "Beetroot Seeds";
            case BEETROOT_SOUP:
                return "Beetroot Soup";
            case DRAGONS_BREATH:
                return "Dragon's Breath";
            case SPLASH_POTION:
                return "Splash Potion";
            case SPECTRAL_ARROW:
                return "Spectral Arrow";
            case TIPPED_ARROW:
                return "Tipped Arrow";
            case LINGERING_POTION:
                return "Lingering Potion";
            case SHIELD:
                return "Shield";
            case ELYTRA:
                return "Elytra";
            case BOAT_SPRUCE:
                return "Spruce Boat";
            case BOAT_BIRCH:
                return "Birch Boat";
            case BOAT_JUNGLE:
                return "Jungle Boat";
            case BOAT_ACACIA:
                return "Acacia Boat";
            case BOAT_DARK_OAK:
                return "Dark Oak Boat";
            case TOTEM:
                return "Totem of Undying";
            case SHULKER_SHELL:
                return "Shulker Shell";
            case IRON_NUGGET:
                return "Iron Nugget";
            case KNOWLEDGE_BOOK:
                return "Knowledge Book";
            case GOLD_RECORD:
                return "13 Disc";
            case GREEN_RECORD:
                return "Cat Disc";
            case RECORD_3:
                return "Blocks Disc";
            case RECORD_4:
                return "Chirp Disc";
            case RECORD_5:
                return "Far Disc";
            case RECORD_6:
                return "Mall Disc";
            case RECORD_7:
                return "Mellohi Disc";
            case RECORD_8:
                return "Stal Disc";
            case RECORD_9:
                return "Strad Disc";
            case RECORD_10:
                return "Ward Disc";
            case RECORD_11:
                return "11 Disc";
            case RECORD_12:
                return "Wait Disc";
        }

        return null;
    }
}
