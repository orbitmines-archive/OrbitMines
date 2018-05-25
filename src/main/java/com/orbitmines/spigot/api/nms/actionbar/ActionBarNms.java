package com.orbitmines.spigot.api.nms.actionbar;


import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface ActionBarNms {

    void send(Collection<? extends Player> players, String actionBar);

}
