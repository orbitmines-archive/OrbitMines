package com.orbitmines.spigot.servers.uhsurvival.utils;

import java.util.Arrays;

public enum ToolType {

    SWORD(Type.WEAPON, Material.WOOD, Material.STONE, Material.IRON, Material.GOLD, Material.DIAMOND),
    BOW(Type.WEAPON, Material.WOOD),
    PICKAXE(Type.TOOL, Material.WOOD, Material.STONE, Material.IRON, Material.GOLD, Material.DIAMOND),
    AXE(Type.TOOL, Material.WOOD, Material.STONE, Material.IRON, Material.GOLD, Material.DIAMOND),
    SPADE(Type.TOOL, Material.WOOD, Material.STONE, Material.IRON, Material.GOLD, Material.DIAMOND),
    HELMET(Type.ARMOR, Material.LEATHER, Material.CHAINMAIL, Material.IRON, Material.GOLD, Material.DIAMOND),
    CHESTPLATE(Type.ARMOR, Material.LEATHER, Material.CHAINMAIL, Material.IRON, Material.GOLD, Material.DIAMOND),
    LEGGINGS(Type.ARMOR, Material.LEATHER, Material.CHAINMAIL, Material.IRON, Material.GOLD, Material.DIAMOND),
    BOOTS(Type.ARMOR, Material.LEATHER, Material.CHAINMAIL, Material.IRON, Material.GOLD, Material.DIAMOND);

    private Material[] acceptedMaterials;
    private Type type;

    ToolType(Type type, Material... acceptedMaterials){
        this.acceptedMaterials = acceptedMaterials;
        this.type = type;
    }

    public boolean isAcceptedMaterial(Material material){
        return Arrays.asList(acceptedMaterials).contains(material);
    }

    public Type getType() {
        return type;
    }

    public static ToolType getType(org.bukkit.Material material){
        for(ToolType type : ToolType.values()){
            if(material.name().endsWith(type.name())){
                return type;
            }
        }
        return null;
    }

    public enum Type {

        WEAPON(SWORD, BOW),
        TOOL(AXE, SPADE, PICKAXE),
        ARMOR(HELMET, CHESTPLATE, LEGGINGS, BOOTS);

        private ToolType[] types;

        Type(ToolType... types){
            this.types = types;
        }

        public ToolType[] getTypes() {
            return types;
        }
    }

    public enum Material {
        WOOD,
        LEATHER,
        STONE,
        CHAINMAIL,
        IRON,
        GOLD,
        DIAMOND;

        public static Material getMaterial(org.bukkit.Material material){
            for(Material m : Material.values()){
                if(material.name().startsWith(m.name())){
                    return m;
                }
            }
            return null;
        }
    }
}
