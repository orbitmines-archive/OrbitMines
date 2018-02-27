package com.orbitmines.spigot.servers.uhsurvival.utils.enums;

/**
 * Created by Robin on 2/27/2018.
 */
public enum Enchantment {

    STRIKE("Strike", 0, 4, 24, null, Action.HIT, ToolType.Type.SWORD, ToolType.Type.AXE),
    LIGHT_CRIT("Lightcrit", 1, 2, 15, Enchantment.DARK_CRIT, Action.HIT, ToolType.Type.SWORD, ToolType.Type.AXE),
    DARK_CRIT("Darkcrit", 2, 2, 15, Enchantment.LIGHT_CRIT, Action.HIT, ToolType.Type.SWORD, ToolType.Type.AXE),
    LIFE_STEAL("Lifesteal", 3, 5, 12, null, Action.HIT, ToolType.Type.SWORD),
    EXPLODING("Exploding", 4, 2, 10, Enchantment.ENDER, Action.SHOOT, ToolType.Type.BOW),
    STUN("Stunning", 5, 2, 23, null, Action.SHOOT, ToolType.Type.BOW),
    ENDER("Ender", 6, 1, 6, Enchantment.EXPLODING, Action.SHOOT, ToolType.Type.BOW),
    AUTO_SMELT("Autosmelt", 7, 3, 14,  null, Action.MINE, ToolType.Type.PICK_AXE, ToolType.Type.AXE),
    NIGHT_VISION("Nightvision", 8, 2, 66, null, Action.INTERACT, ToolType.Type.HELMET),
    EXPLOSIVE_DEATH("ExplosiveDeath", 9, 2, 36, Enchantment.RESURRECTION, Action.PROTECT, ToolType.Type.CHEST_PLATE),
    REFLECT("Reflect", 10, 4, 8, null, Action.PROTECT, ToolType.ARMOR.getTypes()),
    ENDER_LEGGINGS("EnderLeggings", 11, 1, 5, null, Action.INTERACT, ToolType.Type.LEGGINGS),
    RESURRECTION("Resurrection", 12, 2, 5, Enchantment.EXPLOSIVE_DEATH, Action.PROTECT, ToolType.ARMOR.getTypes());
    //TODO: ADD LIGHTNING ENCHANTMENT


    private String name;
    private int chance;
    private int id;
    private int maxLevel;
    private Action action;
    private ToolType.Type[] types;
    private Enchantment counterEnchantment;

    Enchantment(String name, int id, int maxLevel, int chance, Enchantment counter, Action action, ToolType.Type... types) {
        this.name = name;
        this.id = id;
        this.maxLevel = maxLevel;
        this.chance = chance;
        this.counterEnchantment = counter;
        this.action = action;
        this.types = types;
    }

    public String getName() {
        return name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public ToolType.Type[] getTypes() {
        return types;
    }

    public int getId() {
        return id;
    }

    public Enchantment getCounterEnchantment() {
        return counterEnchantment;
    }

    public Action getAction() {
        return action;
    }

    public static Enchantment getEnchantment(String ench){
        for(Enchantment enchantment : Enchantment.values()){
            if(enchantment.name.equalsIgnoreCase(ench)){
                return enchantment;
            }
        }
        return null;
    }

    public int getChance() {
        return chance;
    }

}
