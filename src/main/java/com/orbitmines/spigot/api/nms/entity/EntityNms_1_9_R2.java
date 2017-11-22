package com.orbitmines.spigot.api.nms.entity;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.Mob;
import com.orbitmines.spigot.api.utils.ReflectionUtils;
import net.minecraft.server.v1_9_R2.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Created by Fadi on 11-5-2016.
 */
public class EntityNms_1_9_R2 implements EntityNms {

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
            case ATTACK_DAMAGE:
                return GenericAttributes.ATTACK_DAMAGE;
            case ATTACK_SPEED:
                return GenericAttributes.f;
            case ARMOR:
                return GenericAttributes.g;
            case ARMOR_TOUGHNESS:
                return GenericAttributes.h;
            case LUCK:
                return GenericAttributes.i;
            default:
                return null;
        }
    }

    @Override
    public void destroyEntityFor(Collection<? extends Player> players, Entity entity) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(((CraftEntity) entity).getHandle().getId());

        for (Player player : players) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    @Override
    public void disguiseAsBlock(Player player, int Id, Collection<? extends Player> players) {
        EntityFallingBlock disguise = new EntityFallingBlock(((CraftPlayer) player).getHandle().world, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), new BlockLog1().getBlockData());
        disguise.locX = player.getLocation().getX();
        disguise.locY = player.getLocation().getY();
        disguise.locZ = player.getLocation().getZ();
        disguise.yaw = player.getLocation().getYaw();
        disguise.pitch = player.getLocation().getPitch();
        disguise.f(((CraftPlayer) player).getHandle().getId());

        PacketPlayOutEntityDestroy packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy((((CraftPlayer) player).getHandle().getId()));
        PacketPlayOutSpawnEntity packetPlayOutSpawnEntity = new PacketPlayOutSpawnEntity(disguise, 70, Id);

        for (Player p : players) {
            if (player == p)
                continue;

            EntityPlayer entityPlayer = ((CraftPlayer) p).getHandle();
            entityPlayer.playerConnection.sendPacket(packetPlayOutEntityDestroy);
            entityPlayer.playerConnection.sendPacket(packetPlayOutSpawnEntity);
        }
    }

    @Override
    public Entity disguiseAsMob(Player player, Mob mob, boolean baby, String displayName, Collection<? extends Player> players) {
        EntityLiving disguise = null;
        World world = ((CraftPlayer) player).getHandle().world;

        switch (mob) {

            case ARMOR_STAND:
                disguise = new EntityArmorStand(world);
            case BAT:
                disguise = new EntityBat(world);
                break;
            case BLAZE:
                disguise = new EntityBlaze(world);
                break;
            case CAVE_SPIDER:
                disguise = new EntityCaveSpider(world);
                break;
            case CHICKEN:
                disguise = new EntityChicken(world);
                break;
            case COW:
                disguise = new EntityCow(world);
                break;
            case CREEPER:
                disguise = new EntityCreeper(world);
                break;
            case ENDER_DRAGON:
                disguise = new EntityEnderDragon(world);
                break;
            case ENDERMAN:
                disguise = new EntityEnderman(world);
                break;
            case ENDERMITE:
                disguise = new EntityEndermite(world);
                break;
            case GHAST:
                disguise = new EntityGhast(world);
                break;
            case GIANT:
                disguise = new EntityGiantZombie(world);
                break;
            case GUARDIAN:
                disguise = new EntityGuardian(world);
                break;
            case HORSE:
                disguise = new EntityHorse(world);
                break;
            case IRON_GOLEM:
                disguise = new EntityIronGolem(world);
                break;
            case MAGMA_CUBE:
                disguise = new EntityMagmaCube(world);
                break;
            case MUSHROOM_COW:
                disguise = new EntityMushroomCow(world);
                break;
            case OCELOT:
                disguise = new EntityOcelot(world);
                break;
            case PIG:
                disguise = new EntityPig(world);
                break;
            case PIG_ZOMBIE:
                disguise = new EntityPigZombie(world);
                break;
            case RABBIT:
                disguise = new EntityRabbit(world);
                break;
            case SHEEP:
                disguise = new EntitySheep(world);
                break;
            case SILVERFISH:
                disguise = new EntitySilverfish(world);
                break;
            case SKELETON:
                disguise = new EntitySkeleton(world);
                break;
            case SLIME:
                disguise = new EntitySlime(world);
                break;
            case SNOWMAN:
                disguise = new EntitySnowman(world);
                break;
            case SPIDER:
                disguise = new EntitySpider(world);
                break;
            case SQUID:
                disguise = new EntitySquid(world);
                break;
            case VILLAGER:
                disguise = new EntityVillager(world);
                break;
            case WITCH:
                disguise = new EntityWitch(world);
                break;
            case WITHER:
                disguise = new EntityWither(world);
                break;
            case WOLF:
                disguise = new EntityWolf(world);
                break;
            case ZOMBIE:
                disguise = new EntityZombie(world);
                break;
        }

        if (disguise == null)
            return null;

        disguise.locX = player.getLocation().getX();
        disguise.locY = player.getLocation().getY();
        disguise.locZ = player.getLocation().getZ();
        disguise.yaw = player.getLocation().getYaw();
        disguise.pitch = player.getLocation().getPitch();
        disguise.f(((CraftPlayer) player).getHandle().getId());

        if (baby && disguise.getBukkitEntity() instanceof Ageable)
            ((Ageable) disguise.getBukkitEntity()).setBaby();

        PacketPlayOutEntityDestroy packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy((((CraftPlayer) player).getHandle().getId()));
        PacketPlayOutSpawnEntityLiving packetPlayOutSpawnEntityLiving = new PacketPlayOutSpawnEntityLiving(disguise);

        for (Player p : players) {
            if (player == p)
                continue;

            EntityPlayer entityPlayer = ((CraftPlayer) p).getHandle();
            entityPlayer.playerConnection.sendPacket(packetPlayOutEntityDestroy);
            entityPlayer.playerConnection.sendPacket(packetPlayOutSpawnEntityLiving);
        }

        if (displayName != null) {
            disguise.setCustomName(displayName);
            disguise.setCustomNameVisible(true);
        }

        return disguise.getBukkitEntity();
    }

    @Override
    public void mountUpdate(final Entity vehicle) {
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
            Field entityCount = ReflectionUtils.getDeclaredField(net.minecraft.server.v1_9_R2.Entity.class, "entityCount");
            int id = entityCount.getInt(null);
            entityCount.setInt(null, id + 1);
            return id;
        } catch (Exception ex) {
            ex.printStackTrace();
            return (int) Math.round(Math.random() * Integer.MAX_VALUE * 0.25);
        }
    }
}
