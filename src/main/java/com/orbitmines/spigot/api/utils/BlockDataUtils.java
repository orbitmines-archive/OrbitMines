package com.orbitmines.spigot.api.utils;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.type.*;

public class BlockDataUtils {

    public static Block setBlock(World world, int x, int y, int z, Material material) {
        Block block = world.getBlockAt(x, y, z);
        block.setType(material);

        return block;
    }

    public static Block update(World world, int x, int y, int z) {
        Block block = world.getBlockAt(x, y, z);
        block.getState().update(true);
        return block;
    }

    public static Block setBed(World world, int x, int y, int z, DyeColor dyeColor, Bed.Part part, BlockFace facing) {
        Block block = setBlock(world, x, y, z, ColorUtils.getBedMaterial(dyeColor));

        Bed data = (Bed) block.getBlockData();
        data.setPart(part);
        data.setFacing(facing);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setBrewingStand(World world, int x, int y, int z, int... bottles) {
        Block block = setBlock(world, x, y, z, Material.BREWING_STAND);

        BrewingStand data = (BrewingStand) block.getBlockData();
        if (bottles != null) {
            for (int i : bottles) {
                data.setBottle(i, true);
            }
        }

        block.setBlockData(data, true);

        return block;
    }

    public static Block setBubbleColumn(World world, int x, int y, int z, boolean drag) {
        Block block = setBlock(world, x, y, z, Material.BUBBLE_COLUMN);

        BubbleColumn data = (BubbleColumn) block.getBlockData();
        data.setDrag(drag);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setChest(World world, int x, int y, int z, Chest.Type type, BlockFace facing, boolean waterlogged) {
        Block block = setBlock(world, x, y, z, Material.CHEST);

        Chest data = (Chest) block.getBlockData();
        data.setType(type);
        data.setFacing(facing);
        data.setWaterlogged(waterlogged);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setCocoa(World world, int x, int y, int z, int age, BlockFace facing) {
        Block block = setBlock(world, x, y, z, Material.COCOA);

        Cocoa data = (Cocoa) block.getBlockData();
        data.setAge(age);
        data.setFacing(facing);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setCommandBlock(World world, int x, int y, int z, boolean conditional, BlockFace facing) {
        Block block = setBlock(world, x, y, z, Material.COMMAND_BLOCK);

        CommandBlock data = (CommandBlock) block.getBlockData();
        data.setConditional(conditional);
        data.setFacing(facing);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setComparator(World world, int x, int y, int z, Comparator.Mode mode, BlockFace facing, boolean powered) {
        Block block = setBlock(world, x, y, z, Material.COMPARATOR);

        Comparator data = (Comparator) block.getBlockData();
        data.setMode(mode);
        data.setFacing(facing);
        data.setPowered(powered);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setDaylightDetector(World world, int x, int y, int z, boolean inverted, int power) {
        Block block = setBlock(world, x, y, z, Material.COMMAND_BLOCK);

        DaylightDetector data = (DaylightDetector) block.getBlockData();
        data.setInverted(inverted);
        data.setPower(power);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setDispenser(World world, int x, int y, int z, boolean triggered, BlockFace facing) {
        Block block = setBlock(world, x, y, z, Material.DISPENSER);

        Dispenser data = (Dispenser) block.getBlockData();
        data.setTriggered(triggered);
        data.setFacing(facing);

        block.setBlockData(data, true);

        return block;
    }

    public enum DoorType {

        ACACIA(Material.ACACIA_DOOR, Material.ACACIA_TRAPDOOR),
        BIRCH(Material.BIRCH_DOOR, Material.BIRCH_TRAPDOOR),
        DARK_OAK(Material.DARK_OAK_DOOR, Material.DARK_OAK_TRAPDOOR),
        JUNGLE(Material.JUNGLE_DOOR, Material.JUNGLE_TRAPDOOR),
        OAK(Material.OAK_DOOR, Material.OAK_TRAPDOOR),
        SPRUCE(Material.SPRUCE_DOOR, Material.SPRUCE_TRAPDOOR),

        IRON(Material.IRON_DOOR, Material.IRON_TRAPDOOR);

        private final Material door;
        private final Material trapDoor;

        DoorType(Material door, Material trapDoor) {
            this.door = door;
            this.trapDoor = trapDoor;
        }

        public Material getDoor() {
            return door;
        }

        public Material getTrapDoor() {
            return trapDoor;
        }
    }

    public static Block setDoor(World world, int x, int y, int z, DoorType doorType, Door.Hinge hinge, Bisected.Half half, BlockFace facing, boolean open, boolean powered) {
        Block block = setBlock(world, x, y, z, doorType.door);

        Door data = (Door) block.getBlockData();
        data.setHinge(hinge);
        data.setHalf(half);
        data.setFacing(facing);
        data.setOpen(open);
        data.setPowered(powered);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setEnderChest(World world, int x, int y, int z, BlockFace facing, boolean waterlogged) {
        Block block = setBlock(world, x, y, z, Material.ENDER_CHEST);

        EnderChest data = (EnderChest) block.getBlockData();
        data.setFacing(facing);
        data.setWaterlogged(waterlogged);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setEndPortalFrame(World world, int x, int y, int z, boolean eye, BlockFace facing) {
        Block block = setBlock(world, x, y, z, Material.ENDER_CHEST);

        EndPortalFrame data = (EndPortalFrame) block.getBlockData();
        data.setEye(eye);
        data.setFacing(facing);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setFarmLand(World world, int x, int y, int z, int moisture) {
        Block block = setBlock(world, x, y, z, Material.FARMLAND);

        Farmland data = (Farmland) block.getBlockData();
        data.setMoisture(moisture);

        block.setBlockData(data, true);

        return block;
    }

    public enum FenceType {

        ACACIA(Material.ACACIA_FENCE, Material.ACACIA_FENCE_GATE),
        BIRCH(Material.BIRCH_FENCE, Material.BIRCH_FENCE_GATE),
        DARK_OAK(Material.DARK_OAK_FENCE, Material.DARK_OAK_FENCE_GATE),
        JUNGLE(Material.JUNGLE_FENCE, Material.JUNGLE_FENCE_GATE),
        OAK(Material.OAK_FENCE, Material.OAK_FENCE_GATE),
        SPRUCE(Material.SPRUCE_FENCE, Material.SPRUCE_FENCE_GATE),

        NETHER_BRICK(Material.NETHER_BRICK_FENCE, null) {
            @Override
            public Material getGate() {
                throw new IllegalStateException("There is no Nether Brick Fence Gate!");
            }
        };

        private final Material fence;
        private final Material gate;

        FenceType(Material fence, Material gate) {
            this.fence = fence;
            this.gate = gate;
        }

        public Material getFence() {
            return fence;
        }

        public Material getGate() {
            return gate;
        }
    }

    public static Block setFence(World world, int x, int y, int z, FenceType fenceType, boolean waterlogged, BlockFace... facing) {
        Block block = setBlock(world, x, y, z, fenceType.fence);

        Fence data = (Fence) block.getBlockData();

        if (facing != null) {
            for (BlockFace face : facing) {
                data.setFace(face, true);
            }
        }

        data.setWaterlogged(waterlogged);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setFire(World world, int x, int y, int z, int age, BlockFace... facing) {
        Block block = setBlock(world, x, y, z, Material.FIRE);

        Fire data = (Fire) block.getBlockData();

        if (facing != null) {
            for (BlockFace face : facing) {
                data.setFace(face, true);
            }
        }

        data.setAge(age);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setFurnace(World world, int x, int y, int z, BlockFace facing, boolean lit) {
        Block block = setBlock(world, x, y, z, Material.FURNACE);

        Furnace data = (Furnace) block.getBlockData();
        data.setFacing(facing);
        data.setLit(lit);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setGate(World world, int x, int y, int z, FenceType fenceType, boolean inWall, BlockFace facing, boolean open, boolean powered) {
        Block block = setBlock(world, x, y, z, fenceType.gate);

        Gate data = (Gate) block.getBlockData();
        data.setInWall(inWall);
        data.setFacing(facing);
        data.setOpen(open);
        data.setPowered(powered);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setGlassPane(World world, int x, int y, int z, boolean waterlogged, BlockFace... facing) {
        return setGlassPane(world, x, y, z, null, waterlogged, facing);
    }

    public static Block setGlassPane(World world, int x, int y, int z, DyeColor dyeColor, boolean waterlogged, BlockFace... facing) {
        Block block = setBlock(world, x, y, z, dyeColor == null ? Material.GLASS_PANE : ColorUtils.getStainedGlassPaneMaterial(dyeColor));

        GlassPane data = (GlassPane) block.getBlockData();

        if (facing != null) {
            for (BlockFace face : facing) {
                data.setFace(face, true);
            }
        }

        data.setWaterlogged(waterlogged);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setHopper(World world, int x, int y, int z, boolean enabled, BlockFace facing) {
        Block block = setBlock(world, x, y, z, Material.HOPPER);

        Hopper data = (Hopper) block.getBlockData();
        data.setEnabled(enabled);
        data.setFacing(facing);

        block.setBlockData(data, true);

        return block;
    }

    /* This might be added later to spigot (to add records to a jukebox for example) */
    @Deprecated
    public static Block setJukebox(World world, int x, int y, int z) {
        return setBlock(world, x, y, z, Material.JUKEBOX);
    }

    public enum WoodType {

        ACACIA(
                Material.ACACIA_PLANKS,
                Material.ACACIA_SAPLING,
                Material.ACACIA_LOG,
                Material.STRIPPED_ACACIA_LOG,
                Material.STRIPPED_ACACIA_WOOD,
                Material.ACACIA_WOOD,
                Material.ACACIA_LEAVES,
                Material.ACACIA_SLAB,
                Material.ACACIA_PRESSURE_PLATE,
                Material.ACACIA_FENCE,
                Material.ACACIA_TRAPDOOR,
                Material.ACACIA_FENCE_GATE,
                Material.ACACIA_BUTTON,
                Material.ACACIA_STAIRS,
                Material.ACACIA_DOOR,
                Material.ACACIA_BOAT
        ),
        BIRCH(
                Material.BIRCH_PLANKS,
                Material.BIRCH_SAPLING,
                Material.BIRCH_LOG,
                Material.STRIPPED_BIRCH_LOG,
                Material.STRIPPED_BIRCH_WOOD,
                Material.BIRCH_WOOD,
                Material.BIRCH_LEAVES,
                Material.BIRCH_SLAB,
                Material.BIRCH_PRESSURE_PLATE,
                Material.BIRCH_FENCE,
                Material.BIRCH_TRAPDOOR,
                Material.BIRCH_FENCE_GATE,
                Material.BIRCH_BUTTON,
                Material.BIRCH_STAIRS,
                Material.BIRCH_DOOR,
                Material.BIRCH_BOAT
        ),
        DARK_OAK(
                Material.DARK_OAK_PLANKS,
                Material.DARK_OAK_SAPLING,
                Material.DARK_OAK_LOG,
                Material.STRIPPED_DARK_OAK_LOG,
                Material.STRIPPED_DARK_OAK_WOOD,
                Material.DARK_OAK_WOOD,
                Material.DARK_OAK_LEAVES,
                Material.DARK_OAK_SLAB,
                Material.DARK_OAK_PRESSURE_PLATE,
                Material.DARK_OAK_FENCE,
                Material.DARK_OAK_TRAPDOOR,
                Material.DARK_OAK_FENCE_GATE,
                Material.DARK_OAK_BUTTON,
                Material.DARK_OAK_STAIRS,
                Material.DARK_OAK_DOOR,
                Material.DARK_OAK_BOAT
        ),
        JUNGLE(
                Material.JUNGLE_PLANKS,
                Material.JUNGLE_SAPLING,
                Material.JUNGLE_LOG,
                Material.STRIPPED_JUNGLE_LOG,
                Material.STRIPPED_JUNGLE_WOOD,
                Material.JUNGLE_WOOD,
                Material.JUNGLE_LEAVES,
                Material.JUNGLE_SLAB,
                Material.JUNGLE_PRESSURE_PLATE,
                Material.JUNGLE_FENCE,
                Material.JUNGLE_TRAPDOOR,
                Material.JUNGLE_FENCE_GATE,
                Material.JUNGLE_BUTTON,
                Material.JUNGLE_STAIRS,
                Material.JUNGLE_DOOR,
                Material.JUNGLE_BOAT
        ),
        OAK(
                Material.OAK_PLANKS,
                Material.OAK_SAPLING,
                Material.OAK_LOG,
                Material.STRIPPED_OAK_LOG,
                Material.STRIPPED_OAK_WOOD,
                Material.OAK_WOOD,
                Material.OAK_LEAVES,
                Material.OAK_SLAB,
                Material.OAK_PRESSURE_PLATE,
                Material.OAK_FENCE,
                Material.OAK_TRAPDOOR,
                Material.OAK_FENCE_GATE,
                Material.OAK_BUTTON,
                Material.OAK_STAIRS,
                Material.OAK_DOOR,
                Material.OAK_BOAT
        ),
        SPRUCE(
                Material.SPRUCE_PLANKS,
                Material.SPRUCE_SAPLING,
                Material.SPRUCE_LOG,
                Material.STRIPPED_SPRUCE_LOG,
                Material.STRIPPED_SPRUCE_WOOD,
                Material.SPRUCE_WOOD,
                Material.SPRUCE_LEAVES,
                Material.SPRUCE_SLAB,
                Material.SPRUCE_PRESSURE_PLATE,
                Material.SPRUCE_FENCE,
                Material.SPRUCE_TRAPDOOR,
                Material.SPRUCE_FENCE_GATE,
                Material.SPRUCE_BUTTON,
                Material.SPRUCE_STAIRS,
                Material.SPRUCE_DOOR,
                Material.SPRUCE_BOAT
        );

        private final Material planks;
        private final Material sapling;
        private final Material log;
        private final Material strippedLog;
        private final Material strippedWood;
        private final Material wood;
        private final Material leaves;
        private final Material slab;
        private final Material pressurePlate;
        private final Material fence;
        private final Material trapDoor;
        private final Material fenceGate;
        private final Material button;
        private final Material stairs;
        private final Material door;
        private final Material boat;

        WoodType(Material planks, Material sapling, Material log, Material strippedLog, Material strippedWood, Material wood, Material leaves, Material slab, Material pressurePlate, Material fence, Material trapDoor, Material fenceGate, Material button, Material stairs, Material door, Material boat) {
            this.planks = planks;
            this.sapling = sapling;
            this.log = log;
            this.strippedLog = strippedLog;
            this.strippedWood = strippedWood;
            this.wood = wood;
            this.leaves = leaves;
            this.slab = slab;
            this.pressurePlate = pressurePlate;
            this.fence = fence;
            this.trapDoor = trapDoor;
            this.fenceGate = fenceGate;
            this.button = button;
            this.stairs = stairs;
            this.door = door;
            this.boat = boat;
        }

        public Material getPlanks() {
            return planks;
        }

        public Material getSapling() {
            return sapling;
        }

        public Material getLog() {
            return log;
        }

        public Material getStrippedLog() {
            return strippedLog;
        }

        public Material getStrippedWood() {
            return strippedWood;
        }

        public Material getWood() {
            return wood;
        }

        public Material getLeaves() {
            return leaves;
        }

        public Material getSlab() {
            return slab;
        }

        public Material getPressurePlate() {
            return pressurePlate;
        }

        public Material getFence() {
            return fence;
        }

        public Material getTrapDoor() {
            return trapDoor;
        }

        public Material getFenceGate() {
            return fenceGate;
        }

        public Material getButton() {
            return button;
        }

        public Material getStairs() {
            return stairs;
        }

        public Material getDoor() {
            return door;
        }

        public Material getBoat() {
            return boat;
        }
    }

    public static Block setLadder(World world, int x, int y, int z, WoodType woodType, boolean persistent, int distance) {
        Block block = setBlock(world, x, y, z, woodType.leaves);

        Leaves data = (Leaves) block.getBlockData();
        data.setPersistent(persistent);
        data.setDistance(distance);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setNoteBlock(World world, int x, int y, int z, Instrument instrument, Note note, boolean powered) {
        Block block = setBlock(world, x, y, z, Material.NOTE_BLOCK);

        NoteBlock data = (NoteBlock) block.getBlockData();
        data.setInstrument(instrument);
        data.setNote(note);
        data.setPowered(powered);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setObserver(World world, int x, int y, int z, BlockFace facing, boolean powered) {
        Block block = setBlock(world, x, y, z, Material.OBSERVER);

        Observer data = (Observer) block.getBlockData();
        data.setFacing(facing);
        data.setPowered(powered);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setPiston(World world, int x, int y, int z, boolean extended, BlockFace facing) {
        Block block = setBlock(world, x, y, z, Material.PISTON);

        Piston data = (Piston) block.getBlockData();
        data.setExtended(extended);
        data.setFacing(facing);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setPistonHead(World world, int x, int y, int z, boolean isShort, TechnicalPiston.Type type, BlockFace facing) {
        Block block = setBlock(world, x, y, z, Material.PISTON_HEAD);

        PistonHead data = (PistonHead) block.getBlockData();
        data.setShort(isShort);
        data.setType(type);
        data.setFacing(facing);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setRail(World world, int x, int y, int z, Rail.Shape shape) {
        Block block = setBlock(world, x, y, z, Material.RAIL);

        Rail data = (Rail) block.getBlockData();
        data.setShape(shape);

        block.setBlockData(data, true);

        return block;
    }

    public enum RailType {

        RAIL(Material.RAIL),
        POWERED_RAIL(Material.POWERED_RAIL),
        ACTIVATOR_RAIL(Material.ACTIVATOR_RAIL),
        DETECTOR_RAIL(Material.DETECTOR_RAIL);

        private final Material rail;

        RailType(Material rail) {
            this.rail = rail;
        }

        public Material getRail() {
            return rail;
        }
    }

    public static Block setRedstoneRail(World world, int x, int y, int z, RailType railType, Rail.Shape shape, boolean powered) {
        if (railType == RailType.RAIL)
            return setRail(world, x, y, z, shape);

        Block block = setBlock(world, x, y, z, railType.rail);

        RedstoneRail data = (RedstoneRail) block.getBlockData();
        data.setShape(shape);
        data.setPowered(powered);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setRedstoneWallTorch(World world, int x, int y, int z, BlockFace facing, boolean lit) {
        Block block = setBlock(world, x, y, z, Material.REDSTONE_WALL_TORCH);

        RedstoneWallTorch data = (RedstoneWallTorch) block.getBlockData();
        data.setFacing(facing);
        data.setLit(lit);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setRedstoneWire(World world, int x, int y, int z, BlockFace facing, RedstoneWire.Connection connection, int power) {
        Block block = setBlock(world, x, y, z, Material.REDSTONE_WIRE);

        RedstoneWire data = (RedstoneWire) block.getBlockData();
        data.setFace(facing, connection);
        data.setPower(power);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setRepeater(World world, int x, int y, int z, BlockFace facing, int delay, boolean locked, boolean powered) {
        Block block = setBlock(world, x, y, z, Material.REPEATER);

        Repeater data = (Repeater) block.getBlockData();
        data.setFacing(facing);
        data.setDelay(delay);
        data.setLocked(locked);
        data.setPowered(powered);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setSapling(World world, int x, int y, int z, WoodType woodType, int stage) {
        Block block = setBlock(world, x, y, z, woodType.sapling);

        Sapling data = (Sapling) block.getBlockData();
        data.setStage(stage);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setSeaPickle(World world, int x, int y, int z, int pickles, boolean waterlogged) {
        Block block = setBlock(world, x, y, z, Material.SEA_PICKLE);

        SeaPickle data = (SeaPickle) block.getBlockData();
        data.setPickles(pickles);
        data.setWaterlogged(waterlogged);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setSign(World world, int x, int y, int z, BlockFace rotation, boolean waterlogged) {
        Block block = setBlock(world, x, y, z, Material.SIGN);

        Sign data = (Sign) block.getBlockData();
        data.setRotation(rotation);
        data.setWaterlogged(waterlogged);

        block.setBlockData(data, true);

        return block;
    }

    public enum SlabType {

        OAK(Material.OAK_SLAB),
        SPRUCE(Material.SPRUCE_SLAB),
        BIRCH(Material.BIRCH_SLAB),
        JUNGLE(Material.JUNGLE_SLAB),
        ACACIA(Material.ACACIA_SLAB),
        DARK_OAK(Material.DARK_OAK_SLAB),
        STONE(Material.STONE_SLAB),
        SANDSTONE(Material.SANDSTONE_SLAB),
        PETRIFIED_OAK(Material.PETRIFIED_OAK_SLAB),
        COBBLESTONE(Material.COBBLESTONE_SLAB),
        BRICK(Material.BRICK_SLAB),
        STONE_BRICK(Material.STONE_BRICK_SLAB),
        NETHER_BRICK(Material.NETHER_BRICK_SLAB),
        QUARTZ(Material.QUARTZ_SLAB),
        RED_SANDSTONE(Material.RED_SANDSTONE_SLAB),
        PURPUR(Material.PURPUR_SLAB),
        PRISMARINE(Material.PRISMARINE_SLAB),
        PRISMARINE_BRICK(Material.PRISMARINE_BRICK_SLAB),
        DARK_PRISMARINE(Material.DARK_PRISMARINE_SLAB);

        private final Material slab;

        SlabType(Material slab) {
            this.slab = slab;
        }

        public Material getSlab() {
            return slab;
        }
    }

    public static Block setSlab(World world, int x, int y, int z, SlabType slabType, Slab.Type type, boolean waterlogged) {
        Block block = setBlock(world, x, y, z, slabType.slab);

        Slab data = (Slab) block.getBlockData();
        data.setType(type);
        data.setWaterlogged(waterlogged);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setSnow(World world, int x, int y, int z, int layers) {
        Block block = setBlock(world, x, y, z, Material.SNOW);

        Snow data = (Snow) block.getBlockData();
        data.setLayers(layers);

        block.setBlockData(data, true);

        return block;
    }

    public enum StairsType {

        PURPUR(Material.PURPUR_STAIRS),
        OAK(Material.OAK_STAIRS),
        COBBLESTONE(Material.COBBLESTONE_STAIRS),
        BRICK(Material.BRICK_STAIRS),
        STONE_BRICK(Material.STONE_BRICK_STAIRS),
        NETHER_BRICK(Material.NETHER_BRICK_STAIRS),
        SANDSTONE(Material.SANDSTONE_STAIRS),
        SPRUCE(Material.SPRUCE_STAIRS),
        BIRCH(Material.BIRCH_STAIRS),
        JUNGLE(Material.ACACIA_STAIRS),
        QUARTZ(Material.QUARTZ_STAIRS),
        ACACIA(Material.ACACIA_STAIRS),
        DARK_OAK(Material.DARK_OAK_STAIRS),
        PRISMARINE(Material.PRISMARINE_STAIRS),
        PRISMARINE_BRICK(Material.PRISMARINE_BRICK_STAIRS),
        DARK_PRISMARINE(Material.DARK_PRISMARINE_STAIRS),
        RED_SANDSTONE(Material.RED_SANDSTONE_STAIRS);

        private final Material stairs;

        StairsType(Material stairs) {
            this.stairs = stairs;
        }

        public Material getSlab() {
            return stairs;
        }
    }

    public static Block setStairs(World world, int x, int y, int z, StairsType stairsType, Stairs.Shape shape, Bisected.Half half, BlockFace facing, boolean waterlogged) {
        Block block = setBlock(world, x, y, z, stairsType.stairs);

        Stairs data = (Stairs) block.getBlockData();
        data.setShape(shape);
        data.setHalf(half);
        data.setFacing(facing);
        data.setWaterlogged(waterlogged);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setStructureBlock(World world, int x, int y, int z, StructureBlock.Mode mode) {
        Block block = setBlock(world, x, y, z, Material.STRUCTURE_BLOCK);

        StructureBlock data = (StructureBlock) block.getBlockData();
        data.setMode(mode);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setSwitch(World world, int x, int y, int z, Switch.Face face, BlockFace facing, boolean powered) {
        Block block = setBlock(world, x, y, z, Material.LEVER);

        Switch data = (Switch) block.getBlockData();
        data.setFace(face);
        data.setFacing(facing);
        data.setPowered(powered);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setTechnicalPiston(World world, int x, int y, int z, TechnicalPiston.Type type, BlockFace facing) {
        Block block = setBlock(world, x, y, z, Material.MOVING_PISTON);

        TechnicalPiston data = (TechnicalPiston) block.getBlockData();
        data.setType(type);
        data.setFacing(facing);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setTrapDoor(World world, int x, int y, int z, DoorType doorType, Bisected.Half half, BlockFace facing, boolean open, boolean powered, boolean waterlogged) {
        Block block = setBlock(world, x, y, z, doorType.trapDoor);

        TrapDoor data = (TrapDoor) block.getBlockData();
        data.setHalf(half);
        data.setFacing(facing);
        data.setOpen(open);
        data.setPowered(powered);
        data.setWaterlogged(waterlogged);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setTripwire(World world, int x, int y, int z, boolean disarmed, boolean attached, boolean powered, BlockFace... facing) {
        Block block = setBlock(world, x, y, z, Material.TRIPWIRE);

        Tripwire data = (Tripwire) block.getBlockData();

        if (facing != null) {
            for (BlockFace face : facing) {
                data.setFace(face, true);
            }
        }

        data.setDisarmed(disarmed);
        data.setAttached(attached);
        data.setPowered(powered);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setTripwireHook(World world, int x, int y, int z, BlockFace facing, boolean attached, boolean powered) {
        Block block = setBlock(world, x, y, z, Material.TRIPWIRE_HOOK);

        TripwireHook data = (TripwireHook) block.getBlockData();
        data.setFacing(facing);
        data.setAttached(attached);
        data.setPowered(powered);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setTurtleEgg(World world, int x, int y, int z, int eggs, int hatch) {
        Block block = setBlock(world, x, y, z, Material.TURTLE_EGG);

        TurtleEgg data = (TurtleEgg) block.getBlockData();
        data.setEggs(eggs);
        data.setHatch(hatch);

        block.setBlockData(data, true);

        return block;
    }

    public static Block setWallSign(World world, int x, int y, int z, BlockFace facing, boolean waterlogged) {
        Block block = setBlock(world, x, y, z, Material.WALL_SIGN);

        WallSign data = (WallSign) block.getBlockData();
        data.setFacing(facing);
        data.setWaterlogged(waterlogged);

        block.setBlockData(data, true);

        return block;
    }
}
