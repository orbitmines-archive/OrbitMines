package com.orbitmines.spigot.servers.kitpvp.handlers.actives;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Cooldown;
import com.orbitmines.spigot.api.handlers.itembuilders.PotionBuilder;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

public class ActiveSugarRush implements Active.Handler {

    private final Cooldown cooldown = new Cooldown(10 * 1000);

    private final PotionBuilder builder = new PotionBuilder(PotionEffectType.SPEED, 3 * 20, 2);

    @Override
    public void trigger(PlayerInteractEvent event, KitPvPPlayer omp, int level) {
        omp.addPotionEffect(builder);
        omp.playSound(Sound.ENTITY_PLAYER_LEVELUP);
    }

    @Override
    public Cooldown getCooldown(int level) {
        return cooldown;
    }

    public PotionBuilder getBuilder(int level) {
        return builder;
    }
}
