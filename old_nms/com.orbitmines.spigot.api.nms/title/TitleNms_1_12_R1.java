package com.orbitmines.spigot.api.nms.title;

import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by Fadi on 30-4-2016.
 */
public class TitleNms_1_12_R1 implements TitleNms {

    public void send(Collection<? extends Player> players, String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        for (Player player : players) {
            player.sendTitle(title, subTitle, fadeIn, stay, fadeOut);
        }
    }
}
