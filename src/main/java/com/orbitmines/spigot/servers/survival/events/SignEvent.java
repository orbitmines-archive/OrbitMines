package com.orbitmines.spigot.servers.survival.events;

import com.orbitmines.api.VipRank;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class SignEvent implements Listener {

    @EventHandler
    public void onChange(SignChangeEvent event) {
        SurvivalPlayer omp = SurvivalPlayer.getPlayer(event.getPlayer());

        if (!omp.isEligible(VipRank.EMERALD))
            return;

        for (int i = 0; i < 4; i++) {
            event.setLine(i, ChatColor.translateAlternateColorCodes('&', event.getLine(i)));
        }
    }
}
