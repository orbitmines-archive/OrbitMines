package com.orbitmines.spigot.api.nms.actionbar;


import com.orbitmines.spigot.api.handlers.chat.ActionBar;
import org.bukkit.entity.Player;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface ActionBarNms {

    public void send(Player player, ActionBar actionBar);

}
