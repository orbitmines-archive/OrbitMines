package com.orbitmines.spigot.servers.kitpvp.handlers.actives;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Cooldown;
import com.orbitmines.spigot.api.handlers.itembuilders.FireworkBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PotionBuilder;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

public class ActiveSugarRush implements Active.Handler {

    private final Cooldown cooldown = new Cooldown(10 * 1000);

    private final PotionBuilder builder = new PotionBuilder(PotionEffectType.SPEED, 3 * 20, 2);

    @Override
    public void trigger(PlayerInteractEvent event, KitPvPPlayer omp, int level) {
        omp.addPotionEffect(builder);
        omp.playSound(Sound.ENTITY_PLAYER_BURP);

        FireworkBuilder builder = new FireworkBuilder(omp.getLocation());
        builder.withColor(Color.WHITE);
        builder.withFade(Color.GRAY);
        builder.withFade(Color.SILVER);
        builder.with(FireworkEffect.Type.BURST);
        builder.withTrail();
        builder.build();
        builder.setVelocity(omp.getVelocity().multiply(-1));
        builder.explode();
    }

    @Override
    public Cooldown getCooldown(int level) {
        return cooldown;
    }

    public PotionBuilder getBuilder(int level) {
        return builder;
    }
}
