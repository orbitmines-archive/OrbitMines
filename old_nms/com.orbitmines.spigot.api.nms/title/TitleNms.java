package com.orbitmines.spigot.api.nms.title;

import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface TitleNms {

    void send(Collection<? extends Player> players, String title, String subTitle, int fadeIn, int stay, int fadeOut);

}
