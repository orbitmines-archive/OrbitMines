package com.orbitmines.spigot.servers.uhsurvival.handlers;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Message;
import com.orbitmines.api.Rarity;
import com.orbitmines.api.Server;
import com.orbitmines.spigot.api.Loot;
import com.orbitmines.spigot.api.handlers.achievements.Achievement;
import com.orbitmines.spigot.api.handlers.achievements.AchievementHandler;

public enum UHSurvivalAchievements implements Achievement {

    ;

    static {

    }

    private final String name;
    private final Rarity rarity;
    private final Loot.Instance[] rewards;
    private final boolean showProgressOnComplete;
    private final Message completedProgress;
    private final Message[] description;

    private AchievementHandler handler;

    UHSurvivalAchievements(String name, Rarity rarity, Loot.Instance reward, boolean showProgressOnComplete, Message completedProgress, Message... description) {
        this(name, rarity, new Loot.Instance[] { reward }, showProgressOnComplete, completedProgress, description);
    }

    UHSurvivalAchievements(String name, Rarity rarity, Loot.Instance[] rewards, boolean showProgressOnComplete, Message completedProgress, Message... description) {
        this.name = name;
        this.rarity = rarity;
        this.rewards = rewards;
        this.showProgressOnComplete = showProgressOnComplete;
        this.completedProgress = completedProgress;
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
        return Server.UHSURVIVAL;
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
    public boolean shouldShowProgressOnComplete() {
        return showProgressOnComplete;
    }

    @Override
    public Message completedProgress(int progress) {
        return completedProgress;
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
