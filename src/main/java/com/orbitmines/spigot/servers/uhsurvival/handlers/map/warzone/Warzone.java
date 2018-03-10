package com.orbitmines.spigot.servers.uhsurvival.handlers.map.warzone;

import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.mapsection.MapSection;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.warzone.lootchest.LootChest;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.ChestRarity;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Robin on 2/27/2018.
 */
public class Warzone extends MapSection {

    //TODO: ADD PVP-TAGGED
    private List<UUID> editors;
    private List<LootChest> chests;

    public Warzone(World world, int minX, int maxX, int minZ, int maxZ, int x, int z) {
        super(world, minX, maxX, minZ, maxZ, x, z);
        this.editors = new ArrayList<>();
        this.chests = new ArrayList<>();
        setPvP(true);
        canSpawnDungeon(false);
        setEnteringMessage("", "");
        setLeavingMessage("", "");

    }
    /* LOOT CHESTS METHODS */
    public void addChest(ChestRarity chestRarity){
        int x = RandomUtils.i(getMaxX(), getMinX());
        int z = RandomUtils.i(getMaxZ(), getMinZ());
        Block location = getWorld().getHighestBlockAt(x,z);
        LootChest chest = new LootChest(chestRarity, location.getLocation());
        chests.add(chest);
        chest.spawn();
    }

    public void addChest(){
        ChestRarity c = null;
        while(c == null) {
            for (ChestRarity chestRarity : ChestRarity.values()) {
                if(RandomUtils.chance(chestRarity.getChance())){
                    c = chestRarity;
                }
            }
        }
        addChest(c);
    }

    public void removeLootChest(LootChest lootChest){
        chests.remove(lootChest);
        lootChest.remove();
    }

    public List<LootChest> getChests() {
        return chests;
    }

    /* EDITORS METHODS */
    public void addEditor(UUID id){
        if(getPlayers().containsKey(id)){
            editors.add(id);
        }
    }

    public void removeEditor(UUID id){
        editors.remove(id);
    }

    public boolean canEdit(UUID id){
        return editors.contains(id);
    }
}
