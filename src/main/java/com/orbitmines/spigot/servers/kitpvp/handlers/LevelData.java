package com.orbitmines.spigot.servers.kitpvp.handlers;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.spigot.api.handlers.chat.Title;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class LevelData {

    public final static int maxLevel = 100;

    private final KitPvPPlayer player;
    private int level;

    private int currentLevelXp;
    private int nextLevelXp;

    public LevelData(KitPvPPlayer player) {
        this.player = player;

        this.level = 0;
        this.currentLevelXp = 0;
        this.nextLevelXp = 0;
    }

    public KitPvPPlayer getPlayer() {
        return player;
    }

    public int getLevel() {
        return level;
    }

    public int getCurrentLevelXp() {
        return currentLevelXp;
    }

    public int getNextLevelXp() {
        return nextLevelXp;
    }

    public String getColor() {
        return getColor(level);
    }

    public String getPrefix() {
        return getColor() + level + " ";
    }

    public void updateExperienceBar() {
        Player player = this.player.getPlayer();
        player.setLevel(level);
        player.setExp(((float) currentLevelXp) / ((float) nextLevelXp));
    }

    public void update(boolean notify) {
        int experience = player.getExperience();

        int level = 0;

        for (int i = 0; i < maxLevel + 1 /* max levels */; i++) {
            int requiredExperience = getRequired(i);

            if (experience >= requiredExperience) {
                experience -= requiredExperience;

                level++;
            } else {
                /* Gained enough experience to level up */
                if (notify && this.level != level) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5, 1);

                    Title title = new Title(new Message(""), new Message("§e§lLevel up! §7Je bent nu " + getColor(level) + "§lLevel " + level + "§7!", "§e§lLevel up! §7You are now " + getColor(level) + "§lLevel " + level + "§7!"), 30, 60, 30);
                    title.send(player);
                }

                this.level = level;
                this.nextLevelXp = requiredExperience;
                this.currentLevelXp = experience;

                return;
            }
        }

        /* Highest level reached */
        this.level = maxLevel;
    }

    private int getRequired(int level) {
        return (level + 1) * (500 + 10 * level);
    }

    public int getExperience(int level) {
        int experience = 0;
        for (int i = 0; i < level; i++) {
            experience += getRequired(i);
        }

        return experience;
    }

    public String getColor(int level) {
        if (level < 10)
            return Color.SILVER.getChatColor();
        else if (level < 20)
            return Color.FUCHSIA.getChatColor();
        else if (level < 30)
            return Color.PURPLE.getChatColor();
        else if (level < 40)
            return Color.AQUA.getChatColor();
        else if (level < 50)
            return Color.BLUE.getChatColor();
        else if (level < 60)
            return Color.TEAL.getChatColor();
        else if (level < 70)
            return Color.LIME.getChatColor();
        else if (level < 80)
            return Color.GREEN.getChatColor();
        else if (level < 90)
            return Color.YELLOW.getChatColor();
        else if (level < 100)
            return Color.ORANGE.getChatColor();
        else
            return Color.RED.getChatColor();
    }
}
