package com.orbitmines.spigot.api.handlers.npc;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public class ArmorStandNpc extends NpcD {

    private static ArrayList<ArmorStandNpc> armorStandNpcs = new ArrayList<>();

    protected ArmorStand armorStand;

    protected boolean arms;
    protected boolean basePlate;
    protected EulerAngle bodyPose;
    protected EulerAngle headPose;
    protected EulerAngle leftArmPose;
    protected EulerAngle leftLegPose;
    protected EulerAngle rightArmPose;
    protected EulerAngle rightLegPose;
    protected ItemStack itemInHand;
    protected ItemStack helmet;
    protected ItemStack chestPlate;
    protected ItemStack leggings;
    protected ItemStack boots;
    protected String customName;
    protected boolean customNameVisible;
    protected boolean gravity;
    protected boolean small;
    protected boolean visible;
    protected boolean marker;

    public ArmorStandNpc(Location spawnLocation) {
        super(spawnLocation);

        this.arms = false;
        this.basePlate = false;
        this.bodyPose = EulerAngle.ZERO;
        this.headPose = EulerAngle.ZERO;
        this.leftArmPose = EulerAngle.ZERO;
        this.leftLegPose = EulerAngle.ZERO;
        this.rightArmPose = EulerAngle.ZERO;
        this.rightLegPose = EulerAngle.ZERO;
        this.customNameVisible = false;
        this.gravity = true;
        this.small = false;
        this.visible = true;
        this.marker = false;
    }

    @Override
    protected void spawn() {
        armorStand = (ArmorStand) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ARMOR_STAND);
        armorStand.setRemoveWhenFarAway(false);

        armorStand.setArms(arms);
        armorStand.setBasePlate(basePlate);
        armorStand.setBodyPose(bodyPose);
        armorStand.setBoots(boots);
        armorStand.setChestplate(chestPlate);
        armorStand.setGravity(gravity);
        armorStand.setHeadPose(headPose);
        armorStand.setHelmet(helmet);
        armorStand.setItemInHand(itemInHand);
        armorStand.setLeftArmPose(leftArmPose);
        armorStand.setLeftLegPose(leftLegPose);
        armorStand.setLeggings(leggings);
        armorStand.setRightArmPose(rightArmPose);
        armorStand.setRightLegPose(rightLegPose);
        armorStand.setSmall(small);
        armorStand.setVisible(visible);
        armorStand.setMarker(marker);

        armorStand.setCustomName(customName);
        armorStand.setCustomNameVisible(customNameVisible);
    }

    @Override
    protected void despawn() {
        if (armorStand != null)
            armorStand.remove();
    }

    @Override
    public void update() {
        if (armorStand == null)
            return;

        armorStand.setRemoveWhenFarAway(false);

        armorStand.setCustomName(customName);//TODO Might 'flicker' when changed too often>?
        armorStand.setCustomNameVisible(customNameVisible);
    }

    @Override
    protected Collection<? extends Entity> getEntities() {
        return Collections.singletonList(armorStand);
    }

    @Override
    protected void addToList() {
        armorStandNpcs.add(this);
    }

    @Override
    protected void removeFromList() {
        armorStandNpcs.remove(this);
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public boolean hasArms() {
        return arms;
    }

    public void setArms(boolean arms) {
        this.arms = arms;

        if (this.armorStand != null)
            this.armorStand.setArms(arms);
    }

    public boolean hasBasePlate() {
        return basePlate;
    }

    public void setBasePlate(boolean basePlate) {
        this.basePlate = basePlate;

        if (this.armorStand != null)
            this.armorStand.setBasePlate(basePlate);
    }

    public EulerAngle getBodyPose() {
        return bodyPose;
    }

    public void setBodyPose(EulerAngle bodyPose) {
        this.bodyPose = bodyPose;

        if (this.armorStand != null)
            this.armorStand.setBodyPose(bodyPose);
    }

    public EulerAngle getHeadPose() {
        return headPose;
    }

    public void setHeadPose(EulerAngle headPose) {
        this.headPose = headPose;

        if (this.armorStand != null)
            this.armorStand.setHeadPose(headPose);
    }

    public EulerAngle getLeftArmPose() {
        return leftArmPose;
    }

    public void setLeftArmPose(EulerAngle leftArmPose) {
        this.leftArmPose = leftArmPose;

        if (this.armorStand != null)
            this.armorStand.setLeftArmPose(leftArmPose);
    }

    public EulerAngle getLeftLegPose() {
        return leftLegPose;
    }

    public void setLeftLegPose(EulerAngle leftLegPose) {
        this.leftLegPose = leftLegPose;

        if (this.armorStand != null)
            this.armorStand.setLeftLegPose(leftLegPose);
    }

    public EulerAngle getRightArmPose() {
        return rightArmPose;
    }

    public void setRightArmPose(EulerAngle rightArmPose) {
        this.rightArmPose = rightArmPose;

        if (this.armorStand != null)
            this.armorStand.setRightArmPose(rightArmPose);
    }

    public EulerAngle getRightLegPose() {
        return rightLegPose;
    }

    public void setRightLegPose(EulerAngle rightLegPose) {
        this.rightLegPose = rightLegPose;

        if (this.armorStand != null)
            this.armorStand.setRightLegPose(rightLegPose);
    }

    public ItemStack getItemInHand() {
        return itemInHand;
    }

    public void setItemInHand(ItemStack itemInHand) {
        this.itemInHand = itemInHand;

        if (this.armorStand != null)
            this.armorStand.setItemInHand(itemInHand);
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;

        if (this.armorStand != null)
            this.armorStand.setHelmet(helmet);
    }

    public ItemStack getChestPlate() {
        return chestPlate;
    }

    public void setChestPlate(ItemStack chestPlate) {
        this.chestPlate = chestPlate;

        if (this.armorStand != null)
            this.armorStand.setChestplate(chestPlate);
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public void setLeggings(ItemStack leggings) {
        this.leggings = leggings;

        if (this.armorStand != null)
            this.armorStand.setLeggings(leggings);
    }

    public ItemStack getBoots() {
        return boots;
    }

    public void setBoots(ItemStack boots) {
        this.boots = boots;

        if (this.armorStand != null)
            this.armorStand.setBoots(boots);
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;

        if (this.armorStand != null)
            this.armorStand.setCustomName(customName);
    }

    public boolean isCustomNameVisible() {
        return customNameVisible;
    }

    public void setCustomNameVisible(boolean customNameVisible) {
        this.customNameVisible = customNameVisible;

        if (this.armorStand != null)
            this.armorStand.setCustomNameVisible(customNameVisible);
    }

    public boolean isGravity() {
        return gravity;
    }

    public void setGravity(boolean gravity) {
        this.gravity = gravity;

        if (this.armorStand != null)
            this.armorStand.setGravity(gravity);
    }

    public boolean isSmall() {
        return small;
    }

    public void setSmall(boolean small) {
        this.small = small;

        if (this.armorStand != null)
            this.armorStand.setSmall(small);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;

        if (this.armorStand != null)
            this.armorStand.setVisible(visible);
    }

    public boolean hasMarker() {
        return marker;
    }

    public void setMarker(boolean marker) {
        this.marker = marker;

        if (this.armorStand != null)
            this.armorStand.setMarker(marker);
    }

    public static ArrayList<ArmorStandNpc> getArmorStandNpcs() {
        return armorStandNpcs;
    }

    public static ArmorStandNpc getArmorStandNpc(Entity entity) {
        for (ArmorStandNpc armorStandNpc : armorStandNpcs) {
            if (armorStandNpc.getArmorStand() == entity) //TODO, might not work?
                return armorStandNpc;
        }
        return null;
    }
}
