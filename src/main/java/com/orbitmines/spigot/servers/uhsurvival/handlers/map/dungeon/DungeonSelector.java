package com.orbitmines.spigot.servers.uhsurvival.handlers.map.dungeon;

import com.orbitmines.api.StaffRank;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class DungeonSelector {

    private HashMap<OMPlayer, Location> firstCorner;
    private HashMap<OMPlayer, Location> secondCorner;

    private StaffRank requiredRank;

    private Material material;
    private byte data;
    private String displayname = "§6§lDungeon Selector";

    DungeonSelector(StaffRank requiredRank, Material m, byte data){
        this.requiredRank = requiredRank;
        this.material = m;
        this.data = data;
        this.firstCorner = new HashMap<>();
        this.secondCorner = new HashMap<>();
    }

    /* CLICK METHODS */
    public void rightClick(OMPlayer player, Location location){
        this.firstCorner.put(player, location);
    }

    public void leftClick(OMPlayer player, Location location){
        this.secondCorner.put(player, location);
    }

    /* GETTERS */
    Location[] getLocations(OMPlayer player){
        return new Location[]{firstCorner.get(player), secondCorner.get(player)};
    }

    public ItemStack getSelector(){
        return new ItemBuilder(material, 1).setDisplayName(displayname).build();
    }

    /* BOOLEANS */
    public boolean hasSelectedArea(OMPlayer player){
        return firstCorner.get(player) != null && secondCorner.get(player) != null;
    }

    public boolean canSelect(StaffRank rank, ItemStack item){
        if(item != null && item.getType() != Material.AIR) {
            if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                return item.getItemMeta().getDisplayName().equals(displayname) && item.getType() == material && item.getData().getData() == data && rank.ordinal() >= requiredRank.ordinal();
            }
        }
        return false;
    }
}
