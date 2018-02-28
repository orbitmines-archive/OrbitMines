package com.orbitmines.spigot.servers.uhsurvival.handlers.tool;

import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Created by Robin on 2/27/2018.
 */
public class ToolInventory {

    private PlayerInventory inv;
    private EntityEquipment eq;

    private Tool mainHand;
    private Tool offHand;
    private Tool helmet;
    private Tool chestplate;
    private Tool leggings;
    private Tool boots;

    public ToolInventory(PlayerInventory playerInventory){
        this.inv = playerInventory;
        updateMainHand();
        updateOffHand();
        updateArmor();
    }

    public ToolInventory(EntityEquipment eq){
        this.eq = eq;
        updateMainHand();
        updateOffHand();
        updateArmor();
    }

    /* UPDATE METHODS */
    public void updateMainHand() {
        if (inv != null) {
            this.mainHand = Tool.getTool(inv.getItemInMainHand(), false);
        } else if(eq != null){
            this.mainHand = Tool.getTool(eq.getItemInMainHand(), false);
        }
    }

    public void updateOffHand() {
        if (inv != null) {
            this.offHand = Tool.getTool(inv.getItemInOffHand(), false);
        } else if(eq != null){
            this.offHand = Tool.getTool(eq.getItemInOffHand(), false);
        }
    }

    public void updateArmor(){
        if(inv != null) {
            this.helmet = Tool.getTool(inv.getHelmet(), false);
            this.chestplate = Tool.getTool(inv.getChestplate(), false);
            this.leggings = Tool.getTool(inv.getLeggings(), false);
            this.boots = Tool.getTool(inv.getBoots(), false);
        } else if(eq != null){
            this.helmet = Tool.getTool(eq.getHelmet(), false);
            this.chestplate = Tool.getTool(eq.getChestplate(), false);
            this.leggings = Tool.getTool(eq.getLeggings(), false);
            this.boots = Tool.getTool(eq.getBoots(), false);
        }
    }

    /* GETTERS */
    public Tool getMainHand() {
        return mainHand;
    }

    public Tool getOffHand() {
        return offHand;
    }

    public Tool getHelmet() {
        return helmet;
    }

    public Tool getChestplate() {
        return chestplate;
    }

    public Tool getLeggings() {
        return leggings;
    }

    public Tool getBoots() {
        return boots;
    }

    public Tool[] getArmor(){
        return new Tool[]{getHelmet(), getChestplate(), getLeggings(), getBoots()};
    }

    /* SETTERS */
    public void setMainHand(ItemStack itemStack, boolean created){
        this.mainHand = Tool.getTool(itemStack, created);
        if(inv != null){
            inv.setItemInMainHand(itemStack);
        } else if(eq != null){
            eq.setItemInMainHand(itemStack);
        }
    }

    public void setOffHand(ItemStack itemStack, boolean created){
        this.offHand = Tool.getTool(itemStack, created);
        if(inv != null){
            inv.setItemInOffHand(itemStack);
        } else if(eq != null){
            eq.setItemInOffHand(itemStack);
        }
    }

    public void setHelmet(ItemStack itemStack, boolean created){
        this.helmet = Tool.getTool(itemStack, created);
        if(inv != null){
            inv.setHelmet(itemStack);
        } else if(eq != null){
            eq.setHelmet(itemStack);
        }
    }

    public void setChestplate(ItemStack itemStack, boolean created){
        this.chestplate = Tool.getTool(itemStack, created);
        if(inv != null){
            inv.setChestplate(itemStack);
        } else if(eq != null){
            eq.setChestplate(itemStack);
        }
    }

    public void setLeggings(ItemStack itemStack, boolean created){
        this.leggings = Tool.getTool(itemStack, created);
        if(inv != null){
            inv.setLeggings(itemStack);
        } else if(eq != null){
            eq.setLeggings(itemStack);
        }
    }

    public void setBoots(ItemStack itemStack, boolean created) {
        this.boots = Tool.getTool(itemStack, created);
        if(inv != null){
            inv.setBoots(itemStack);
        } else if(eq != null){
            eq.setBoots(itemStack);
        }
    }
}
