package com.orbitmines.spigot.servers.uhsurvival.handlers.map.block;

import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.utils.MathUtils;
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

    public boolean breakBlock(UHPlayer player, Location block){
        Tool tool = player.getUHInventory().getMainHand();
        Block b = blocks.get(block.getBlock().getType());
        if(b != null){
            if(b.canBreakBlock(tool)){
                tool.addExp((int) b.getBrokeExp());
                if(MathUtils.randomize(0, 100, (int) b.getOutputChance())) {
                    b.breakBlock(player, block);
                }
                if(tool.isEnchanted(Enchantment.AUTO_SMELT) && b.isSmeltable()){
                    block.getBlock().setType(Material.AIR);
                    int amount = 1;
                    if(b.canApplyFortune()) {
                         //TODO: ADD FORTUNE WITH MODIFIERS
                    }
                    block.getWorld().dropItemNaturally(block, b.getSmeltedItem().setAmount(amount).build());
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public void registerBlock(Block block){
        this.blocks.put(block.getMaterial(), block);
    }

    public static abstract class Block {

        private Material material;
        private byte data;

        private ToolType.Type acceptableTool;
        private ToolType.ToolMaterial minimumToolLevel;

        private int minimumLevel;
        private Enchantment requiredEnchantment;

        private boolean smeltable;

        private Material smeltedItem;
        private byte smeltedByte;

        private boolean appliedFortune;

        private double brokeExp;

        private double outputChance;

        public Block(Material m, byte data, ToolType.Type acceptableTool){
            this.material = m;
            this.data = data;
            this.acceptableTool = acceptableTool;
            this.minimumLevel = 0;
            this.minimumToolLevel = ToolType.ToolMaterial.WOOD;
            this.requiredEnchantment = null;
            this.smeltable = false;
            this.smeltedItem = null;
            this.smeltedByte = 0;
            this.appliedFortune = false;
            this.brokeExp = 0;
            this.outputChance = 100;
        }

        public Block(Material material, byte data, ToolType.Type acceptableTool, ToolType.ToolMaterial minimumToolLevel, int minimumLevel, Enchantment requiredEnchantment, boolean smeltable, Material smeltedItem, byte smeltedByte, boolean appliedFortune, double brokeExp, double outputChance) {
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
            this.outputChance = outputChance;
        }

        public boolean canBreakBlock(Tool tool){
            if(tool.getType() == acceptableTool){
                if(tool.getToolMaterial() == minimumToolLevel){
                    if(tool.getLevel() == minimumLevel){
                        if(requiredEnchantment != null && tool.isEnchanted(requiredEnchantment)){
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

        public void setMinimumToolLevel(ToolType.ToolMaterial minimumToolMaterial) {
            this.minimumToolLevel = minimumToolMaterial;
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

        public double getOutputChance() {
            return outputChance;
        }

        public void setOutputChance(double outputChance) {
            this.outputChance = outputChance;
        }
    }
}
