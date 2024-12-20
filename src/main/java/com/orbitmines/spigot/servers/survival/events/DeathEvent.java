package com.orbitmines.spigot.servers.survival.events;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.utils.BotToken;
import com.orbitmines.discordbot.utils.DiscordSpigotUtils;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class DeathEvent implements Listener {

    private Survival survival;

    public DeathEvent(Survival survival) {
        this.survival = survival;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        SurvivalPlayer omp = SurvivalPlayer.getPlayer(p);

        p.setHealth(p.getMaxHealth());
        p.setFoodLevel(20);

        omp.setBackLocation(p.getLocation());

        new BukkitRunnable() {
            @Override
            public void run() {
                p.teleport(survival.getLobbySpawn());
                p.setVelocity(new Vector(0, 0, 0));
                p.setFireTicks(0);

                omp.clearExperience();
                omp.clearPotionEffects();
            }
        }.runTaskLater(survival.getOrbitMines(), 1);

        DiscordBot discord = survival.getDiscord();
        BotToken token = survival.getToken();

        discord.getChannelFor(token).sendMessage(
                ":skull_crossbones:" +
                        event.getDeathMessage().replaceAll(omp.getName(true),
                                DiscordSpigotUtils.getDisplay(discord, token, omp))
        ).queue();
    }
}
