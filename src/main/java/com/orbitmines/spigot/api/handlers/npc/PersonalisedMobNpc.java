package com.orbitmines.spigot.api.handlers.npc;
/*
 * OrbitMines - @author Fadi Shawki - 15-6-2018
 */

import com.orbitmines.spigot.api.Mob;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardString;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public abstract class PersonalisedMobNpc extends MobNpc {

    //TODO FIX AFTER SOME TIME APPEARING
    private Map<OMPlayer, Hologram> nameTags;

    public PersonalisedMobNpc(Mob mob, Location spawnLocation, ScoreboardString... displayName) {
        super(mob, spawnLocation, displayName);

        nameTags = new HashMap<>();
    }

    public abstract ScoreboardString[] getLines(OMPlayer omp);

    @Override
    public void update() {
        for (Hologram hologram : nameTags.values()) {
            hologram.setYOff(getYOff());
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

    @Override
    public void setMob(Mob mob) {
        super.setMob(mob);

        for (Hologram hologram : nameTags.values()) {
            hologram.setYOff(getYOff());
            hologram.update();
        }
    }

    @Override
    public Hologram getNameTag() {
        throw new IllegalStateException();
    }

    public Map<OMPlayer, Hologram> getNameTags() {
        return nameTags;
    }

    public void onLogin(OMPlayer omp) {
        Hologram hologram = new Hologram(spawnLocation, getYOff(), Hologram.Face.UP);
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
