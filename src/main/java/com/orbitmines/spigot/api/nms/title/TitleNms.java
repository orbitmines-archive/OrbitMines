package com.orbitmines.spigot.api.nms.title;

import com.orbitmines.spigot.api.handlers.chat.Title;
import org.bukkit.entity.Player;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface TitleNms {

    void send(Player player, Title title);

}
