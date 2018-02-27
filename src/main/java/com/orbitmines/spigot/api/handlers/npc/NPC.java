package com.orbitmines.spigot.api.handlers.npc;

import com.orbitmines.spigot.api.Mob;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Villager;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class NPC {

    private static List<NPC> npcs = new ArrayList<>();

    protected Mob mob;
    protected Location location;
    protected String displayName;
    protected Entity entity;
    private boolean onFire;

    protected ItemStack itemInHand;
    protected ItemStack helmet;
    protected ItemStack chestPlate;
    protected ItemStack leggings;
    protected ItemStack boots;

    protected SpigotRunnable runnable;

    protected ArmorStandNpc nameTag;

    protected InteractAction interactAction;

    public NPC(Mob mob, Location location, String displayName) {
        this(mob, location, displayName, null);
    }

    public NPC(Mob mob, Location location, String displayName, InteractAction interactAction) {
        npcs.add(this);

        this.mob = mob;
        this.location = location;
        this.displayName = displayName;

        this.interactAction = interactAction;
    }

    public Mob getMob() {
        return mob;
    }

    public void setMob(Mob mob) {
        this.mob = mob;

        spawn();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;

        spawn();
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;

        if (entity == null)
            return;

        entity.setCustomNameVisible(false);

        if (nameTag == null) {
            ArmorStandNpc.ClickAction action;
            if (interactAction != null)
                action = new ArmorStandNpc.ClickAction() {
                    @Override
                    public void click(PlayerInteractAtEntityEvent event, OMPlayer player, ArmorStandNpc item) {
                        interactAction.click(player, NPC.this);
                    }
                };
            else
                action = null;

            /* TODO: Add Offset for smaller&larger entities, (1.8 Fix) */
            Location location = this.location.clone();
            switch (mob) {
                case WITHER_SKELETON:
                    location.add(0, 0.35, 0);
                    break;
            }

            nameTag = new ArmorStandNpc(location, false, action);
            nameTag.setVisible(false);
            nameTag.setGravity(false);

            nameTag.setCustomName(displayName);
            nameTag.setCustomNameVisible(true);
            nameTag.spawn();
        } else {
            nameTag.setCustomName(displayName);
        }
    }

    public Entity getEntity() {
        return entity;
    }

    public boolean isOnFire() {
        return onFire;
    }

    public void setOnFire(boolean onFire) {
        this.onFire = onFire;
    }

    public Location getFixedLocation() {
        return mob == Mob.BAT || mob == Mob.SQUID || mob == Mob.GUARDIAN ? location.clone().add(0, 1, 0) : location;
    }

    public SpigotRunnable getRunnable() {
        return runnable;
    }

    public void spawn() {
        remove();

        if (this instanceof MovingNpc)
            this.entity = mob.spawnMoving(getFixedLocation());
        else
            this.entity = mob.spawn(getFixedLocation());

        setDisplayName(displayName);

        if (entity != null) {
            if (runnable == null) {
                this.runnable = new SpigotRunnable(SpigotRunnable.TimeUnit.TICK, 3) {
                    @Override
                    public void run() {
                        if (entity != null && (getEntity().isDead() || !getEntity().isValid())) {
                            spawn();
                            setItemInHand(itemInHand);
                            setHelmet(helmet);
                            setChestplate(chestPlate);
                            setLeggings(leggings);
                            setBoots(boots);
                        }

                        if (!isOnFire())
                            entity.setFireTicks(0);
                        else
                            entity.setFireTicks(Integer.MAX_VALUE);
                    }
                };
            }
        } else if (runnable != null) {
            runnable.cancel();
        }
    }

    public void remove() {
        if (entity != null)
            entity.remove();
    }

    public void delete() {
        remove();
        npcs.remove(this);
    }

    public void click(OMPlayer player) {
        if (interactAction != null)
            interactAction.click(player, this);
    }
    
    public void setItemInHand(ItemStack item) {
        itemInHand = item;
        ((LivingEntity) entity).getEquipment().setItemInHand(item);
    }
    
    public void setItemInMainHand(ItemStack item) {
        itemInHand = item;
        ((LivingEntity) entity).getEquipment().setItemInMainHand(item);
    }
    
    public void setItemInOffHand(ItemStack item) {
        itemInHand = item;
        ((LivingEntity) entity).getEquipment().setItemInOffHand(item);
    }

    public void setHelmet(ItemStack item) {
        helmet = item;
        ((LivingEntity) entity).getEquipment().setHelmet(item);
    }

    public void setChestplate(ItemStack item) {
        chestPlate = item;
        ((LivingEntity) entity).getEquipment().setChestplate(item);
    }

    public void setLeggings(ItemStack item) {
        leggings = item;
        ((LivingEntity) entity).getEquipment().setLeggings(item);
    }

    public void setBoots(ItemStack item) {
        boots = item;
        ((LivingEntity) entity).getEquipment().setBoots(item);
    }

    public void setSkeletonType(Skeleton.SkeletonType skeletonType) {
        ((Skeleton) entity).setSkeletonType(skeletonType);
    }

    public void setVillagerProfession(Villager.Profession villagerProfession) {
        ((Villager) entity).setProfession(villagerProfession);
    }

    public static NPC getNpc(Entity entity) {
        for (NPC npc : npcs) {
            if (npc.getEntity() == entity)
                return npc;
        }
        return null;
    }

    public static List<NPC> getNpcs() {
        return npcs;
    }

    public static abstract class InteractAction {

        public abstract void click(OMPlayer player, NPC clicked);

    }
}
