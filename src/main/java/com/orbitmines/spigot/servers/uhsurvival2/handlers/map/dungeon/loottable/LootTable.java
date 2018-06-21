package com.orbitmines.spigot.servers.uhsurvival2.handlers.map.dungeon.loottable;

import com.orbitmines.spigot.api.utils.MathUtils;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class LootTable {

    private String name;
    private List<LootItem> items;

    LootTable(String name){
        this.name = name;
        this.items = new ArrayList<>();
    }

    /* RANDOMIZE METHODS */
    public void randomizeInventory(Inventory inventory){
        for(int i = 0; i < inventory.getSize(); i++){
            for(LootItem lootItem : items){
                if(MathUtils.randomize(0, 100, (int) lootItem.getChance())){
                    inventory.setItem(i, lootItem.build());
                }
            }
        }
    }

    /* GETTERS */
    public String getName() {
        return name;
    }

    public List<LootItem> getItems() {
        return items;
    }

    /* SETTERS */
    public void addLootItem(LootItem item){
        this.items.add(item);
    }
}
