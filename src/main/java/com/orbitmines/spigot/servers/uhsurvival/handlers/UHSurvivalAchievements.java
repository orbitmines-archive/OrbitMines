package com.orbitmines.spigot.servers.uhsurvival.handlers;

import com.orbitmines.api.Message;
import com.orbitmines.api.Rarity;
import com.orbitmines.api.Server;
import com.orbitmines.spigot.api.Loot;
import com.orbitmines.spigot.api.handlers.achievements.Achievement;
import com.orbitmines.spigot.api.handlers.achievements.AchievementHandler;

public enum UHSurvivalAchievements implements Achievement {

    ;


    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Server getServer() {
        return null;
    }

    @Override
    public Rarity getRarity() {
        return null;
    }

    @Override
    public Loot.Instance[] getRewards() {
        return new Loot.Instance[0];
    }

    @Override
    public Message[] getDescription() {
        return new Message[0];
    }

    @Override
    public AchievementHandler getHandler() {
        return null;
    }
}
