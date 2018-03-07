package com.orbitmines.spigot.servers.uhsurvival.handlers.map.warzone.lootchest;

import com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.loottable.LootTable;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.ChestRarity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;


/**
 * Created by Robin on 3/7/2018.
 */
public class LootChest {

    private static LootTable normalLoottable = new NormalLootTable();
    private static LootTable rareLoottable = new RareLootTable();
    private static LootTable legendaryLoottable = new LegendaryLootTable();

    private ChestRarity chestRarity;
    private Location location;

    public LootChest(ChestRarity rarity, Location location){
        this.chestRarity = rarity;
        this.location = location;
    }

    public void spawn(){
        LootTable lootTable = null;
        switch(chestRarity){
            case NORMAL:
                lootTable = normalLoottable;
                break;
            case RARE:
                lootTable = rareLoottable;
                break;
            case LEGENDARY:
                lootTable = legendaryLoottable;
                break;
        }
        if(lootTable != null){
            Block block = location.getBlock();
            block.setType(Material.CHEST);
            if(block instanceof Chest){
                Chest chest = (Chest) block;
                lootTable.randomizeInventory(chest.getInventory(), true, 10 /*TODO: ADD LEVEL SYSTEM*/);
            }
        }
    }

    public void remove(){

    }

    public ChestRarity getChestRarity() {
        return chestRarity;
    }

    public Location getLocation() {
        return location;
    }

    //TODO: POPULATE LOOT-TABLES
    private static class NormalLootTable extends LootTable {

        private NormalLootTable() {
            super("NormalLootChest");
            setupItems();
        }

        private void setupItems(){

        }
    }

    private static class RareLootTable extends LootTable {

        private RareLootTable() {
            super("RareLootChest");
            setupItems();
        }

        private void setupItems(){

        }
    }

    private static class LegendaryLootTable extends LootTable {

        private LegendaryLootTable() {
            super("LegendaryLootChest");
            setupItems();
        }

        private void setupItems(){

        }
    }
}
