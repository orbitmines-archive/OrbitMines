package com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.loottable;

import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival.handlers.tool.Tool;
import com.orbitmines.spigot.servers.uhsurvival.handlers.tool.enchantments.EnchantmentManager;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.Enchantment;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.ToolType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;


/**
 * Created by Robin on 1/7/2018.
 */
public class LootItem {

    private ItemBuilder item;

    private boolean enchantable;
    private boolean isTool;

    private int level;
    private HashMap<Enchantment, Integer> enchantments;

    private int maxAmount;
    private double chance;

    /* CONSTRUCTOR */
    public LootItem(Material m, byte data, int maxAmount, double chance){
        this.item = new ItemBuilder(m, 1, data);
        this.maxAmount = maxAmount;
        this.chance = chance;
        if(ToolType.Type.getType(m) != null){
            this.isTool = true;
            this.enchantable = true;
        }
        this.level = 1;
        this.enchantments = new HashMap<>();
    }

    /* BUILD METHODS (Item, Tool) */
    protected ItemStack buildItem(){
        return item.setAmount(MathUtils.randomInteger(maxAmount) + 1).build();
    }

    protected Tool buildTool(boolean enchanted, int expLevels){
        if(isTool){
            Tool tool = Tool.getTool(buildItem(), false);
            tool.setLevel(level);
            if(enchanted && enchantable && !enchantments.isEmpty()){
                EnchantmentManager.enchant(tool, expLevels);
            } else {
                for(Enchantment ench : enchantments.keySet()){
                    int level = enchantments.get(ench);
                    tool.addEnchantment(ench, level);
                }
            }
            return tool;
        }
        return null;
    }

    protected Tool buildTool(boolean enchanted){
        return buildTool(enchanted, MathUtils.randomInteger(30) + 1);
    }

    /* GETTERS */
    public ItemBuilder getItem() {
        return item;
    }

    public boolean isEnchantable() {
        return enchantable;
    }

    public boolean isTool() {
        return isTool;
    }

    protected double getChance() {
        return chance;
    }

    public String serialize() {
        String e = "";
        for(Enchantment ench : enchantments.keySet()){
            e = e + ench.getName() + ":" + enchantments.get(ench) + "/";
        }
        e = e.substring(0, e.length() - 1);
        return item.getMaterial() + "|" + item.getDurability() + "|" + maxAmount + "|" + chance + "|" + level + "|" + e;
    }

    public static LootItem getLootItem(String data){
        String[] s = data.split("\\|");
        LootItem item = new LootItem(Material.getMaterial(s[0]), Byte.parseByte(s[1]), Integer.parseInt(s[2]), Double.parseDouble(s[3]));
        item.setLevel(Integer.parseInt(s[4]));
        if(s[5] != null){
            String[] st = s[5].split("/");
            for(String str : st){
                String[] stri = str.split(":");
                Enchantment e = Enchantment.getEnchantment(stri[0]);
                if(e != null){
                    int level = Integer.parseInt(stri[1]);
                    item.addEnchantment(e, level);
                }
            }
        }
        return item;
    }

    /* SETTERS */
    public LootItem setLevel(int level) {
        this.level = level;
        return this;
    }

    public LootItem addEnchantment(Enchantment enchantment, int level) {
        this.enchantments.put(enchantment, level);
        return this;
    }

    public LootItem setAmount(int maxAmount){
        this.maxAmount = maxAmount;
        return this;
    }

    public LootItem setChance(double chance){
        this.chance = chance;
        return this;
    }

    /* TO METHODS */
    public ItemStack toItem(){
        item.setDisplayName("LootItem: " + item.getMaterial() + ":" + item.getDurability());
        item.addLore("Chance: " + chance);
        item.addLore("MaxAmount: " + maxAmount);
        if(isTool){
            item.addLore("Level: " + level);
            item.addLore("Enchantments: ");
            for(Enchantment enchantment : enchantments.keySet()){
                int level = enchantments.get(enchantment);
                item.addLore("  -" + enchantment.getName() + ":" + level);
            }
        }
        return item.build();
    }

    public static LootItem toLoot(ItemStack item){
        double chance = MathUtils.getInteger(item.getItemMeta().getLore().get(0).split(" ")[1]);
        int maxAmount = MathUtils.getInteger(item.getItemMeta().getLore().get(1).split(" ")[1]);
        LootItem i = new LootItem(item.getType(), item.getData().getData(), maxAmount, chance);
        if(i.isTool){
            int level = MathUtils.getInteger(item.getItemMeta().getLore().get(2).split(" ")[2]);
            i.setLevel(level);
            for(int y = 3; y < item.getItemMeta().getLore().size(); y++){
                String e = item.getItemMeta().getLore().get(y).substring(3);
                String[] ench = e.split(":");
                Enchantment en = Enchantment.getEnchantment(ench[0]);
                if(en != null){
                    int l = MathUtils.getInteger(ench[1]);
                    i.addEnchantment(en, l);
                }
            }
        }
        return i;
    }
}
