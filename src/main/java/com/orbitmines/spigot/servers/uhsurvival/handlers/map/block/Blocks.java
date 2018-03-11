package com.orbitmines.spigot.servers.uhsurvival.handlers.map.block;

import com.orbitmines.spigot.servers.uhsurvival.UHSurvival;
import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.ToolType;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.World;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 * Created by Robin on 3/8/2018.
 */
class Blocks {

    private UHSurvival uhSurvival;

    Blocks(BlockManager blockManager, UHSurvival uhSurvival) {
        this.uhSurvival = uhSurvival;
        blockManager.registerBlock(new BlockStone());
        blockManager.registerBlock(new BlockCoalOre());
        blockManager.registerBlock(new BlockIronOre());
        blockManager.registerBlock(new BlockGoldOre());
        blockManager.registerBlock(new BlockRedstoneOre());
        blockManager.registerBlock(new BlockDiamondOre());
        blockManager.registerBlock(new BlockObsidian());
    }

    /* OVER-WORLD BLOCKS */
    private class BlockStone extends BlockManager.Block {

        BlockStone() {
            super(World.WORLD, Material.STONE, (byte) 0, ToolType.Type.PICK_AXE);
            setMinimumLevel(1);
            setSmeltable(true);
            setSmeltedItem(Material.STONE);
            setBrokeExp(4);
        }

        @Override
        public void breakBlock(UHPlayer player, Location block) {}
    }

    private class BlockCoalOre extends BlockManager.Block {

        BlockCoalOre() {
            super(World.WORLD, Material.COAL_ORE, (byte) 0, ToolType.Type.PICK_AXE);
            setAppliedFortune(true);
            setOutputChance(20);
            setBrokeExp(10);
            setMinimumToolLevel(ToolType.ToolMaterial.STONE);
            setMinimumLevel(5);
        }

        @Override
        public void breakBlock(UHPlayer player, Location block) {
            block.getWorld().createExplosion(block, 2F, true);
        }
    }

    private class BlockIronOre extends BlockManager.Block {

        BlockIronOre() {
            super(World.WORLD, Material.IRON_ORE, (byte) 0, ToolType.Type.PICK_AXE);
            setMinimumToolLevel(ToolType.ToolMaterial.STONE);
            setMinimumLevel(10);
            setBrokeExp(25);
            setSmeltable(true);
            setSmeltedItem(Material.IRON_INGOT);
        }

        @Override
        public void breakBlock(UHPlayer player, Location block) {}
    }

    private class BlockGoldOre extends BlockManager.Block {

        BlockGoldOre() {
            super(World.WORLD, Material.GOLD_ORE, (byte) 0, ToolType.Type.PICK_AXE);
            setSmeltable(true);
            setSmeltedItem(Material.GOLD_INGOT);
            setMinimumToolLevel(ToolType.ToolMaterial.IRON);
            setMinimumLevel(10);
            setBrokeExp(40);
        }

        @Override
        public void breakBlock(UHPlayer player, Location block) {}
    }

    private class BlockRedstoneOre extends BlockManager.Block {

        BlockRedstoneOre() {
            super(World.WORLD, Material.REDSTONE_ORE, (byte) 0, ToolType.Type.PICK_AXE);
            setAppliedFortune(true);
            setMinimumLevel(12);
            setMinimumToolLevel(ToolType.ToolMaterial.IRON);
            setOutputChance(15);
            setOutputChance(35);
        }

        @Override
        public void breakBlock(UHPlayer player, Location block) {
            block.getBlock().setType(Material.REDSTONE_WIRE);
        }
    }

    private class BlockDiamondOre extends BlockManager.Block {

        BlockDiamondOre() {
            super(World.WORLD, Material.DIAMOND_ORE, (byte) 0, ToolType.Type.PICK_AXE);
            setMinimumToolLevel(ToolType.ToolMaterial.IRON);
            setMinimumLevel(15);
            setAppliedFortune(true);
            setBrokeExp(100);
        }

        @Override
        public void breakBlock(UHPlayer player, Location block) {}
    }

    private class BlockObsidian extends BlockManager.Block {

        BlockObsidian() {
            super(World.WORLD, Material.OBSIDIAN, (byte) 0, ToolType.Type.PICK_AXE);
            setMinimumLevel(8);
            setMinimumToolLevel(ToolType.ToolMaterial.DIAMOND);
            setBrokeExp(40);
            setOutputChance(50);
        }

        @Override
        public void breakBlock(UHPlayer player, Location block) {
            block.getBlock().setType(Material.STATIONARY_LAVA);
        }
    }
}
