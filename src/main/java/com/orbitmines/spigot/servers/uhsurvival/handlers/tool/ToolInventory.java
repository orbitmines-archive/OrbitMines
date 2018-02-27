package com.orbitmines.spigot.servers.uhsurvival.handlers.tool;

import org.bukkit.inventory.PlayerInventory;

/**
 * Created by Robin on 2/27/2018.
 */
public class ToolInventory {

    private PlayerInventory inv;

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

    public void updateMainHand(){
        this.mainHand = Tool.getTool(inv.getItemInMainHand(), false);
    }

    public void updateOffHand(){
        this.offHand = Tool.getTool(inv.getItemInOffHand(), false);
    }

    public void updateArmor(){
        this.helmet = Tool.getTool(inv.getHelmet(), false);
        this.chestplate = Tool.getTool(inv.getChestplate(), false);
        this.leggings = Tool.getTool(inv.getLeggings(), false);
        this.boots = Tool.getTool(inv.getBoots(), false);
    }

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
}
