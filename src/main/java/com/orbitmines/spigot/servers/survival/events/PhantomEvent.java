package com.orbitmines.spigot.servers.survival.events;

import com.orbitmines.spigot.api.utils.WorldUtils;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;

import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class PhantomEvent implements Listener {

    private final double spawnDistance = Math.pow(40D, 2D);

    @EventHandler
    public void onChange(PlayerBedEnterEvent event) {
        if (event.isCancelled())
            return;

        SurvivalPlayer omp = SurvivalPlayer.getPlayer(event.getPlayer());
        omp.updateLastBedEnter();
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        if (event.isCancelled() || !(event.getEntity() instanceof Phantom) || event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL)
            return;

        List<Player> players = WorldUtils.getNearbyPlayers(event.getLocation(), spawnDistance);

        for (Player player : players) {
            SurvivalPlayer omp = SurvivalPlayer.getPlayer(player);

            if (!omp.canSpawnPhantom())
                event.setCancelled(true);
        }
    }
}
