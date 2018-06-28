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

    private static final String LEVEL = " §fLevel§8: §b%d ";
    private static final String EXP = " §fExp§8: §b%.1f§9/§b%.1f ";
    private static final String MAXED_OUT = " §4Maxed Out ";
    private static final String LINE = "§8§m-----------------------";

    private static final int OFFSET_EXP_LEFT = 12;
    private static final int OFFSET_EXP_RIGHT = 1;

    private static final int OFFSET_LEVEL_LEFT = 14;
    private static final int OFFSET_LEVEL_RIGHT = 1;
    //WHEN EDITING THIS, DON'T FORGET TO EDIT THE / ASWELL IN THE updateTool(boolean) METHOD!

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
            lore.add(0, LINE);
            lore.add(LEVEL_INDEX, String.format(LEVEL, level));
            lore.add(2, LINE);
            if (!maxedOut) {
                lore.add(EXP_INDEX, String.format(EXP, exp, maxExp));
            } else {
                lore.add(EXP_INDEX, MAXED_OUT);
            }
            lore.add(4, LINE);
        }

        /* UPDATING LORE */
        if (!maxedOut && update) {
            lore.set(LEVEL_INDEX, String.format(LEVEL, level));
            lore.set(EXP_INDEX, String.format(EXP, exp, maxExp));
        } else {
            if (maxedOut) {
                lore.set(LEVEL_INDEX, String.format(LEVEL, MAX_LEVEL));
                lore.set(EXP_INDEX, MAXED_OUT);
            }
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    private void initTool() {
        if (!lore.isEmpty()) {
            if (!lore.get(EXP_INDEX).equalsIgnoreCase(MAXED_OUT)) {
                level = MathUtils.getInteger(lore.get(LEVEL_INDEX).substring(OFFSET_LEVEL_LEFT, lore.get(LEVEL_INDEX).length() - OFFSET_LEVEL_RIGHT));
                String s = lore.get(EXP_INDEX).substring(OFFSET_EXP_LEFT, lore.get(EXP_INDEX).length() - OFFSET_EXP_RIGHT);
                String[] str = s.split("/");
                exp = MathUtils.getDouble(str[0].substring(0, str[0].length() - 2));
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
