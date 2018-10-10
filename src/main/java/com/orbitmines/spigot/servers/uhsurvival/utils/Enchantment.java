package com.orbitmines.spigot.servers.uhsurvival.utils;

import com.orbitmines.spigot.servers.uhsurvival.handlers.item.tool.enchantments.Enchantments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Enchantment {

    LIGHTNING("Lightning", 100, 2, new Enchantments.EnchantmentLightning(), ToolType.SWORD, ToolType.BOW),
    EXPLODING("Exploding", 100, 4, new Enchantments.EnchantmentExploding(), ToolType.BOW),
    DRILL("Drill", 100, 1, new Enchantments.EnchantmentDrill(), ToolType.PICKAXE),
    VEINMINER("Veinminer", 100, 4, new Enchantments.EnchantmentVeinMiner(), ToolType.PICKAXE, ToolType.SPADE, ToolType.AXE);

    private Enchantments output;

    private String name;
    private ToolType[] types;

    private int maxLevel;

    private double chance;

    Enchantment(String name, double chance, int maxLevel, Enchantments enchantments, ToolType... types){
        this.name = name;
        this.chance = chance;
        this.maxLevel =  maxLevel;
        this.output = enchantments;
        this.types = types;
    }

    public String getName() {
        return name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public double getChance() {
        return chance;
    }

    public static List<Enchantment> getEnchantments(ToolType type){
        List<Enchantment> enchantments = new ArrayList<>();
        for(Enchantment enchantment : Enchantment.values()){
            System.out.println(enchantment.getName());
            if(Arrays.asList(enchantment.types).contains(type)){
                enchantments.add(enchantment);
            }
        }
        return enchantments;
    }

    public static Enchantment getEnchantment(String name){
        for(Enchantment enchantment : Enchantment.values()){
            if(enchantment.getName().equals(name)){
                return enchantment;
            }
        }
        return null;
    }

    public Enchantments getOutput() {
        return output;
    }
}
