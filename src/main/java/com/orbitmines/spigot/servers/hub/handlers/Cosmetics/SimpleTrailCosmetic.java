package com.orbitmines.spigot.servers.hub.handlers.Cosmetics;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.particle.Particle;

public class SimpleTrailCosmetic extends TrailCosmetic {

    private Particle particle;

    private SimpleTrailCosmetic(OMPlayer player, Particle particle) {
        super(player);
        this.particle = particle;
    }


    public void update() {
        super.update();
        particle.setLocation(player.getLocation());
        particle.send();
    }

    public static SimpleTrailCosmetic TestTrail(OMPlayer p) {
        Particle test = new Particle(org.bukkit.Particle.FLAME);

        return new SimpleTrailCosmetic(p, test);
    }
}
