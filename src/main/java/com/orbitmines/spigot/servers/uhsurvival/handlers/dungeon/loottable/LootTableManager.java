package com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.loottable;

import com.orbitmines.spigot.servers.uhsurvival.utils.FileBuilder;
import org.bukkit.World;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Robin on 2/19/2018.
 */
public class LootTableManager {

    private static LootTableManager lootTableManager = new LootTableManager();

    protected HashMap<String, LootTable> lootTables;
    protected HashMap<LootTable, FileBuilder> files;

    LootTableManager() {
        this.lootTables = new HashMap<>();
        this.files = new HashMap<>();
    }

    public LootTable createLootTable(World world, String name, LootItem... lootItems) {
        LootTable lootTable = new LootTable(name);
        FileBuilder fb = new FileBuilder("loottable_" + name, world.getWorldFolder().getPath() + "/dungeons/loottables");
        for (LootItem item : lootItems) {
            lootTable.addLootItem(item);
        }
        this.files.put(lootTable, fb);
        this.lootTables.put(name, lootTable);
        return lootTable;
    }

    public LootTable getLootTable(String name) {
        return lootTables.get(name);
    }

    public boolean isLootTable(String name) {
        return lootTables.get(name) != null;
    }

    /* REGISTER */
    public void registerLootTable(World world, LootTable lootTable) {
        this.lootTables.put(lootTable.getName(), lootTable);
        this.files.put(lootTable, new FileBuilder("loottable_" + lootTable.getName(), world.getWorldFolder().getPath() + "/dungeons/loottables"));
    }

    /* DESERIALIZE & SERIALIZE */
    public void serialize() {
        for (LootTable lootTable : lootTables.values()) {
            lootTable.serialize(files.get(lootTable));
        }
    }

    public void deserialize(File f) {
        File[] files = f.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.getName().startsWith("loottable_")) {
                    FileBuilder fb = new FileBuilder(file);
                    LootTable lootTable = new LootTable((String) fb.get("name"));
                    int index = (int) fb.get("items");
                    for (int i = 0; i < index; i++) {
                        lootTable.addLootItem(LootItem.getLootItem((String) fb.get("item." + index)));
                    }
                    this.lootTables.put(lootTable.getName(), lootTable);
                    this.files.put(lootTable, fb);
                }
            }
        }
    }

    public static LootTableManager get(){
        return lootTableManager;
    }
}
