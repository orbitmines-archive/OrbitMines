package com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.loottable;

import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival.utils.FileBuilder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 1/7/2018.
 */
public class LootTable {

    private String lootName;
    private List<LootItem> lootItems;

    /* CONSTRUCTORS */
    protected LootTable(String lootName){
        this.lootName = lootName;
        this.lootItems = new ArrayList<>();
    }

    /* GETTERS */
    public String getName() {
        return lootName;
    }

    public List<ItemStack> getDrops(){
        List<ItemStack> items = new ArrayList<>();
        for(LootItem lootItem : lootItems){
            if(MathUtils.randomize(0, 100, (int) lootItem.getChance())){
                if(lootItem.isTool()){
                    items.add(lootItem.buildTool(true).getItem());
                } else{
                    items.add(lootItem.buildItem());
                }
            }
        }
        return items;
    }

    public List<LootItem> getLootItems() {
        return lootItems;
    }

    public void randomizeInventory(Inventory inventory, boolean enchanted, int explevels){
        for(int i = 0; i < inventory.getSize(); i++) {
            for (LootItem lootItem : lootItems) {
                if (MathUtils.randomize(0, 100, (int) lootItem.getChance())) {
                    if(lootItem.isTool()){
                        lootItem.buildTool(enchanted, explevels);
                    } else {
                        inventory.setItem(i, lootItem.buildItem());
                    }
                }
            }
        }
    }

    /* ADDERS */
    public void addLootItem(LootItem item){
        this.lootItems.add(item);
    }

    public void serialize(FileBuilder f){
        f.set("name", lootName);
        int index = 0;
        for(LootItem item : lootItems){
            f.set("item." + index, item.serialize());
            index++;
        }
        f.set("items", index);
        f.save();
    }

}
