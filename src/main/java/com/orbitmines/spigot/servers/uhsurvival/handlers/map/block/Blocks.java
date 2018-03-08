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

    Blocks(BlockManager blockManager, UHSurvival uhSurvival){
        this.uhSurvival = uhSurvival;
        switch (blockManager.getWorld()){
            case WORLD:
                blockManager.registerBlock(new BlockStone());
                blockManager.registerBlock(new BlockCoalOre());
                blockManager.registerBlock(new BlockIronOre());
                break;
            case NETHER:

                break;
            case END:

                break;
        }
    }
    /* OVER-WORLD BLOCKS */
    private class BlockStone extends BlockManager.Block {

        public BlockStone() {
            super(World.WORLD, Material.STONE, (byte) 0, ToolType.Type.PICK_AXE);
            setMinimumLevel(1);
            setSmeltable(true);
            setSmeltedItem(Material.STONE);
            setBrokeExp(4);
        }

        @Override
        public void breakBlock(UHPlayer player, Location block) {

        }
    }

    private class BlockCoalOre extends BlockManager.Block {

        public BlockCoalOre() {
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

        public BlockIronOre() {
            super(World.WORLD, Material.IRON_ORE, (byte) 0, ToolType.Type.PICK_AXE);
            setMinimumToolLevel(ToolType.ToolMaterial.STONE);
            setMinimumLevel(10);
            setBrokeExp(25);
            setSmeltable(true);
            setSmeltedItem(Material.IRON_INGOT);
        }

        @Override
        public void breakBlock(UHPlayer player, Location block) {

        }
    }
}
