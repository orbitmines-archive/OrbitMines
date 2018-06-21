package com.orbitmines.spigot.servers.uhsurvival2.handlers.tool;

import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ToolInventory {

    private PlayerInventory inv;
    private EntityEquipment eq;

    private Tool mainHand, offHand;
    private Tool helmet, chestplate, leggings, boots;

    public ToolInventory(PlayerInventory inv){
        this.inv = inv;
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
    private void updateMainHand() {
        ItemStack item = inv == null ? eq.getItemInMainHand() : inv.getItemInMainHand();
        if(mainHand == null || !mainHand.getItem().isSimilar(item)) {
            this.mainHand = Tool.getTool(item);
        }
    }

    private void updateOffHand() {
        ItemStack item = inv == null ? eq.getItemInMainHand() : inv.getItemInMainHand();
        if(offHand == null || !offHand.getItem().isSimilar(item)){
            this.offHand = Tool.getTool(item);

        }
    }

    private void updateHelmet() {
        ItemStack item = inv == null ? eq.getHelmet() : inv.getHelmet();
        if(helmet == null || !helmet.getItem().isSimilar(item)){
            this.helmet = Tool.getTool(item);
        }
    }

    private void updateChestPlate() {
        ItemStack item = inv == null ? eq.getChestplate() : inv.getChestplate();
        if(chestplate == null || !chestplate.getItem().isSimilar(item)){
            this.chestplate =  Tool.getTool(item);
        }
    }

    private void updateLeggings() {
        ItemStack item = inv == null ? eq.getLeggings() : inv.getLeggings();
        if(leggings == null || !leggings.getItem().isSimilar(item)){
            this.leggings = Tool.getTool(item);
        }
    }

    private void updateBoots() {
        ItemStack item = inv == null ? eq.getBoots() : inv.getBoots();
        if(boots == null || !boots.getItem().isSimilar(item)){
            this.boots = Tool.getTool(item);
        }
    }

    private void updateArmor(){
        updateHelmet();
        updateChestPlate();
        updateLeggings();
        updateBoots();
    }

    /* GETTERS */
    public Tool getMainHand() {
        updateMainHand();
        return mainHand;
    }

    public Tool getOffHand() {
        updateOffHand();
        return offHand;
    }

    public Tool getHelmet() {
        updateHelmet();
        return helmet;
    }

    public Tool getChestplate() {
        updateChestPlate();
        return chestplate;
    }

    public Tool getLeggings() {
        updateLeggings();
        return leggings;
    }

    public Tool getBoots() {
        updateBoots();
        return boots;
    }

    public Tool[] getArmor(){
        return new Tool[]{getHelmet(), getChestplate(), getLeggings(), getBoots()};
    }
}
