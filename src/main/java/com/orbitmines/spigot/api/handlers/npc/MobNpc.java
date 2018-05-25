package com.orbitmines.spigot.api.handlers.npc;

import com.orbitmines.spigot.api.Mob;
import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardString;
import com.orbitmines.spigot.api.nms.npc.MobNpcNms;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public class MobNpc extends NpcD {

    private static ArrayList<MobNpc> mobNpcs = new ArrayList<>();

    protected Mob mob;
    protected Entity entity;
    protected boolean onFire;

    protected Hologram nameTag;

    protected ItemStack itemInHand;//TODO second hand zombies etc?
    protected ItemStack helmet;
    protected ItemStack chestPlate;
    protected ItemStack leggings;
    protected ItemStack boots;

    protected SpigotRunnable runnable;

    public MobNpc(Mob mob, Location spawnLocation, ScoreboardString... displayName) {
        super(spawnLocation);

        this.mob = mob;
        this.onFire = false;

        //TODO yOff for other mobs.
        nameTag = new Hologram(spawnLocation, 1.75, Hologram.Face.UP);

        if (displayName == null)
            return;

        for (ScoreboardString string : displayName) {
            nameTag.addLine(string, false);
        }
    }

    @Override
    protected void spawn() {//TODO DISABLE_COLLISION/DISABLE_SOUNDS OPTIONABLE, DISABLE_GRAVITY for 1.10+
        if (this instanceof MovingMobNpc)
            this.entity = mob.spawn(getFixedLocation(), MobNpcNms.Option.DISABLE_ATTACK);
        else
            this.entity = mob.spawn(getFixedLocation(), MobNpcNms.Option.DISABLE_ATTACK, MobNpcNms.Option.DISABLE_MOVEMENT);

        /* Also spawn NameTag */
        nameTag.spawn();

        this.runnable = new SpigotRunnable(SpigotRunnable.TimeUnit.TICK, 3) {
            @Override
            public void run() {
                if (entity == null)
                    return;

                entity.setFireTicks(onFire ? Integer.MAX_VALUE : 0);//TODO via NMS (make it so fire cant dissapear)?
            }
        };
    }

    @Override
    protected void despawn() {
        if (entity != null)
            entity.remove();

        if (runnable != null)
            runnable.cancel();

        /* Also despawn NameTag */
        nameTag.despawn();
    }

    @Override
    public void update() {
        /* Also update NameTag */
        nameTag.update();
    }

    @Override
    protected Collection<? extends Entity> getEntities() {
        return Collections.singletonList(entity);
    }

    @Override
    protected void addToList() {
        mobNpcs.add(this);
    }

    @Override
    protected void removeFromList() {
        mobNpcs.remove(this);
    }

    /* NameTag might be blocking the player from interacting with the mob, so we give the NameTag the same interaction. */
    @Override
    public void setInteractAction(InteractAction interactAction) {
        super.setInteractAction(interactAction);
        nameTag.setInteractAction(interactAction);
    }

    /* Also create NameTag */
    @Override
    public void create(Collection<? extends Player> createFor) {
        super.create(createFor);
        nameTag.create(createFor);
    }

    /* Also destroy NameTag */
    @Override
    public void destroy() {
        super.destroy();
        nameTag.destroy();
    }

    /* Also hide NameTag */
    @Override
    public void hideFor(Collection<? extends Player> players) {
        super.hideFor(players);
        nameTag.hideFor(players);
    }

    public Location getFixedLocation() {
        //TODO
        return spawnLocation;
    }

    public Mob getMob() {
        return mob;
    }

    public void setMob(Mob mob) {
        this.mob = mob;

        if (entity != null)
            create();
    }


//    public void setSkeletonType(Skeleton.SkeletonType skeletonType) {
//        ((Skeleton) entity).setSkeletonType(skeletonType);
//    }
//
//    public void setVillagerProfession(Villager.Profession villagerProfession) {
//        ((Villager) entity).setProfession(villagerProfession);
//    }1.8

    public Entity getEntity() {
        return entity;
    }

    public boolean isOnFire() {
        return onFire;
    }

    public void setOnFire(boolean onFire) {
        this.onFire = onFire;
    }

    public Hologram getNameTag() {
        return nameTag;
    }

    public ItemStack getItemInHand() {
        return itemInHand;
    }

//    public void setItemInHand(ItemStack itemInHand) {
//        this.itemInHand = itemInHand;
//
//        if (entity != null)
//            ((LivingEntity) entity).getEquipment().setItemInHand(itemInHand);
//    }1.8

    public void setItemInMainHand(ItemStack item) {
        itemInHand = item;

        if (entity != null)
            ((LivingEntity) entity).getEquipment().setItemInMainHand(item);
    }

    public void setItemInOffHand(ItemStack item) {
        itemInHand = item;

        if (entity != null)
            ((LivingEntity) entity).getEquipment().setItemInOffHand(item);
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;

        if (entity != null)
            ((LivingEntity) entity).getEquipment().setHelmet(helmet);
    }

    public ItemStack getChestPlate() {
        return chestPlate;
    }

    public void setChestPlate(ItemStack chestPlate) {
        this.chestPlate = chestPlate;

        if (entity != null)
            ((LivingEntity) entity).getEquipment().setChestplate(chestPlate);
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public void setLeggings(ItemStack leggings) {
        this.leggings = leggings;

        if (entity != null)
            ((LivingEntity) entity).getEquipment().setLeggings(leggings);
    }

    public ItemStack getBoots() {
        return boots;
    }

    public void setBoots(ItemStack boots) {
        this.boots = boots;

        if (entity != null)
            ((LivingEntity) entity).getEquipment().setBoots(boots);
    }

    public static ArrayList<MobNpc> getMobNpcs() {
        return mobNpcs;
    }

    public static MobNpc getMobNpc(Entity entity) {
        for (MobNpc mobNpc : mobNpcs) {
            if (mobNpc.getEntity() == entity) //TODO, might not work?
                return mobNpc;
        }
        return null;
    }
}
