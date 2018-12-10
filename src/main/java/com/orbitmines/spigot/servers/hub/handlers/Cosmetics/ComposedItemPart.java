package com.orbitmines.spigot.servers.hub.handlers.Cosmetics;

import com.orbitmines.spigot.api.handlers.npc.ArmorStandNpc;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class ComposedItemPart {

    public enum PartType {SMALL_BLOCK, MEDIUM_BLOCK}

    private ArmorStand item;
    private Vector offset;


    public ComposedItemPart (ArmorStand item, Vector offset) {
        setItem(item);
        this.offset = offset;
    }

    public void setItem(ArmorStand item) {
        item.setGravity(false);
        item.setVisible(false);
        item.setBasePlate(false);
        this.item = item;
    }

    public ArmorStand getItem() {
        return item;
    }

    public void update(Location loc) {
        Location newLoc = loc.clone();
        double yaw = Math.toRadians(newLoc.getYaw());
        double sin = Math.sin(yaw);
        double cos = Math.cos(yaw);
        double newX = offset.getX() * cos - offset.getZ() * sin;
        double newZ = offset.getX() * sin + offset.getZ() * cos;
        item.teleport(newLoc.add(new Vector(newX, offset.getY(), newZ)));
    }

    public static ComposedItemPart buildPart(PartType type, Material mat, Location offset) {
        switch (type) {
            case SMALL_BLOCK: {
                ArmorStandNpc block = new ArmorStandNpc(offset);
                block.setSmall(true);
                block.setItemInHand(new ItemStack(mat));
                block.setRightArmPose(new EulerAngle(Math.toRadians(345),Math.toRadians(225),0));
                block.create();
                Vector loc = new Vector(offset.getX(), offset.getY(), offset.getZ());
                return new ComposedItemPart(block.getArmorStand(), loc.add(new Vector(-0.02,1,0.12)));
            }
            case MEDIUM_BLOCK: {
                ArmorStandNpc block = new ArmorStandNpc(offset);
                block.setHelmet(new ItemStack(mat));
                block.create();
                Vector loc = new Vector(offset.getX(), offset.getY(), offset.getZ());
                return new ComposedItemPart(block.getArmorStand(), loc.add(new Vector(0,0,0)));
            }
        }
        return null;
    }
}
