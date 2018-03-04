package com.orbitmines.spigot.servers.uhsurvival.handlers.map.block;

import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.tool.Tool;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.Enchantment;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.ToolType;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;

/**
 * Created by Robin on 3/4/2018.
 */
public class BlockManager {

    private HashMap<Material, Block> blocks;

    public BlockManager(){
        this.blocks = new HashMap<>();
    }

    public void registerBlock(Block block){
        this.blocks.put(block.getMaterial(), block);
    }

    public static abstract class Block {

        private Material material;
        private byte data;

        private ToolType.Type acceptableTool;
        private ToolType.ToolLevel minimumToolLevel;

        private int minimumLevel;
        private Enchantment requiredEnchantment;

        private boolean smeltable;

        private Material smeltedItem;
        private byte smeltedByte;

        private boolean appliedFortune;

        private double brokeExp;

        public Block(Material m, byte data, ToolType.Type acceptableTool){
            this.material = m;
            this.data = data;
            this.acceptableTool = acceptableTool;
            this.minimumLevel = 0;
            this.minimumToolLevel = ToolType.ToolLevel.WOOD;
            this.requiredEnchantment = null;
            this.smeltable = false;
            this.smeltedItem = null;
            this.smeltedByte = 0;
            this.appliedFortune = false;
            this.brokeExp = 0;
        }

        public Block(Material material, byte data, ToolType.Type acceptableTool, ToolType.ToolLevel minimumToolLevel, int minimumLevel, Enchantment requiredEnchantment, boolean smeltable, Material smeltedItem, byte smeltedByte, boolean appliedFortune, double brokeExp) {
            this.material = material;
            this.data = data;
            this.acceptableTool = acceptableTool;
            this.minimumToolLevel = minimumToolLevel;
            this.minimumLevel = minimumLevel;
            this.requiredEnchantment = requiredEnchantment;
            this.smeltable = smeltable;
            this.smeltedItem = smeltedItem;
            this.smeltedByte = smeltedByte;
            this.appliedFortune = appliedFortune;
            this.brokeExp = brokeExp;
        }

        public boolean canBreakBlock(Tool tool){
            if(tool.getType() == acceptableTool){
                if(tool.getToolLevel() == minimumToolLevel){
                    if(tool.getLevel() == minimumLevel){
                        if(tool.isEnchanted(requiredEnchantment)){
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        public ItemBuilder getSmeltedItem(){
            return new ItemBuilder(smeltedItem, 1, smeltedByte);
        }

        public void setSmeltedItem(Material smeltedItem) {
            this.smeltedItem = smeltedItem;
        }

        public abstract void breakBlock(UHPlayer player, Location block);

        public void setMinimumToolLevel(ToolType.ToolLevel minimumToolLevel) {
            this.minimumToolLevel = minimumToolLevel;
        }

        public void setMinimumLevel(int minimumLevel) {
            this.minimumLevel = minimumLevel;
        }

        public void setSmeltedByte(byte smeltedByte) {
            this.smeltedByte = smeltedByte;
        }

        public void setAppliedFortune(boolean appliedFortune) {
            this.appliedFortune = appliedFortune;
        }

        public void setRequiredEnchantment(Enchantment requiredEnchantment) {
            this.requiredEnchantment = requiredEnchantment;
        }

        public Material getMaterial() {
            return material;
        }

        public byte getData() {
            return data;
        }

        public double getBrokeExp() {
            return brokeExp;
        }

        public void setBrokeExp(double brokeExp) {
            this.brokeExp = brokeExp;
        }

        public boolean isSmeltable() {
            return smeltable;
        }

        public void setSmeltable(boolean smeltable) {
            this.smeltable = smeltable;
        }

        public boolean canApplyFortune(){
            return appliedFortune;
        }
    }
}
