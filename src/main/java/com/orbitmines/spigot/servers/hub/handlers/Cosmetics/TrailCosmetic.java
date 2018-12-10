package com.orbitmines.spigot.servers.hub.handlers.Cosmetics;

import com.orbitmines.spigot.api.handlers.OMPlayer;

public abstract class TrailCosmetic extends Cosmetic {

    protected OMPlayer player;


    protected TrailCosmetic(OMPlayer player) {
        this.player = player;
    }

    public void update() {

    }

}
