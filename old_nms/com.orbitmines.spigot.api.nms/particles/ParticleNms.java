package com.orbitmines.spigot.api.nms.particles;

import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface ParticleNms {

    void send(Collection<? extends Player> players, String particle, boolean longDistance, float x, float y, float z, float xOff, float yOff, float zOff, float speed, int amount, int... args);

}
