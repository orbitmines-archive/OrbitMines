package com.orbitmines.spigot.servers.uhsurvival.utils.enums;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Robin on 2/27/2018.
 */
public enum ToolType {

    ARMOR(Type.HELMET, Type.CHEST_PLATE, Type.LEGGINGS, Type.BOOTS),
    TOOL(Type.PICK_AXE, Type.AXE, Type.SPADE, Type.HOE),
    WEAPON(Type.SWORD, Type.BOW),
    ALL(Type.SWORD, Type.BOW, Type.HELMET, Type.CHEST_PLATE, Type.LEGGINGS, Type.BOOTS, Type.PICK_AXE, Type.AXE, Type.SPADE);

    private Type[] types;

    ToolType(Type... types) {
        this.types = types;
    }

    public Type[] getTypes() {
        return types;
    }

    public enum Type {

        SWORD(WEAPON, Action.HIT, Action.INTERACT),
        BOW(WEAPON, Action.SHOOT),
        PICK_AXE(TOOL, Action.MINE),
        AXE(TOOL, Action.MINE),
        SPADE(TOOL, Action.MINE),
        HOE(TOOL, Action.INTERACT),
        HELMET(ARMOR, Action.PROTECT),
        CHEST_PLATE(ARMOR, Action.PROTECT),
        LEGGINGS(ARMOR, Action.PROTECT, Action.INTERACT),
        BOOTS(ARMOR, Action.PROTECT);

        private ToolType type;
        private List<Action> actions;

        Type(ToolType type, Action... actions) {
            this.type = type;
            this.actions = Arrays.asList(actions);
        }

        public ToolType getType() {
            return type;
        }

        public static Type getType(ItemStack item) {
            return getType(item.getType());
        }

        public static Type getType(Material m){
            for (Type type : Type.values()) {
                if (m.name().endsWith(type.name())) {
                    return type;
                }
            }
            return null;
        }

        public List<Action> getActions() {
            return actions;
        }
    }

    //TODO: MOVE BLOCK.JAVA TO ANOTHER CLASS
    public enum Block {

        STONE(Material.STONE, 0, 1.5, true, false),
        GRANITE(Material.STONE, 1, 2, false, false),
        DIORITE(Material.STONE, 3, 2, false, false),
        ANDESITE(Material.STONE, 5, 2, false, false),
        STAINED_CLAY(Material.STAINED_CLAY, -1, 2, false, false),
        DIAMOND_ORE(Material.DIAMOND_ORE, 0, 50.0, false, true),
        EMERALD_ORE(Material.EMERALD_ORE, 0, 80.0, false, true),
        LAPIS_ORE(Material.LAPIS_ORE, 0, 25.0, false, true),
        REDSTONE_ORE(Material.REDSTONE_ORE, 0, 20.0, false, true),
        GOLD_ORE(Material.GOLD_ORE, 0, 30.0, true, false),
        IRON_ORE(Material.IRON_ORE, 0, 10.0, true, false),
        COAL_ORE(Material.COAL_ORE, 0, 5.0, false, true),
        NETHER_QUARTZ(Material.QUARTZ_ORE, 0, 120.0, false, true),
        OAK(Material.LOG, 0, 5.0, true, false),
        SPRUCE(Material.LOG, 1, 5.0, true, false),
        BIRCH(Material.LOG, 2, 5.0, true, false),
        JUNGLE(Material.LOG, 3, 5.0, true, false),
        ACACIA(Material.LOG_2, 0, 10.0, true, false),
        DARK_OAK(Material.LOG_2, 1, 5.0, true, false),
        GRAVEL(Material.GRAVEL, 0, 15.0, false, true),
        DIRT(Material.DIRT, 2, 0, false, false),
        COARSE(Material.DIRT, 1, 12.0, false, false),
        PODZOL(Material.DIRT, 2, 20.0, false, false),
        GRASS(Material.GRASS, 0, 4, false, false),
        SAND(Material.SAND, 0, 3, true, false),
        RED_SAND(Material.SAND, 1, 25.0, true, false);

        private Material m;
        private double exp;
        private byte data;
        private boolean smeltable;
        private boolean fortune;

        Block(Material m, int data, double exp, boolean smeltable, boolean fortune){
            this.m = m;
            this.data = (byte) data;
            this.exp = exp;
            this.smeltable = smeltable;
            this.fortune = fortune;
        }

        public Material getMaterial() {
            return m;
        }

        public double getExp() {
            return exp;
        }

        public boolean isSmeltable() {
            return smeltable;
        }

        public boolean appliedFortune(){
            return fortune;
        }

        public byte getData() {
            return data;
        }

        public static Block getBlockByMaterial(Material m, byte data){
            for(Block block : Block.values()){
                if(block.getMaterial() == m){
                    if(block.getData() != -1){
                        if(block.getData() == data){
                            return block;
                        }
                    }else {
                        return block;
                    }
                }
            }
            return null;
        }
    }

}
