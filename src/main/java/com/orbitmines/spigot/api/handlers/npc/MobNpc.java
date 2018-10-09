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
public class MobNpc extends Npc {

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

        spawnLocation.add(0, getSpawnYOff(this.mob), 0);

        if (this instanceof PersonalisedMobNpc)
            return;

        nameTag = new Hologram(spawnLocation, getYOff(), Hologram.Face.UP);

        if (displayName == null)
            return;

        for (ScoreboardString string : displayName) {
            nameTag.addLine(string, false);
        }
    }

    @Override
    protected void spawn() {
        if (this instanceof MovingMobNpc)
            this.entity = mob.spawn(getFixedLocation(), MobNpcNms.Option.DISABLE_ATTACK);
        else
            this.entity = mob.spawn(getFixedLocation(), MobNpcNms.Option.DISABLE_ATTACK, MobNpcNms.Option.DISABLE_MOVEMENT);

        /* Also spawn NameTag */
        if (nameTag != null && !(this instanceof PersonalisedMobNpc))
            nameTag.spawn();

        this.runnable = new SpigotRunnable(SpigotRunnable.TimeUnit.TICK, 3) {
            @Override
            public void run() {
                if (entity == null)
                    return;

                entity.setFireTicks(onFire ? Integer.MAX_VALUE : 0);
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
        if (nameTag != null && !(this instanceof PersonalisedMobNpc))
            nameTag.despawn();
    }

    @Override
    public void update() {
        /* Also update NameTag */
        if (nameTag != null && !(this instanceof PersonalisedMobNpc)) {
            nameTag.setYOff(getYOff());
            nameTag.update();
        }
    }

    @Override
    public Collection<Entity> getEntities() {
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

        if (nameTag != null)
            nameTag.setInteractAction(interactAction);
    }

    /* Also create NameTag */
    @Override
    public void create(Collection<? extends Player> createFor) {
        super.create(createFor);

        if (nameTag != null)
            nameTag.create(createFor);
    }

    /* Also destroy NameTag */
    @Override
    public void destroy() {
        super.destroy();

        if (nameTag != null)
            nameTag.destroy();
    }

    /* Also hide NameTag */
    @Override
    public void hideFor(Collection<? extends Player> players) {
        super.hideFor(players);

        if (nameTag != null)
            nameTag.hideFor(players);
    }

    public Location getFixedLocation() {
        //TODO
        return spawnLocation;
    }

    @Override
    public void setSpawnLocation(Location spawnLocation) {
        super.setSpawnLocation(spawnLocation.clone().add(0, getSpawnYOff(this.mob), 0));
    }

    public Mob getMob() {
        return mob;
    }

    public void setMob(Mob mob) {
        this.spawnLocation.add(0, getSpawnYOffPrev(this.mob), 0);

        this.mob = mob;

        if (entity != null)
            create();

        if (nameTag != null) {
            this.nameTag.setYOff(getYOff());
            this.nameTag.update();
        }
    }

    protected double getSpawnYOffPrev(Mob previous) {
        return getSpawnYOff(this.mob) - getSpawnYOff(previous);
    }

    protected double getSpawnYOff(Mob mob) {
        switch (mob) {
            case DOLPHIN:
                return 1.25;
        }
        return 0;
    }

    protected double getYOff() {
        switch (mob) {
            case COD:
            case DOLPHIN:
            case ENDERMITE:
            case SALMON:
            case SILVERFISH:
            case TURTLE:
                return 0.75;

            case OCELOT:
                return 1;

            case BAT:
            case CAVE_SPIDER:
            case CHICKEN:
            case PARROT:
            case PIG:
            case PUFFERFISH:
            case RABBIT:
            case SHULKER:
            case SPIDER:
            case SQUID:
            case TROPICAL_FISH:
            case VEX:
            case WOLF:
                return 1.25;

            case GUARDIAN:
            case MAGMA_CUBE:
            case SLIME:
                return 1.5;

            case BLAZE:
            case COW:
            case CREEPER:
            case EVOKER:
            case ILLUSIONER:
            case MUSHROOM_COW:
            case PHANTOM:
            case POLAR_BEAR:
            case SHEEP:
            case SNOWMAN:
            case STRAY:
            case VILLAGER:
            case VINDICATOR:
            case WITCH:
            case ZOMBIE:
            case ZOMBIE_VILLAGER:
                return 1.75;

            case HUSK:
            case MULE:
            case PIG_ZOMBIE:
            case SKELETON:
            case DROWNED:
                return 2;

            case DONKEY:
            case ENDERMAN:
            case HORSE:
            case LLAMA:
            case SKELETON_HORSE:
            case ZOMBIE_HORSE:
                return 2.25;

            case ELDER_GUARDIAN:
            case WITHER:
            case WITHER_SKELETON:
                return 2.5;

            case IRON_GOLEM:
                return 2.75;

            case ENDER_DRAGON:
                return 3.25;

            case GHAST:
                return 5;

            case GIANT:
                return 11;
        }

        return 1.75;
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
            if (mobNpc.getEntity() == entity)
                return mobNpc;
        }
        return null;
    }
}
