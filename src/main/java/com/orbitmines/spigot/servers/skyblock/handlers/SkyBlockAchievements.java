package com.orbitmines.spigot.servers.skyblock.handlers;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Message;
import com.orbitmines.api.Rarity;
import com.orbitmines.api.Server;
import com.orbitmines.spigot.api.Loot;
import com.orbitmines.spigot.api.handlers.achievements.Achievement;
import com.orbitmines.spigot.api.handlers.achievements.AchievementHandler;

public enum SkyBlockAchievements implements Achievement {

    ;

    static {

    }

    private final String name;
    private final Rarity rarity;
    private final Loot.Instance[] rewards;
    private final Message[] description;

    private AchievementHandler handler;

    SkyBlockAchievements(String name, Rarity rarity, Loot.Instance reward, Message... description) {
        this(name, rarity, new Loot.Instance[] { reward }, description);
    }

    SkyBlockAchievements(String name, Rarity rarity, Loot.Instance[] rewards, Message... description) {
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
        return Server.SKYBLOCK;
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
    public Message[] getDescription() {
        return description;
    }

    @Override
    public AchievementHandler getHandler() {
        return handler;
    }
}
