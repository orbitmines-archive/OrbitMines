package com.orbitmines.spigot.api.handlers.achievements;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Message;
import com.orbitmines.api.Rarity;
import com.orbitmines.api.Server;
import com.orbitmines.spigot.api.Loot;

public interface Achievement {

    int getId();

    String getName();

    Server getServer();

    Rarity getRarity();

    Loot.Instance[] getRewards();

    boolean shouldShowProgressOnComplete();

    Message completedProgress(int progress);

    Message[] getDescription();

    AchievementHandler getHandler();

}
