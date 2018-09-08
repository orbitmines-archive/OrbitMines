package com.orbitmines.spigot.servers.uhsurvival.handlers.map.block;

import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival.UHSurvival;
import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.item.tool.Tool;
import com.orbitmines.spigot.servers.uhsurvival.handlers.item.tool.enchantments.Enchantments;
import com.orbitmines.spigot.servers.uhsurvival.utils.Enchantment;
import com.orbitmines.spigot.servers.uhsurvival.utils.ToolType;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockManager {

    private UHSurvival uhSurvival;

    private HashMap<Material, List<Block>> blocks;

    public BlockManager(UHSurvival uhSurvival){
        this.blocks = new HashMap<>();
        this.uhSurvival = uhSurvival;
        /* init blocks */
        new Block(Material.STONE, (byte) 0, ToolType.PICKAXE, 250){};
        new Block(Material.DIRT, (byte) 0, ToolType.SPADE, 5){};
        new Block(Material.OAK_LOG, (byte) 0, ToolType.AXE, 5){};
    }

    /* BOOLEANS */
    public boolean breakBlock(UHPlayer player, org.bukkit.block.Block block){
        Tool tool = player.getToolInventory().getMainHand();
        Block b = getBlock(block);
        if(tool != null) {
            if (b != null) {
                if (b.canBreakBlock(tool)) {
                    if (b.canOutput()) {
                        b.output(player, block.getLocation());

                    }
                    tool.addExp(b.getExp());
                } else {
                    return true;
                }
            }
            for (Enchantment enchantment : tool.getEnchantments().keySet()) {
                if (enchantment.getOutput() instanceof Enchantments.BreakEnchantment) {
                    if (MathUtils.randomize(0, 100, (int) enchantment.getChance())) {
                        ((Enchantments.BreakEnchantment) enchantment.getOutput()).output(player, block, tool.getLevel(enchantment));
                    }
                }
            }
        }
        return false;
    }

    /* SETTERS */
    private void addBlock(Block block){
        List<Block> blocks = this.blocks.get(block.material);
        if(blocks == null){
            blocks = new ArrayList<>();
        }
        blocks.add(block);
        this.blocks.put(block.material, blocks);
    }

    /* GETTERS */
    private Block getBlock(org.bukkit.block.Block block){
        List<Block> blocks = this.blocks.get(block.getType());
        if(blocks != null) {
            for (Block b : blocks) {
                if (b.isBlock(block)) {
                    return b;
                }
            }
        }
        return null;
    }

    /* SUB-CLASSES */
    public abstract class Block {

        private Material material;
        private byte data;

        private ToolType tool;
        private ToolType.Material toolMaterial;

        private int minimumLevel;

        private double exp;

        private double outputChance;

        public Block(Material material, byte data, ToolType type, double exp){
            this(material, data, type, ToolType.Material.WOOD, 0, exp, 100);
        }

        public Block(Material material, byte data, ToolType type, int minimumLevel, double exp){
            this(material, data, type, ToolType.Material.WOOD, minimumLevel, exp, 0);
        }

        public Block(Material material, byte data, ToolType type, ToolType.Material toolMaterial, double exp){
            this(material, data, type, toolMaterial, 0, exp, 0);
        }

        public Block(Material material, byte data, ToolType type, ToolType.Material toolMaterial, int minimumLevel, double exp, double outputchance){
            this.material = material;
            this.data = data;
            this.tool = type;
            this.toolMaterial = toolMaterial;
            this.minimumLevel = minimumLevel;
            this.exp = exp;
            this.outputChance = outputchance;
            addBlock(this);
        }

        /* ABSTRACT METHODS */
        public void output(UHPlayer player, Location block){}

        /* BOOLEANS */
        boolean canBreakBlock(Tool tool){
            if(tool.getType() == this.tool){
                if(tool.getMaterial().ordinal() >= toolMaterial.ordinal()){
                    return tool.getLevel() >= minimumLevel;
                }
            }
            return false;
        }

        boolean isBlock(org.bukkit.block.Block block){
            return block.getType() == material && block.getData() == data;
        }

        boolean canOutput(){
            return MathUtils.randomize(0, 100,(int) outputChance);
        }

        /* GETTERS */
        double getExp() {
            return exp;
        }

        /* SETTERS */

        public void setMaterial(ToolType.Material toolMaterial) {
            this.toolMaterial = toolMaterial;
        }

        public void setLevel(int minimumLevel) {
            this.minimumLevel = minimumLevel;
        }

        public void setExp(double exp) {
            this.exp = exp;
        }

        public void setChance(double outputChance) {
            this.outputChance = outputChance;
        }
    }
}
