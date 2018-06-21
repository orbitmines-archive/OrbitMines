package com.orbitmines.spigot.servers.uhsurvival2.handlers.tool;

import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival2.utils.ToolType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Tool {

    private static final int LEVEL_INDEX = 1;
    private static final int EXP_INDEX = 3;

    private static int MAX_LEVEL = 20;

    private List<String> lore;

    private ItemStack item;

    private ToolType type;
    private ToolType.Material material;

    private int level;
    private double exp;
    private double maxExp;
    private boolean maxedOut;

    private Tool(ItemStack item, ToolType type) {
        this.item = item;
        this.type = type;
        this.material = ToolType.Material.getMaterial(item.getType());
        this.lore = !item.hasItemMeta() ? new ArrayList<>() : !item.getItemMeta().hasLore() ? new ArrayList<>() : item.getItemMeta().getLore();
        if (lore.isEmpty()) {
            updateTool(false);
        } else {
            initTool();
        }
    }

    /* TOOL METHODS */
    private void updateTool(boolean update) {
        ItemMeta meta = item.getItemMeta();

        /* FIRST CREATION OF LORE */
        if (!update) {
            this.level = 1;
            this.exp = 0;
            this.maxExp = 500;
            this.maxedOut = false;
            lore.add(0, "-----------------------");
            lore.add(LEVEL_INDEX, " Level: " + level + " ");
            lore.add(2, "-----------------------");
            if (!maxedOut) {
                lore.add(EXP_INDEX, " Exp: " + exp + "/" + maxExp + " ");
            } else {
                lore.add(EXP_INDEX, " Maxed Out");
            }
            lore.add(4, "-----------------------");
        }

        /* UPDATING LORE */
        if (!maxedOut && update) {
            lore.set(LEVEL_INDEX, " Level: " + level + " ");
            lore.set(EXP_INDEX, " Exp: " + exp + "/" + maxExp + " ");
        } else {
            if (maxedOut) {
                lore.set(LEVEL_INDEX, " Level: " + MAX_LEVEL + " ");
                lore.set(EXP_INDEX, " Maxed Out ");
            }
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    private void initTool() {
        if (!lore.isEmpty()) {
            if (!lore.get(EXP_INDEX).equalsIgnoreCase(" Maxed Out ")) {
                level = MathUtils.getInteger(lore.get(LEVEL_INDEX).substring(8, lore.get(LEVEL_INDEX).length() - 1));
                String s = lore.get(EXP_INDEX).substring(6, lore.get(EXP_INDEX).length() - 1);
                String[] str = s.split("/");
                exp = MathUtils.getDouble(str[0]);
                maxExp = MathUtils.getDouble(str[1]);
            } else {
                setMaxedOut();
            }
        }
    }

    static Tool getTool(ItemStack item) {
        if (item != null && item.getType() != Material.AIR) {
            ToolType type = ToolType.getType(item.getType());
            if (type != null) {
                return new Tool(item, type);
            }
        }
        return null;
    }

    /* EXP METHODS */
    private void setExp(double exp) {
        this.exp = exp;
        levelUp();
        updateTool(true);
    }

    public void addExp(double exp) {
        setExp(this.exp + exp);
    }

    /* MAXED OUT METHODS */
    private boolean isMaxedOut() {
        return maxedOut || level >= MAX_LEVEL;
    }

    private void setMaxedOut() {
        this.maxedOut = true;
        this.level = MAX_LEVEL;
        this.exp = 0;
        this.maxExp = 0;
    }

    /* LEVEL METHODS */
    public int getLevel() {
        return level;
    }

    private void setLevel(int level) {
        this.level = MathUtils.clamp(level, 1, MAX_LEVEL);
    }

    private void levelUp() {
        if (!maxedOut) {
            if (exp >= maxExp) {
                this.exp -= maxExp;
                this.maxExp = 500 * level;
                this.addLevel(1);
            }
        }
    }

    public void addLevel(int level) {
        setLevel(this.level + level);
        if (isMaxedOut()) {
            setMaxedOut();
        }
    }

    /* GETTERS */
    public ItemStack getItem() {
        return item;
    }

    public ToolType.Material getMaterial() {
        return material;
    }

    public ToolType getType() {
        return type;
    }
}
