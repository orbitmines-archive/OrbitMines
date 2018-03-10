package com.orbitmines.spigot.servers.uhsurvival.handlers.map.warzone.lootchest;

import com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.loottable.LootItem;
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

    private int timeLeft;

    public LootChest(ChestRarity rarity, Location location){
        this.chestRarity = rarity;
        this.location = location;
        this.timeLeft = rarity.getTime();
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
        Chest chest = (Chest) location.getBlock();
        chest.getBlockInventory().clear();
        location.getBlock().setType(Material.AIR);
    }

    public Location getLocation() {
        return location;
    }

    public boolean hasTimeLeft(){
        return timeLeft <= 0;
    }

    public void tick(int times){
        timeLeft -= times;
    }

    //TODO: POPULATE LOOT-TABLES
    private static class NormalLootTable extends LootTable {

        private NormalLootTable() {
            super("NormalLootChest");
            setupItems();
        }

        private void setupItems(){
            this.addLootItem(new LootItem(Material.APPLE, (byte) 0, 6, 33.0));
            this.addLootItem(new LootItem(Material.LOG, (byte) 0, 6, 15.0));
            this.addLootItem(new LootItem(Material.WOOD, (byte) 0, 12, 15.0));
            this.addLootItem(new LootItem(Material.WOOD_SWORD, (byte) 0, 1, 7.0));
            this.addLootItem(new LootItem(Material.WOOD_AXE, (byte) 0, 1, 7.0));
            this.addLootItem(new LootItem(Material.WOOD_PICKAXE, (byte) 0, 1, 7.0));
            this.addLootItem(new LootItem(Material.WOOD_SPADE, (byte) 0, 1, 7.0));
            this.addLootItem(new LootItem(Material.STONE_SWORD, (byte) 0, 1, 3.5));
            this.addLootItem(new LootItem(Material.STONE_PICKAXE, (byte) 0, 1, 3.5));
            this.addLootItem(new LootItem(Material.STONE_SPADE, (byte) 0, 1, 3.5));
            this.addLootItem(new LootItem(Material.STONE_AXE, (byte) 0, 1, 3.5));
            this.addLootItem(new LootItem(Material.BREAD, (byte) 0, 7, 5.0));
            this.addLootItem(new LootItem(Material.IRON_NUGGET, (byte) 0, 5, 2.0));
            this.addLootItem(new LootItem(Material.GOLD_NUGGET, (byte) 0, 5, 2.0));
        }
    }

    private static class RareLootTable extends LootTable {

        private RareLootTable() {
            super("RareLootChest");
            setupItems();
        }

        private void setupItems() {
            this.addLootItem(new LootItem(Material.IRON_SWORD, (byte) 0, 1, 7.5));
            this.addLootItem(new LootItem(Material.IRON_SPADE, (byte) 0, 1, 7.5));
            this.addLootItem(new LootItem(Material.IRON_AXE, (byte) 0, 1, 7.5));
            this.addLootItem(new LootItem(Material.IRON_PICKAXE, (byte) 0, 1, 7.5));
            this.addLootItem(new LootItem(Material.GOLD_INGOT, (byte) 0, 6, 5.0));
            this.addLootItem(new LootItem(Material.IRON_INGOT, (byte) 0, 5, 4.0));
            this.addLootItem(new LootItem(Material.SADDLE, (byte) 0, 1, 10.0));
            this.addLootItem(new LootItem(Material.GOLD_BARDING, (byte) 0, 1, 12));
            this.addLootItem(new LootItem(Material.IRON_BARDING,  (byte) 0, 1, 7.0));
            this.addLootItem(new LootItem(Material.QUARTZ, (byte) 0, 16, 3.5));
            this.addLootItem(new LootItem(Material.BLAZE_ROD, (byte) 0, 5, 4.0));
            this.addLootItem(new LootItem(Material.NETHER_WARTS, (byte) 0, 5, 2.5));
        }
    }

    private static class LegendaryLootTable extends LootTable {

        private LegendaryLootTable() {
            super("LegendaryLootChest");
            setupItems();
        }

        private void setupItems(){
            this.addLootItem(new LootItem(Material.DIAMOND_SWORD, (byte) 0, 1, 3.5));
            this.addLootItem(new LootItem(Material.DIAMOND_PICKAXE, (byte) 0, 1, 3.5));
            this.addLootItem(new LootItem(Material.DIAMOND_SPADE, (byte) 0, 1, 3.5));
            this.addLootItem(new LootItem(Material.DIAMOND_AXE, (byte) 0, 1, 3.5));
            this.addLootItem(new LootItem(Material.IRON_HELMET, (byte) 0, 1, 4.5));
            this.addLootItem(new LootItem(Material.IRON_CHESTPLATE, (byte) 0, 1, 4.5));
            this.addLootItem(new LootItem(Material.IRON_LEGGINGS, (byte) 0, 1, 4.5));



        }
    }
}
