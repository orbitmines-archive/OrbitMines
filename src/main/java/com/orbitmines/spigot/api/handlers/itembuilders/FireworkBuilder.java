package com.orbitmines.spigot.api.handlers.itembuilders;

import com.madblock.api.Color;
import com.madblock.api.utils.RandomUtils;
import com.madblock.spigot.MadBlock;
import com.madblock.spigot.api.utils.ColorUtils;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class FireworkBuilder {

    private Firework firework;
    private FireworkMeta fireworkMeta;
    private FireworkEffect.Builder builder;

    public FireworkBuilder(Location location) {
        this.firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        this.fireworkMeta = firework.getFireworkMeta();
        this.builder = FireworkEffect.builder();
    }

    public Firework getFirework() {
        return firework;
    }

    public FireworkMeta getFireworkMeta() {
        return fireworkMeta;
    }

    public FireworkEffect.Builder getBuilder() {
        return builder;
    }

    public FireworkBuilder with(FireworkEffect.Type type) {
        builder.with(type);

        return this;
    }

    public FireworkBuilder withColor(Color color) {
        builder.withColor(ColorUtils.toBukkitColor(color));

        return this;
    }

    public FireworkBuilder withFade(Color color) {
        builder.withFade(ColorUtils.toBukkitColor(color));

        return this;
    }

    public FireworkBuilder withFlicker() {
        builder.withFlicker();

        return this;
    }

    public FireworkBuilder withTrail() {
        builder.withTrail();

        return this;
    }

    public FireworkBuilder randomize() {
        builder.withColor(ColorUtils.random());
        builder.withColor(ColorUtils.random());
        builder.withFade(ColorUtils.random());
        builder.withFade(ColorUtils.random());

        if (RandomUtils.RANDOM.nextBoolean())
            builder.withFlicker();
        if (RandomUtils.RANDOM.nextBoolean())
            builder.withTrail();

        return this;
    }

    public FireworkBuilder build() {
        fireworkMeta.addEffects(builder.build());
        firework.setFireworkMeta(fireworkMeta);

        return this;
    }

    public FireworkBuilder setVelocity(Vector vector) {
        firework.setVelocity(vector);

        return this;
    }

    public FireworkBuilder explode() {
        MadBlock.getInstance().getNms().firework().explode(firework);

        return this;
    }
}
