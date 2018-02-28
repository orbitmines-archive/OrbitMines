package com.orbitmines.spigot.servers.uhsurvival.handlers.tool;

import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.Enchantment;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.ToolType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Robin on 2/27/2018.
 */
public class Tool {

    private static final int MAX_LEVEL = 20;

    private ItemStack item;
    private ToolType.Type type;

    private int level;
    private int exp;
    private int maxExp;

    private boolean maxedOut;

    private HashMap<Enchantment, Integer> enchantments;
    private List<String> lore;

    private Tool(ItemStack item, boolean created) {
        this.item = item;
        this.type = ToolType.Type.getType(item.getType());
        if (created) {
            this.level = 1;
            this.exp = 0;
            this.maxExp = 500;
            this.maxedOut = false;
            this.lore = new ArrayList<>();
            this.enchantments = new HashMap<>();
        } else {
            this.lore = item.getItemMeta().getLore();
            deserialize();
        }
        updateTool();
    }

    /* LORE METHODS */
    public void updateTool() {
        try {
            if (lore.get(2).equals(" Maxed Out ")) {
                level = Integer.parseInt(lore.get(1).substring(8, lore.get(1).length() - 1));
                String s = lore.get(2).substring(6, lore.get(2).length() - 1);
                String[] str = s.split("/");
                exp = MathUtils.getInteger(str[0]);
                maxExp = MathUtils.getInteger(str[1]);
            } else {
                setMaxedOut();
            }
            for (int i = 4; i < lore.size(); i++) {
                String str = lore.get(i);
                String[] s = str.split(" ");
                Enchantment ench = Enchantment.getEnchantment(s[0]);
                if (ench != null) {
                    int level = MathUtils.getInteger(s[1]);
                    if (level != -1) {
                        enchantments.put(ench, level);
                    }
                }
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
    }

    private void deserialize() {
        ItemMeta meta = item.getItemMeta();
        lore.set(1, " Level: " + level + " ");
        if (isMaxedOut()) {
            lore.set(2, " Exp: " + exp + "/" + maxExp + " ");
        } else {
            lore.set(2, " Maxed Out ");
        }
        int index = 4;
        for (Enchantment ench : enchantments.keySet()) {
            int level = enchantments.get(ench);
            lore.set(index, ench + " " + level);
            index++;
        }
        lore.set(index, "-----");
        meta.setLore(lore);
        item.setItemMeta(meta);


    }

    /* GETTERS */
    public ToolType.Type getType() {
        return type;
    }

    public ItemStack getItem() {
        return item;
    }

    /* LEVEL METHODS */
    public void setLevel(int level) {
        this.level = MathUtils.clamp(level, 1, MAX_LEVEL);
    }

    private void addLevel(int level) {
        setLevel(this.level + level);

    }

    private void setExp(int exp) {
        this.exp = exp;
        levelUp();
    }

    private void addExp(int exp) {
        setExp(this.exp + exp);
    }

    private void levelUp() {
        if (isMaxedOut()) {
            setMaxedOut();
        } else {
            if (exp >= maxExp) {
                this.exp -= maxExp;
                this.maxExp = 500 * level;
                this.addLevel(1);
            }
        }
    }

    private boolean isMaxedOut() {
        return maxedOut || level >= MAX_LEVEL;
    }

    public void setMaxedOut() {
        this.maxedOut = true;
        this.level = MAX_LEVEL;
        this.exp = 0;
        this.maxExp = 0;
    }

    //ENCHANTMENT METHODS (add, getters, isEnchanted)
    public void addEnchantment(Enchantment enchantment, int level) {
        enchantments.put(enchantment, level);
        updateTool();
    }

    public boolean isEnchanted(Enchantment enchantment) {
        return enchantments.containsKey(enchantment);
    }

    public boolean isEnchanted() {
        return enchantments.isEmpty();
    }

    public int getEnchantment(Enchantment enchantment){
        if(isEnchanted(enchantment)){
            return enchantments.get(enchantment);
        }
        return 0;
    }

    public HashMap<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }


    /* STATIC METHODS */
    public static Tool getTool(ItemStack item, boolean created) {
        if (item != null) {
            return new Tool(item, created);
        } else {
            return null;
        }
    }
}
