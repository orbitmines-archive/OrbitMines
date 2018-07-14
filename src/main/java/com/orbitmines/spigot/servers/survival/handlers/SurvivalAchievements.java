package com.orbitmines.spigot.servers.survival.handlers;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Rarity;
import com.orbitmines.api.Server;
import com.orbitmines.spigot.api.Loot;
import com.orbitmines.spigot.api.handlers.achievements.Achievement;
import com.orbitmines.spigot.api.handlers.achievements.AchievementHandler;

public enum SurvivalAchievements implements Achievement {

    ;

    static {

    }

    private final String name;
    private final Rarity rarity;
    private final Loot.Instance[] rewards;
    private final String[] description;

    private AchievementHandler handler;

    SurvivalAchievements(String name, Rarity rarity, Loot.Instance reward, String... description) {
        this(name, rarity, new Loot.Instance[] { reward }, description);
    }

    SurvivalAchievements(String name, Rarity rarity, Loot.Instance[] rewards, String... description) {
        this.name = name;
        this.rarity = rarity;
        this.rewards = rewards;
        this.description = description;
    }

    @Override
    public int getId() {
        return ordinal();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Server getServer() {
        return Server.SURVIVAL;
    }

    @Override
    public Rarity getRarity() {
        return rarity;
    }

    @Override
    public Loot.Instance[] getRewards() {
        return rewards;
    }

    @Override
    public String[] getDescription() {
        return description;
    }

    @Override
    public AchievementHandler getHandler() {
        return handler;
    }
}
