package com.orbitmines.spigot.api.nms.entity;

import com.orbitmines.spigot.api.Mob;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface EntityNms {

    void setInvisible(Entity entity, boolean bl);

    void destroyEntity(Player player, Entity entity);

    void setAttribute(Entity entity, Attribute attribute, double d);

    double getAttribute(Entity entity, Attribute attribute);

    void destroyEntityFor(Collection<? extends Player> players, Entity entity);

    void disguiseAsBlock(Player player, int Id, Collection<? extends Player> players);

    Entity disguiseAsMob(Player player, Mob mob, boolean baby, String displayName, Collection<? extends Player> players);

    void mountUpdate(Entity vehicle);

    void navigate(LivingEntity le, Location location, double v);

    int nextEntityId();

    enum Attribute {

        MAX_HEALTH,
        FOLLOW_RANGE,
        KNOCKBACK_RESISTANCE,
        MOVEMENT_SPEED,
        FLYING_SPEED,
        ATTACK_DAMAGE,
        ATTACK_SPEED,
        ARMOR,
        ARMOR_TOUGHNESS,
        LUCK;

    }
}
