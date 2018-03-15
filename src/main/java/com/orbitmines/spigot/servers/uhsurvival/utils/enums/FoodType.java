package com.orbitmines.spigot.servers.uhsurvival.utils.enums;

import com.orbitmines.api.Language;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import org.bukkit.Material;

/**
 * Created by Robin on 3/1/2018.
 */
public enum FoodType {

    MEAT(60, Material.COOKED_BEEF, "§c§l", "Vlees", "Meat"),
    FISH(180, Material.RAW_FISH, "§b§l", "Vis", "Fish"),
    FRUIT(180, Material.APPLE, "§a§l", "Fruit", "Fruit"),
    VEGETABLES(90, Material.CARROT_ITEM, "§2§l", "Groente", "Vegetables"),
    SWEET(180,Material.COOKIE, "§f§l", "Snoepjes", "Sweets"),
    WATER(60, Material.POTION, "§1§l", "Water", "Water"),
    MALICIOUS(180, Material.BREAD, "§5§l", "Vreemd", "Malicious"),
    SOUPS(270, Material.MUSHROOM_SOUP, "§d§l", "Soep", "Soups");

    private int seconds;
    private Material material;
    private String color;
    private String dutch;
    private String english;

    FoodType(int seconds, Material material, String color, String dutch, String english){
        this.material = material;
        this.color = color;
        this.dutch = dutch;
        this.english = english;
        this.seconds = seconds;
    }

    public String getColor() {
        return color;
    }

    public int getSeconds() {
        return seconds;
    }

    public ItemBuilder getBuilder(Language language) {
        String displayname;
        switch (language) {
            case DUTCH:
                displayname = this.dutch;
                break;
            case ENGLISH:
                displayname = this.english;
                break;
            default:
                displayname = this.english;
                break;
        }
        return new ItemBuilder(material).setDisplayName(color + displayname);
    }
}
