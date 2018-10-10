package com.orbitmines.spigot.servers.uhsurvival.handlers.map.dungeon.loottable;

import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival.UHSurvival;
import com.orbitmines.spigot.servers.uhsurvival.utils.FileBuilder;
import org.bukkit.Material;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LootTableManager {

    private List<LootTable> lootTables;
    private HashMap<LootTable, FileBuilder> files;

    private UHSurvival uhSurvival;

    private String directory = "loottables";

    public LootTableManager(UHSurvival uhSurvival){
        this.uhSurvival = uhSurvival;
        this.lootTables = new ArrayList<>();
        this.files = new HashMap<>();
        init();
    }

    /* SETTERS */
    public void createLootTable(String name, LootItem... items){
        if(!isLootTable(name)){
            LootTable lootTable = new LootTable(name);
            FileBuilder fb = new FileBuilder(directory, "loottable_" + name + ".yml");
            for(LootItem lootItem : items){
                lootTable.addLootItem(lootItem);
            }
            lootTables.add(lootTable);
            files.put(lootTable, fb);
        }
    }

    /* BOOLEANS */
    public boolean isLootTable(String lootName){
        return getLootTable(lootName) != null;
    }

    /* GETTERS */
    public LootTable getLootTable(String lootName){
        for(LootTable lootTable : lootTables){
            if(lootTable.getName().equals(lootName)){
                return lootTable;
            }
        }
        return null;
    }

    public List<LootTable> getLootTables() {
        return lootTables;
    }

    /* FILE METHODS */
    public void serialize(){
        for(LootTable lootTable : files.keySet()){
            FileBuilder f = files.get(lootTable);
            f.set("name", lootTable.getName());
            int index = 0;
            for(LootItem item : lootTable.getItems()){
                f.set("item." + index, item.toString());
            }
            f.set("items", index);
            f.save();
        }
    }

    private void init(){
        File file = new File(directory);
        if(file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.getName().startsWith("loottable_")) {
                        FileBuilder fb = new FileBuilder(f);
                        LootTable lootTable = new LootTable((String) fb.get("name"));
                        int index = (int) fb.get("items");
                        for (int i = 0; i < index; i++) {
                            String[] values = ((String) fb.get("item." + i)).split("\\|");
                            LootItem item = new LootItem(Material.valueOf(values[0]), Byte.parseByte(values[1]), MathUtils.getInteger(values[2]), MathUtils.getDouble(values[3]));
                            lootTable.addLootItem(item);
                        }
                        lootTables.add(lootTable);
                        this.files.put(lootTable, fb);
                    }
                }
            }
        }
    }
}
