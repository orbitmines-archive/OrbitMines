package com.orbitmines.spigot.api.handlers.npc;
/*
 * OrbitMines - @author Fadi Shawki - 15-6-2018
 */

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardString;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public abstract class PersonalisedFloatingItem extends FloatingItem {

    //TODO FIX AFTER SOME TIME APPEARING
    private Map<OMPlayer, Hologram> nameTags;

    public PersonalisedFloatingItem(ItemBuilder itemBuilder, Location spawnLocation) {
        super(itemBuilder, spawnLocation);

        nameTags = new HashMap<>();
    }

    public abstract ScoreboardString[] getLines(OMPlayer omp);

    @Override
    public void update() {
        for (Hologram hologram : nameTags.values()) {
            hologram.update();
        }
    }

    @Override
    protected void spawn() {
        super.spawn();

        for (OMPlayer omp : OMPlayer.getPlayers()) {
            onLogin(omp);
        }
    }

    @Override
    protected void despawn() {
        super.despawn();

        for (OMPlayer omp : OMPlayer.getPlayers()) {
            onLogout(omp);
        }
    }

    public Map<OMPlayer, Hologram> getNameTags() {
        return nameTags;
    }

    public void onLogin(OMPlayer omp) {
        Hologram hologram = new Hologram(spawnLocation.clone().subtract(0, 1, 0));
        hologram.create(omp.getPlayer());

        nameTags.put(omp, hologram);

        for (ScoreboardString string : getLines(omp)) {
            hologram.addLine(string, true);
        }
    }

    public void onLogout(OMPlayer omp) {
        if (!nameTags.containsKey(omp))
            return;

        /* Destroy hologram on logout, cause the hologram was only meant for this player */
        nameTags.get(omp).destroy();

        nameTags.remove(omp);
    }
}
