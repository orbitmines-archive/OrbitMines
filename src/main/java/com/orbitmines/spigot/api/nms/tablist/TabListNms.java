package com.orbitmines.spigot.api.nms.tablist;

import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface TabListNms {

    void send(Collection<? extends Player> players, String header, String footer);

}
