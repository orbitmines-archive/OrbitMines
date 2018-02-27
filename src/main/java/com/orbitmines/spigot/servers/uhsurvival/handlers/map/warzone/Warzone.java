package com.orbitmines.spigot.servers.uhsurvival.handlers.map.warzone;

import com.orbitmines.spigot.servers.uhsurvival.handlers.map.mapsection.MapSection;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Robin on 2/27/2018.
 */
public class Warzone extends MapSection {

    private List<UUID> editors;

    public Warzone(World world, int minX, int maxX, int minZ, int maxZ, int x, int z) {
        super(world, minX, maxX, minZ, maxZ, x, z);
        this.editors = new ArrayList<>();
        setPvP(true);
    }

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
