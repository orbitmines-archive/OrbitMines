package com.orbitmines.spigot.api.handlers.achievements;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.handlers.OMPlayer;

public class EmptyAchievementHandler extends AchievementHandler {

    public EmptyAchievementHandler(Achievement achievement) {
        super(achievement);
    }

    @Override
    public boolean hasCompleted(OMPlayer omp) {
        return false;
    }
}
