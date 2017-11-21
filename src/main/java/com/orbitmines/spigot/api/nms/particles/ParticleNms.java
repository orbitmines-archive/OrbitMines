package com.orbitmines.spigot.api.nms.particles;

import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface ParticleNms {

    public void send(Collection<? extends Player> players, Particle particle, float x, float y, float z, float xSize, float ySize, float zSize, float special, int amount);

}
