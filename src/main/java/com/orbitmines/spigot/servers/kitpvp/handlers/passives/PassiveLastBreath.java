package com.orbitmines.spigot.servers.kitpvp.handlers.passives;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.handlers.itembuilders.PotionBuilder;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.potion.PotionEffectType;

public class PassiveLastBreath implements Passive.LowHealthHandler<DummyEvent> {

    private final PotionBuilder builder = new PotionBuilder(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0);

    @Override
    public void trigger(DummyEvent event, int level) {
        KitPvPPlayer player = event.getPlayer();
        PotionBuilder builder = getBuilder(level);

        if (!player.hasPotionEffect(builder.getType()))
            player.addPotionEffect(builder);
    }

    @Override
    public void triggerOff(DummyEvent event, int level) {
        KitPvPPlayer player = event.getPlayer();
        PotionEffectType type = getBuilder(level).getType();

        player.clearPotionEffect(type);
    }

    @Override
    public double getPercentage(int level) {
        return 0.25D; /* Below 25% */
    }

    public PotionBuilder getBuilder(int level) {
        return builder;
    }
}
