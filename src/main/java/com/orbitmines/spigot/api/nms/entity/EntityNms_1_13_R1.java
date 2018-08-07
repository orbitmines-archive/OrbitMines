package com.orbitmines.spigot.api.nms.entity;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.utils.ReflectionUtils;
import net.minecraft.server.v1_13_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.Collection;

public class EntityNms_1_13_R1 implements EntityNms {

    @Override
    public void setInvisible(Entity entity, boolean bl) {
        ((CraftEntity) entity).getHandle().setInvisible(bl);
    }

    @Override
    public void destroyEntity(Player player, Entity entity) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(((CraftEntity) entity).getHandle().getId()));
    }

    @Override
    public void setAttribute(Entity entity, Attribute attribute, double d) {
        AttributeInstance instance = ((CraftLivingEntity) entity).getHandle().getAttributeInstance(getAttribute(attribute));
        instance.setValue(d);
    }

    @Override
    public double getAttribute(Entity entity, Attribute attribute) {
        return ((CraftLivingEntity) entity).getHandle().getAttributeInstance(getAttribute(attribute)).b();
    }

    private IAttribute getAttribute(Attribute attribute) {
        switch (attribute) {

            case MAX_HEALTH:
                return GenericAttributes.maxHealth;
            case FOLLOW_RANGE:
                return GenericAttributes.FOLLOW_RANGE;
            case KNOCKBACK_RESISTANCE:
                return GenericAttributes.c;
            case MOVEMENT_SPEED:
                return GenericAttributes.MOVEMENT_SPEED;
            case FLYING_SPEED:
                return GenericAttributes.e;
            case ATTACK_DAMAGE:
                return GenericAttributes.ATTACK_DAMAGE;
            case ATTACK_SPEED:
                return GenericAttributes.g;
            case ARMOR:
                return GenericAttributes.h;
            case ARMOR_TOUGHNESS:
                return GenericAttributes.i;
            case LUCK:
                return GenericAttributes.j;
            default:
                return null;
        }
    }

    @Override
    public void destroyEntitiesFor(Collection<? extends Entity> entities, Collection<? extends Player> players) {
        for (Entity entity : entities) {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(((CraftEntity) entity).getHandle().getId());

            for (Player player : players) {
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }

    @Override
    public void disguiseAsBlock(Player player, int Id, Collection<? extends Player> players) {
        //TODO BLOCK DISGUISE
    }

    @Override
    public Entity disguiseAsMob(Player player, EntityType entityType, boolean baby, String displayName, Collection<? extends Player> players) {
        //TODO MOB DISGUISE
        return null;
    }

    @Override
    public void mountUpdate(Entity vehicle) {
        new BukkitRunnable() {
            @Override
            public void run() {
                PacketPlayOutMount packet = new PacketPlayOutMount(((CraftPlayer) vehicle).getHandle());
                ((CraftPlayer) vehicle).getHandle().playerConnection.sendPacket(packet);
            }
        }.runTaskLater(OrbitMines.getInstance(), 1);
    }

    @Override
    public void navigate(LivingEntity le, Location location, double v) {
        ((EntityInsentient) ((CraftLivingEntity) le).getHandle()).getNavigation().a(location.getX(), location.getY(), location.getZ(), v);
    }

    @Override
    public int nextEntityId() {
        try {
            Field entityCount = ReflectionUtils.getDeclaredField(net.minecraft.server.v1_13_R1.Entity.class, "entityCount");
            int id = entityCount.getInt(null);
            entityCount.setInt(null, id + 1);
            return id;
        } catch (Exception ex) {
            ex.printStackTrace();
            return (int) Math.round(Math.random() * Integer.MAX_VALUE * 0.25);
        }
    }
}
