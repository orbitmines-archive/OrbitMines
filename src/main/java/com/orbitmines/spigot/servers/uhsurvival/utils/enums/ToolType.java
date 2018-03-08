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

        public ToolType getType() {
            return type;
        }

        public List<Action> getActions() {
            return actions;
        }
    }

    public enum ToolMaterial {

        WOOD,
        LEATHER,
        STONE,
        GOLD,
        IRON,
        DIAMOND;

        public static ToolMaterial getToolLevelByType(Material m){
            for(ToolMaterial toolMaterial : ToolMaterial.values()){
                if(m.name().startsWith(toolMaterial.name())){
                    return toolMaterial;
                }
            }
            return null;
        }

    }
}
